package api.contracts.form;

import api.business.entities.Field;
import api.contracts.base.BaseResponse;

import java.util.List;

public class GetFormResponse extends BaseResponse{
    public List<Field> fields;
}

