package api.helpers.validator;

import api.contracts.base.ErrorDto;

import java.util.ArrayList;

public interface IFullStopValidator {
    ArrayList<ErrorDto> getErrors();
}
