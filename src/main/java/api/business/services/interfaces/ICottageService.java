package api.business.services.interfaces;

import api.business.entities.Cottage;

import java.util.List;

public interface ICottageService {
    List<Cottage> getAllCottages();

    void createCottage(Cottage cottage);
}
