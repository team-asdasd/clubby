package api.handlers.utilities;

import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorCodes;
import com.google.api.client.http.HttpStatusCodes;

public class StatusResolver {

    public static int getStatusCode(BaseResponse response) {

        if (response.Errors == null) {
            return HttpStatusCodes.STATUS_CODE_OK;
        } else {
            if (response.Errors.stream().allMatch(errorDto ->
                    errorDto.Code.ordinal() >= ErrorCodes.VALIDATION_ERROR.ordinal() &&
                    errorDto.Code.ordinal() <= ErrorCodes.DUPLICATE_USERNAME.ordinal()
            )) {
                return 400;
            }

            return HttpStatusCodes.STATUS_CODE_SERVER_ERROR;
        }
    }
}
