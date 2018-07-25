package org.lamsfoundation.lams.tool.dokumaran.dao;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranConfigItem;

public interface DokumaranConfigItemDAO extends IBaseDAO {
    void saveOrUpdate(DokumaranConfigItem toConfig);

    public DokumaranConfigItem getConfigItemByKey(final String configKey);
}
