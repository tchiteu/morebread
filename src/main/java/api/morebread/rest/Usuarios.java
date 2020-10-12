package api.morebread.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import api.morebread.model.Usuario;
import api.morebread.jdbc.UsuarioDAO;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Usuarios extends UtilRest {
	@POST
	public Response cadastro(Usuario usuario) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();

		if(usuarioDAO.cadastra(usuario)) {
			return this.buildResponse("Usuário cadastrado com sucesso.");
		}
		else {
			return this.buildErrorResponse("Erro ao cadastrar usuário.");
		}
	}

	@GET
	public Response buscaTodos() {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		List<Usuario> usuarios = usuarioDAO.busca();
		
		return this.buildResponse(usuarios);
	}
}