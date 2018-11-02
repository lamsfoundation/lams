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


package org.lamsfoundation.lams.tool.imageGallery.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.imageGallery.dao.ImageVoteDAO;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageVote;
import org.springframework.stereotype.Repository;

/**
 * Hibernate implementation of <code>ImageVoteDAO</code>.
 *
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.tool.imageGallery.dao.ImageVoteDAO
 */
@Repository
public class ImageVoteDAOHibernate extends LAMSBaseDAO implements ImageVoteDAO {
    private static final String FIND_BY_IMAGE_AND_USER = "from " + ImageVote.class.getName()
	    + " as r where r.createBy.userId = ? and r.imageGalleryItem.uid=?";

    private static final String FIND_IMAGE_VOTES_COUNT_BY_USER = "select count(*) from " + ImageVote.class.getName()
	    + " as r where r.voted=true and r.createBy.userId = ?";

    private static final String FIND_IMAGE_VOTES_COUNT_BY_IMAGE = "select count(*) from " + ImageVote.class.getName()
	    + " as r where r.voted=true and r.imageGalleryItem.uid = ? and r.createBy.session.sessionId=?";

    @Override
    public ImageVote getImageVoteByImageAndUser(Long imageUid, Long userId) {
	List<ImageVote> list = (List<ImageVote>) doFind(ImageVoteDAOHibernate.FIND_BY_IMAGE_AND_USER, new Object[] { userId, imageUid });
	if ((list == null) || (list.size() == 0)) {
	    return null;
	}
	return (ImageVote) list.get(0);
    }

    @Override
    public int getNumImageVotesByImageUid(Long imageUid, Long sessionId) {
	List<?> list = doFind(ImageVoteDAOHibernate.FIND_IMAGE_VOTES_COUNT_BY_IMAGE, new Object[] { imageUid, sessionId });
	if ((list == null) || (list.size() == 0)) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    @Override
    public int getNumImageVotesByUserId(Long userId) {
	List<?> list = doFind(ImageVoteDAOHibernate.FIND_IMAGE_VOTES_COUNT_BY_USER, userId);
	if ((list == null) || (list.size() == 0)) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

}
