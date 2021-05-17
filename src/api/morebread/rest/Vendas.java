package api.morebread.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import api.morebread.model.Venda;
import api.morebread.model.Retorno;
import api.morebread.jdbc.AuthDAO;
import api.morebread.jdbc.VendaDAO;

@Path("/vendas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Vendas extends UtilRest {
	private VendaDAO vendaDAO = new VendaDAO();
	private AuthDAO authDAO = new AuthDAO();
	
	@POST
	public Response cadastraVenda(Venda venda, @Context HttpHeaders httpheaders) {
		if (!authDAO.verificaToken(httpheaders.getHeaderString("Authorization"))) {
			return this.unauthorizedResponse();
		}
		
		Retorno retorno = vendaDAO.cadastrar(venda);
		
		if (!retorno.getErro()) {
			retorno.setStatusCode(200);
			retorno.setMensagem("Venda cadastrada com sucesso!");
			return this.buildResponse(retorno);
		}
		else {
			return this.buildErrorResponse(404, "Erro ao cadastrar venda.");
		}
	}
	
	@GET
	public Response buscaVendas(@Context HttpHeaders httpheaders) {
		if (!authDAO.verificaToken(httpheaders.getHeaderString("Authorization"))) {
			return this.unauthorizedResponse();
		}
		
		Retorno retorno = vendaDAO.listar();
		
		if (!retorno.getErro()) {
			retorno.setStatusCode(200);
			return this.buildResponse(retorno);
		}
		else {
			return this.buildErrorResponse(404, "Erro ao buscar vendas.");
		}
	}
	
	@DELETE
	@Path("/{id}")
	public Response DeletaVenda(@PathParam("id") Integer id, @Context HttpHeaders httpheaders) {
		if (!authDAO.verificaToken(httpheaders.getHeaderString("Authorization"))) {
			return this.unauthorizedResponse();
		}
		
		if(vendaDAO.deletar(id)) {
			return this.buildResponse(200, "Venda deletada com sucesso!");
		}	
		else {
			return this.buildErrorResponse(404, "Erro ao deletar venda.");
		}
	}
}