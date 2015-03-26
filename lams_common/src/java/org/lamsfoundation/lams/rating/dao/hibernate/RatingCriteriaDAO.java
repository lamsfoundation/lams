/**************************************************************** 
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org) 
 * ============================================================= 
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/ 
 * 
 * This program is free software; you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation. 
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU General Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA 
 * 
 * http://www.gnu.org/licenses/gpl.txt 
 * **************************************************************** 
 */

/* $Id$ */
package org.lamsfoundation.lams.rating.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.rating.dao.IRatingCriteriaDAO;
import org.lamsfoundation.lams.rating.model.Rating;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.rating.model.ToolActivityRatingCriteria;

public class RatingCriteriaDAO extends BaseDAO implements IRatingCriteriaDAO {

    private static final String FIND_BY_TOOL_CONTENT_ID = "from " + RatingCriteria.class.getName()
	    + " as r where r.toolContentId=? order by r.orderId asc";

    @Override
    public void saveOrUpdate(RatingCriteria criteria) {
	this.getHibernateTemplate().saveOrUpdate(criteria);
	this.getHibernateTemplate().flush();
    }

    @Override
    public void deleteRatingCriteria(Long ratingCriteriaId) {
	this.deleteById(RatingCriteria.class, ratingCriteriaId);
    }

    @Override
    public List<RatingCriteria> getByToolContentId(Long toolContentId) {
	return (List<RatingCriteria>) (getHibernateTemplate().find(FIND_BY_TOOL_CONTENT_ID,
		new Object[] { toolContentId }));
    }
    
    public RatingCriteria getByUid(Long ratingCriteriaId) {
	if (ratingCriteriaId != null) {
	    Object o = getHibernateTemplate().get(RatingCriteria.class, ratingCriteriaId);
	    return (RatingCriteria) o;
	} else {
	    return null;
	}
    }
}
