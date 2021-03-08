package org.rebelo.demoSB.repositorio;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.search.FullTextQuery;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.rebelo.demoSB.entidade.AnuncioVeiculo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

public class AnuncioVeiculoMecanismoDeBuscaImpl implements AnuncioVeiculoMecanismoDeBusca  {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	@Transactional(readOnly = true)
	public /*List<AnuncioVeiculo>*/ Page<AnuncioVeiculo> pesquisar(final String keywords, int limit, int offset) {

		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

	    QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
	        .buildQueryBuilder().forEntity(AnuncioVeiculo.class).get();
	    
	    org.apache.lucene.search.Query luceneQuery = queryBuilder
	        .keyword()
	        .onFields("modelo", "descricao")
	        .matching(keywords)
	        .createQuery();

	   
	    org.hibernate.search.FullTextQuery jpaQuery = 
	    		(FullTextQuery) fullTextEntityManager.createFullTextQuery(luceneQuery, AnuncioVeiculo.class); 
	   
	    jpaQuery.getResultSize();
	    
	    jpaQuery.setMaxResults(limit);
	    jpaQuery.setFirstResult(offset);
	    	    
	    Page<AnuncioVeiculo> pagina = new PageImpl<AnuncioVeiculo>(jpaQuery.getResultList(), 
	    		PageRequest.of(offset, limit),  jpaQuery.getResultSize());
	    
	    return pagina;
	  }
		
}
