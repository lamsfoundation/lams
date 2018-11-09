package org.lamsfoundation.lams.tool.dokumaran.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tl_ladoku11_configuration")
public class DokumaranConfigItem implements java.io.Serializable {
    private static final long serialVersionUID = 930482766653472631L;

    public static final String KEY_ETHERPAD_URL = "EtherpadUrl";
    public static final String KEY_API_KEY = "ApiKey";
    
    @Id
    @Column(name = "uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "config_key")
    private String configKey;
    
    @Column(name = "config_value")
    private String configValue;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
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
