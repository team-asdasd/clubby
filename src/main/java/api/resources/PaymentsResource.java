package api.resources;

import api.contracts.base.ErrorDto;
import api.contracts.payments.GetPaymentInfoRequest;
import api.contracts.payments.GetPayseraParamsRequest;
import api.contracts.payments.PayseraCallbackRequest;
import api.contracts.payments.GetPaymentInfoResponse;
import api.contracts.payments.GetPayseraParamsResponse;
import api.contracts.payments.PayseraCallbackResponse;
import api.handlers.payments.GetPaymentInfoHandler;
import api.handlers.payments.GetPayseraParamsHandler;
import api.handlers.payments.PayseraCallbackHandler;
import api.handlers.utilities.StatusResolver;
import io.swagger.annotations.ApiOperation;

import javax.ejb.EJB;
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
 * Created by Mindaugas on 29/04/2016.
 */
@Path("/payments")
public class PaymentsResource {
    @Inject
    private GetPayseraParamsHandler getPayseraParamsHandler;
    @Inject
    private GetPaymentInfoHandler getPaymentInfoHandler;
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
    @Path("payment/{paymentId}")
    @ApiOperation(value = "Get payment info", response = GetPaymentInfoResponse.class)
    public Response getPaymentInfo(@PathParam("paymentId") int paymentId) {
        GetPaymentInfoRequest request = new GetPaymentInfoRequest();

        request.PaymentId = paymentId;

        GetPaymentInfoResponse response = getPaymentInfoHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @GET
    @Produces("application/json")
    @Path("payseraCallback")
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
