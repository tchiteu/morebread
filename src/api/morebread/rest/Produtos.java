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

import api.morebread.model.Produto;
import api.morebread.model.Retorno;
import api.morebread.jdbc.ProdutoDAO;
import api.morebread.jdbc.AuthDAO;

@Path("/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Produtos extends UtilRest {
	private AuthDAO authDAO = new AuthDAO();
	private ProdutoDAO produtoDAO = new ProdutoDAO();
	
	@POST
	public Response cadastro(Produto produto, @Context HttpHeaders httpheaders) {
		if (!authDAO.verificaToken(httpheaders.getHeaderString("Authorization"))) {
			return this.unauthorizedResponse();
		}

		if(produtoDAO.cadastrar(produto)) {
			return this.buildResponse(201, "Produto cadastrado com sucesso");
		}
		else {
			return this.buildErrorResponse(400, "Erro ao cadastrar produto");
		}
	}

	@GET
	public Response buscarTodos(@Context HttpHeaders httpheaders) {
		if (!authDAO.verificaToken(httpheaders.getHeaderString("Authorization"))) {
			return this.unauthorizedResponse();
		}
		
		List<Produto> produtos = produtoDAO.buscar();

		Retorno retorno = new Retorno(200);
		retorno.setProdutos(produtos);
		
		return this.buildResponse(retorno);
	}
//
//	@GET
//	@Path("/{id}")
//	public Response buscarPorId(@PathParam("id") int id, @Context HttpHeaders httpheaders) {
//		if (!authDAO.verificaToken(httpheaders.getHeaderString("Authorization"))) {
//			return this.unauthorizedResponse();
//		}
//		
//		UsuarioDAO usuarioDAO = new UsuarioDAO();
//		
//		Usuario usuario = usuarioDAO.buscarPorId(id);
//
//		if(usuario.getNome() != null) {
//			Retorno retorno = new Retorno(200);
//			retorno.setUsuario(usuario);
//			
//			return this.buildResponse(retorno);
//		}
//		else {
//			return this.buildErrorResponse(404, "Usuário não encontrado");
//		}
//	}
//
	@PUT
	@Path("/{id}")
	public Response editar(@PathParam("id") Integer id, Produto produto, @Context HttpHeaders httpheaders) {
		if (!authDAO.verificaToken(httpheaders.getHeaderString("Authorization"))) {
			return this.unauthorizedResponse();
		}
		
		if(produtoDAO.editar(id, produto)) {
			return this.buildResponse(200, "Produto editado com sucesso");
		} else {
			return this.buildErrorResponse(400, "Erro ao editar produto");
		}
	}

	@DELETE
	@Path("/{id}")
	public Response deletar(@PathParam("id") Integer id, @Context HttpHeaders httpheaders) {
		if (!authDAO.verificaToken(httpheaders.getHeaderString("Authorization"))) {
			return this.unauthorizedResponse();
		}

		if(produtoDAO.deletar(id)) {
			return this.buildResponse(200, "Produto deletado.");
		} else {
			return this.buildErrorResponse(400, "Erro ao deletar produto");
		}
	}
}