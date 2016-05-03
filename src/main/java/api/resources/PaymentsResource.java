package api.resources;

import api.contracts.payments.GetPaymentInfoRequest;
import api.contracts.payments.GetPaymentInfoResponse;
import api.handlers.payments.GetPaymentInfoHandler;
import api.handlers.utilities.StatusResolver;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Created by Mindaugas on 29/04/2016.
 */
@Path("/payments")
public class PaymentsResource {
    @Inject
    private GetPaymentInfoHandler getPaymentInfoHandler;

    @GET
    @Produces("application/json")
    @Path("{paymentId}")
    @ApiOperation(value = "Get payment info", response = GetPaymentInfoResponse.class)
    public Response getPaymentInfo(@PathParam("paymentId") int paymentId) {
        GetPaymentInfoRequest request = new GetPaymentInfoRequest();

        request.PaymentId = paymentId;

        GetPaymentInfoResponse response = getPaymentInfoHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }
}
