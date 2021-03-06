package org.rebelo.demoSB.controladores;

import org.rebelo.demoSB.DTO.LoginDTO;
import org.rebelo.demoSB.seguranca.Constantes;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/*
 * A autenticação é implementada na realidade pelos filtros do Spring Security (que tem precedência sobre 
 * os controladores); Este controlador é apenas um fake para constar na documentação do Swagger e 
 * esclarecer os usuários sobre o endpoint de autenticação.
 */

@Api(value = "Endpoint de Autenticação de Usuários", 
tags = "Endpoint de Autenticação de Usuários")
@RestController
public class ControladorAutenticacao {

	 final String endpoint = Constantes.LOGIN_API;
	
	public ControladorAutenticacao() {
	
	}
	
	@ApiOperation(value = "Login", notes = "Login com cpf e senha")
	@RequestMapping(value = endpoint, method = RequestMethod.POST)
	   public void login(@RequestBody LoginDTO dadosDeLogin) 
	   {
	       throw new IllegalStateException("Implementado pelo Spring Security");
	   }

}
