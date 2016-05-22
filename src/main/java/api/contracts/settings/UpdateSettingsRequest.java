package api.contracts.settings;

import api.contracts.base.BaseRequest;
import api.contracts.dto.UpdateSettingsDto;

import java.util.List;

public class UpdateSettingsRequest extends BaseRequest {
    public List<UpdateSettingsDto> settings;
}
