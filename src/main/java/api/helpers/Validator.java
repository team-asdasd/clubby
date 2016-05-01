package api.helpers;

import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;

import javax.enterprise.context.ApplicationScoped;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class Validator {
    public static <T> void checkNotNull(List<ErrorDto> errors, String field, T entity) throws NoSuchFieldException, IllegalAccessException {
        if (entity.getClass().getField(field).get(entity) == null) {
            errors.add(new ErrorDto(field + " missing", ErrorCodes.VALIDATION_ERROR));
        }
    }

    public static <T> ArrayList<ErrorDto> checkAllNotNull(T entity) {
        ArrayList<ErrorDto> errors = new ArrayList<>();
        if (entity == null) {
            errors.add(new ErrorDto("Entity missing", ErrorCodes.VALIDATION_ERROR));
        } else {
            try {
                for (Field field : entity.getClass().getFields()) {
                    checkNotNull(errors, field.getName(), entity);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return errors;
    }

}
