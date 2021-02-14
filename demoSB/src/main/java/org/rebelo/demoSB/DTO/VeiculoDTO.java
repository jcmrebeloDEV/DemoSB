package org.rebelo.demoSB.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.rebelo.demoSB.entidade.Contato;
import org.rebelo.demoSB.entidade.Enum.Marca;

public class VeiculoDTO {
	
	private long id;
	
	private String nomeDoAnunciante;
	
	private List<Contato> contatos;
	
	 Marca marca;
	
	 String modelo;
	 
	 int ano;
	
	 BigDecimal preco;
	
	 String descricao;
	 
     LocalDateTime dataDeCadastro;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Contato> getContatos() {
		return contatos;
	}

	public void setContatos(List<Contato> contatos) {
		this.contatos = contatos;
	}

	public Marca getMarca() {
		return marca;
	}

	public void setMarca(Marca marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNomeDoAnunciante() {
		return nomeDoAnunciante;
	}

	public void setNomeDoAnunciante(String nomeDoAnunciante) {
		this.nomeDoAnunciante = nomeDoAnunciante;
	}

	public LocalDateTime getDataDeCadastro() {
		return dataDeCadastro;
	}

	public void setDataDeCadastro(LocalDateTime dataDeCadastro) {
		this.dataDeCadastro = dataDeCadastro;
	}

}
