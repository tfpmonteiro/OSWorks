package com.tfpmonteiro.osworks.api.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tfpmonteiro.osworks.api.model.ClienteDto;
import com.tfpmonteiro.osworks.api.model.ClienteSaveInput;
import com.tfpmonteiro.osworks.api.model.ClienteUpdateInput;
import com.tfpmonteiro.osworks.domain.model.Cliente;
import com.tfpmonteiro.osworks.domain.repository.ClienteRepository;
import com.tfpmonteiro.osworks.domain.service.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	//TODO REFATORAR PARA AS CLASSES CLIENTEDTO E CLIENTEINPUT
	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private ModelMapper modelMapper;

	@GetMapping
	public List<ClienteDto> listar() {
		return toCollection(clienteRepository.findAll());
	}

	@GetMapping("/{clienteId}")
	public ResponseEntity<ClienteDto> listarPorId(@PathVariable Long clienteId) {
		Optional<Cliente> cliente = clienteRepository.findById(clienteId);

		if (cliente.isPresent()) {
			return ResponseEntity.ok(modelMapper.map(cliente.get(), ClienteDto.class));
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ClienteDto cadastrar(@Valid @RequestBody ClienteSaveInput cliente) {
		return modelMapper.map(clienteService.salvar(toModel(cliente)), ClienteDto.class);
	}

	@PutMapping("/{clienteId}")
	@ResponseStatus(HttpStatus.OK)
	public ClienteDto atualizar(@Valid @PathVariable Long clienteId, @RequestBody ClienteUpdateInput cliente) {

		if (!clienteRepository.existsById(clienteId)) {
			new Throwable("Houve um erro ao recuperar os dados do cliente informado.");
			return null;
		}

		cliente.setId(clienteId);
		Cliente retorno = clienteService.salvar(updateToModel(cliente));
		return modelMapper.map(retorno, ClienteDto.class);
	}

	@DeleteMapping("/{clienteId}")
	public ResponseEntity<Void> excluir(@PathVariable Long clienteId) {

		if (!clienteRepository.existsById(clienteId)) {
			return ResponseEntity.notFound().build();
		}

		clienteService.excluir(clienteId);
		return ResponseEntity.noContent().build();
	}
	
	private Cliente toModel(ClienteSaveInput cliente) {
		return modelMapper.map(cliente, Cliente.class);
	}
	
	private Cliente updateToModel(ClienteUpdateInput cliente) {
		return modelMapper.map(cliente, Cliente.class);
	}

	private ClienteDto toModel(Cliente cliente) {
		return modelMapper.map(cliente, ClienteDto.class);
	}
	
	private List<ClienteDto> toCollection(List<Cliente> clientes){
		return clientes.stream()
				.map(cliente -> toModel(cliente))
				.collect(Collectors.toList());
	}
}
