package org.lamsfoundation.lams.tool.gmap.model;

/**
 *
 */
public class GmapConfigItem implements java.io.Serializable {

    private static final long serialVersionUID = 930482766653472636L;

    public static final String KEY_GMAP_KEY = "GmapKey";

    Long id;
    String configKey;
    String configValue;

    // empty contsructor
    public GmapConfigItem() {
    }

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
