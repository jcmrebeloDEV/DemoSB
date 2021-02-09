package org.rebelo.demoSB.entidade;

import java.io.Serializable;
import java.math.BigDecimal;

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

import org.rebelo.demoSB.DTO.VeiculoDTO;
import org.rebelo.demoSB.entidade.Enum.Marca;


@Entity
public class Veiculo  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	 @ManyToOne
	 @JoinColumn(name="cpf_fk")
	 //@NotNull
	 private Usuario usuario;
	
	 @NotNull
	 Marca marca;
	 
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
	 
	 @NotNull
	 @Size(min=2, max=500)
	 String descricao;

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
	 
	public VeiculoDTO toVeiculoDTO() {
		
		VeiculoDTO veiculoDTO = new VeiculoDTO();
		
		veiculoDTO.setId(this.getId());
		veiculoDTO.setAno(this.getAno());
		veiculoDTO.setMarca(this.getMarca());
		veiculoDTO.setPreco(this.getPreco());
		veiculoDTO.setModelo(this.getModelo());
		veiculoDTO.setDescricao(this.getDescricao());
		veiculoDTO.setContatos(this.getUsuario().getContatosAdicionais());
		veiculoDTO.setNomeDoAnunciante(this.getUsuario().getNome());
		
		return veiculoDTO;
		
	}

}
