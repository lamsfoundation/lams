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

package org.lamsfoundation.lams.tool.videoRecorder.dao.hibernate;

import java.util.List;

import org.hibernate.FlushMode;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.videoRecorder.dao.IVideoRecorderDAO;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorder;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderAttachment;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * DAO for accessing the VideoRecorder objects - Hibernate specific code.
 */
public class VideoRecorderDAO extends BaseDAO implements IVideoRecorderDAO {

    private static final String FIND_FORUM_BY_CONTENTID = "from VideoRecorder videoRecorder where videoRecorder.toolContentId=?";

    private static final String FIND_INSTRUCTION_FILE = "from " + VideoRecorderAttachment.class.getName()
	    + " as i where tool_content_id=? and i.file_uuid=? and i.file_version_id=? and i.file_type=?";

    public VideoRecorder getByContentId(Long toolContentId) {
	List list = getHibernateTemplate().find(VideoRecorderDAO.FIND_FORUM_BY_CONTENTID, toolContentId);
	if (list != null && list.size() > 0) {
	    return (VideoRecorder) list.get(0);
	} else {
	    return null;
	}
    }

    public void saveOrUpdate(VideoRecorder videoRecorder) {
	this.getHibernateTemplate().saveOrUpdate(videoRecorder);
	this.getHibernateTemplate().flush();
    }

    public void deleteInstructionFile(Long toolContentId, Long uuid, Long versionId, String type) {
	HibernateTemplate templ = this.getHibernateTemplate();
	if (toolContentId != null && uuid != null && versionId != null) {
	    List list = getSession().createQuery(VideoRecorderDAO.FIND_INSTRUCTION_FILE).setLong(0,
		    toolContentId.longValue()).setLong(1, uuid.longValue()).setLong(2, versionId.longValue())
		    .setString(3, type).list();
	    if (list != null && list.size() > 0) {
		VideoRecorderAttachment file = (VideoRecorderAttachment) list.get(0);
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
