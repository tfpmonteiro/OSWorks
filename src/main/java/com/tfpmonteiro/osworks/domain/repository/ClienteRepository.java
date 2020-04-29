package com.tfpmonteiro.osworks.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfpmonteiro.osworks.api.model.ClienteDto;
import com.tfpmonteiro.osworks.domain.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>{

	List<ClienteDto> findByNome(String nome);
	List<ClienteDto> findByNomeContaining(String nome);
	Cliente findByEmail(String email);
}
