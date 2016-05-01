package api.business.services.interfaces;

import api.business.entities.Cottage;

import java.util.List;

public interface ICottageService {
    List<Cottage> getAllCottages(String title, int beds);

    void createCottage(Cottage cottage);

    Cottage getCottage(int id);

    void deleteCottage(int id);
}
