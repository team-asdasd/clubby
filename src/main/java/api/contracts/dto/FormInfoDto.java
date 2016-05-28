package api.contracts.dto;

import api.business.entities.Field;
import api.business.entities.FormResult;

public class FormInfoDto {
    public String description;
    public String type;
    public String value;
    public String name;

    public FormInfoDto(FormResult formResult) {
        this.description = formResult.getField().getDescription();
        this.type = formResult.getField().getType();
        this.value = formResult.getValue();
        name = formResult.getField().getName();
    }

    public FormInfoDto(Field field) {
        this.description = field.getDescription();
        this.type = field.getType();
        value = "";
        name = field.getName();
    }

    public FormInfoDto() {
    }
}
