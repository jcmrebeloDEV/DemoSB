package org.rebelo.demoSB.repositorio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import org.rebelo.demoSB.entidade.*;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioVeiculo extends JpaRepository<Veiculo, Long> {

	@Query(value = "select v from Veiculo v where v.usuario.cpf = ?1", nativeQuery = false)
	Page<Veiculo>  listarPorUsuario(String cpf, Pageable pageable);

	Page<Veiculo> findAll(Pageable pageable);

	Page<Veiculo> findByModeloContainingIgnoreCase(String query, Pageable pageable);

}
