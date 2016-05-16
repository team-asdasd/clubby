package api.resources;

import api.contracts.recommendations.ConfirmRecommendationRequest;
import api.contracts.recommendations.GetRecommendationsRequest;
import api.contracts.recommendations.SendRecommendationRequest;
import api.contracts.recommendations.ConfirmRecommendationResponse;
import api.contracts.recommendations.GetRecommendationsResponse;
import api.contracts.recommendations.SendRecommendationResponse;
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
@Produces({"application/json; charset=UTF-8"})
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
    @ApiOperation(value = "Gets recommendation recommendations list", response = GetRecommendationsResponse.class)
    public Response getRecommendations() {
        GetRecommendationsRequest request = new GetRecommendationsRequest();

        GetRecommendationsResponse response = getRecommendationsHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }
}
