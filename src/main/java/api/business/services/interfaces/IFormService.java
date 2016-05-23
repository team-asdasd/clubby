package api.business.services.interfaces;

import api.business.entities.Field;
import api.business.entities.FormResult;
import api.business.entities.User;
import api.contracts.base.ErrorDto;
import api.contracts.dto.FormInfoDto;
import api.contracts.dto.SubmitFormDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface IFormService {

    List<Field> getForm();

    Field getFieldByName(String name);

    List<FormResult> getMyFields();

    List<Field> getVisibleFields();

    FormResult getFormResult(String fieldName, int userId);

    void saveFormResults(List<SubmitFormDto> formDtos, User user);

    ArrayList<ErrorDto> validateFormFields(List<SubmitFormDto> fields);
}
