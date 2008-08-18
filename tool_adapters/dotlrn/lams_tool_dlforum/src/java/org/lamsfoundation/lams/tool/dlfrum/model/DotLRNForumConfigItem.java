package org.lamsfoundation.lams.tool.dlfrum.model;

import org.apache.log4j.Logger;

/**
 * @hibernate.class table="tl_dlfrum10_configuration"
 */
public class DotLRNForumConfigItem implements java.io.Serializable
{

	private static final long serialVersionUID = 2910983748293847627L;

	static Logger log = Logger.getLogger(DotLRNForumConfigItem.class.getName());
	
	public static final String KEY_EXTERNAL_TOOL_SERVER = "toolAdapterServlet";
	
	Long id;
	String configKey;
	String configValue;
	
	// empty contsructor
	public DotLRNForumConfigItem() {}

	
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
     *            		   not-null="false" unique="true"    
     */
	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	/** 
     * @hibernate.property column="config_value" length="255"
     *            		   not-null="false"    
     */
	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}
}
