package org.lamsfoundation.lams.tool.wookie.dao.hibernate;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.wookie.dao.IWookieConfigItemDAO;
import org.lamsfoundation.lams.tool.wookie.model.WookieConfigItem;
import org.springframework.stereotype.Repository;

@Repository
public class WookieConfigItemDAO extends LAMSBaseDAO implements IWookieConfigItemDAO {
    private static final String LOAD_CONFIG_ITEM_BY_KEY = "from WookieConfigItem configuration"
	    + " where configuration.configKey=:key";

	public WookieConfigItem getConfigItemByKey(final String configKey) {
		return (WookieConfigItem) getSession().createQuery(LOAD_CONFIG_ITEM_BY_KEY).setString("key", configKey).uniqueResult();
	}

    public void saveOrUpdate(WookieConfigItem mdlForumConfigItem) {
    	getSession().saveOrUpdate(mdlForumConfigItem);
    	getSession().flush();
    }
}
