package org.rebelo.demoSB.testesDeIntegracao;

import org.junit.jupiter.api.Test;
import org.rebelo.demoSB.entidade.Usuario;
import org.rebelo.demoSB.entidade.Veiculo;
import org.rebelo.demoSB.entidade.Enum.Marca;
import org.rebelo.demoSB.repositorio.RepositorioVeiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TestesIntegracaoVeiculo {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private RepositorioVeiculo repositorioVeiculo;

	
	private Veiculo geraVeiculo() {
		
		 Veiculo veiculo = new Veiculo();
		 veiculo.setModelo("One");
		 veiculo.setMarca(Marca.MINI);
		 veiculo.setAno(2010);
		 veiculo.setDescricao("Carro bacana, turbo! Em excelente estado de conservação.");
		 veiculo.setDataDeCadastro(LocalDateTime.now());
		 veiculo.setPreco(BigDecimal.valueOf(55990.00));
		 
		 return veiculo;
		
	}
	
	//@Test
	public String obterTokenJWT(String cpf, String password) throws Exception {

		Usuario user = new Usuario();
		user.setCpf(cpf);
		user.setSenha(password);

		ResultActions result = mockMvc
				.perform(post("/usuarios/login/")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk());
			
		return result.andReturn().getResponse().getHeaderValue("Authorization").toString();

	}

	@Test
	public void testeControladorVeiculo_Criar_Autorizado() throws Exception {

		String cpf = "74018276036";
		String senha = "12345678";
		
		String token = obterTokenJWT(cpf, senha);
		

			mockMvc.perform(post("/veiculos/criar/", cpf)
			.header("Authorization", token)
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(geraVeiculo())))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.modelo", is("One")))
			.andReturn();

	}
	
	@Test
	public void testeControladorVeiculo_Criar_NAO_Autorizado() throws Exception {
						
			mockMvc.perform(post("/veiculos/criar/")
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(geraVeiculo())))
			.andExpect(status().isUnauthorized())
			.andReturn();

	}

	@Test
	public void testeControladorVeiculo_Listar() throws Exception {

		Veiculo v = repositorioVeiculo.findById((long) 3).get(); // primeiro registro de veiculo no BD

		int numeroRegistros =(int) repositorioVeiculo.count();
		
		mockMvc.perform(get("/veiculos/listar/?page=0&size={size}",numeroRegistros))//uma pagina com todos os registros
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json"))
		.andExpect(jsonPath("$.content", hasSize(numeroRegistros)))
		.andExpect(jsonPath("$.content[0].modelo", is(v.getModelo())))
		.andReturn();

	}

	@Test
	public void testeControladorVeiculo_PesquisarPorModelo() throws Exception {

		mockMvc.perform(get("/veiculos/pesquisarpormodelo/{query}/?page=0&size=1", "One"))
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json"))
		.andExpect(jsonPath("$.content", hasSize(1)))
		.andExpect(jsonPath("$.content[0].marca", is(Marca.MINI.toString())))
		.andReturn();

	}

	@Test
	public void testeControladorVeiculo_Buscar() throws Exception {

		mockMvc.perform(get("/veiculos/buscar/{id}", "2"))
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json"))
		.andExpect(jsonPath("$.id", is(2)))
		.andReturn();

	}

	/* outros testes omitidos */

}
