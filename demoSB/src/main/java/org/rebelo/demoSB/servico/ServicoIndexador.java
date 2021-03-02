package org.rebelo.demoSB.servico;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

public class ServicoIndexador implements ApplicationListener<ApplicationReadyEvent> {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent arg0) {
		
		//Para caso haja registros pré-existentes no BD antes de implementar o Hibernate search
		//initializeHibernateSearch(); 
	}


		public void initializeHibernateSearch() {

			try {
				FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
				fullTextEntityManager.createIndexer().startAndWait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	


}
