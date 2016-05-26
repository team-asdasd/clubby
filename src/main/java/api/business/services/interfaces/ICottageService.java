package api.business.services.interfaces;

import api.business.entities.Cottage;
import api.business.entities.Service;

import java.util.List;

public interface ICottageService {
    void save(Cottage cottage);

    Cottage get(int id);

    List<Cottage> getByFilters(String title, int beds, String dateFrom, String dateTo, int priceFrom, int priceTo);

    void delete(int id);

    List<Service> getCottageServices(int id);
}
