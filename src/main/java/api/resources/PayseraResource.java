package api.resources;

import api.contracts.base.ErrorDto;
import api.contracts.payments.GetPayseraParamsRequest;
import api.contracts.payments.GetPayseraParamsResponse;
import api.contracts.payments.PayseraCallbackRequest;
import api.contracts.payments.PayseraCallbackResponse;
import api.handlers.payments.GetPayseraParamsHandler;
import api.handlers.payments.PayseraCallbackHandler;
import api.handlers.utilities.StatusResolver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by Mindaugas on 03/05/2016.
 */
@Api(value = "paysera")
@Path("/paysera")
public class PayseraResource {
    @Inject
    private GetPayseraParamsHandler getPayseraParamsHandler;
    @Inject
    private PayseraCallbackHandler payseraCallbackHandler;

    @GET
    @Produces("application/json")
    @Path("parameters/{paymentId}")
    @ApiOperation(value = "Get paysera params for payment", response = GetPayseraParamsResponse.class)
    public Response getPayseraParams(@PathParam("paymentId") int paymentId){
        GetPayseraParamsRequest request = new GetPayseraParamsRequest();

        request.PaymentId = paymentId;

        GetPayseraParamsResponse response = getPayseraParamsHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @GET
    @Produces("application/json")
    @Path("callback")
    @ApiOperation(value = "Paysera callback url")
    public void payseraCallback(@Context HttpServletRequest request, @Context HttpServletResponse response) throws IOException {
        PayseraCallbackRequest callbackRequest = new PayseraCallbackRequest();
        callbackRequest.data = request.getParameter("data");
        callbackRequest.ss1 = request.getParameter("ss1");
        callbackRequest.ss2 = request.getParameter("ss2");

        PayseraCallbackResponse resp = payseraCallbackHandler.handle(callbackRequest);

        if(resp.Errors != null && resp.Errors.size() > 0){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            for(ErrorDto error : resp.Errors){
                response.getWriter().write(error.toString());
            }
        }else{
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("OK");
        }

        response.getWriter().flush();
        response.getWriter().close();
    }
}

