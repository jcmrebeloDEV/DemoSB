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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.rebelo.demoSB.repositorio.RepositorioUsuario;
import javax.validation.Valid;
import org.rebelo.demoSB.DTO.UsuarioDTO;
import org.rebelo.demoSB.entidade.*;

@Api(value = "Endpoints para Gerenciar Usuários", 
tags = "Endpoints para Gerenciar Usuários")
@RestController
@RequestMapping("/usuarios")
public class ControladorUsuarioApi {

	private RepositorioUsuario repositorioUsuario;
	private BCryptPasswordEncoder codificadorDeSenha;

	public ControladorUsuarioApi(RepositorioUsuario repositorioUsuario, BCryptPasswordEncoder codificadorDeSenha) {
		this.repositorioUsuario = repositorioUsuario;
		this.codificadorDeSenha = codificadorDeSenha;
	}
	
		
	@ApiOperation(value = "Lista os usuários")
	@ApiResponses({
	    @ApiResponse(code = 200, message = "Sucesso")
	})
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token JWT para autenticação", required = true, 
	allowEmptyValue = false, paramType = "header", 
	example = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9...")
	@GetMapping("/listar")
	// somente Administradores podem listar os usuarios
	@PreAuthorize("hasAuthority('ADMIN')") 
	@ResponseBody
	public ResponseEntity<Page<UsuarioDTO>> listar(
			@ApiParam("Número da página (padrão é 0)") @RequestParam(defaultValue = "0") int p,
			@ApiParam("Número de registros por página (padrão é 10)") @RequestParam(defaultValue = "10") int n) {

		if (p < 0 || n < 1)
			return ResponseEntity.badRequest().build();
		
		Page<UsuarioDTO> pagina = this.repositorioUsuario
				.findAll(PageRequest.of(p, n, Sort.by("nome").descending()))
				.map(u->u.toUsuarioDTO());


		return ResponseEntity.ok().body(pagina);

	}
	
	@ApiOperation(value = "Pesquisa os usuários por nome")
	@ApiResponses({
	    @ApiResponse(code = 200, message = "Sucesso")
	})
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token JWT para autenticação", required = true, 
	allowEmptyValue = false, paramType = "header", 
	example = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9...")
	// somente Administradores podem pesquisar os usuarios
	@GetMapping("/pesquisar/por/nome/{query}")
	@PreAuthorize("hasAuthority('ADMIN')") 
	@ResponseBody
	public ResponseEntity<Page<UsuarioDTO>> pesquisarPorNome(
			@ApiParam("Nome completo ou parte do nome do usuário") @PathVariable String query, 
			@ApiParam("Número da página (padrão é 0)") @RequestParam(defaultValue = "0") int p,
			@ApiParam("Número de registros por página (padrão é 10)") @RequestParam(defaultValue = "10") int n) {
		
		if (p < 0 || n < 1)
			return ResponseEntity.badRequest().build();
		
		Page<UsuarioDTO> pagina = this.repositorioUsuario
				.findByNomeContainingIgnoreCase(query,PageRequest.of(p, n, Sort.by("nome").descending()))
				.map(u->u.toUsuarioDTO());


		return ResponseEntity.ok().body(pagina);
	}

	/* 
	 Os dados de um usuário podem ser acessados apenas por ele mesmo ou por
	 administradores. Lembrando que a propriedade authentication.name é o cpf do usuário logado 
	 */
	@ApiOperation(value = "Retorna um usuário pelo seu Cpf")
	@ApiResponses({
	    @ApiResponse(code = 200, message = "Sucesso")
	})
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token JWT para autenticação", required = true, 
	allowEmptyValue = false, paramType = "header", 
	example = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9...")
	@GetMapping("/buscar/{cpf}")
	@PreAuthorize("hasAuthority('ADMIN')  or #cpf == authentication.name")
	@ResponseBody
	public ResponseEntity<UsuarioDTO> buscar(@ApiParam("Cpf do usuário") @PathVariable String cpf) {

		return this.repositorioUsuario.findById(cpf).map(usuario -> ResponseEntity.ok().body(usuario.toUsuarioDTO()))
				.orElse(ResponseEntity.notFound().build());

	}

	//Qualquer pessoa pode se cadastrar no sistema
	@ApiOperation(value = "Cria um novo usuário")
	@ApiResponses({
	    @ApiResponse(code = 200, message = "Sucesso")
	})
	@PostMapping("/criar/")
	public UsuarioDTO criar(@ApiParam("Dados do novo usuário") @Valid @RequestBody Usuario usr) {

		usr.getContatosAdicionais().forEach((c) -> c.setUsuario(usr));

		usr.setSenha(this.codificadorDeSenha.encode(usr.getSenha()));

		return this.repositorioUsuario.save(usr).toUsuarioDTO();

	}

	/* 
	 Os dados de um usuário podem ser atualizados apenas por ele mesmo ou por
	 administradores. Lembrando que a propriedade authentication.name é o cpf do usuário logado 
	 */
	@ApiOperation(value = "Atualiza os dados de um usuário")
	@ApiResponses({
	    @ApiResponse(code = 200, message = "Sucesso")
	})
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token JWT para autenticação", required = true, 
	allowEmptyValue = false, paramType = "header", 
	example = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9...")
	@PutMapping("/atualizar/{cpf}")
	@PreAuthorize("hasAuthority('ADMIN')  or #cpf == authentication.name")
	public ResponseEntity<UsuarioDTO> atualizar(
			@ApiParam("Cpf do usuário") @PathVariable String cpf, 
			@ApiParam("Dados a serem atualizados do usuário") @Valid @RequestBody Usuario usr) {

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
	@ApiOperation(value = "Exclui um usuário")
	@ApiResponses({
	    @ApiResponse(code = 200, message = "Sucesso")
	})
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token JWT para autenticação", required = true, 
	allowEmptyValue = false, paramType = "header", 
	example = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9...")
	@DeleteMapping(path = { "/excluir/{cpf}" })
	@PreAuthorize("hasAuthority('ADMIN')  or #cpf == authentication.name")
	public ResponseEntity<?> excluir(@ApiParam("Cpf do usuário") @PathVariable String cpf) {
		return this.repositorioUsuario.findById(cpf).map(record -> {
			this.repositorioUsuario.deleteById(cpf);
			return ResponseEntity.ok().build();
		}).orElse(ResponseEntity.notFound().build());
	}

	/* outros testes omitidos */
	
}

