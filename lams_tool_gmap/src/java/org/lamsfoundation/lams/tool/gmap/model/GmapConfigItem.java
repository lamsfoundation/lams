package org.lamsfoundation.lams.tool.gmap.model;

/**
 * @hibernate.class table="tl_lagmap10_configuration"
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
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     * 
     */
    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    /**
     * @hibernate.property column="config_key" length="30"
     *                     not-null="false" unique="true"
     */
    public String getConfigKey() {
	return configKey;
    }

    public void setConfigKey(String configKey) {
	this.configKey = configKey;
    }

    /**
     * @hibernate.property column="config_value" length="255"
     *                     not-null="false"
     */
    public String getConfigValue() {
	return configValue;
    }

    public void setConfigValue(String configValue) {
	this.configValue = configValue;
    }
}
