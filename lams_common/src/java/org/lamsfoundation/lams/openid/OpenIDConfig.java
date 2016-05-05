package org.lamsfoundation.lams.openid;

public class OpenIDConfig {
    private String configKey;
    private String configValue;

    public static final String KEY_ENABLED = "enabled";
    public static final String KEY_PORTAL_URL = "portalURL";
    public static final String KEY_TRUSTED_IDPS = "trustedIDPs";

    public OpenIDConfig() {
    }

    public String getConfigKey() {
	return configKey;
    }

    public void setConfigKey(String configKey) {
	this.configKey = configKey;
    }

    public String getConfigValue() {
	return configValue;
    }

    public void setConfigValue(String configValue) {
	this.configValue = configValue;
    }
}
