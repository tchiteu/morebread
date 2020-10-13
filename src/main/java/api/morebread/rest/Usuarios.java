package api.morebread.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
	public Response buscarTodos() {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		List<Usuario> usuarios = usuarioDAO.buscar();
		
		return this.buildResponse(usuarios);
	}

	@GET
	@Path("/{id}")
	public Response buscarPorId(@PathParam("id") int id) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		
		Usuario usuario = usuarioDAO.buscarPorId(id);

		if(usuario.getNome() != null) {
			return this.buildResponse(usuario);
		}
		else {
			return this.buildErrorResponse("Usuário não encontrado.");
		}
	}

	@PUT
	@Path("/{id}")
	public Response editar(@PathParam("id") int id, Usuario usuario) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		
		if(usuarioDAO.editar(id, usuario)) {
			return this.buildResponse("Usuário editado com sucesso");
		} else {
			return this.buildErrorResponse("Erro ao editar usuário.");
		}
	}

	@DELETE
	@Path("/{id}")
	public Response deletar(@PathParam("id") int id) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();

		if(usuarioDAO.deletar(id)) {
			return this.buildResponse("Usuário deletado.");
		} else {
			return this.buildErrorResponse("Erro ao deletar usuário.");
		}
	}
}