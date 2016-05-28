package api.helpers.validator;

public interface IRequestValidator extends IAuthenticationValidator {
    IRequestValidator isMember();

    IRequestValidator isAdministrator();

    <T> IRequestValidator allFieldsSet(T entity);
}
