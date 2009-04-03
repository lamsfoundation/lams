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
/* $$Id$$ */

package org.lamsfoundation.lams.tool.mindmap.dao.hibernate;

import java.util.List;

import org.hibernate.FlushMode;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.mindmap.dao.IMindmapDAO;
import org.lamsfoundation.lams.tool.mindmap.model.Mindmap;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapAttachment;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * DAO for accessing the Mindmap objects - Hibernate specific code.
 */
public class MindmapDAO extends BaseDAO implements IMindmapDAO {

    private static final String FIND_MINDMAP_BY_CONTENTID = "from Mindmap mindmap where mindmap.toolContentId = ?";

    private static final String FIND_INSTRUCTION_FILE = "from " + MindmapAttachment.class.getName()
	    + " as i where tool_content_id=? and i.file_uuid=? and i.file_version_id=? and i.file_type=?";
    
    private static final String FIND_MINDMAP_BY_UID =
	"from Mindmap mindmap where mindmap.uid = ?";
    
    public Mindmap getByContentId(Long toolContentId) {
	List list = getHibernateTemplate().find(MindmapDAO.FIND_MINDMAP_BY_CONTENTID, toolContentId);
	if (list != null && list.size() > 0) {
	    return (Mindmap) list.get(0);
	} else {
	    return null;
	}
    }
    
    public Mindmap getMindmapByUid(Long Uid) {
	List list = getHibernateTemplate().find(MindmapDAO.FIND_MINDMAP_BY_UID, Uid);
	if (list != null && list.size() > 0) {
	    return (Mindmap) list.get(0);
	} else {
	    return null;
	}
    }
    
    public void saveOrUpdate(Mindmap mindmap) {
	this.getHibernateTemplate().saveOrUpdate(mindmap);
	this.getHibernateTemplate().flush();
    }

    public void deleteInstructionFile(Long toolContentId, Long uuid, Long versionId, String type) {
	HibernateTemplate templ = this.getHibernateTemplate();
	if (toolContentId != null && uuid != null && versionId != null) {
	    List list = getSession().createQuery(MindmapDAO.FIND_INSTRUCTION_FILE).setLong(0,
		    toolContentId.longValue()).setLong(1, uuid.longValue()).setLong(2, versionId.longValue())
		    .setString(3, type).list();
	    if (list != null && list.size() > 0) {
		MindmapAttachment file = (MindmapAttachment) list.get(0);
		this.getSession().setFlushMode(FlushMode.AUTO);
		templ.delete(file);
		templ.flush();
	    }
	}

    }

    public void releaseFromCache(Object o) {
	getSession().evict(o);

    }
}
