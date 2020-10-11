package api.morebread.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import api.morebread.model.Usuario;
import api.morebread.jdbc.UsuarioDAO;

@Path("/usuarios")
public class Usuarios extends UtilRest {
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/")
	public Response cadastro(Usuario usuario) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();

		if(usuarioDAO.cadastra(usuario)) {
			return this.buildResponse("Usuário cadastrado com sucesso.");
		}
		else {
			return this.buildErrorResponse("Erro ao cadastrar usuário.");
		}
	}
}