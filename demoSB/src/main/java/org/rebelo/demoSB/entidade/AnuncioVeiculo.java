package org.rebelo.demoSB.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import org.rebelo.demoSB.DTO.AnuncioVeiculoDTO;
import org.rebelo.demoSB.entidade.Enum.Marca;

import io.swagger.annotations.ApiModelProperty;


@Entity
@Indexed
public class AnuncioVeiculo  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(hidden=true)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	 @ApiModelProperty(hidden=true)
	 @ManyToOne
	 @JoinColumn(name="cpf_fk")
	 //@NotNull
	 private Usuario usuario;
	
	 @NotNull
	 Marca marca;
	 
	 @Field
	 @NotNull
	 @Size(min=2, max=50)
	 String modelo;
	 
	 @NotNull
	 @Max(2021)
	 @Min(1910)
	 int ano;
	 
	 @NotNull
	 @DecimalMin(value = "0.0", inclusive = false)
	 BigDecimal preco;
	 
	 @Field
	 @NotNull
	 @Size(min=2, max=500)
	 String descricao;
	 
	 LocalDateTime dataDeCadastro;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
	
	public LocalDateTime getDataDeCadastro() {
		return dataDeCadastro;
	}

	public void setDataDeCadastro(LocalDateTime dataDeCadastro) {
		this.dataDeCadastro = dataDeCadastro;
	}
	 
	public AnuncioVeiculoDTO toAnuncioVeiculoDTO() {
		
		AnuncioVeiculoDTO anuncioVeiculoDTO = new AnuncioVeiculoDTO();
		
		anuncioVeiculoDTO.setId(this.getId());
		anuncioVeiculoDTO.setAno(this.getAno());
		anuncioVeiculoDTO.setDataDeCadastro(this.getDataDeCadastro());
		anuncioVeiculoDTO.setMarca(this.getMarca());
		anuncioVeiculoDTO.setPreco(this.getPreco());
		anuncioVeiculoDTO.setModelo(this.getModelo());
		anuncioVeiculoDTO.setDescricao(this.getDescricao());
		anuncioVeiculoDTO.setContatoPrincipal(this.getUsuario().getEmail());
		anuncioVeiculoDTO.setContatosAdicionais(this.getUsuario().getContatosAdicionais());
		anuncioVeiculoDTO.setNomeDoAnunciante(this.getUsuario().getNome());
		
		return anuncioVeiculoDTO;
		
	}

	

}
