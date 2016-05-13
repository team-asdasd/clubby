package api.contracts.form;

import api.contracts.base.BaseRequest;

import java.sql.Date;

public class SubmitFormRequest extends BaseRequest {
    public String PhoneNumber;
    public String Address;
    public String Birthdate;
    public String About;
    public String Photo;
}
