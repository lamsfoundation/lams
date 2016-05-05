package org.lamsfoundation.lams.comments.dao;

public interface ICommentLikeDAO {

    boolean addLike(Long commentUid, Integer userId, Integer vote);
}