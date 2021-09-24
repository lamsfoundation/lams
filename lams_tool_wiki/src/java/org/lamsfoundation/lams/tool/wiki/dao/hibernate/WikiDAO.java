/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.wiki.dao.hibernate;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.wiki.dao.IWikiDAO;
import org.lamsfoundation.lams.tool.wiki.model.Wiki;
import org.lamsfoundation.lams.tool.wiki.model.WikiPage;
import org.springframework.stereotype.Repository;

/**
 * DAO for accessing the Wiki objects - Hibernate specific code.
 */
@Repository
public class WikiDAO extends LAMSBaseDAO implements IWikiDAO {

    private static final String FIND_FORUM_BY_CONTENTID = "from Wiki wiki where wiki.toolContentId=?";

    @Override
    public Wiki getByContentId(Long toolContentId) {
	List list = doFindCacheable(FIND_FORUM_BY_CONTENTID, toolContentId);
	if (list != null && list.size() > 0) {
	    Wiki wiki = (Wiki) list.get(0);
	    removeDuplicatePages(wiki);
	    return wiki;
	} else {
	    return null;
	}

    }

    @Override
    public void saveOrUpdate(Wiki wiki) {
	// Removing duplicate pages
	removeDuplicatePages(wiki);
	getSession().saveOrUpdate(wiki);
    }

    /**
     * Although we are dealing with a set, still somehow duplicates are coming
     * through. This method removes them.
     *
     * @param wiki
     */
    public void removeDuplicatePages(Wiki wiki) {
	Set<WikiPage> wikiPages = wiki.getWikiPages();
	if (wikiPages != null) {
	    Set<WikiPage> wikiPagesCopy = new HashSet<>(wikiPages);
	    Iterator<WikiPage> it = wikiPages.iterator();
	    while (it.hasNext()) {
		WikiPage page = it.next();
		if (containsDuplicate(page, wikiPagesCopy)) {
		    it.remove();
		    wikiPagesCopy = new HashSet<>(wikiPages);
		}
	    }
	}
    }

    private boolean containsDuplicate(WikiPage compPage, Set<WikiPage> wikiPages) {
	int count = 0;
	for (WikiPage page : wikiPages) {
	    if (page.getTitle().equals(compPage.getTitle())) {
		count++;
	    }
	}
	return count > 1;
    }
}
