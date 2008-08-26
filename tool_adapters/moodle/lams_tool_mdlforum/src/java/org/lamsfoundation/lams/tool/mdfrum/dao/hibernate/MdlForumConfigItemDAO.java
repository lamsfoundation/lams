package org.lamsfoundation.lams.tool.mdfrum.dao.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.mdfrum.dao.IMdlForumConfigItemDAO;
import org.lamsfoundation.lams.tool.mdfrum.model.MdlForumConfigItem;
import org.springframework.orm.hibernate3.HibernateCallback;
public class MdlForumConfigItemDAO extends BaseDAO implements IMdlForumConfigItemDAO
{
	private static final String LOAD_CONFIG_ITEM_BY_KEY = "from MdlForumConfigItem configuration" 
	+ " where configuration.configKey=:key";
	
	public MdlForumConfigItem getConfigItemByKey(final String configKey)
    {
		return (MdlForumConfigItem) getHibernateTemplate().execute(new HibernateCallback()
         {
             public Object doInHibernate(Session session) throws HibernateException
             {
            	 return session.createQuery(LOAD_CONFIG_ITEM_BY_KEY)
                               .setString("key",configKey)
                               .uniqueResult();
             }
         });
        
    }
	
	public void saveOrUpdate(MdlForumConfigItem mdlForumConfigItem) {
		this.getHibernateTemplate().saveOrUpdate(mdlForumConfigItem);
		this.getHibernateTemplate().flush();
	}
}
