package com.tfpmonteiro.osworks.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.tfpmonteiro.osworks.domain.exception.EntidadeNaoEncontadaException;
import com.tfpmonteiro.osworks.domain.exception.ExceptionsDomain;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;
	
	@ExceptionHandler(EntidadeNaoEncontadaException.class)
	public ResponseEntity<Object> handleEEntidadeNaoEncontadaException(ExceptionsDomain ex, WebRequest request) {
		var status = HttpStatus.BAD_REQUEST;
		
		var erro = new RetornoStatusError();
		erro.setStatus(status.value());
		erro.setTitulo(ex.getMessage());
		erro.setDataHora(OffsetDateTime.now());
		
		return handleExceptionInternal(ex, erro, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(ExceptionsDomain.class)
	public ResponseEntity<Object> handleExceptionsDomain(ExceptionsDomain ex, WebRequest request) {
		var status = HttpStatus.BAD_REQUEST;
		
		var erro = new RetornoStatusError();
		erro.setStatus(status.value());
		erro.setTitulo(ex.getMessage());
		erro.setDataHora(OffsetDateTime.now());
		
		return handleExceptionInternal(ex, erro, new HttpHeaders(), status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

		var campos = new ArrayList<RetornoStatusError.Campo>();
		
		for(ObjectError error: ex.getBindingResult().getAllErrors()) {
			String nome = ((FieldError) error).getField();
			String mensagem = messageSource.getMessage(error, LocaleContextHolder.getLocale());
			
			campos.add(new RetornoStatusError.Campo(nome, mensagem));
		}
		
		var retornoStatusErro = new RetornoStatusError();
		retornoStatusErro.setStatus(status.value());
		retornoStatusErro.setTitulo("Um ou mais campos estão inválidos. "
				+ "Faça o preenchimento correto e tente novamente.");
		retornoStatusErro.setDataHora(OffsetDateTime.now());
		retornoStatusErro.setCampos(campos);
		
		return super.handleExceptionInternal(ex, retornoStatusErro, headers, status, request);
	}
}
