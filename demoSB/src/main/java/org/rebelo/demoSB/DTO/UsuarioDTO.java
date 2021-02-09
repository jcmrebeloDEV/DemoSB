package org.rebelo.demoSB.DTO;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.rebelo.demoSB.entidade.Contato;

public class UsuarioDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String cpf;

	private String nome;

	private String email;

	private LocalDate nascimento;

	private boolean admin; // flag para identificar se o usuario é administrador

	private List<Contato> contatosAdicionais;

	public UsuarioDTO() {
		super();
		this.contatosAdicionais = new ArrayList<Contato>();

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

	public LocalDate getNascimento() {
		return nascimento;
	}

	public void setNascimento(LocalDate nascimento) {
		this.nascimento = nascimento;
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

}
