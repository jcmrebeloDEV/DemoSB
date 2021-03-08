package org.rebelo.demoSB.repositorio;

import java.util.List;

import org.rebelo.demoSB.entidade.AnuncioVeiculo;
import org.springframework.data.domain.Page;

public interface AnuncioVeiculoMecanismoDeBusca {

	Page<AnuncioVeiculo> pesquisar(final String keywords, int limit, int offset);
	
}
