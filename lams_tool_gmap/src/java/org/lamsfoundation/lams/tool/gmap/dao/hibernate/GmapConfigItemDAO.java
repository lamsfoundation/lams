package org.lamsfoundation.lams.tool.gmap.dao.hibernate;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.gmap.dao.IGmapConfigItemDAO;
import org.lamsfoundation.lams.tool.gmap.model.GmapConfigItem;
import org.springframework.stereotype.Repository;

@Repository
public class GmapConfigItemDAO extends LAMSBaseDAO implements IGmapConfigItemDAO {
    private static final String LOAD_CONFIG_ITEM_BY_KEY = "from GmapConfigItem configuration"
	    + " where configuration.configKey=:key";

    @Override
    public GmapConfigItem getConfigItemByKey(final String configKey) {
	return (GmapConfigItem) getSession().createQuery(LOAD_CONFIG_ITEM_BY_KEY).setString("key", configKey)
		.uniqueResult();
    }

    @Override
    public void saveOrUpdate(GmapConfigItem mdlForumConfigItem) {
	getSession().saveOrUpdate(mdlForumConfigItem);
	getSession().flush();
    }
}
