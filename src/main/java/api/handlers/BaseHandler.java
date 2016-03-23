package api.handlers;

import api.contracts.requests.BaseRequest;
import api.contracts.responses.BaseResponse;

public abstract class BaseHandler<TRequest extends BaseRequest, TResponse extends BaseResponse> {

    public final TResponse Handle(TRequest request) {
        if (!IsValid(request)) {
            throw new IllegalArgumentException();
        }

        return HandleBase(request);
    }

    public abstract boolean IsValid(TRequest request);

    public abstract TResponse HandleBase(TRequest request);
}
