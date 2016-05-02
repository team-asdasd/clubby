package api.resources;

import api.contracts.requests.ConfirmRecommendationRequest;
import api.contracts.requests.GetRecommendationsRequestsRequest;
import api.contracts.requests.SendRecommendationRequest;
import api.contracts.responses.ConfirmRecommendationResponse;
import api.contracts.responses.GetRecommendationsResponse;
import api.contracts.responses.SendRecommendationResponse;
import api.handlers.Recommendation.ConfirmRecommendationHandler;
import api.handlers.Recommendation.GetRecommendationsHandler;
import api.handlers.Recommendation.SendRecommendationHandler;
import api.handlers.utilities.StatusResolver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Api(value = "recommendation")
@Path("/recommendation")
@Produces({"application/json"})
public class RecommendationResource {
    @Inject
    private ConfirmRecommendationHandler confirmRecommendationHandler;
    @Inject
    private SendRecommendationHandler sendRecommendationHandler;
    @Inject
    private GetRecommendationsHandler getRecommendationsHandler;

    @POST
    @Path("/confirm/{recommendationCode}")
    @ApiOperation(value = "Recommends user by recommendation code", response = ConfirmRecommendationResponse.class)
    public Response recommend(@PathParam("recommendationCode") String recommendationCode) {
        ConfirmRecommendationRequest request = new ConfirmRecommendationRequest();
        request.recommendationCode = recommendationCode;

        ConfirmRecommendationResponse response = confirmRecommendationHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @POST
    @Path("/send/{userEmail}")
    @ApiOperation(value = "Sends recommendation request", response = SendRecommendationResponse.class)
    public Response sendRecommendRequest(@PathParam("userEmail") String userEmail) {
        SendRecommendationRequest request = new SendRecommendationRequest();
        request.UserEmail = userEmail;

        SendRecommendationResponse response = sendRecommendationHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @GET
    @ApiOperation(value = "Gets recommendation requests list", response = GetRecommendationsResponse.class)
    public Response sendRecommendRequest() {
        GetRecommendationsRequestsRequest request = new GetRecommendationsRequestsRequest();

        GetRecommendationsResponse response = getRecommendationsHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }
}
