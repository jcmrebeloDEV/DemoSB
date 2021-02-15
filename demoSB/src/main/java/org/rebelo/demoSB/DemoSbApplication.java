package org.rebelo.demoSB;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.rebelo.demoSB.entidade.Contato;
import org.rebelo.demoSB.entidade.Usuario;
import org.rebelo.demoSB.entidade.Veiculo;
import org.rebelo.demoSB.entidade.Enum.Marca;
import org.rebelo.demoSB.entidade.Enum.TipoDoContato;
import org.rebelo.demoSB.repositorio.RepositorioUsuario;
import org.rebelo.demoSB.repositorio.RepositorioVeiculo;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableJpaRepositories
public class DemoSbApplication {

	
	
	@Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	//metodo "ad hoc" para popular o BD H2 com alguns registros iniciais
	@Bean
	public ApplicationRunner initializer(RepositorioVeiculo repositorioVeiculo, 
			RepositorioUsuario  repositorioUsuario,
			BCryptPasswordEncoder bCryptPasswordEncoder){
		
		 return args -> {
			 String cpf = "74018276036";

				Usuario u = new Usuario();
				u.setCpf(cpf);
				u.setAdmin(false);
				u.setEmail("email@email.com");
				u.setNome("Usuario de Teste 1");
				u.setSenha(bCryptPasswordEncoder.encode("12345678"));
				u.setNascimento(LocalDate.parse("1990-08-01"));
				
				Contato c = new Contato();
				c.setTipo(TipoDoContato.WHATSAPP);
				c.setValor("(21)999999999");
				c.setUsuario(u);
				ArrayList<Contato> lista = new ArrayList<Contato>();
				lista.add(c);
				u.setContatosAdicionais(lista);
				
				u.setVeiculosAnunciados(null);
				
				repositorioUsuario.save(u);
				
				Veiculo v1 = new Veiculo();
				v1.setUsuario(repositorioUsuario.findById(cpf).get());
				v1.setModelo("Roadster");
				v1.setMarca(Marca.MERCEDES);
				v1.setAno(2007);
				v1.setDescricao("carro bacana, turbo!");
				v1.setDataDeCadastro(LocalDateTime.now());
				v1.setPreco(BigDecimal.valueOf(75000.00));

				repositorioVeiculo.save(v1);
				
				Veiculo v2 = new Veiculo();
				v2.setUsuario(repositorioUsuario.findById(cpf).get());
				v2.setModelo("328i");
				v2.setMarca(Marca.BMW);
				v2.setAno(2007);
				v2.setDescricao("carro legal");
				v2.setDataDeCadastro(LocalDateTime.now());
				v2.setPreco(BigDecimal.valueOf(75000.00));

				repositorioVeiculo.save(v2);

			 
		 };
	   
	}
	
	public static void main(String[] args) {
		SpringApplication.run(DemoSbApplication.class, args);
	}

}
