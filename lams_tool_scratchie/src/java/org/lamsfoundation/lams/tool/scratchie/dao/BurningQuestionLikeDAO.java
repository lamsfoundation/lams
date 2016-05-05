package org.lamsfoundation.lams.tool.scratchie.dao;

public interface BurningQuestionLikeDAO {

    boolean addLike(Long burningQuestionUid, Long sessionId);

    void removeLike(Long burningQuestionUid, Long sessionId);
}
