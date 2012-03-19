package org.lamsfoundation.lams.tool.wiki.dao.hibernate;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.wiki.dao.IWikiPageDAO;
import org.lamsfoundation.lams.tool.wiki.dto.WikiPageDTO;
import org.lamsfoundation.lams.tool.wiki.model.Wiki;
import org.lamsfoundation.lams.tool.wiki.model.WikiPage;
import org.lamsfoundation.lams.tool.wiki.model.WikiSession;

public class WikiPageDAO extends BaseDAO implements IWikiPageDAO {

    public static final String GET_BY_WIKI_AND_TITLE = "from tl_lawiki10_wiki_page in class "
	    + WikiPage.class.getName() + " where wiki_uid=? AND title=? AND wiki_session_uid=null";

    public static final String GET_BY_SESSION_AND_TITLE = "from tl_lawiki10_wiki_page in class "
	    + WikiPage.class.getName() + " where wiki_session_uid=? AND title=?";

    public static final String REMOVE_WIKI_REFERENCES = "UPDATE tl_lawiki10_wiki_page_content "
	    + "SET body=REPLACE(body,?,?) WHERE editor IS NULL";
    
    public static final String CHANGE_WIKI_JAVASCRIPT_METHOD = "javascript:changeWikiPage('?')";

    public void saveOrUpdate(WikiPage wikiPage) {
	this.getHibernateTemplate().saveOrUpdate(wikiPage);
    }

    public WikiPage getByWikiAndTitle(Wiki wiki, String title) {
	if (wiki != null && title != null && title.length() > 0) {
	    Long wikiId = wiki.getUid();
	    Query query = this.getSession().createQuery(GET_BY_WIKI_AND_TITLE);
	    query.setLong(0, wikiId);
	    query.setString(1, title);
	    return (WikiPage) query.uniqueResult();
	}
	return null;
    }

    public WikiPage getBySessionAndTitle(WikiSession wikiSession, String title) {
	if (wikiSession != null && title != null && title.length() > 0) {
	    Long wikiId = wikiSession.getUid();
	    Query query = this.getSession().createQuery(GET_BY_SESSION_AND_TITLE);
	    query.setLong(0, wikiId);
	    query.setString(1, title);
	    return (WikiPage) query.uniqueResult();
	}
	return null;
    }

    public void delete(Object object) {
	// remove references to the removed page
	WikiPage removedWikiPage = (WikiPage) object;
	String title = removedWikiPage.getTitle();
	String escapedTitle = WikiPageDTO.javaScriptEscape(title);
	String codeToReplace = WikiPageDAO.CHANGE_WIKI_JAVASCRIPT_METHOD.replace("?", escapedTitle);
	String replacementCode = "#";

	SQLQuery query = this.getSession().createSQLQuery(REMOVE_WIKI_REFERENCES);
	query.setString(0, codeToReplace);
	query.setString(1, replacementCode);
	
	super.delete(object);
	query.executeUpdate();
    }
}