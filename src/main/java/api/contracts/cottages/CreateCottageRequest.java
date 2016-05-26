package api.contracts.cottages;

import api.contracts.base.BaseRequest;
import api.contracts.dto.ServiceDto;

import java.util.List;

public class CreateCottageRequest extends BaseRequest{
    public int id;
    public String title;
    public int beds;
    public String image;
    public String price;
    public String description;
    public String availableFrom;
    public String availableTo;
    public List<ServiceDto> services;
}
