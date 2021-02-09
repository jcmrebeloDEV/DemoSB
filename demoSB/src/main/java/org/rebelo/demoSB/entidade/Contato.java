package org.rebelo.demoSB.entidade;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.rebelo.demoSB.entidade.Enum.TipoDoContato;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Contato implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	 @ManyToOne
	 @JoinColumn(name="cpf_fk")
	 @JsonIgnore
	 @NotNull
	 private Usuario usuario;
	
	@NotNull
	private TipoDoContato tipo; //email, telefone fixo, telefone celular, whatsapp,facebook, outros...
	
	@NotNull
	@Size(min=5, max=240)
	private String valor; //o numero de telefone, ou whatsapp, ou site, etc
	
	
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

	public TipoDoContato getTipo() {
		return tipo;
	}

	public void setTipo(TipoDoContato tipo) {
		this.tipo = tipo;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}



	
	
	

}
