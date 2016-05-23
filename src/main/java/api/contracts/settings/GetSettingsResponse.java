package api.contracts.settings;

import api.business.entities.Configuration;
import api.contracts.base.BaseResponse;

import java.util.List;

public class GetSettingsResponse extends BaseResponse {
    public List<Configuration> settings;
}
