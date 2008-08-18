package org.lamsfoundation.lams.tool.dlfrum.dao.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.dlfrum.dao.IDotLRNForumConfigItemDAO;
import org.lamsfoundation.lams.tool.dlfrum.model.DotLRNForumConfigItem;
import org.springframework.orm.hibernate3.HibernateCallback;
public class DotLRNForumConfigItemDAO extends BaseDAO implements IDotLRNForumConfigItemDAO
{
	private static final String LOAD_CONFIG_ITEM_BY_KEY = "from DotLRNForumConfigItem configuration" 
	+ " where configuration.configKey=:key";
	
	public DotLRNForumConfigItem getConfigItemByKey(final String configKey)
    {
		return (DotLRNForumConfigItem) getHibernateTemplate().execute(new HibernateCallback()
         {
             public Object doInHibernate(Session session) throws HibernateException
             {
            	 return session.createQuery(LOAD_CONFIG_ITEM_BY_KEY)
                               .setString("key",configKey)
                               .uniqueResult();
             }
         });
        
    }
	
	public void saveOrUpdate(DotLRNForumConfigItem dotLRNForumConfigItem) {
		this.getHibernateTemplate().saveOrUpdate(dotLRNForumConfigItem);
		this.getHibernateTemplate().flush();
	}
}
