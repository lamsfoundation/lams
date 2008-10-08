package org.lamsfoundation.lams.tool.wiki.dao.hibernate;

import org.lamsfoundation.lams.tool.wiki.model.WikiPageContent;
import org.lamsfoundation.lams.tool.wiki.dao.IWikiPageContentDAO;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;

public class WikiPageContentDAO extends BaseDAO implements IWikiPageContentDAO {

    public void saveOrUpdate(WikiPageContent content) {
	this.getHibernateTemplate().saveOrUpdate(content);
    }

}
