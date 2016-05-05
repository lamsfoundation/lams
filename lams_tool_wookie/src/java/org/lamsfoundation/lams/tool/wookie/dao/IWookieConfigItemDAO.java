package org.lamsfoundation.lams.tool.wookie.dao;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.wookie.model.WookieConfigItem;

public interface IWookieConfigItemDAO extends IBaseDAO {
    void saveOrUpdate(WookieConfigItem toConfig);

    public WookieConfigItem getConfigItemByKey(final String configKey);
}
