package org.rebelo.demoSB.controladores;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.rebelo.demoSB.repositorio.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

	public RepositorioVeiculo getRepositorioVeiculo() {
		return this.repositorioVeiculo;
	}

	@GetMapping("/listar")
	@ResponseBody
	public List<VeiculoDTO> listar() {

		return this.repositorioVeiculo.findAll().stream().map(veiculo -> veiculo.toVeiculoDTO())
				.collect(Collectors.toList());

	}

	@GetMapping("/listarporusuario/{cpf}")
	@ResponseBody
	public List<VeiculoDTO> listarPorUsuario(@PathVariable String cpf) {

		return this.repositorioVeiculo.listarPorUsuario(cpf).stream().map(veiculo -> veiculo.toVeiculoDTO())
				.collect(Collectors.toList());

	}

	@GetMapping("/pesquisarpormodelo/{query}")
	@ResponseBody
	public List<VeiculoDTO> pesquisarpormodelo(@PathVariable String query) {

		return this.repositorioVeiculo.findByModeloContainingIgnoreCase(query).stream()
				.map(veiculo -> veiculo.toVeiculoDTO()).collect(Collectors.toList());

	}

	@GetMapping("/buscar/{id}")
	public ResponseEntity<VeiculoDTO> buscar(@PathVariable long id) {

		return this.repositorioVeiculo.findById(id).map(veiculo -> ResponseEntity.ok().body(veiculo.toVeiculoDTO()))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/criar/{cpf}")
	/*
	 * Os anúncios podem ser criados/modificados/excluidos apenas pelo usuário
	 * proprietário ou por administradores. Lembrando que a propriedade
	 * authentication.name é o cpf do usuário logado
	 */
	@PreAuthorize("hasAuthority('ADMIN')  or #cpf == authentication.name")
	public VeiculoDTO criar(@PathVariable String cpf, @Valid @RequestBody Veiculo veic) {

		Usuario usr = this.repositorioUsuario.findById(cpf).get();

		veic.setUsuario(usr);
		veic.setDataDeCadastro(LocalDateTime.now());

		return this.repositorioVeiculo.save(veic).toVeiculoDTO();

	}

	@PutMapping("/atualizar/{id}")
	@PreAuthorize("hasAuthority('ADMIN')  or "
			+ "this.getRepositorioVeiculo().findById(#id).get().getUsuario().getCpf() == authentication.name")
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

	@DeleteMapping(path = { "/excluir/{id}" })
	@PreAuthorize("hasAuthority('ADMIN')  or "
			+ "this.getRepositorioVeiculo().findById(#id).get().getUsuario().getCpf() == authentication.name")
	public ResponseEntity<?> excluir(@PathVariable long id) {
		return this.repositorioVeiculo.findById(id).map(record -> {
			this.repositorioVeiculo.deleteById(id);
			return ResponseEntity.ok().build();
		}).orElse(ResponseEntity.notFound().build());
	}

}
