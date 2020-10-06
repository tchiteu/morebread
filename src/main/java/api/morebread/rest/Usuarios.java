package api.morebread.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import api.morebread.model.Usuario;
import api.morebread.controller.UsuariosController;


@Path("/usuarios")
public class Usuarios {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/")
	public static Response busca() {
		Response retorno = UsuariosController.buscaTodos();
		
		return retorno;
	}
}