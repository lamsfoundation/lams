package org.lamsfoundation.lams.tool.wiki.dao.hibernate;

import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.wiki.dao.IWikiPageDAO;
import org.lamsfoundation.lams.tool.wiki.dto.WikiPageDTO;
import org.lamsfoundation.lams.tool.wiki.model.Wiki;
import org.lamsfoundation.lams.tool.wiki.model.WikiPage;
import org.lamsfoundation.lams.tool.wiki.model.WikiSession;
import org.springframework.stereotype.Repository;

@Repository
public class WikiPageDAO extends LAMSBaseDAO implements IWikiPageDAO {

    public static final String GET_BY_WIKI_AND_TITLE = "from tl_lawiki10_wiki_page in class " + WikiPage.class.getName()
	    + " where wiki_uid=:wikiId AND title=:title AND wiki_session_uid=null";

    public static final String GET_BY_SESSION_AND_TITLE = "from tl_lawiki10_wiki_page in class "
	    + WikiPage.class.getName() + " where wiki_session_uid=:wikiId AND title=:title";

    public static final String REMOVE_WIKI_REFERENCES = "UPDATE tl_lawiki10_wiki_page_content AS content "
	    + "JOIN tl_lawiki10_wiki_page AS page ON content.wiki_page_uid=page.uid "
	    + "SET content.body=REPLACE(content.body,:codeToReplace,:replacementCode) WHERE content.editor IS NULL AND page.wiki_uid=:parentWikiUid";

    public static final String CHANGE_WIKI_JAVASCRIPT_METHOD = "javascript:changeWikiPage('?')";

    @Override
    public void saveOrUpdate(WikiPage wikiPage) {
	getSession().saveOrUpdate(wikiPage);
    }

    @Override
    public WikiPage getByWikiAndTitle(Wiki wiki, String title) {
	if (wiki != null && title != null && title.length() > 0) {
	    Long wikiId = wiki.getUid();
	    Query query = getSessionFactory().getCurrentSession().createQuery(GET_BY_WIKI_AND_TITLE);
	    query.setParameter("wikiId", wikiId);
	    query.setParameter("title", title);
	    return (WikiPage) query.uniqueResult();
	}
	return null;
    }

    @Override
    public WikiPage getBySessionAndTitle(WikiSession wikiSession, String title) {
	if (wikiSession != null && title != null && title.length() > 0) {
	    Long wikiId = wikiSession.getUid();
	    Query query = getSessionFactory().getCurrentSession().createQuery(GET_BY_SESSION_AND_TITLE);
	    query.setParameter("wikiId", wikiId);
	    query.setParameter("title", title);
	    return (WikiPage) query.uniqueResult();
	}
	return null;
    }

    @Override
    public void delete(Object object) {
	// remove references to the removed page
	WikiPage removedWikiPage = (WikiPage) object;
	String title = removedWikiPage.getTitle();
	String escapedTitle = WikiPageDTO.javaScriptEscape(title);
	String codeToReplace = WikiPageDAO.CHANGE_WIKI_JAVASCRIPT_METHOD.replace("?", escapedTitle);
	String replacementCode = "#";

	NativeQuery<?> query = getSessionFactory().getCurrentSession().createNativeQuery(REMOVE_WIKI_REFERENCES);
	query.setParameter("codeToReplace", codeToReplace);
	query.setParameter("replacementCode", replacementCode);
	query.setParameter("parentWikiUid", removedWikiPage.getParentWiki().getUid());

	super.delete(object);
	query.executeUpdate();
    }
}