package api.resources;

import api.contracts.requests.GetPaymentInfoRequest;
import api.contracts.requests.GetPayseraParamsRequest;
import api.contracts.responses.GetPaymentInfoResponse;
import api.contracts.responses.GetPayseraParamsResponse;
import api.handlers.payments.GetPaymentInfoHandler;
import api.handlers.payments.GetPayseraParamsHandler;
import api.handlers.utilities.StatusResolver;
import io.swagger.annotations.ApiOperation;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Mindaugas on 29/04/2016.
 */
@Path("/payments")
public class PaymentsResource {
    @EJB
    private GetPayseraParamsHandler getPayseraParamsHandler;
    @EJB
    private GetPaymentInfoHandler getPaymentInfoHandler;

    @GET
    @Produces("application/json")
    @Path("/parameters/{paymentId}")
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
    @Path("/payment/{paymentId}")
    @ApiOperation(value = "Get payment info", response = GetPaymentInfoResponse.class)
    public Response getPaymentInfo(@PathParam("paymentId") int paymentId) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        GetPaymentInfoRequest request = new GetPaymentInfoRequest();

        request.PaymentId = paymentId;

        GetPaymentInfoResponse response = getPaymentInfoHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    }
