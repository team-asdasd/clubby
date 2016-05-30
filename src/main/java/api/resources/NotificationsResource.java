package api.resources;

import api.contracts.base.BaseRequest;
import api.contracts.base.BaseResponse;
import api.contracts.notifications.CreateNotificationRequest;
import api.contracts.notifications.NotificationsResponse;
import api.contracts.notifications.ReadNotificationsRequest;
import api.handlers.notifications.CreateNotificationHandler;
import api.handlers.notifications.GetNotificationsHandler;
import api.handlers.notifications.ReadNotificationHandler;
import api.handlers.reservations.CreateReservationsGroupsHandler;
import api.handlers.utilities.StatusResolver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Api(value = "notifications")
@Path("/notifications")
@Produces({"application/json; charset=UTF-8"})
public class NotificationsResource {
    @Inject
    private GetNotificationsHandler getNotificationsHandler;
    @Inject
    private CreateNotificationHandler createNotificationsHandler;
    @Inject
    private ReadNotificationHandler readNotificationHandler;

    @GET
    @ApiOperation(value = "Returns all notifications", response = NotificationsResponse.class)
    public Response getAll() {
        NotificationsResponse response = getNotificationsHandler.handle(new BaseRequest());

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @POST
    @ApiOperation(value = "Creates new notification", response = BaseResponse.class)
    public Response create(CreateNotificationRequest request) {
        BaseResponse response = createNotificationsHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }

    @POST
    @Path("/read")
    @ApiOperation(value = "Mark notifications as read", response = BaseResponse.class)
    public Response create(ReadNotificationsRequest request) {
        BaseResponse response = readNotificationHandler.handle(request);

        int statusCode = StatusResolver.getStatusCode(response);

        return Response.status(statusCode).entity(response).build();
    }
}
