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
		
		Retorno retorno = vendaDAO.cadastraVenda(venda);
		
		if (!retorno.getErro()) {
			retorno.setStatusCode(200);
			retorno.setMensagem("Venda cadastrada com sucesso!");
			return this.buildResponse(retorno);
		}
		else {
			return this.buildErrorResponse(404, "Erro ao cadastrar venda.");
		}
	}
}