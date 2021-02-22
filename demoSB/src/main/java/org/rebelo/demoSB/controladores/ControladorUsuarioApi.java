package org.rebelo.demoSB.controladores;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.rebelo.demoSB.repositorio.RepositorioUsuario;
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
	public ResponseEntity<Page<UsuarioDTO>> listar(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "1") int size) {

		if (page < 0 || size < 1)
			return ResponseEntity.badRequest().build();
		
		Page<UsuarioDTO> pagina = this.repositorioUsuario
				.findAll(PageRequest.of(page, size, Sort.by("nome").descending()))
				.map(u->u.toUsuarioDTO());


		return ResponseEntity.ok().body(pagina);

	}
	
	@GetMapping("/pesquisarpornome/{query}")
	@ResponseBody
	public ResponseEntity<Page<UsuarioDTO>> pesquisarPorNome(@PathVariable String query,  @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "1") int size) {
		
		if (page < 0 || size < 1)
			return ResponseEntity.badRequest().build();
		
		Page<UsuarioDTO> pagina = this.repositorioUsuario
				.findByNomeContainingIgnoreCase(query,PageRequest.of(page, size, Sort.by("nome").descending()))
				.map(u->u.toUsuarioDTO());


		return ResponseEntity.ok().body(pagina);
	}

	/* 
	 Os dados de um usuário podem ser acessados apenas por ele mesmo ou por
	 administradores. Lembrando que a propriedade authentication.name é o cpf do usuário logado 
	 */
	@GetMapping("/buscar/{cpf}")
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

	/* 
	 Os dados de um usuário podem ser atualizados apenas por ele mesmo ou por
	 administradores. Lembrando que a propriedade authentication.name é o cpf do usuário logado 
	 */
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

	/* 
	 Um usuário pode ser excluido apenas por ele mesmo ou por
	 administradores. Lembrando que a propriedade authentication.name é o cpf do usuário logado 
	 */
	@DeleteMapping(path = { "/excluir/{cpf}" })
	@PreAuthorize("hasAuthority('ADMIN')  or #cpf == authentication.name")
	public ResponseEntity<?> excluir(@PathVariable String cpf) {
		return this.repositorioUsuario.findById(cpf).map(record -> {
			this.repositorioUsuario.deleteById(cpf);
			return ResponseEntity.ok().build();
		}).orElse(ResponseEntity.notFound().build());
	}

}
