package org.rebelo.demoSB.seguranca;

import org.rebelo.demoSB.seguranca.Constantes;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.rebelo.demoSB.seguranca.UserDetailsServiceImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled=true)  //para poder usar a  anotação @PreAuthorize
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private UserDetailsServiceImpl detalhesDoUsuario;
    private BCryptPasswordEncoder codificadorDeSenha;

    public WebSecurity(UserDetailsServiceImpl detalhesDoUsuario, BCryptPasswordEncoder codificadorDeSenha) {
        this.detalhesDoUsuario = detalhesDoUsuario;
        this.codificadorDeSenha = codificadorDeSenha;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, Constantes.CADASTRO_API).permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/veiculos/listar/").permitAll()
                .antMatchers("/veiculos/listarporusuario/**").permitAll()
                .antMatchers("/veiculos/buscar/**").permitAll()
                .antMatchers("/veiculos/pesquisarpormodelo/**").permitAll()
                .antMatchers("/h2-console/**").permitAll() //para usar o console do DB H2
                .anyRequest().authenticated()
                .and()
                .addFilter(new FiltroAutenticadorJWT(authenticationManager()))
                .addFilter(new FiltroAutorizadorJWT(authenticationManager()))
                // desabilita a criação de sessao (modo stateless)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(detalhesDoUsuario).passwordEncoder(codificadorDeSenha);
    }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
    return source;
  }
}

      
      
