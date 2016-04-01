package api.handlers.base;

import api.contracts.requests.base.BaseRequest;
import api.contracts.responses.base.BaseResponse;

public abstract class BaseHandler<TRequest extends BaseRequest, TResponse extends BaseResponse> {

    public final TResponse Handle(TRequest request) {
        try {
            if (!IsValid(request)) {
                throw new IllegalArgumentException();
            }

            return HandleBase(request);
        } catch (Exception e) {
            return HandleError(e);
        }
    }

    protected abstract boolean IsValid(TRequest request);

    protected abstract TResponse HandleBase(TRequest request);

    protected abstract TResponse HandleError(Exception e);
}
