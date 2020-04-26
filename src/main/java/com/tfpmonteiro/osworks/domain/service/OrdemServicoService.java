package com.tfpmonteiro.osworks.domain.service;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfpmonteiro.osworks.domain.exception.EntidadeNaoEncontadaException;
import com.tfpmonteiro.osworks.domain.exception.ExceptionsDomain;
import com.tfpmonteiro.osworks.domain.model.Cliente;
import com.tfpmonteiro.osworks.domain.model.Comentario;
import com.tfpmonteiro.osworks.domain.model.OrdemServico;
import com.tfpmonteiro.osworks.domain.model.StatusOrdemServico;
import com.tfpmonteiro.osworks.domain.repository.ClienteRepository;
import com.tfpmonteiro.osworks.domain.repository.ComentarioRepository;
import com.tfpmonteiro.osworks.domain.repository.OrdemServicoRepository;

@Service
public class OrdemServicoService {

	@Autowired
	private OrdemServicoRepository ordemServicoRepository;
	
	@Autowired
	private ClienteRepository clienteReository;
	
	@Autowired
	private ComentarioRepository comentarioRepository;
	
	public OrdemServico criar(OrdemServico ordemServico) {
		Cliente cliente = buscarCliente(ordemServico);
		
		ordemServico.setStatus(StatusOrdemServico.ABERTA);
		ordemServico.setDataAbertura(OffsetDateTime.now());
		ordemServico.setCliente(cliente);
		
		
		return ordemServicoRepository.save(ordemServico);
	}

	private Cliente buscarCliente(OrdemServico ordemServico) {
		return clienteReository.findById(ordemServico.getcliente().getId())
				.orElseThrow(() -> new ExceptionsDomain("Cliente não encontrado"));
	}
	
	public Comentario adicionarComentario(Long ordemServico, String descricao) {
		
		OrdemServico os = buscarOrdemServico(ordemServico);
		
		Comentario comentario = new Comentario();
		comentario.setDataEnvio(OffsetDateTime.now());
		comentario.setDescricao(descricao);
		comentario.setOrdemServico(os);
		
		return comentarioRepository.save(comentario);
	}

	private OrdemServico buscarOrdemServico(Long ordemServico) {
		return ordemServicoRepository.findById(ordemServico)
				.orElseThrow(() -> new EntidadeNaoEncontadaException("Ordem de Serviço não encontrado"));
	}
	
	public void finalizar(Long osId) {
		OrdemServico ordemServico = buscarOrdemServico(osId);
		
		ordemServico.finalizar();
		
		ordemServicoRepository.save(ordemServico);
	}

	public void cancelar(Long ordemServicoId) {
		OrdemServico ordemServico = buscarOrdemServico(ordemServicoId);
		
		ordemServico.cancelar();
		
		ordemServicoRepository.save(ordemServico);
		
	}
}
