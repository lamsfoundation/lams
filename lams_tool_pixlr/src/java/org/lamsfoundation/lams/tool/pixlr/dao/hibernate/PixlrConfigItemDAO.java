package org.lamsfoundation.lams.tool.pixlr.dao.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.pixlr.dao.IPixlrConfigItemDAO;
import org.lamsfoundation.lams.tool.pixlr.model.PixlrConfigItem;
import org.springframework.orm.hibernate4.HibernateCallback;

public class PixlrConfigItemDAO extends BaseDAO implements IPixlrConfigItemDAO {
    private static final String LOAD_CONFIG_ITEM_BY_KEY = "from PixlrConfigItem configuration"
	    + " where configuration.configKey=:key";

    public PixlrConfigItem getConfigItemByKey(final String configKey) {
	return (PixlrConfigItem) getHibernateTemplate().execute(new HibernateCallback() {
	    public Object doInHibernate(Session session) throws HibernateException {
		return session.createQuery(LOAD_CONFIG_ITEM_BY_KEY).setString("key", configKey).uniqueResult();
	    }
	});

    }

    public void saveOrUpdate(PixlrConfigItem mdlForumConfigItem) {
	this.getHibernateTemplate().saveOrUpdate(mdlForumConfigItem);
	this.getHibernateTemplate().flush();
    }
}
