package org.lamsfoundation.lams.tool.dokumaran.model;

/**
 *
 */
public class DokumaranConfigItem implements java.io.Serializable {

    private static final long serialVersionUID = 930482766653472631L;

    public static final String KEY_ETHERPAD_URL = "EtherpadUrl";
    
    public static final String KEY_API_KEY = "ApiKey";
    
    private Long id;
    private String configKey;
    private String configValue;

    /**
     *
     * 
     */
    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    /** 
    *
    *
    */
    public String getConfigKey() {
	return configKey;
    }

    public void setConfigKey(String configKey) {
	this.configKey = configKey;
    }

    /** 
    *
    *
    */
    public String getConfigValue() {
	return configValue;
    }

    public void setConfigValue(String configValue) {
	this.configValue = configValue;
    }
}
