package org.lamsfoundation.lams.tool.dokumaran.dao.hibernate;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.dokumaran.dao.DokumaranConfigItemDAO;
import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranConfigItem;
import org.springframework.stereotype.Repository;

@Repository
public class DokumaranConfigItemDAOHibernate extends LAMSBaseDAO implements DokumaranConfigItemDAO {
    private static final String LOAD_CONFIG_ITEM_BY_KEY = "from DokumaranConfigItem configuration"
	    + " where configuration.configKey=:key";

    @Override
    public DokumaranConfigItem getConfigItemByKey(final String configKey) {
	return (DokumaranConfigItem) getSession().createQuery(LOAD_CONFIG_ITEM_BY_KEY).setParameter("key", configKey)
		.uniqueResult();
    }

    @Override
    public void saveOrUpdate(DokumaranConfigItem mdlForumConfigItem) {
	getSession().saveOrUpdate(mdlForumConfigItem);
	getSession().flush();
    }
}
