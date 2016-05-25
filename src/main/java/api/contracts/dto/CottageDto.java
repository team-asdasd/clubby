package api.contracts.dto;

import api.business.entities.Cottage;
import api.business.entities.Service;

import java.util.ArrayList;
import java.util.List;

public class CottageDto {
    public int id;
    public String title;
    public int beds;
    public String image;
    public int price;
    public String description;
    public String availableFrom;
    public String availableTo;
    public List<ServiceDto> services;

    public CottageDto() {
    }

    public CottageDto(Cottage cottage) {
        this();
        id = cottage.getId();
        title = cottage.getTitle();
        beds = cottage.getBedcount();
        image = cottage.getImageurl();
        price = cottage.getPrice();
        description = cottage.getDescription();
        availableFrom = String.format("%02d-%02d", cottage.getAvailableFrom().getMonth() +1, cottage.getAvailableFrom().getDate());
        availableTo = String.format("%02d-%02d", cottage.getAvailableTo().getMonth() +1, cottage.getAvailableTo().getDate());
        services = new ArrayList<>();
        for(Service s : cottage.getServices()){
            ServiceDto dto = new ServiceDto();
            dto.description = s.getDescription();
            dto.price = s.getPrice();
            services.add(dto);
        }
    }
}
