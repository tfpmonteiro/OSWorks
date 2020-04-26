package com.tfpmonteiro.osworks.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tfpmonteiro.osworks.api.model.ComentarioDto;
import com.tfpmonteiro.osworks.api.model.ComentarioInput;
import com.tfpmonteiro.osworks.domain.exception.EntidadeNaoEncontadaException;
import com.tfpmonteiro.osworks.domain.model.Comentario;
import com.tfpmonteiro.osworks.domain.model.OrdemServico;
import com.tfpmonteiro.osworks.domain.repository.OrdemServicoRepository;
import com.tfpmonteiro.osworks.domain.service.OrdemServicoService;

@RestController
@RequestMapping("/ordens-servico/{ordemServicoId}/comentarios")
public class ComentarioController {

	@Autowired
	private OrdemServicoService ordemServicoService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private OrdemServicoRepository ordemServicoRepository;
	
	@GetMapping
	public List<ComentarioDto> listar(@PathVariable Long ordemServicoId) {
		OrdemServico ordemservico = ordemServicoRepository.findById(ordemServicoId)
				.orElseThrow(() -> new EntidadeNaoEncontadaException("Ordem de Serviço não encontrada."));
		
		return toCollectionDto(ordemservico.getComentarios());
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ComentarioDto adicionarComentario(@PathVariable Long ordemServicoId, @Valid @RequestBody ComentarioInput comentarioInput) {
		
		Comentario comentario = ordemServicoService.adicionarComentario(ordemServicoId, comentarioInput.getDescricao());
		return toModel(comentario);
	}
	
	private ComentarioDto toModel(Comentario comentario) {
		return modelMapper.map(comentario, ComentarioDto.class);
	}
	
	private List<ComentarioDto> toCollectionDto(List<Comentario> comentarios) {
		return comentarios.stream()
				.map(comentario -> toModel(comentario))
				.collect(Collectors.toList());
	}
}
