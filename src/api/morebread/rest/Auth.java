package api.morebread.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import api.morebread.model.Usuario;
import api.morebread.model.Retorno;
import api.morebread.jdbc.AuthDAO;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Auth extends UtilRest {
	@POST
	public Response login(Usuario usuario) {
		AuthDAO authDAO = new AuthDAO();
		Retorno retorno = authDAO.login(usuario);

		if (!retorno.getErro()) {
			retorno.setStatusCode(200);
			return this.buildResponse(retorno);
		}
		else {
			return this.buildErrorResponse(404, "Erro ao realizar login.");
		}
	}

	@PUT
	public Response logout(@Context HttpHeaders httpheaders) {
	  String token = httpheaders.getHeaderString("Authorization");
	  AuthDAO authDAO = new AuthDAO();
		
	  if (authDAO.logout(token)) {
		return this.buildResponse(204, "Deslogado com sucesso!");
	  } 
	  else {
		return this.buildErrorResponse(404, "Erro apagar token");
	  }
	}
}