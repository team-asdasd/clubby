package api.contracts.form;

import api.contracts.base.BaseResponse;
import api.contracts.dto.FormState;

public class GetFormResponse extends BaseResponse{
    public boolean ShowPhoneNumber;
    public boolean ShowAddress;
    public boolean ShowBirthDate;
    public boolean ShowAbout;
    public boolean ShowPhoto;

    public GetFormResponse() {
    }

    public GetFormResponse(FormState formState) {
        ShowAddress = formState.Address;
        ShowPhoneNumber = formState.PhoneNumber;
        ShowBirthDate = formState.BirthDate;
        ShowAbout = formState.About;
        ShowPhoto = formState.Photo;
    }
}

