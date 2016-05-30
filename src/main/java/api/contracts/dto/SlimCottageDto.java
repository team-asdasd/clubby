package api.contracts.dto;

import api.business.entities.Cottage;
import api.business.entities.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SlimCottageDto {
    public int version;
    public int id;
    public String title;
    public int beds;
    public String image;
    public int price;
    public String description;
    public String availableFrom;
    public String availableTo;

    public SlimCottageDto(Cottage cottage) {
        version = cottage.getVersion();
        id = cottage.getId();
        title = cottage.getTitle();
        beds = cottage.getBedcount();
        image = cottage.getImageurl();
        price = cottage.getPrice();
        description = cottage.getDescription();
        DateFormat df = new SimpleDateFormat("MM-dd");
        availableFrom = df.format(cottage.getAvailableFrom());
        availableTo = df.format(cottage.getAvailableTo());
    }
}
