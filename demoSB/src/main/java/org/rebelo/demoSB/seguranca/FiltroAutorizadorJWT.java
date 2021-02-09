package org.rebelo.demoSB.seguranca;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.rebelo.demoSB.seguranca.Constantes;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class FiltroAutorizadorJWT extends BasicAuthenticationFilter {

	public FiltroAutorizadorJWT(AuthenticationManager authManager) {
        super(authManager);
    }
	
	  @Override
	    protected void doFilterInternal(HttpServletRequest req,
	                                    HttpServletResponse res,
	                                    FilterChain chain) throws IOException, ServletException {
	        String header = req.getHeader(Constantes.HEADER_STRING);

	        if (header == null || !header.startsWith(Constantes.TOKEN_PREFIX)) {
	            chain.doFilter(req, res);
	            return;
	        }

	        UsernamePasswordAuthenticationToken authentication = this.getAuthentication(req);

	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        chain.doFilter(req, res);
	    }
	
	
	  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
	       
		  String token = request.getHeader(Constantes.HEADER_STRING);
	        if (token != null) {
	           //decodifica o token JWT
	        	
	        	DecodedJWT jwt = JWT.require(Algorithm.HMAC512(Constantes.SECRET.getBytes()))
	                    .build()
	                    .verify(token.replace(Constantes.TOKEN_PREFIX, ""));
	        	
	        	String user = jwt.getSubject();
	        		        		        	
	        	List<GrantedAuthority> autoridades = jwt.getClaim(Constantes.TOKEN_PREFIX_AUTORIDADES).asList(String.class)
	        			.stream().map(s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList());
	        	
	   
	            if (user != null) {
	            	
	            //autoridades.forEach(a -> System.out.print("AUTORIDADES LOGIN:   "+a.getAuthority()));
	            	
	                return new UsernamePasswordAuthenticationToken(user, null, autoridades/*new ArrayList<>()*/);
	            }
	            return null;
	        }
	        return null;
	    }
	  
}
