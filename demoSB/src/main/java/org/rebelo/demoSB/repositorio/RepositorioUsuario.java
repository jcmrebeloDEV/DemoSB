package org.rebelo.demoSB.repositorio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.rebelo.demoSB.entidade.*;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioUsuario extends JpaRepository<Usuario, String> {

	Page<Usuario> findAll(Pageable pageable);

	Page<Usuario> findByNomeContainingIgnoreCase(String query, Pageable pageable);
	
}