package api.morebread.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import api.morebread.model.Retorno;

public class UtilRest {
	public Response buildResponse(Retorno retorno) {
		try {
		  ResponseBuilder response = Response.status(retorno.getStatusCode());
		  retorno.toResponse();
		  response.entity(retorno);
		
		  return response.build();
		}
		catch(Exception e) {
		  e.printStackTrace();
		  String mensagemErro = e.getMessage();

		  return this.buildErrorResponse(500, mensagemErro);
		}
	}
	
	public Response buildResponse(int status_code, String mensagem) {
	  try {
		Retorno retorno = new Retorno(status_code, mensagem);
		ResponseBuilder response = Response.status(status_code);
		retorno.toResponse();
		response = response.entity(retorno);
		
		return response.build();
	  }
	  catch(Exception e) {
		e.printStackTrace();
		String mensagemErro = e.getMessage();

		return this.buildErrorResponse(500, mensagemErro);
	  }
	}
	
	public Response buildErrorResponse(int status_code, String mensagem) {
		ResponseBuilder response = Response.status(status_code);
		Retorno retorno = new Retorno(mensagem);
		response = response.entity(retorno);
	
	  return response.build();
	}
	
	public Response unauthorizedResponse() {
	  ResponseBuilder response = Response.status(401);
	  Retorno retorno = new Retorno("Não autorizado!");
	  response = response.entity(retorno);
	
	  return response.build();
	}
}