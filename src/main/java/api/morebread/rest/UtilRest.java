package api.morebread.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;


public class UtilRest {
	public class CustomJson {
		public String msg;
		public Object data;
		
		public CustomJson(String msg) {
			this.msg = msg;
		}

		public CustomJson(Object data) {
			this.data = data;
		}
	}

	public Response buildResponse(Object result) {
		try {
			return Response.ok(result).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return this.buildErrorResponse(ex.getMessage());
		}
	}
	
	public Response buildErrorResponse(String msg) {
		ResponseBuilder rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		CustomJson json = new CustomJson(msg);

		rb = rb.entity(json);
		
		return rb.build();
	}
}