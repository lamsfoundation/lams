package org.lamsfoundation.lams.tool.assessment.dao.hibernate;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.assessment.dao.AssessmentConfigDAO;

public class AssessmentConfigDAOHibernate extends LAMSBaseDAO implements AssessmentConfigDAO {
    @Override
    public void setConfigValue(String key, String value) {
	getSession()
		.createNativeQuery("UPDATE tl_laasse10_configuration SET config_value = :value WHERE config_key = :key")
		.setParameter("key", key).setParameter("value", value).executeUpdate();
    }

    @Override
    public String getConfigValue(String key) {
	return (String) getSession()
		.createNativeQuery("SELECT config_value FROM tl_laasse10_configuration WHERE config_key = :key")
		.setParameter("key", key).uniqueResult();
    }
}
