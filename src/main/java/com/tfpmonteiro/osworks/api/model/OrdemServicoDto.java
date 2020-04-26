package com.tfpmonteiro.osworks.api.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.tfpmonteiro.osworks.domain.model.StatusOrdemServico;

public class OrdemServicoDto {

	private Long id;
	private ClienteDto cliente;
	private String descricao;;
	private BigDecimal preco;
	private StatusOrdemServico status;
	private OffsetDateTime dataAbertura;
	private OffsetDateTime dataEncerramento;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ClienteDto getCliente() {
		return cliente;
	}
	public void setCliente(ClienteDto cliente) {
		this.cliente = cliente;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public BigDecimal getPreco() {
		return preco;
	}
	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}
	public StatusOrdemServico getStatus() {
		return status;
	}
	public void setStatus(StatusOrdemServico status) {
		this.status = status;
	}
	public OffsetDateTime getDataAbertura() {
		return dataAbertura;
	}
	public void setDataAbertura(OffsetDateTime dataAbertura) {
		this.dataAbertura = dataAbertura;
	}
	public OffsetDateTime getDataEncerramento() {
		return dataEncerramento;
	}
	public void setDataEncerramento(OffsetDateTime dataEncerramento) {
		this.dataEncerramento = dataEncerramento;
	}
	
	
}
