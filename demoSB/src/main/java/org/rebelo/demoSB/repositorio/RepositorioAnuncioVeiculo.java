package org.rebelo.demoSB.repositorio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import org.rebelo.demoSB.entidade.*;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioAnuncioVeiculo extends JpaRepository<AnuncioVeiculo, Long> {

	@Query(value = "select v from AnuncioVeiculo v where v.usuario.cpf = ?1", nativeQuery = false)
	Page<AnuncioVeiculo>  listarPorUsuario(String cpf, Pageable pageable);

	Page<AnuncioVeiculo> findAll(Pageable pageable);

	Page<AnuncioVeiculo> findByModeloContainingIgnoreCase(String query, Pageable pageable);

}
