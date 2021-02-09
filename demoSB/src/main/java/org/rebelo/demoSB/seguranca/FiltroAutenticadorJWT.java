package org.rebelo.demoSB.seguranca;

import org.rebelo.demoSB.seguranca.Constantes;
import org.rebelo.demoSB.entidade.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.userdetails.User;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;//usar o velho util.Date devido à Api do JWT

/*
 * Classe que lida com o Login.
 */
public class FiltroAutenticadorJWT extends UsernamePasswordAuthenticationFilter{

	
	  private AuthenticationManager authenticationManager;

	    public FiltroAutenticadorJWT(AuthenticationManager authenticationManager) {
	        this.authenticationManager = authenticationManager;
	        
	        setFilterProcessesUrl(Constantes.LOGIN_API); //end-point para o login
	    }
	    
	    @Override
	    public Authentication attemptAuthentication(HttpServletRequest req,
	                                                HttpServletResponse res) throws AuthenticationException {
	        try {
	        	Usuario creds = new ObjectMapper()
	                    .readValue(req.getInputStream(), Usuario.class);

	            return authenticationManager.authenticate(
	                    new UsernamePasswordAuthenticationToken(
	                            creds.getCpf(), //username (no caso o cpf)
	                            creds.getSenha(), //senha
	                            new ArrayList<>()) //lista vazia (apenas username e senha sao fornecidas no login)
	            );
	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        }
	    }
	    
	    @Override
	    protected void successfulAuthentication(HttpServletRequest req,
	                                            HttpServletResponse res,
	                                            FilterChain chain,
	                                            Authentication auth) throws IOException, ServletException {

	    	//obtem a lista de autoridades como um array de strings
	    	 String[] autoridades =  auth.getAuthorities().stream()
	    			.map( a -> a.getAuthority()).toArray(String[]::new);  
	       	 
	   	    	
	    	 String token = JWT.create()
	                 .withSubject(((User) auth.getPrincipal()).getUsername()) 
	                 .withArrayClaim(Constantes.TOKEN_PREFIX_AUTORIDADES, autoridades)
	                 .withExpiresAt(new Date(System.currentTimeMillis() + Constantes.EXPIRATION_TIME))
	                 .sign(Algorithm.HMAC512(Constantes.SECRET.getBytes()));

	         //String body = ((Usuario) auth.getPrincipal()).getUserName() + " " + token;

	    	//auth.getAuthorities().forEach(a -> System.out.print("AUTORIDADES PARA TOKEN:   "+a.getAuthority()));
	    	 
	    	
	         res.addHeader(Constantes.HEADER_STRING, Constantes.TOKEN_PREFIX + token);
	         
	         //res.getWriter().write(body);
	         //res.getWriter().flush();
	     }
	    	
	    
	    
}
