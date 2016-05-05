package org.lamsfoundation.lams.tool.wiki.dao.hibernate;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.wiki.dao.IWikiPageContentDAO;
import org.lamsfoundation.lams.tool.wiki.model.WikiPageContent;

public class WikiPageContentDAO extends BaseDAO implements IWikiPageContentDAO {

    @Override
    public void saveOrUpdate(WikiPageContent content) {
	this.getHibernateTemplate().saveOrUpdate(content);
    }

}
