package org.rebelo.demoSB.seguranca;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.rebelo.demoSB.repositorio.RepositorioUsuario;
import org.rebelo.demoSB.entidade.Usuario;

import java.util.ArrayList;
import java.util.List;
 
/*
 * O Spring security usa internamente o objeto UserDetails retornado para
 * verificar a senha e username contra os valores fornecidos no login
 * 
 * 
 * NOTA: Aqui a propriedade username usada é o próprio cpf (que é identificador único)
 * do usuário. Poderia ser qualquer outro identificador único,
 * como o próprio atributo padrão "username".
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private RepositorioUsuario repositorioUsuario;
   
    public UserDetailsServiceImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       
    	List<GrantedAuthority> autoridades = new ArrayList<GrantedAuthority>();
    	
    	//A propriedade "username" usada é o cpf do usuário
    	Usuario usuario = repositorioUsuario.findById(username).get(); 
    	
        if (usuario == null) {
            throw new UsernameNotFoundException(username);
        }
        
        if (usuario.isAdmin()) {
        	autoridades.add(new SimpleGrantedAuthority("ADMIN"));
       }
        
        return new User(usuario.getCpf(), usuario.getSenha(), autoridades);
    }
    
    
  
    
    
    
}