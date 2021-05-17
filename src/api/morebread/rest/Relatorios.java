package api.morebread.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import api.morebread.model.Retorno;
import api.morebread.jdbc.AuthDAO;
import api.morebread.jdbc.VendaDAO;

@Path("/relatorios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Relatorios extends UtilRest {
	private VendaDAO vendaDAO = new VendaDAO();
	private AuthDAO authDAO = new AuthDAO();
	
	@GET
	@Path("/vendas")
	public Response relatorioVendas(@QueryParam("dt_inicio") String dt_inicio, @QueryParam("dt_fim") String dt_fim, @Context HttpHeaders httpheaders) {
		if (!authDAO.verificaToken(httpheaders.getHeaderString("Authorization"))) {
			return this.unauthorizedResponse();
		}
		
		Retorno retorno = vendaDAO.gerarRelatorio(dt_inicio, dt_fim);
		
		if (!retorno.getErro()) {
			retorno.setStatusCode(200);
			return this.buildResponse(retorno);
		}
		else {
			return this.buildErrorResponse(404, "Erro ao gerar relatório.");
		}
	}
}