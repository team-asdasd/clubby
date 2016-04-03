package api.contracts.responses.base;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;

public class BaseResponse {
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public ArrayList<ErrorDto> Errors;
}
