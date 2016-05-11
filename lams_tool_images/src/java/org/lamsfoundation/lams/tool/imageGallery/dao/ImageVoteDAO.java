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


package org.lamsfoundation.lams.tool.imageGallery.dao;

import org.lamsfoundation.lams.tool.imageGallery.model.ImageVote;

/**
 * DAO interface for <code>ImageVote</code>.
 *
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.tool.imageGallery.model.ImageRating
 */
public interface ImageVoteDAO extends DAO {

    /**
     * Return imageVote by the given imageUid and userId.
     *
     * @param imageUid
     * @param userId
     * @return
     */
    public ImageVote getImageVoteByImageAndUser(Long imageUid, Long userId);

    /**
     * Return number of imageVotes that users have done for this image.
     *
     * @param imageUid
     * @param userId
     * @return
     */
    public int getNumImageVotesByImageUid(Long imageUid, Long sessionId);

    /**
     * Return number of imageVotes made by user.
     *
     * @param userId
     * @return
     */
    public int getNumImageVotesByUserId(Long userId);

}
