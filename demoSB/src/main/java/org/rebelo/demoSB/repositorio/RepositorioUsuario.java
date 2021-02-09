package org.rebelo.demoSB.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.rebelo.demoSB.entidade.*;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioUsuario extends JpaRepository<Usuario, String> {

	//Usuario findByUserName(String username);
	
}