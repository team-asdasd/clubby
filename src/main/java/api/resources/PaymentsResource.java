package api.resources;

import api.contracts.base.BaseResponse;
import api.contracts.payments.*;
import api.handlers.payments.*;
import api.handlers.utilities.StatusResolver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Api(value = "payments")
@Path("/payments")
@Produces({"application/json; charset=UTF-8"})
public class PaymentsResource {
    @Inject
    private GetPaymentInfoHandler getPaymentInfoHandler;
    @Inject
    private GetMyHistoryPamentsHandler getMyHistoryPamentsHandler;
    @Inject
    private PayClubbyHandler payClubbyHandler;
    @Inject
    private GetPendingPaymentsHandler getPendingPaymentsHandler;
    @Inject
    private GetBalanceHandler getBalanceHandler;
    @Inject
    private GetPaymentsHandler getPaymentsHandler;
    @Inject
    private GiftPointsHandler giftPointsHandler;
    @Inject
    private ChangePaymentPriceHandler changePaymentPriceHandler;

    @GET
    @Path("{paymentId}")
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
    @Path("pay/{paymentId}")
    @ApiOperation(value = "Get payment info", response = PayClubbyResponse.class)
    public Response pay(@PathParam("paymentId") int paymentId) {
        PayClubbyRequest request = new PayClubbyRequest();

        request.PaymentId = paymentId;

        BaseResponse response = payClubbyHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @GET
    @Produces("application/json")
    @Path("me/history")
    @ApiOperation(value = "Get my bpayments", response = GetMyHistoryPaymetsResponse.class)
    public Response getMyHistoryPaments() {
        GetMyHistoryPaymetsRequest request = new GetMyHistoryPaymetsRequest();

        GetMyHistoryPaymetsResponse response = getMyHistoryPamentsHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @GET
    @Produces("application/json")
    @Path("me/pending")
    @ApiOperation(value = "Get my pending payments", response = GetPendingPaymentsResponse.class)
    public Response getMyPendingPaments() {
        GetPendingPaymentsRequest request = new GetPendingPaymentsRequest();

        GetPendingPaymentsResponse response = getPendingPaymentsHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @GET
    @Produces("application/json")
    @Path("me/balance")
    @ApiOperation(value = "Get my current balance", response = GetBalanceResponse.class)
    public Response getMyBalance() {
        GetBalanceRequest request = new GetBalanceRequest();

        GetBalanceResponse response = getBalanceHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @GET
    @Produces("application/json")
    @Path("all/{paymentType}")
    @ApiOperation(value = "Get all/by type payments", response = GetPaymentsResponse.class)
    public Response getPayments(@PathParam("paymentType") int paymentTypeId) {
        GetPaymentsRequest request = new GetPaymentsRequest();

        request.paymentTypeId = paymentTypeId;

        BaseResponse response = getPaymentsHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @POST
    @Produces("application/json")
    @Path("points/give")
    @ApiOperation(value = "Gives a gift of points to a user.", response = BaseResponse.class)
    public Response getPayments(GiftPointsRequest request) {
        BaseResponse response = giftPointsHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }
    @PUT
    @Produces("application/json")
    @Path("membership")
    @ApiOperation(value = "Changes yearly membership payment price.", response = BaseResponse.class)
    public Response changePaymentAmount(ChangePaymentPriceRequest request) {
        BaseResponse response = changePaymentPriceHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }
}
