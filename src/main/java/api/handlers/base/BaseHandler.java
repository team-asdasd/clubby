package api.handlers.base;

import api.contracts.requests.base.BaseRequest;
import api.contracts.responses.base.BaseResponse;
import api.contracts.responses.base.ErrorCodes;
import api.contracts.responses.base.ErrorDto;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseHandler<TRequest extends BaseRequest, TResponse extends BaseResponse> {

    public final TResponse handle(TRequest request) {
        try {
            ArrayList<ErrorDto> errors = validate(request);

            if (errors.isEmpty()) {
                return handleBase(request);
            } else {
                return handleErrors(errors);
            }
        } catch (Exception e) {
            return handleException(e);
        }
    }

    private TResponse handleErrors(List<ErrorDto> errors) {
        TResponse response = createResponse();

        response.Errors = (ArrayList<ErrorDto>) errors;

        return response;
    }

    public abstract ArrayList<ErrorDto> validate(TRequest request);

    public abstract TResponse handleBase(TRequest request);

    public abstract TResponse createResponse();

    public TResponse handleException(Exception e) {
        TResponse response = createResponse();

        response.Errors = new ArrayList<>();

        ErrorDto error = new ErrorDto(getMessage(e), ErrorCodes.GENERAL_ERROR);

        response.Errors.add(error);

        return response;
    }

    private String getMessage(Exception e) {
        String message = e.getMessage();
        if (message == null || message.isEmpty()) {
            return String.format("Unknown error (%s)", e.getClass());
        } else {
            return message;
        }
    }
}
