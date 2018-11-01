package org.lamsfoundation.lams.tool.pixlr.dao.hibernate;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.pixlr.dao.IPixlrConfigItemDAO;
import org.lamsfoundation.lams.tool.pixlr.model.PixlrConfigItem;
import org.springframework.stereotype.Repository;

@Repository
public class PixlrConfigItemDAO extends LAMSBaseDAO implements IPixlrConfigItemDAO {
    private static final String LOAD_CONFIG_ITEM_BY_KEY = "from PixlrConfigItem configuration"
	    + " where configuration.configKey=:key";

    @Override
    public PixlrConfigItem getConfigItemByKey(final String configKey) {
	return (PixlrConfigItem) getSession().createQuery(LOAD_CONFIG_ITEM_BY_KEY).setParameter("key", configKey)
		.uniqueResult();

    }

    @Override
    public void saveOrUpdate(PixlrConfigItem mdlForumConfigItem) {
	getSession().saveOrUpdate(mdlForumConfigItem);
	getSession().flush();
    }
}
