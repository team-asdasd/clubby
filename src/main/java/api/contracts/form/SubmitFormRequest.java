package api.contracts.form;

import api.contracts.base.BaseRequest;
import api.contracts.dto.SubmitFormDto;

import java.util.List;

public class SubmitFormRequest extends BaseRequest {
    public List<SubmitFormDto> fields;
}
