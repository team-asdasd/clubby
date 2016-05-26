package api.handlers.cottages;

import api.business.entities.Cottage;
import api.business.services.interfaces.ICottageService;
import api.contracts.cottages.GetCottagesResponse;
import api.contracts.cottages.GetCottagesRequest;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.dto.CottageDto;
import api.handlers.base.BaseHandler;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class GetCottagesHandler extends BaseHandler<GetCottagesRequest, GetCottagesResponse> {
    @Inject
    private ICottageService cottageService;

    @Override
    public ArrayList<ErrorDto> validate(GetCottagesRequest request) {
        Subject currentUser = SecurityUtils.getSubject();

        ArrayList<ErrorDto> errors = new ArrayList<>();

        if (!currentUser.isAuthenticated()) {
            errors.add(new ErrorDto("Not authenticated.", ErrorCodes.AUTHENTICATION_ERROR));
        }

        return errors;
    }

    @Override
    public GetCottagesResponse handleBase(GetCottagesRequest request) {
        GetCottagesResponse response = createResponse();

        List<Cottage> allCottages = cottageService.getByFilters(request.title, request.bedcount, request.dateFrom, request.dateTo, request.priceFrom, request.priceTo);

        response.cottages = allCottages.stream().map(CottageDto::new).collect(Collectors.toList());

        return response;
    }

    @Override
    public GetCottagesResponse createResponse() {
        return new GetCottagesResponse();
    }
}
