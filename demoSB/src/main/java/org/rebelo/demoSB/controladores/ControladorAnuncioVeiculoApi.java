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
import org.rebelo.demoSB.repositorio.*;
import java.time.LocalDateTime;
import java.util.Optional;


import javax.validation.Valid;

import org.rebelo.demoSB.DTO.AnuncioVeiculoDTO;
import org.rebelo.demoSB.entidade.*;

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

	@GetMapping("/listar")
	@ResponseBody
	public ResponseEntity<Page<AnuncioVeiculoDTO>> listar(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "1") int size) {

		if (page < 0 || size < 1)
			return ResponseEntity.badRequest().build();
		
		Page<AnuncioVeiculoDTO> pagina = this.repositorioAnuncioVeiculo
				.findAll(PageRequest.of(page, size, Sort.by("modelo")))
				.map(v->v.toAnuncioVeiculoDTO());


		return ResponseEntity.ok().body(pagina);

	}

	
	@GetMapping("/listarporusuario/{cpf}")
	@ResponseBody
	public ResponseEntity<Page<AnuncioVeiculoDTO>>  listarPorUsuario(@PathVariable String cpf, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "1") int size) {

		if (page < 0 || size < 1)
			return ResponseEntity.badRequest().build();
		
		Page<AnuncioVeiculoDTO> pagina = this.repositorioAnuncioVeiculo
				.listarPorUsuario(cpf,PageRequest.of(page, size, Sort.by("modelo")))
				.map(v->v.toAnuncioVeiculoDTO());


		return ResponseEntity.ok().body(pagina);

	}

	
	@GetMapping("/pesquisarpormodelo/{query}")
	@ResponseBody
	public ResponseEntity<Page<AnuncioVeiculoDTO>> pesquisarPorModelo(@PathVariable String query,  @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "1") int size) {
		
		if (page < 0 || size < 1)
			return ResponseEntity.badRequest().build();
		
		Page<AnuncioVeiculoDTO> pagina = this.repositorioAnuncioVeiculo
				.findByModeloContainingIgnoreCase(query,PageRequest.of(page, size, Sort.by("modelo")))
				.map(v->v.toAnuncioVeiculoDTO());


		return ResponseEntity.ok().body(pagina);
	}

	@GetMapping("/buscar/{id}")
	public ResponseEntity<AnuncioVeiculoDTO> buscar(@PathVariable long id) {

		return this.repositorioAnuncioVeiculo.findById(id).map(anunVeiculo -> ResponseEntity.ok().body(anunVeiculo.toAnuncioVeiculoDTO()))
				.orElse(ResponseEntity.notFound().build());
	}

	/*
	 * Os anúncios podem ser criados/modificados/excluidos apenas pelo usuário
	 * proprietário ou administradores. Lembrando que a
	 * propriedade authentication.name é o cpf do usuário logado
	 */
	
	//Insere um novo anúncio de veiculo
	@PostMapping("/criar")
	@PreAuthorize("isAuthenticated()")
	public AnuncioVeiculoDTO criar(Authentication authentication, @Valid @RequestBody AnuncioVeiculo anuncioVeiculo) {
				
		String cpfDoUsuario = authentication.getName();

		Usuario usr = this.repositorioUsuario.findById(cpfDoUsuario).get();

		anuncioVeiculo.setUsuario(usr);
		anuncioVeiculo.setDataDeCadastro(LocalDateTime.now());

		return this.repositorioAnuncioVeiculo.save(anuncioVeiculo).toAnuncioVeiculoDTO();

	}

	//Edita o anúncio do veiculo pelo seu ID
	@PutMapping("/atualizar/{id}")
	@PreAuthorize("hasAuthority('ADMIN') or this.verificaSeAnuncioPertenceUsuario(#id, authentication.name)")
	public ResponseEntity<AnuncioVeiculoDTO> atualizar(@PathVariable long id, @Valid @RequestBody AnuncioVeiculo anuncioVeiculo) {

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
	@DeleteMapping(path = { "/excluir/{id}" })
	@PreAuthorize("hasAuthority('ADMIN') or this.verificaSeAnuncioPertenceUsuario(#id, authentication.name)")
	public ResponseEntity<?> excluir(@PathVariable long id) {
		return this.repositorioAnuncioVeiculo.findById(id).map(record -> {
			this.repositorioAnuncioVeiculo.deleteById(id);
			return ResponseEntity.ok().build();
		}).orElse(ResponseEntity.notFound().build());
	}

}
