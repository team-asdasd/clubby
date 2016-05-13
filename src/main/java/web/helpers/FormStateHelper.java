package web.helpers;

import api.business.entities.Configuration;
import api.business.persistance.ISimpleEntityManager;
import api.contracts.dto.FormState;

import javax.inject.Inject;

public class FormStateHelper {
    @Inject
    ISimpleEntityManager em;

    public FormState getFormState() {
        FormState state = new FormState();
        state.Address = Boolean.valueOf(em.getById(Configuration.class, "show_address").getValue());
        state.About = Boolean.valueOf(em.getById(Configuration.class, "show_about").getValue());
        state.PhoneNumber = Boolean.valueOf(em.getById(Configuration.class, "show_phone_number").getValue());
        state.Photo = Boolean.valueOf(em.getById(Configuration.class, "show_photo").getValue());
        state.BirthDate = Boolean.valueOf(em.getById(Configuration.class, "show_birth_date").getValue());

        return state;
    }
}
