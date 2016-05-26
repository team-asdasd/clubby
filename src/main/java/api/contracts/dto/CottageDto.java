package api.contracts.dto;

import api.business.entities.Cottage;
import api.business.entities.Service;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CottageDto {
    public int version;
    public int id;
    public String title;
    public int beds;
    public String image;
    public String price;
    public String description;
    public String availableFrom;
    public String availableTo;
    public List<ExistingServiceDto> services;

    public CottageDto() {
    }

    public CottageDto(Cottage cottage) {
        this();
        version = cottage.getVersion();
        id = cottage.getId();
        title = cottage.getTitle();
        beds = cottage.getBedcount();
        image = cottage.getImageurl();
        DecimalFormat def = new DecimalFormat("#0.##");
        price = def.format(cottage.getPrice() /100d);
        description = cottage.getDescription();
        DateFormat df = new SimpleDateFormat("MM-dd");
        availableFrom = df.format(cottage.getAvailableFrom());
        availableTo = df.format(cottage.getAvailableTo());
        services = new ArrayList<>();

        if (cottage.getServices() != null) {
            for (Service s : cottage.getServices()) {
                ExistingServiceDto dto = new ExistingServiceDto();
                dto.description = s.getDescription();
                dto.price = def.format(s.getPrice() / 100d);
                dto.maxCount = s.getMaxCount();
                dto.id = s.getId();
                services.add(dto);
            }
        }
    }
}
