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

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.mc.McOptionDTO;
import org.lamsfoundation.lams.tool.mc.dao.IMcOptionsContentDAO;
import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation for database access to McOptionsContent for the mc tool.
 *
 * @author Ozgur Demirtas
 */
public class McOptionsContentDAO extends HibernateDaoSupport implements IMcOptionsContentDAO {
    private static Logger logger = Logger.getLogger(McOptionsContentDAO.class.getName());

    private static final String FIND_OPTIONS_BY_QUESTION_UID = "from mcOptsContent in class McOptsContent where mcOptsContent.mcQueContentId=:mcQueContentUid order by mcOptsContent.displayOrder";

    @Override
    public List<McOptsContent> findMcOptionsContentByQueId(Long questionUid) {
	if (questionUid != null) {
	    List<McOptsContent> list = getSession().createQuery(FIND_OPTIONS_BY_QUESTION_UID)
		    .setLong("mcQueContentUid", questionUid.longValue()).list();
	    return list;
	}
	return null;
    }

    @Override
    public List<McOptionDTO> getOptionDtos(Long questionUid) {
	List<McOptionDTO> optionDtos = new LinkedList();

	if (questionUid != null) {
	    List<McOptsContent> options = getSession().createQuery(FIND_OPTIONS_BY_QUESTION_UID)
		    .setLong("mcQueContentUid", questionUid.longValue()).list();

	    if (options != null && options.size() > 0) {
		for (McOptsContent option : options) {
		    McOptionDTO optionDto = new McOptionDTO(option);
		    optionDtos.add(optionDto);
		}
	    }
	}
	return optionDtos;
    }

    @Override
    public void updateMcOptionsContent(McOptsContent mcOptsContent) {
	this.getHibernateTemplate().update(mcOptsContent);
    }

    public void flush() {
	this.getHibernateTemplate().flush();
    }
}