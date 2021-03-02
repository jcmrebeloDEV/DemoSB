package org.rebelo.demoSB.repositorio;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.rebelo.demoSB.entidade.AnuncioVeiculo;
import org.springframework.transaction.annotation.Transactional;

public class AnuncioVeiculoMecanismoDeBuscaImpl implements AnuncioVeiculoMecanismoDeBusca  {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	@Transactional(readOnly = true)
	public List<AnuncioVeiculo> pesquisar(final String keywords, int limit, int offset) {

		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

	    QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
	        .buildQueryBuilder().forEntity(AnuncioVeiculo.class).get();
	    
	    org.apache.lucene.search.Query luceneQuery = queryBuilder
	        .keyword()
	        .onFields("modelo", "descricao")
	        .matching(keywords)
	        .createQuery();

	    // wrap Lucene query in a javax.persistence.Query
	    javax.persistence.Query jpaQuery =
	        fullTextEntityManager.createFullTextQuery(luceneQuery, AnuncioVeiculo.class);

	    jpaQuery.setMaxResults(limit);
	    jpaQuery.setFirstResult(offset);

	    // execute search
	    return jpaQuery.getResultList();
	  }
		
}
