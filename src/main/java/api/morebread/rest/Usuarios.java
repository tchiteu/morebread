package api.morebread.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.Produces;
// import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;

import api.morebread.model.Usuario;
import api.morebread.controller.UsuariosController;

@Path("/usuarios")
public class Usuarios {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/")
	public Usuario legal(String msg) {
		Usuario retorno = UsuariosController.cadastraUsuario("Mat","Mat","Mat","Mat");

		return retorno;
	}
}