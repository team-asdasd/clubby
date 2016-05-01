package api.handlers.cottages;

import api.business.entities.Cottage;
import api.business.services.interfaces.ICottageService;
import api.contracts.base.ErrorDto;
import api.contracts.cottages.GetCottageRequest;
import api.contracts.cottages.GetCottageResponse;
import api.contracts.dto.CottageDto;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class GetCottageHandler extends BaseHandler<GetCottageRequest, GetCottageResponse> {
    @Inject
    private ICottageService cottageService;

    @Override
    public ArrayList<ErrorDto> validate(GetCottageRequest request) {
        return Validator.checkAllNotNull(request);
    }

    @Override
    public GetCottageResponse handleBase(GetCottageRequest request) {
        GetCottageResponse response = createResponse();

        Cottage cottage = cottageService.getCottage(request.Id);

        response.Cottage = new CottageDto(cottage.getId(), cottage.getTitle(), cottage.getBedcount(), cottage.getImageurl());

        return response;
    }

    @Override
    public GetCottageResponse createResponse() {
        return new GetCottageResponse();
    }
}
