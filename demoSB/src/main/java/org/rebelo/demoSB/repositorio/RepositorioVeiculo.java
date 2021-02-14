package org.rebelo.demoSB.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import org.rebelo.demoSB.entidade.*;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioVeiculo extends JpaRepository<Veiculo, Long> {

	@Query(value = "select v from Veiculo v where v.usuario.cpf = ?1", nativeQuery = false)
	List<Veiculo> listarPorUsuario(String cpf);

	List<Veiculo> findByModeloContainingIgnoreCase(String query);

}
