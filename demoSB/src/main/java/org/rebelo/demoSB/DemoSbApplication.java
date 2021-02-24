package org.rebelo.demoSB;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.rebelo.demoSB.entidade.Contato;
import org.rebelo.demoSB.entidade.Usuario;
import org.rebelo.demoSB.entidade.AnuncioVeiculo;
import org.rebelo.demoSB.entidade.Enum.Marca;
import org.rebelo.demoSB.entidade.Enum.TipoDoContato;
import org.rebelo.demoSB.repositorio.RepositorioUsuario;
import org.rebelo.demoSB.repositorio.RepositorioAnuncioVeiculo;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableJpaRepositories
public class DemoSbApplication {

	
	
	@Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	//metodo "ad hoc" para popular o BD H2 com alguns registros iniciais
	@Bean
	public ApplicationRunner initializer(RepositorioAnuncioVeiculo repositorioAnuncioVeiculo, 
			RepositorioUsuario  repositorioUsuario,
			BCryptPasswordEncoder bCryptPasswordEncoder){
		
		 return args -> {
			 			 
			    Usuario admin = new Usuario();
			    admin.setCpf("67681905049");
			    admin.setAdmin(true);
			    admin.setEmail("admin@admin.com");
			    admin.setNome("admin");
			    admin.setSenha(bCryptPasswordEncoder.encode("12345678"));
			    admin.setNascimento(LocalDate.parse("1980-03-01"));

			    repositorioUsuario.save(admin);
			 
				Usuario u = new Usuario();
				u.setCpf("74018276036");
				u.setAdmin(false);
				u.setEmail("email@email.com");
				u.setNome("Usuario de Teste 1");
				u.setSenha(bCryptPasswordEncoder.encode("12345678"));
				u.setNascimento(LocalDate.parse("1990-08-01"));
				
				Contato c = new Contato();
				c.setTipo(TipoDoContato.WHATSAPP);
				c.setValor("(21)999999999");
				c.setUsuario(u);
				ArrayList<Contato> lista = new ArrayList<Contato>();
				lista.add(c);
				u.setContatosAdicionais(lista);
				
				u.setVeiculosAnunciados(null);
				
				repositorioUsuario.save(u);
				
				AnuncioVeiculo v1 = new AnuncioVeiculo();
				v1.setUsuario(repositorioUsuario.findById("74018276036").get());
				v1.setModelo("SLK Roadster");
				v1.setMarca(Marca.MERCEDES);
				v1.setAno(2007);
				v1.setDescricao("Carro bacana, turbo! Em excelente estado de conservação.");
				v1.setDataDeCadastro(LocalDateTime.now());
				v1.setPreco(BigDecimal.valueOf(75000.00));

				repositorioAnuncioVeiculo.save(v1);
				
				AnuncioVeiculo v2 = new AnuncioVeiculo();
				v2.setUsuario(repositorioUsuario.findById("74018276036").get());
				v2.setModelo("328i");
				v2.setMarca(Marca.BMW);
				v2.setAno(2007);
				v2.setDescricao("Carro legal,teto solar, todo revisado em oficina credenciada");
				v2.setDataDeCadastro(LocalDateTime.now());
				v2.setPreco(BigDecimal.valueOf(75000.00));

				repositorioAnuncioVeiculo.save(v2);

			 
		 };
	   
	}
	
	public static void main(String[] args) {
		SpringApplication.run(DemoSbApplication.class, args);
	}

}
