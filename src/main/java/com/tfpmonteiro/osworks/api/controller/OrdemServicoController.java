package com.tfpmonteiro.osworks.api.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tfpmonteiro.osworks.api.model.OrdemServicoDto;
import com.tfpmonteiro.osworks.api.model.OrdemServicoInput;
import com.tfpmonteiro.osworks.domain.model.OrdemServico;
import com.tfpmonteiro.osworks.domain.repository.OrdemServicoRepository;
import com.tfpmonteiro.osworks.domain.service.OrdemServicoService;

@RestController
@RequestMapping("/ordens-servico")
public class OrdemServicoController {

	@Autowired
	private OrdemServicoService ordemServicoService;

	@Autowired
	private OrdemServicoRepository ordemservicoRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public OrdemServicoDto criar(@Valid @RequestBody OrdemServicoInput osInput) {
		OrdemServico ordemServico = toEntity(osInput);
		
		return toModel(ordemServicoService.criar(ordemServico));
	}

	@GetMapping
	public List<OrdemServicoDto> listar() {
		return toCollectionModel(ordemservicoRepository.findAll());
	}

	@GetMapping("/{ordemServicoId}")
	public ResponseEntity<OrdemServicoDto> listarPorId(@PathVariable Long ordemServicoId) {
		Optional<OrdemServico> ordemServico = ordemservicoRepository.findById(ordemServicoId);

		if (ordemServico.isPresent()) {
			OrdemServicoDto OsModel = toModel(ordemServico.get());
			return ResponseEntity.ok(OsModel);
		}

		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{ordemServicoId}/finalizar")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void finalizarOrdemServico(@PathVariable Long ordemServicoId) {
		ordemServicoService.finalizar(ordemServicoId);
	}
	
	@PutMapping("/{ordemServicoId}/cancelar")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void cancelarrOrdemServico(@PathVariable Long ordemServicoId) {
		ordemServicoService.cancelar(ordemServicoId);
	}
	
	private OrdemServicoDto toModel(OrdemServico ordemservico) {
		return modelMapper.map(ordemservico, OrdemServicoDto.class);
	}
	
	private List<OrdemServicoDto> toCollectionModel (List<OrdemServico> ordemServico){
		return ordemServico.stream()
				.map(ordem -> toModel(ordem))
				.collect(Collectors.toList());
	}
	
	private OrdemServico toEntity(OrdemServicoInput osInput) {
		return modelMapper.map(osInput, OrdemServico.class);
	}

}
