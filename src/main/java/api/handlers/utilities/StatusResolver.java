package api.handlers.utilities;

import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorCodes;

import java.util.ArrayList;
import java.util.List;

public class StatusResolver {

    public static int getStatusCode(BaseResponse response) {
        if (response.Errors == null) {
            return HttpStatusCodes.OK;
        } else {
            if (response.Errors.stream().anyMatch(errorDto -> errorDto.Code.ordinal() == ErrorCodes.UNAUTHENTICATED.ordinal())) {
                return HttpStatusCodes.UNAUTHORIZED;
            }

            if (response.Errors.stream().anyMatch(errorDto -> errorDto.Code.ordinal() == ErrorCodes.ACCESS_DENIED.ordinal())) {
                return HttpStatusCodes.FORBIDDEN;
            }

            if (response.Errors.stream().anyMatch(errorDto -> errorDto.Code.ordinal() == ErrorCodes.NOT_FOUND.ordinal())) {
                return HttpStatusCodes.NOT_FOUND;
            }

            if (isBadRequest(response)) {
                return HttpStatusCodes.BAD_REQUEST;
            }


            return HttpStatusCodes.INTERNAL_SERVER_ERROR;
        }
    }

    private static boolean isBadRequest(BaseResponse response) {
        ArrayList<ErrorCodes> badRequestCodes = new ArrayList<>();
        badRequestCodes.add(ErrorCodes.DUPLICATE_EMAIL);
        badRequestCodes.add(ErrorCodes.DUPLICATE_USERNAME);
        badRequestCodes.add(ErrorCodes.INCORRECT_EMAIL);
        badRequestCodes.add(ErrorCodes.LOW_BALANCE);
        badRequestCodes.add(ErrorCodes.VALIDATION_ERROR);

        return response.Errors.stream().allMatch(errorDto -> badRequestCodes.contains(errorDto.Code));
    }
}
