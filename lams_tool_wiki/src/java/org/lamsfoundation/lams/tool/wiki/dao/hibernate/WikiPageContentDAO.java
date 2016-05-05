package org.lamsfoundation.lams.tool.wiki.dao.hibernate;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.wiki.dao.IWikiPageContentDAO;
import org.lamsfoundation.lams.tool.wiki.model.WikiPageContent;
import org.springframework.stereotype.Repository;

@Repository
public class WikiPageContentDAO extends LAMSBaseDAO implements IWikiPageContentDAO {

    @Override
    public void saveOrUpdate(WikiPageContent content) {
	getSession().saveOrUpdate(content);
    }

}
