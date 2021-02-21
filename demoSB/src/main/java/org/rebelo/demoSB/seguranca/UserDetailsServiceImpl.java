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

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private RepositorioUsuario repositorioUsuario;

	public UserDetailsServiceImpl(RepositorioUsuario repositorioUsuario) {
		this.repositorioUsuario = repositorioUsuario;
	}

	/*
	 * NOTA: Aqui a propriedade username usada é o próprio cpf (que é identificador único)
	 * do usuário. Poderia ser qualquer outro identificador único,
	 * como o próprio atributo "username".
	 */
	@Override
	public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {

		List<GrantedAuthority> autoridades = new ArrayList<GrantedAuthority>();

		Usuario usuario = repositorioUsuario.findById(cpf)
				.orElseThrow(() -> new UsernameNotFoundException(cpf));

		if (usuario.isAdmin())
			autoridades.add(new SimpleGrantedAuthority("ADMIN"));

		return new User(usuario.getCpf(), usuario.getSenha(), autoridades);
	}

}