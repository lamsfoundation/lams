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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc.dao.hibernate;

import java.util.List;

import org.hibernate.FlushMode;
import org.lamsfoundation.lams.tool.mc.dao.IMcContentDAO;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Ozgur Demirtas
 *         <p>
 *         Hibernate implementation for database access to McContent for the mc tool.
 *         </p>
 */
public class McContentDAO extends HibernateDaoSupport implements IMcContentDAO {

    private static final String FIND_MC_CONTENT = "from " + McContent.class.getName() + " as mc where content_id=?";

    public McContent getMcContentByUID(Long uid) {
	return (McContent) this.getHibernateTemplate().get(McContent.class, uid);
    }

    public McContent findMcContentById(Long mcContentId) {
	String query = "from McContent as mc where mc.mcContentId = ?";
	HibernateTemplate templ = this.getHibernateTemplate();

	List list = getSession().createQuery(FIND_MC_CONTENT).setLong(0, mcContentId.longValue()).list();

	if (list != null && list.size() > 0) {
	    McContent mc = (McContent) list.get(0);
	    return mc;
	}
	return null;
    }

    public void saveMcContent(McContent mcContent) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().saveOrUpdate(mcContent);
    }

    public void updateMcContent(McContent mcContent) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().update(mcContent);
    }

    public void saveOrUpdateMc(McContent mc) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().saveOrUpdate(mc);
    }

    public void removeMcById(Long mcContentId) {
	HibernateTemplate templ = this.getHibernateTemplate();
	if (mcContentId != null) {
	    List list = getSession().createQuery(FIND_MC_CONTENT).setLong(0, mcContentId.longValue()).list();

	    if (list != null && list.size() > 0) {
		McContent mc = (McContent) list.get(0);
		this.getSession().setFlushMode(FlushMode.AUTO);
		templ.delete(mc);
		templ.flush();
	    }
	}
    }

    public void removeMcSessions(McContent mcContent) {
	this.getHibernateTemplate().deleteAll(mcContent.getMcSessions());
    }

    public void addMcSession(Long mcContentId, McSession mcSession) {
	McContent content = findMcContentById(mcContentId);
	mcSession.setMcContent(content);
	content.getMcSessions().add(mcSession);
	this.getHibernateTemplate().saveOrUpdate(mcSession);
	this.getHibernateTemplate().saveOrUpdate(content);
    }

    public List findAll(Class objClass) {
	String query = "from obj in class " + objClass.getName();
	return this.getHibernateTemplate().find(query);
    }

    public void flush() {
	this.getHibernateTemplate().flush();
    }

    @Override
    public void delete(Object object) {
	getHibernateTemplate().delete(object);
    }
}
