package org.rebelo.demoSB.repositorio;

import java.util.List;

import org.rebelo.demoSB.entidade.AnuncioVeiculo;

public interface AnuncioVeiculoMecanismoDeBusca {

	List<AnuncioVeiculo> pesquisar(final String keywords, int limit, int offset);
	
}
