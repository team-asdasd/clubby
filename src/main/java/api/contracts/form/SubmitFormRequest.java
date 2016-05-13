package api.contracts.form;

import api.contracts.base.BaseRequest;
import api.contracts.dto.FormDto;

import java.util.List;

public class SubmitFormRequest extends BaseRequest {
    public List<FormDto> fields;
}
