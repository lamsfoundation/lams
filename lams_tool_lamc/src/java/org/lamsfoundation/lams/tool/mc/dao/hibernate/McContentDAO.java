/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcContentDAO;
import org.lamsfoundation.lams.tool.mc.model.McContent;
import org.lamsfoundation.lams.tool.mc.model.McSession;
import org.springframework.stereotype.Repository;

/**
 * @author Ozgur Demirtas
 *         <p>
 *         Hibernate implementation for database access to McContent for the mc tool.
 *         </p>
 */
@Repository
public class McContentDAO extends LAMSBaseDAO implements IMcContentDAO {

    private static final String FIND_MC_CONTENT = "from " + McContent.class.getName()
	    + " as mc where content_id=:mcContentId";

    @Override
    public McContent getMcContentByUID(Long uid) {
	return this.getSession().get(McContent.class, uid);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public McContent findMcContentById(Long mcContentId) {
	List list = getSessionFactory().getCurrentSession().createQuery(FIND_MC_CONTENT)
		.setParameter("mcContentId", mcContentId.longValue()).setCacheable(true).list();

	if (list != null && list.size() > 0) {
	    McContent mc = (McContent) list.get(0);
	    return mc;
	}
	return null;
    }

    @Override
    public void saveMcContent(McContent mcContent) {
	this.getSession().saveOrUpdate(mcContent);
    }

    @Override
    public void updateMcContent(McContent mcContent) {
	this.getSession().update(mcContent);
    }

    @Override
    public void saveOrUpdateMc(McContent mc) {
	this.getSession().saveOrUpdate(mc);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void removeMcById(Long mcContentId) {
	if (mcContentId != null) {
	    List list = getSessionFactory().getCurrentSession().createQuery(FIND_MC_CONTENT)
		    .setParameter("mcContentId", mcContentId.longValue()).list();

	    if (list != null && list.size() > 0) {
		McContent mc = (McContent) list.get(0);
		getSession().delete(mc);
		getSession().flush();
	    }
	}
    }

    @Override
    public void removeMcSessions(McContent mcContent) {
	deleteAll(mcContent.getMcSessions());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addMcSession(Long mcContentId, McSession mcSession) {
	McContent content = findMcContentById(mcContentId);
	mcSession.setMcContent(content);
	content.getMcSessions().add(mcSession);
	this.getSession().saveOrUpdate(mcSession);
	this.getSession().saveOrUpdate(content);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List findAll(Class objClass) {
	String query = "from obj in class " + objClass.getName();
	return doFind(query);
    }

    @Override
    public void flush() {
	this.getSession().flush();
    }

    @Override
    public void delete(Object object) {
	getSession().delete(object);
    }
}
