package org.lamsfoundation.lams.openid;

public class OpenIDConfig {
	private String configKey;
	private String configValue;
	
	public static final String KEY_ENABLED = "enabled";
	public static final String KEY_PORTAL_URL = "portalURL";
	public static final String KEY_TRUSTED_IDPS = "trustedIDPs";
	
	public OpenIDConfig () {
	}

	/**
	 * @hibernate.id type="java.lang.Long" column="config_key"
	 */
	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	/**
	 * @hibernate.property column="config_value"
	 */
	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}
}
