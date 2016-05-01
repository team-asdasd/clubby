package api.contracts.dto;

import api.business.entities.Cottage;

public class CottageDto {
    public int Id;
    public String Title;
    public int Beds;
    public String Image;

    public CottageDto() {
    }

    public CottageDto(Cottage cottage) {
        this();

        Id = cottage.getId();
        Title = cottage.getTitle();
        Beds = cottage.getBedcount();
        Image = cottage.getImageurl();
    }
}
