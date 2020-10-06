package api.morebread.rest;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import api.morebread.model.Usuario;
import api.morebread.controller.UsuariosController;

@Path("/usuarios")
public class Usuarios {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/")
	public ArrayList<Usuario> buscaTodos() {
		ArrayList<Usuario> usuarios = UsuariosController.buscaTodos();

		return usuarios;
	}
}