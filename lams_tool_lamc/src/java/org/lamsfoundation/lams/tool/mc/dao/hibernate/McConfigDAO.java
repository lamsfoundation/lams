package org.lamsfoundation.lams.tool.mc.dao.hibernate;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcConfigDAO;

public class McConfigDAO extends LAMSBaseDAO implements IMcConfigDAO {
    @Override
    public void setConfigValue(String key, String value) {
	getSession()
		.createNativeQuery("UPDATE tl_lamc11_configuration SET config_value = :value WHERE config_key = :key")
		.setParameter("key", key).setParameter("value", value).executeUpdate();
    }

    @Override
    public String getConfigValue(String key) {
	return (String) getSession()
		.createNativeQuery("SELECT config_value FROM tl_lamc11_configuration WHERE config_key = :key")
		.setParameter("key", key).uniqueResult();
    }
}
