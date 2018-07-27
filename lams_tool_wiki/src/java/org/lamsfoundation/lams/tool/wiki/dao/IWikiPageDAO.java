package org.lamsfoundation.lams.tool.wiki.dao;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.wiki.model.Wiki;
import org.lamsfoundation.lams.tool.wiki.model.WikiPage;
import org.lamsfoundation.lams.tool.wiki.model.WikiSession;

public interface IWikiPageDAO extends IBaseDAO {

    void saveOrUpdate(WikiPage wikipage);

    WikiPage getByWikiAndTitle(Wiki wiki, String title);

    WikiPage getBySessionAndTitle(WikiSession wikiSession, String title);
}
