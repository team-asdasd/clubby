package api.business.services.interfaces;

import api.business.entities.Cottage;

import java.util.List;

public interface ICottageService {
    void save(Cottage cottage);

    Cottage get(int id);

    List<Cottage> getByFilters(String title, int beds);

    void delete(int id);
}
