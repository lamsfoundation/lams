package org.lamsfoundation.lams.tool.gmap.dao.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.gmap.dao.IGmapConfigItemDAO;
import org.lamsfoundation.lams.tool.gmap.model.GmapConfigItem;
import org.springframework.orm.hibernate4.HibernateCallback;
public class GmapConfigItemDAO extends BaseDAO implements IGmapConfigItemDAO
{
	private static final String LOAD_CONFIG_ITEM_BY_KEY = "from GmapConfigItem configuration" 
	+ " where configuration.configKey=:key";
	
	public GmapConfigItem getConfigItemByKey(final String configKey)
    {
		return (GmapConfigItem) getHibernateTemplate().execute(new HibernateCallback()
         {
             public Object doInHibernate(Session session) throws HibernateException
             {
            	 return session.createQuery(LOAD_CONFIG_ITEM_BY_KEY)
                               .setString("key",configKey)
                               .uniqueResult();
             }
         });
        
    }
	
	public void saveOrUpdate(GmapConfigItem mdlForumConfigItem) {
		this.getHibernateTemplate().saveOrUpdate(mdlForumConfigItem);
		this.getHibernateTemplate().flush();
	}
}
