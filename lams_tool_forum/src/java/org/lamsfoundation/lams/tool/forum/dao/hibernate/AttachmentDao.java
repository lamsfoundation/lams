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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.forum.dao.hibernate;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.forum.dao.IAttachmentDAO;
import org.lamsfoundation.lams.tool.forum.model.Attachment;
import org.springframework.stereotype.Repository;

/**
 * User: conradb
 * Date: 7/06/2005
 * Time: 12:23:49
 */
@Repository
public class AttachmentDao extends LAMSBaseDAO implements IAttachmentDAO {

    @Override
    public Attachment getById(final Long attachmentId) {
	Attachment entity = (Attachment) getSession().get(Attachment.class, attachmentId);
	return entity;
    }
}
