package api.helpers.validator;

import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Validator implements IRequestValidator {
    private final ArrayList<ErrorDto> errors;
    private Subject sub;

    public Validator() {
        this.errors = new ArrayList<>();
        sub = SecurityUtils.getSubject();
    }

    public IAuthenticationValidator isAuthenticated() {
        if (!sub.isAuthenticated()) {
            errors.add(new ErrorDto("Not authenticated.", ErrorCodes.UNAUTHENTICATED));
        }

        return this;
    }

    public IRequestValidator isMember() {
        isAuthenticated();
        if (errors.isEmpty() && !sub.hasRole("member")) {
            errors.add(new ErrorDto("User is not a member.", ErrorCodes.UNAUTHENTICATED));
        }

        return this;
    }

    public IRequestValidator isAdministrator() {
        isAuthenticated();
        if (errors.isEmpty() && !sub.hasRole("administrator")) {
            errors.add(new ErrorDto("User is not an administrator.", ErrorCodes.UNAUTHENTICATED));
        }

        return this;
    }

    public <T> IRequestValidator allFieldsSet(T entity) {
        checkAllNotNull(entity, this.errors);
        return this;
    }

    @Override
    public IRequestValidator isValidId(int id) {
        if (id <= 0) {
            errors.add(new ErrorDto("Id cannot be zero or less.", ErrorCodes.VALIDATION_ERROR));
        }

        return this;
    }

    private <T> void checkAllNotNull(T entity, ArrayList<ErrorDto> errors) {
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
    }

    private <T> void checkNotNull(List<ErrorDto> errors, String field, T entity) throws NoSuchFieldException, IllegalAccessException {
        if (entity.getClass().getField(field).get(entity) == null) {
            errors.add(new ErrorDto(field + " missing", ErrorCodes.VALIDATION_ERROR));
        }
    }

    public ArrayList<ErrorDto> getErrors() {
        return errors;
    }
}
