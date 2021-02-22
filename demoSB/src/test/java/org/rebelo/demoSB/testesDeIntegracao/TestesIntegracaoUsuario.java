package org.rebelo.demoSB.testesDeIntegracao;

import org.junit.jupiter.api.Test;
import org.rebelo.demoSB.entidade.Usuario;
import org.rebelo.demoSB.repositorio.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TestesIntegracaoUsuario {
	

	@Autowired
	private RepositorioUsuario repositorioUsuario;
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	private String obterTokenJWT(String cpf, String password) throws Exception {

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
	public void testeControladorUsuario_Listar() throws Exception {
		
		//apenas administradores podem acessar a lista de usuários
		
		int numeroRegistros =(int) repositorioUsuario.count();
		
		String cpf = "67681905049"; //cpf de um Administrador
		String senha = "12345678";
		
		String token_com_privilegio_Administrador = obterTokenJWT(cpf, senha);
									
			mockMvc.perform(get("/usuarios/listar/?page=0&size={size}",numeroRegistros)//uma pagina com todos os registros
		   .header("Authorization", token_com_privilegio_Administrador)			
		   .contentType("application/json"))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.content", hasSize(numeroRegistros)))
			.andExpect(jsonPath("$.last", is(true)))
			.andReturn();
				
	}
	
	
	@Test
	public void testeControladorUsuario_PesquisarPorNome() throws Exception {
		
		//apenas administradores podem acessar a lista de usuários
			
		String cpf = "67681905049"; //cpf de um Administrador
		String senha = "12345678";
		
		String token_com_privilegio_Administrador = obterTokenJWT(cpf, senha);
									
			mockMvc.perform(get("/usuarios/pesquisarpornome/{nome}/?page=0&size=1","ste")//pagina com todos os registros
		   .header("Authorization", token_com_privilegio_Administrador)			
		   .contentType("application/json"))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.content", hasSize(1))) //retorna "usuario de teste 1"
			.andExpect(jsonPath("$.last", is(true)))
			.andReturn();
				
	}
	
	
	@Test
	public void testeControladorUsuario_Listar_NAO_Autorizado() throws Exception {
		
		//apenas administradores podem acessar a lista de usuários
		
		
		String cpf = "74018276036"; //cpf de um usuario comum
		String senha = "12345678";
		
		String token = obterTokenJWT(cpf, senha);
									
			mockMvc.perform(get("/usuarios/listar/")
		   .header("Authorization", token)			
		   .contentType("application/json"))
			.andExpect(status().isForbidden())
			.andReturn();
			
		
	}

}
