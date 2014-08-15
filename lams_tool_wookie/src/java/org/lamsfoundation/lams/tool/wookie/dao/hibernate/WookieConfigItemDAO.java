package org.lamsfoundation.lams.tool.wookie.dao.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.wookie.dao.IWookieConfigItemDAO;
import org.lamsfoundation.lams.tool.wookie.model.WookieConfigItem;
import org.springframework.orm.hibernate4.HibernateCallback;

public class WookieConfigItemDAO extends BaseDAO implements IWookieConfigItemDAO {
    private static final String LOAD_CONFIG_ITEM_BY_KEY = "from WookieConfigItem configuration"
	    + " where configuration.configKey=:key";

    public WookieConfigItem getConfigItemByKey(final String configKey) {
	return (WookieConfigItem) getHibernateTemplate().execute(new HibernateCallback() {
	    public Object doInHibernate(Session session) throws HibernateException {
		return session.createQuery(LOAD_CONFIG_ITEM_BY_KEY).setString("key", configKey).uniqueResult();
	    }
	});

    }

    public void saveOrUpdate(WookieConfigItem mdlForumConfigItem) {
	this.getHibernateTemplate().saveOrUpdate(mdlForumConfigItem);
	this.getHibernateTemplate().flush();
    }
}
