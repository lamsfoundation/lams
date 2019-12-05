package org.lamsfoundation.lams.tool.assessment.dao;

import org.lamsfoundation.lams.dao.IBaseDAO;

public interface AssessmentConfigDAO extends IBaseDAO {
    void setConfigValue(String key, String value);

    String getConfigValue(String key);
}
