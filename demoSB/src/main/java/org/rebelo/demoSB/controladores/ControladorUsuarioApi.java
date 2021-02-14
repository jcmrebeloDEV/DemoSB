package org.rebelo.demoSB.controladores;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.rebelo.demoSB.repositorio.RepositorioUsuario;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.rebelo.demoSB.DTO.UsuarioDTO;
import org.rebelo.demoSB.entidade.*;

@RestController
@RequestMapping("/usuarios")
public class ControladorUsuarioApi {

	private RepositorioUsuario repositorioUsuario;
	private BCryptPasswordEncoder codificadorDeSenha;

	public ControladorUsuarioApi(RepositorioUsuario repositorioUsuario, BCryptPasswordEncoder codificadorDeSenha) {
		this.repositorioUsuario = repositorioUsuario;
		this.codificadorDeSenha = codificadorDeSenha;
	}
	
	@GetMapping("/listar")
	// somente Administradores podem listar os usuarios
	@PreAuthorize("hasAuthority('ADMIN')") 
	@ResponseBody
	public List<UsuarioDTO> listar() {

		/*
		 * SecurityContext securityContext = SecurityContextHolder.getContext();
		 * securityContext.getAuthentication().getAuthorities() .forEach(s ->
		 * System.out.print("METODO:    "+s.getAuthority()));
		 */

		return this.repositorioUsuario.findAll().stream().map(usuario -> usuario.toUsuarioDTO())
				.collect(Collectors.toList());

	}

	@GetMapping("/buscar/{cpf}")
	/* 
	 Os dados de um usuário podem ser acessados apenas por ele mesmo ou por
	 administradores. Lembrando que a propriedade authentication.name é o cpf do usuário logado 
	 */
	@PreAuthorize("hasAuthority('ADMIN')  or #cpf == authentication.name")
	@ResponseBody
	public ResponseEntity<UsuarioDTO> buscar(@PathVariable String cpf) {

		return this.repositorioUsuario.findById(cpf).map(usuario -> ResponseEntity.ok().body(usuario.toUsuarioDTO()))
				.orElse(ResponseEntity.notFound().build());

	}

	//Qualquer pessoa pode se cadastrar no sistema
	@PostMapping("/criar/")
	public UsuarioDTO criar(@Valid @RequestBody Usuario usr) {

		usr.getContatosAdicionais().forEach((c) -> c.setUsuario(usr));

		usr.setSenha(this.codificadorDeSenha.encode(usr.getSenha()));

		return this.repositorioUsuario.save(usr).toUsuarioDTO();

	}

	@PutMapping("/atualizar/{cpf}")
	@PreAuthorize("hasAuthority('ADMIN')  or #cpf == authentication.name")
	public ResponseEntity<UsuarioDTO> atualizar(@PathVariable String cpf, @Valid @RequestBody Usuario usr) {

		usr.getContatosAdicionais().forEach((c) -> c.setUsuario(usr));

		return this.repositorioUsuario.findById(cpf).map(registrOriginal -> {
			registrOriginal.setCpf(usr.getCpf());
			registrOriginal.setNome(usr.getNome());
			registrOriginal.setEmail(usr.getEmail());

			registrOriginal.setSenha(this.codificadorDeSenha.encode(usr.getSenha()));
			registrOriginal.setNascimento(usr.getNascimento());

			registrOriginal.getContatosAdicionais().clear();
			registrOriginal.getContatosAdicionais().addAll(usr.getContatosAdicionais());

			Usuario registroAtualizado = this.repositorioUsuario.save(registrOriginal);
			return ResponseEntity.ok().body(registroAtualizado.toUsuarioDTO());

		}).orElse(ResponseEntity.notFound().build());

	}

	@DeleteMapping(path = { "/excluir/{cpf}" })
	@PreAuthorize("hasAuthority('ADMIN')  or #cpf == authentication.name")
	public ResponseEntity<?> excluir(@PathVariable String cpf) {
		return this.repositorioUsuario.findById(cpf).map(record -> {
			this.repositorioUsuario.deleteById(cpf);
			return ResponseEntity.ok().build();
		}).orElse(ResponseEntity.notFound().build());
	}

}
