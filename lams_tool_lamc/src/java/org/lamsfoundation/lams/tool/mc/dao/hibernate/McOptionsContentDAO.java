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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.lamsfoundation.lams.tool.mc.McCandidateAnswersDTO;
import org.lamsfoundation.lams.tool.mc.dao.IMcOptionsContentDAO;
import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Ozgur Demirtas
 *         <p>
 *         Hibernate implementation for database access to McOptionsContent for the mc tool.
 *         </p>
 */
public class McOptionsContentDAO extends HibernateDaoSupport implements IMcOptionsContentDAO {
    static Logger logger = Logger.getLogger(McOptionsContentDAO.class.getName());

    private static final String FIND_MC_OPTIONS_CONTENT = "from mcOptsContent in class McOptsContent where mcOptsContent.mcQueContentId=:mcQueContentUid order by mcOptsContent.displayOrder";
    private static final String LOAD_OPTION_CONTENT_BY_OPTION_TEXT = "from mcOptsContent in class McOptsContent where mcOptsContent.mcQueOptionText=:option and mcOptsContent.mcQueContentId=:mcQueContentUid";

    public McOptsContent getMcOptionsContentByUID(Long uid) {
	return (McOptsContent) this.getHibernateTemplate().get(McOptsContent.class, uid);
    }

    public List<McOptsContent> findMcOptionsContentByQueId(Long mcQueContentId) {
	HibernateTemplate templ = this.getHibernateTemplate();
	if (mcQueContentId != null) {
	    List list = getSession().createQuery(FIND_MC_OPTIONS_CONTENT)
		    .setLong("mcQueContentUid", mcQueContentId.longValue()).list();
	    return list;
	}
	return null;
    }

    public List<McCandidateAnswersDTO> populateCandidateAnswersDTO(Long mcQueContentId) {
	List<McCandidateAnswersDTO> listCandidateAnswersData = new LinkedList();

	if (mcQueContentId != null) {
	    List<McOptsContent> options = getSession().createQuery(FIND_MC_OPTIONS_CONTENT)
		    .setLong("mcQueContentUid", mcQueContentId.longValue()).list();

	    if (options != null && options.size() > 0) {
		for (McOptsContent option : options) {
		    McCandidateAnswersDTO mcCandidateAnswersDTO = new McCandidateAnswersDTO();
		    mcCandidateAnswersDTO.setCandidateAnswer(option.getMcQueOptionText());
		    mcCandidateAnswersDTO.setCorrect(new Boolean(option.isCorrectOption()).toString());
		    listCandidateAnswersData.add(mcCandidateAnswersDTO);
		}
	    }
	}
	return listCandidateAnswersData;
    }

    public List<String> findMcOptionCorrectByQueId(Long mcQueContentId) {

	List<String> listOptionCorrect = new LinkedList<String>();

	if (mcQueContentId != null) {
	    List<McOptsContent> options = getSession().createQuery(FIND_MC_OPTIONS_CONTENT)
		    .setLong("mcQueContentUid", mcQueContentId.longValue()).list();

	    if (options != null && options.size() > 0) {
		for (McOptsContent option : options) {
		    listOptionCorrect.add(new Boolean(option.isCorrectOption()).toString());
		}
	    }
	}
	return listOptionCorrect;
    }

    public McOptsContent getOptionContentByOptionText(final String option, final Long mcQueContentUid) {
	List list = getSession().createQuery(LOAD_OPTION_CONTENT_BY_OPTION_TEXT).setString("option", option)
		.setLong("mcQueContentUid", mcQueContentUid.longValue()).list();

	if (list != null && list.size() > 0) {
	    McOptsContent mcq = (McOptsContent) list.get(0);
	    return mcq;
	}
	return null;
    }

    public void saveMcOptionsContent(McOptsContent mcOptsContent) {
	this.getHibernateTemplate().save(mcOptsContent);
    }

    public void updateMcOptionsContent(McOptsContent mcOptsContent) {
	this.getHibernateTemplate().update(mcOptsContent);
    }

    public void removeMcOptionsContentByUID(Long uid) {
	McOptsContent mco = (McOptsContent) getHibernateTemplate().get(McOptsContent.class, uid);
	this.getHibernateTemplate().delete(mco);
    }

    public void removeMcOptionsContentByQueId(Long mcQueContentId) {
	HibernateTemplate templ = this.getHibernateTemplate();
	List list = getSession().createQuery(FIND_MC_OPTIONS_CONTENT)
		.setLong("mcQueContentUid", mcQueContentId.longValue()).list();

	if (list != null && list.size() > 0) {
	    Iterator listIterator = list.iterator();
	    while (listIterator.hasNext()) {
		McOptsContent mcOptsContent = (McOptsContent) listIterator.next();
		this.getSession().setFlushMode(FlushMode.AUTO);
		templ.delete(mcOptsContent);
	    }
	}
    }

    public void removeMcOptionsContent(McOptsContent mcOptsContent) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().delete(mcOptsContent);
    }

    public void flush() {
	this.getHibernateTemplate().flush();
    }
}