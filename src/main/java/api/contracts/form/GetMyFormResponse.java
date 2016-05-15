package api.contracts.form;

import api.business.entities.FormResult;
import api.contracts.base.BaseResponse;

import java.util.List;

public class GetMyFormResponse extends BaseResponse {
    public List<FormResult> fields;
}
