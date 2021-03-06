package org.rebelo.demoSB.controladores;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
import springfox.documentation.annotations.ApiIgnore;

import org.rebelo.demoSB.repositorio.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.rebelo.demoSB.DTO.AnuncioVeiculoDTO;
import org.rebelo.demoSB.entidade.*;

@Api(value = "Endpoints para Gerenciar Anúncios de Veículos", 
tags = "Endpoints para Gerenciar Anúncios de Veículos")
@RestController
@RequestMapping("/anuncios/veiculos")
public class ControladorAnuncioVeiculoApi {

	private RepositorioAnuncioVeiculo repositorioAnuncioVeiculo;
	private RepositorioUsuario repositorioUsuario;

	public ControladorAnuncioVeiculoApi(RepositorioAnuncioVeiculo repositorioAnuncioVeiculo, RepositorioUsuario repositorioUsuario) {

		this.repositorioAnuncioVeiculo = repositorioAnuncioVeiculo;
		this.repositorioUsuario = repositorioUsuario;
	}

	public boolean verificaSeAnuncioPertenceUsuario(long id, String cpfDoUsuarioLogado) {

		Optional<AnuncioVeiculo> opVeic = this.repositorioAnuncioVeiculo.findById(id);
		
		if (opVeic.isPresent()) {

			return cpfDoUsuarioLogado.equals(opVeic.get().getUsuario().getCpf());

		}

		return false;
	}

	/*
	 * //@GetMapping("/listar") //@ResponseBody public List<VeiculoDTO> listar() {
	 * 
	 * return this.repositorioVeiculo.findAll().stream().map(veiculo ->
	 * veiculo.toVeiculoDTO()) .collect(Collectors.toList());
	 * 
	 * }
	 */

	@ApiOperation(value = "Lista os anúncios de veículos")
	@ApiResponses({
	    @ApiResponse(code = 200, message = "Sucesso")
	})
	@GetMapping("/listar")
	@ResponseBody
	public ResponseEntity<Page<AnuncioVeiculoDTO>> listar(
			@ApiParam("Número da página (padrão é 0)") @RequestParam(defaultValue = "0") int p,
			@ApiParam("Número de registros por página (padrão é 10)") @RequestParam(defaultValue = "10") int n) {

		if (p < 0 || n < 1)
			return ResponseEntity.badRequest().build();
		
		Page<AnuncioVeiculoDTO> pagina = this.repositorioAnuncioVeiculo
				.findAll(PageRequest.of(p, n, Sort.by("modelo")))
				.map(v->v.toAnuncioVeiculoDTO());


		return ResponseEntity.ok().body(pagina);

	}

	@ApiOperation(value = "Lista os anúncios de veículos por usuário") 
	@ApiResponses({
	    @ApiResponse(code = 200, message = "Sucesso")
	})
	@GetMapping("/listar/por/usuario/{cpf}")
	@ResponseBody
	public ResponseEntity<Page<AnuncioVeiculoDTO>>  listarPorUsuario(
			@ApiParam("Cpf do usuário. ")@PathVariable String cpf,
			@ApiParam("Número da página (padrão é 0)") @RequestParam(defaultValue = "0") int p,
			@ApiParam("Número de registros por página (padrão é 10)") @RequestParam(defaultValue = "10") int n) {

		if (p < 0 || n < 1)
			return ResponseEntity.badRequest().build();
		
		Page<AnuncioVeiculoDTO> pagina = this.repositorioAnuncioVeiculo
				.listarPorUsuario(cpf,PageRequest.of(p, n, Sort.by("modelo")))
				.map(v->v.toAnuncioVeiculoDTO());


		return ResponseEntity.ok().body(pagina);

	}

	//pesquisa usando o Hibernate Search
	@ApiOperation(value = "Pesquisa os anúncios de veículos pelos campos descrição e modelo") 
	@ApiResponses({
	    @ApiResponse(code = 200, message = "Sucesso")
	})
	@GetMapping("/pesquisar")
	@ResponseBody
	public ResponseEntity<List<AnuncioVeiculoDTO>> pesquisar(
			@ApiParam("Palavras-chave a serem pesquisadas. ") @RequestParam(defaultValue ="") String q,
			@ApiParam("Número da página (padrão é 0)") @RequestParam(defaultValue = "0") int p,
			@ApiParam("Número de registros por página (padrão é 10)") @RequestParam(defaultValue = "10") int n) {
		
		List<AnuncioVeiculoDTO> searchResults = null;
		
		try {
			searchResults = this.repositorioAnuncioVeiculo.pesquisar(q, n, p)
					.stream().map(a-> a.toAnuncioVeiculoDTO())
					.collect(Collectors.toList());;

		} catch (Exception ex) {
			// Nothing
		}


		return ResponseEntity.ok().body(searchResults);
	}
	
	@ApiOperation(value = "Retorna o anúncio de veículos pelo identificador") 
	@ApiResponses({
	    @ApiResponse(code = 200, message = "Sucesso")
	})
	@GetMapping("/buscar/{id}")
	public ResponseEntity<AnuncioVeiculoDTO> buscar(@ApiParam("identificador do anúncio. ") 
	@PathVariable long id) {

		return this.repositorioAnuncioVeiculo.findById(id).map(anunVeiculo -> ResponseEntity.ok().body(anunVeiculo.toAnuncioVeiculoDTO()))
				.orElse(ResponseEntity.notFound().build());
	}

	/*
	 * Os anúncios podem ser criados/modificados/excluidos apenas pelo usuário
	 * proprietário ou administradores. Lembrando que a
	 * propriedade authentication.name é o cpf do usuário logado
	 */
	
	//Insere um novo anúncio de veiculo
	@ApiOperation(value = "Cria um novo anúncio de veículo") 
	@ApiResponses({
	    @ApiResponse(code = 200, message = "Sucesso")
	})
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token JWT para autenticação", required = true, 
	allowEmptyValue = false, paramType = "header", 
	example = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9...")
	@PostMapping("/criar")
	@PreAuthorize("isAuthenticated()")
	public AnuncioVeiculoDTO criar(
			@ApiIgnore Authentication authentication, 
			@ApiParam("informações a serem inseridas do novo anúncio. ") 
	@Valid @RequestBody AnuncioVeiculo anuncioVeiculo) {
				
		String cpfDoUsuario = authentication.getName();

		Usuario usr = this.repositorioUsuario.findById(cpfDoUsuario).get();

		anuncioVeiculo.setUsuario(usr);
		anuncioVeiculo.setDataDeCadastro(LocalDateTime.now());

		return this.repositorioAnuncioVeiculo.save(anuncioVeiculo).toAnuncioVeiculoDTO();

	}

	//Edita o anúncio do veiculo pelo seu ID
	@ApiOperation(value = "Edita o anúncio do veiculo pelo seu ID") 
	@ApiResponses({
	    @ApiResponse(code = 200, message = "Sucesso")
	})
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token JWT para autenticação", required = true, 
	allowEmptyValue = false, paramType = "header", 
	example = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9...")
	@PutMapping("/atualizar/{id}")
	@PreAuthorize("hasAuthority('ADMIN') or this.verificaSeAnuncioPertenceUsuario(#id, authentication.name)")
	public ResponseEntity<AnuncioVeiculoDTO> atualizar(@ApiParam("identificador do anúncio. ") 
	@PathVariable long id, 
	@ApiParam("informações a serem atualizadas do anúncio. ") 
	@Valid @RequestBody AnuncioVeiculo anuncioVeiculo) {

		return this.repositorioAnuncioVeiculo.findById(id).map(registrOriginal -> {

			registrOriginal.setAno(anuncioVeiculo.getAno());
			registrOriginal.setDescricao(anuncioVeiculo.getDescricao());
			registrOriginal.setMarca(anuncioVeiculo.getMarca());
			registrOriginal.setModelo(anuncioVeiculo.getModelo());
			registrOriginal.setPreco(anuncioVeiculo.getPreco());

			AnuncioVeiculo registroAtualizado = this.repositorioAnuncioVeiculo.save(registrOriginal);
			return ResponseEntity.ok().body(registroAtualizado.toAnuncioVeiculoDTO());

		}).orElse(ResponseEntity.notFound().build());

	}

	//Exclui o anúncio do veiculo pelo seu ID
	@ApiOperation(value = "Exclui o anúncio do veiculo pelo seu ID") 
	@ApiResponses({
	    @ApiResponse(code = 200, message = "Sucesso")
	})
	@ApiImplicitParam(name = "Authorization", value = "Bearer Token JWT para autenticação", required = true, 
	allowEmptyValue = false, paramType = "header", 
	example = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9...")
	@DeleteMapping(path = { "/excluir/{id}" })
	@PreAuthorize("hasAuthority('ADMIN') or this.verificaSeAnuncioPertenceUsuario(#id, authentication.name)")
	public ResponseEntity<?> excluir(@ApiParam("identificador do anúncio. ") 
	@PathVariable long id) {
		return this.repositorioAnuncioVeiculo.findById(id).map(record -> {
			this.repositorioAnuncioVeiculo.deleteById(id);
			return ResponseEntity.ok().build();
		}).orElse(ResponseEntity.notFound().build());
	}

}
