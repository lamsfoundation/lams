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



package org.lamsfoundation.lams.comments.dao.hibernate;

import org.lamsfoundation.lams.comments.dao.ICommentLikeDAO;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;

public class CommentLikeDAO extends LAMSBaseDAO implements ICommentLikeDAO {

    private static String INSERT_LIKE = "INSERT IGNORE INTO lams_comment_likes(comment_uid, user_id, vote) VALUES (:comment,:user,:vote);";

    @Override
    public boolean addLike(Long commentUid, Integer userId, Integer vote) {
	int status = getSession().createNativeQuery(INSERT_LIKE).setParameter("comment", commentUid)
		.setParameter("user", userId).setParameter("vote", vote).executeUpdate();
	return status == 1;
    }

}
