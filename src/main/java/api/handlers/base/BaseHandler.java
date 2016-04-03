package api.handlers.base;

import api.contracts.requests.base.BaseRequest;
import api.contracts.responses.base.BaseResponse;
import api.contracts.responses.base.ErrorCodes;
import api.contracts.responses.base.ErrorDto;

import java.util.ArrayList;

public abstract class BaseHandler<TRequest extends BaseRequest, TResponse extends BaseResponse> {

    public final TResponse handle(TRequest request) {
        try {
            if (!isValid(request)) {
                throw new IllegalArgumentException();
            }

            return handleBase(request);
        } catch (Exception e) {
            return handleError(e);
        }
    }

    protected abstract boolean isValid(TRequest request);

    protected abstract TResponse handleBase(TRequest request);

    protected abstract TResponse createResponse();

    protected TResponse handleError(Exception e) {
        TResponse response = createResponse();

        response.Errors = new ArrayList<>();

        ErrorDto error = new ErrorDto();
        error.Message = getMessage(e);
        error.Code = ErrorCodes.GENERAL_ERROR;

        response.Errors.add(error);

        return response;
    }

    private String getMessage(Exception e) {
        String message = e.getMessage();
        if (message == null || message.isEmpty()) {
            return "Unknown error";
        } else {
            return message;
        }
    }
}
