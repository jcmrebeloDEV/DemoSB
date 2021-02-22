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

import org.rebelo.demoSB.DTO.VeiculoDTO;
import org.rebelo.demoSB.entidade.*;

@RestController
@RequestMapping("/veiculos")
public class ControladorVeiculoApi {

	private RepositorioVeiculo repositorioVeiculo;
	private RepositorioUsuario repositorioUsuario;

	public ControladorVeiculoApi(RepositorioVeiculo repositorioVeiculo, RepositorioUsuario repositorioUsuario) {

		this.repositorioVeiculo = repositorioVeiculo;
		this.repositorioUsuario = repositorioUsuario;
	}

	public boolean verificaSeVeiculoPertenceUsuario(long idVeiculo, String cpfDoUsuarioLogado) {

		Optional<Veiculo> opVeic = this.repositorioVeiculo.findById(idVeiculo);
		
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
	public ResponseEntity<Page<VeiculoDTO>> listar(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "1") int size) {

		if (page < 0 || size < 1)
			return ResponseEntity.badRequest().build();
		
		Page<VeiculoDTO> pagina = this.repositorioVeiculo
				.findAll(PageRequest.of(page, size, Sort.by("modelo")))
				.map(v->v.toVeiculoDTO());


		return ResponseEntity.ok().body(pagina);

	}

	
	@GetMapping("/listarporusuario/{cpf}")
	@ResponseBody
	public ResponseEntity<Page<VeiculoDTO>>  listarPorUsuario(@PathVariable String cpf, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "1") int size) {

		if (page < 0 || size < 1)
			return ResponseEntity.badRequest().build();
		
		Page<VeiculoDTO> pagina = this.repositorioVeiculo
				.listarPorUsuario(cpf,PageRequest.of(page, size, Sort.by("modelo")))
				.map(v->v.toVeiculoDTO());


		return ResponseEntity.ok().body(pagina);

	}

	
	@GetMapping("/pesquisarpormodelo/{query}")
	@ResponseBody
	public ResponseEntity<Page<VeiculoDTO>> pesquisarPorModelo(@PathVariable String query,  @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "1") int size) {
		
		if (page < 0 || size < 1)
			return ResponseEntity.badRequest().build();
		
		Page<VeiculoDTO> pagina = this.repositorioVeiculo
				.findByModeloContainingIgnoreCase(query,PageRequest.of(page, size, Sort.by("modelo")))
				.map(v->v.toVeiculoDTO());


		return ResponseEntity.ok().body(pagina);
	}

	@GetMapping("/buscar/{id}")
	public ResponseEntity<VeiculoDTO> buscar(@PathVariable long id) {

		return this.repositorioVeiculo.findById(id).map(veiculo -> ResponseEntity.ok().body(veiculo.toVeiculoDTO()))
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
	public VeiculoDTO criar(Authentication authentication, @Valid @RequestBody Veiculo veic) {
				
		String cpfDoUsuario = authentication.getName();

		Usuario usr = this.repositorioUsuario.findById(cpfDoUsuario).get();

		veic.setUsuario(usr);
		veic.setDataDeCadastro(LocalDateTime.now());

		return this.repositorioVeiculo.save(veic).toVeiculoDTO();

	}

	//Edita o anúncio do veiculo pelo seu ID
	@PutMapping("/atualizar/{id}")
	@PreAuthorize("hasAuthority('ADMIN') or this.verificaSeVeiculoPertenceUsuario(#id, authentication.name)")
	public ResponseEntity<VeiculoDTO> atualizar(@PathVariable long id, @Valid @RequestBody Veiculo veiculo) {

		return this.repositorioVeiculo.findById(id).map(registrOriginal -> {

			registrOriginal.setAno(veiculo.getAno());
			registrOriginal.setDescricao(veiculo.getDescricao());
			registrOriginal.setMarca(veiculo.getMarca());
			registrOriginal.setModelo(veiculo.getModelo());
			registrOriginal.setPreco(veiculo.getPreco());

			Veiculo registroAtualizado = this.repositorioVeiculo.save(registrOriginal);
			return ResponseEntity.ok().body(registroAtualizado.toVeiculoDTO());

		}).orElse(ResponseEntity.notFound().build());

	}

	//Exclui o anúncio do veiculo pelo seu ID
	@DeleteMapping(path = { "/excluir/{id}" })
	@PreAuthorize("hasAuthority('ADMIN') or this.verificaSeVeiculoPertenceUsuario(#id, authentication.name)")
	public ResponseEntity<?> excluir(@PathVariable long id) {
		return this.repositorioVeiculo.findById(id).map(record -> {
			this.repositorioVeiculo.deleteById(id);
			return ResponseEntity.ok().build();
		}).orElse(ResponseEntity.notFound().build());
	}

}
