package org.lamsfoundation.lams.tool.mc.dao;

import org.lamsfoundation.lams.dao.IBaseDAO;

public interface IMcConfigDAO extends IBaseDAO {
    void setConfigValue(String key, String value);

    String getConfigValue(String key);
}
