package org.rebelo.demoSB.entidade;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;
import org.rebelo.demoSB.DTO.UsuarioDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@NotNull(message = "campo cpf deve ser informado")
	@NotBlank(message = "campo cpf não pode ser vazio")
	@CPF(message = "Cpf deve ser válido formato xxx.xxx.xxx-xx")
	private String cpf;

	@NotNull
	@Size(min = 2, max = 30)
	private String nome;

	/*
	 * @NotNull
	 * 
	 * @NotBlank
	 * 
	 * @Size(min=2, max=30) private String userName;
	 * 
	 */
	@Email
	private String email;

	@Past
	private LocalDate nascimento;

	@NotNull
	@Size(min = 8)
	private String senha;

	@JsonIgnore
	private boolean admin; // flag para identificar se o usuario é administrador

	@OneToMany(mappedBy = "usuario", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<Contato> contatosAdicionais;
	
	@JsonIgnore //anuncios de veiculos tem seu próprio repositório
	@OneToMany(mappedBy = "usuario", orphanRemoval = true, cascade = { CascadeType.ALL })
	private List<AnuncioVeiculo> veiculosAnunciados;

	public Usuario() {
		super();
		this.contatosAdicionais = new ArrayList<Contato>();
		this.admin = false; // usuario criado sempre sem privilegio de admin
	}

	public List<Contato> getContatosAdicionais() {
		return contatosAdicionais;
	}

	public void setContatosAdicionais(List<Contato> contatosAdicionais) {
		this.contatosAdicionais = contatosAdicionais;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	/*
	 * public String getUserName() { return userName; }
	 * 
	 * public void setUserName(String userName) { this.userName = userName; }
	 */

	public LocalDate getNascimento() {
		return nascimento;
	}

	public void setNascimento(LocalDate nascimento) {
		this.nascimento = nascimento;
	}

	public String getSenha() {
		return this.senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UsuarioDTO toUsuarioDTO() {

		UsuarioDTO usuarioDto = new UsuarioDTO();

		usuarioDto.setAdmin(this.isAdmin());
		usuarioDto.setContatosAdicionais(this.getContatosAdicionais());
		usuarioDto.setCpf(this.getCpf());
		usuarioDto.setEmail(this.getEmail());
		usuarioDto.setNascimento(this.getNascimento());
		usuarioDto.setNome(this.getNome());

		return usuarioDto;
	}

	public List<AnuncioVeiculo> getVeiculosAnunciados() {
		return veiculosAnunciados;
	}

	public void setVeiculosAnunciados(List<AnuncioVeiculo> veiculosAnunciados) {
		this.veiculosAnunciados = veiculosAnunciados;
	}

}
