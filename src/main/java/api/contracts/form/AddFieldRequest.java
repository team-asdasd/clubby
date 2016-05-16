package api.contracts.form;

import api.contracts.base.BaseRequest;

public class AddFieldRequest extends BaseRequest {
    public String name;
    public String type;
    public String validationRegex;
    public boolean required;
    public boolean visible;
    public String description;
}
