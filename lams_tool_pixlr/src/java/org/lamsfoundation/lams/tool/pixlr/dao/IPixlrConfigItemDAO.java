package org.lamsfoundation.lams.tool.pixlr.dao;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.pixlr.model.PixlrConfigItem;

public interface IPixlrConfigItemDAO extends IBaseDAO {
    void saveOrUpdate(PixlrConfigItem toConfig);

    public PixlrConfigItem getConfigItemByKey(final String configKey);
}
