package org.rebelo.demoSB.testesDeIntegracao;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.rebelo.demoSB.entidade.Usuario;
import org.rebelo.demoSB.entidade.Veiculo;
import org.rebelo.demoSB.entidade.Enum.Marca;
import org.rebelo.demoSB.repositorio.RepositorioUsuario;
import org.rebelo.demoSB.repositorio.RepositorioVeiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import static org.hamcrest.Matchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class TestesIntegracaoVeiculo {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private RepositorioVeiculo repositorioVeiculo;

	@Autowired
	private RepositorioUsuario repositorioUsuario;
			
	
	@Test
	void testeControladorVeiculoListar() throws Exception {
		
		Veiculo v = repositorioVeiculo.findById((long) 2).get(); //primeiro registro de veiculo no BD
		
			
		this.mockMvc.perform(get("/veiculos/listar/")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$",hasSize(2)))
				.andExpect(jsonPath("$[0].modelo", is(v.getModelo())))
				.andReturn();

	}

	@Test
	public void testeControladorVeiculoPesquisarPorModelo() throws Exception {
 				
		this.mockMvc.perform(get("/veiculos/pesquisarpormodelo/{query}", "roadster"))
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json"))
		.andExpect(jsonPath("$",hasSize(1)))
		.andExpect(jsonPath("$[0].marca", is(Marca.MERCEDES.toString())))
		.andReturn();

	}

	@Test
	public void testeControladorVeiculoBuscar() throws Exception {

		this.mockMvc.perform(get("/veiculos/buscar/{id}", "2")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.id", is(2)))
				.andReturn();

	}

	/* outros testes omitidos */

}
