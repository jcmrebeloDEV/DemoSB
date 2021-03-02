package org.rebelo.demoSB.testesDeIntegracao;

import org.junit.jupiter.api.Test;
import org.rebelo.demoSB.entidade.Usuario;
import org.rebelo.demoSB.entidade.AnuncioVeiculo;
import org.rebelo.demoSB.entidade.Enum.Marca;
import org.rebelo.demoSB.repositorio.RepositorioAnuncioVeiculo;
import org.rebelo.demoSB.repositorio.RepositorioUsuario;
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
public class TestesIntegracaoAnuncioVeiculo {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private RepositorioAnuncioVeiculo repositorioAnuncioVeiculo;
	private RepositorioUsuario repositorioUsuario;

	
	private AnuncioVeiculo geraAnuncioVeiculo() {
				
		 AnuncioVeiculo anuncioVeiculo = new AnuncioVeiculo();
		 anuncioVeiculo.setModelo("One");
		 anuncioVeiculo.setMarca(Marca.MINI);
		 anuncioVeiculo.setAno(2010);
		 anuncioVeiculo.setDescricao("Carro bacana, turbo! Em excelente estado de conservação.");
		 anuncioVeiculo.setDataDeCadastro(LocalDateTime.now());
		 anuncioVeiculo.setPreco(BigDecimal.valueOf(55990.00));
		 
		 return anuncioVeiculo;
		
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
	public void testeControladorAnuncioVeiculo_Criar_Autorizado() throws Exception {

		String cpf = "74018276036";
		String senha = "12345678";
		
		String token = obterTokenJWT(cpf, senha);
		

			mockMvc.perform(post("/anuncios/veiculos/criar/", cpf)
			.header("Authorization", token)
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(geraAnuncioVeiculo())))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.modelo", is("One")))
			.andReturn();

	}
	
	@Test
	public void testeControladorAnuncioVeiculo_Criar_NAO_Autorizado() throws Exception {
						
			mockMvc.perform(post("/anuncios/veiculos/criar/")
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(geraAnuncioVeiculo())))
			.andExpect(status().isUnauthorized())
			.andReturn();

	}

	@Test
	public void testeControladorAnuncioVeiculo_Listar() throws Exception {

		AnuncioVeiculo v = repositorioAnuncioVeiculo.findById((long) 3).get(); // primeiro registro de veiculo no BD

		int numeroRegistros =(int) repositorioAnuncioVeiculo.count();
		
		mockMvc.perform(get("/anuncios/veiculos/listar/?p=0&n={numeroRegistros}",numeroRegistros))//uma pagina com todos os registros
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json"))
		.andExpect(jsonPath("$.content", hasSize(numeroRegistros)))
		.andExpect(jsonPath("$.content[0].modelo", is(v.getModelo())))
		.andReturn();

	}

	@Test
	public void testeControladorAnuncioVeiculo_Pesquisar() throws Exception {
		
		mockMvc.perform(get("/anuncios/veiculos/pesquisar/?q={query}&p={pagina}&n={numeroRegistros}", "Roadster turbo", "0","10"))
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json"))
		.andExpect(jsonPath("$", hasSize(2)))
		.andExpect(jsonPath("$[0].marca", is(Marca.MERCEDES.toString())))
		.andReturn();

	}

	@Test
	public void testeControladorAnuncioVeiculo_Buscar() throws Exception {

		mockMvc.perform(get("/anuncios/veiculos/buscar/{id}", "2"))
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json"))
		.andExpect(jsonPath("$.id", is(2)))
		.andReturn();

	}

	/* outros testes omitidos */

}
