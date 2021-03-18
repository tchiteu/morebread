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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import api.morebread.model.Usuario;
import api.morebread.model.Retorno;
import api.morebread.jdbc.UsuarioDAO;
import api.morebread.jdbc.AuthDAO;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Usuarios extends UtilRest {
	private AuthDAO authDAO = new AuthDAO();
	
	@POST
	public Response cadastro(Usuario usuario, @Context HttpHeaders httpheaders) {
		if (!authDAO.verificaToken(httpheaders.getHeaderString("Authorization"))) {
			return this.unauthorizedResponse();
		}
		
		UsuarioDAO usuarioDAO = new UsuarioDAO();

		if(usuarioDAO.cadastrar(usuario)) {
			return this.buildResponse(201, "Usuário cadastrado com sucesso");
		}
		else {
			return this.buildErrorResponse(400, "Erro ao cadastrar usuário");
		}
	}

	@GET
	public Response buscarTodos(@Context HttpHeaders httpheaders) {
		if (!authDAO.verificaToken(httpheaders.getHeaderString("Authorization"))) {
			return this.unauthorizedResponse();
		}
		
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		List<Usuario> usuarios = usuarioDAO.buscar();

		Retorno retorno = new Retorno(200);
		retorno.setUsuarios(usuarios);
		
		return this.buildResponse(retorno);
	}

	@GET
	@Path("/{id}")
	public Response buscarPorId(@PathParam("id") int id, @Context HttpHeaders httpheaders) {
		if (!authDAO.verificaToken(httpheaders.getHeaderString("Authorization"))) {
			return this.unauthorizedResponse();
		}
		
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		
		Usuario usuario = usuarioDAO.buscarPorId(id);

		if(usuario.getNome() != null) {
			Retorno retorno = new Retorno(200);
			retorno.setUsuario(usuario);
			
			return this.buildResponse(retorno);
		}
		else {
			return this.buildErrorResponse(404, "Usuário não encontrado");
		}
	}

	@PUT
	@Path("/{id}")
	public Response editar(@PathParam("id") int id, Usuario usuario, @Context HttpHeaders httpheaders) {
		if (!authDAO.verificaToken(httpheaders.getHeaderString("Authorization"))) {
			return this.unauthorizedResponse();
		}
		
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		
		if(usuarioDAO.editar(id, usuario)) {
			return this.buildResponse(200, "Usuário editado com sucesso");
		} else {
			return this.buildErrorResponse(400, "Erro ao editar usuário");
		}
	}

	@DELETE
	@Path("/{id}")
	public Response deletar(@PathParam("id") int id, @Context HttpHeaders httpheaders) {
		if (!authDAO.verificaToken(httpheaders.getHeaderString("Authorization"))) {
			return this.unauthorizedResponse();
		}
		
		UsuarioDAO usuarioDAO = new UsuarioDAO();

		if(usuarioDAO.deletar(id)) {
			return this.buildResponse(200, "Usuário deletado.");
		} else {
			return this.buildErrorResponse(400, "Erro ao deletar usuário");
		}
	}
}