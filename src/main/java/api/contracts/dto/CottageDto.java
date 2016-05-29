package api.contracts.dto;

import api.business.entities.Cottage;
import api.business.entities.Service;

import java.util.ArrayList;
import java.util.List;

public class CottageDto extends SlimCottageDto {

    public List<ExistingServiceDto> services;

    public CottageDto(Cottage cottage) {
        super(cottage);

        services = new ArrayList<>();

        if (cottage.getServices() != null) {
            for (Service s : cottage.getServices()) {
                ExistingServiceDto dto = new ExistingServiceDto();
                dto.description = s.getDescription();
                dto.price = s.getPrice();
                dto.maxCount = s.getMaxCount();
                dto.id = s.getId();
                services.add(dto);
            }
        }
    }
}
