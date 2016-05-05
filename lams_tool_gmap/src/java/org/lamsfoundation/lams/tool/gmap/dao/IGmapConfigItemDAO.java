package org.lamsfoundation.lams.tool.gmap.dao;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.gmap.model.GmapConfigItem;

public interface IGmapConfigItemDAO extends IBaseDAO {
    void saveOrUpdate(GmapConfigItem toConfig);

    public GmapConfigItem getConfigItemByKey(final String configKey);
}
