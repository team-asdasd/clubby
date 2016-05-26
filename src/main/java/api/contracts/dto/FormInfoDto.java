package api.contracts.dto;

import api.business.entities.FormResult;

public class FormInfoDto {
    public String description;
    public String type;
    public String value;

    public FormInfoDto(FormResult formResult){
        this.description = formResult.getField().getDescription();
        this.type = formResult.getField().getType();
        this.value = formResult.getValue();
    }

    public FormInfoDto(){}
}
