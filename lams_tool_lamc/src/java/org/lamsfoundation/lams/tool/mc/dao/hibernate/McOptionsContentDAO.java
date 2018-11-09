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
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcOptionsContentDAO;
import org.lamsfoundation.lams.tool.mc.dto.McOptionDTO;
import org.lamsfoundation.lams.tool.mc.model.McOptsContent;
import org.springframework.stereotype.Repository;

/**
 * Hibernate implementation for database access to McOptionsContent for the mc tool.
 *
 * @author Ozgur Demirtas
 */
@Repository
public class McOptionsContentDAO extends LAMSBaseDAO implements IMcOptionsContentDAO {
    private static Logger logger = Logger.getLogger(McOptionsContentDAO.class.getName());

    private static final String FIND_OPTIONS_BY_QUESTION_UID = "from mcOptsContent in class McOptsContent where mcOptsContent.mcQueContent.uid=:mcQueContentUid order by mcOptsContent.displayOrder";

    @SuppressWarnings("unchecked")
    @Override
    public List<McOptsContent> findMcOptionsContentByQueId(Long questionUid) {
	if (questionUid != null) {
	    List<McOptsContent> list = getSessionFactory().getCurrentSession().createQuery(FIND_OPTIONS_BY_QUESTION_UID)
		    .setParameter("mcQueContentUid", questionUid).list();
	    return list;
	}
	return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<McOptionDTO> getOptionDtos(Long questionUid) {
	List<McOptionDTO> optionDtos = new LinkedList<McOptionDTO>();

	if (questionUid != null) {
	    List<McOptsContent> options = getSessionFactory().getCurrentSession()
		    .createQuery(FIND_OPTIONS_BY_QUESTION_UID).setParameter("mcQueContentUid", questionUid)
		    .list();

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
	this.getSession().update(mcOptsContent);
    }

    @Override
    public void flush() {
	this.getSession().flush();
    }
}