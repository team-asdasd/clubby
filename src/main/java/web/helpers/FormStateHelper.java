package web.helpers;

import api.business.entities.Configuration;
import api.business.persistance.ISimpleEntityManager;
import api.contracts.dto.FormState;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class FormStateHelper {
    @Inject
    ISimpleEntityManager em;
    public FormState getFormState(){
        Gson gson = new Gson();
        Configuration value = em.getById(Configuration.class, "membership_form_state");
        return gson.fromJson(value.getValue(), FormState.class);
    }
}
