package org.lamsfoundation.lams.lesson.dao;

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.lesson.FavoriteLesson;
import org.lamsfoundation.lams.lesson.Lesson;

/**
 * DAO methods for FavoriteLesson class.
 * 
 * @author Andrey Balan
 */
public interface IFavoriteLessonDAO extends IBaseDAO {
    
    /**
     * Returns FavoriteLesson object for the given user and lesson.
     * 
     * @param lessonId
     * @param userId
     * @return
     */
    FavoriteLesson getFavoriteLesson(Long lessonId, Integer userId);

    /**
     * Return all lessons that were marked as favorite by the specified user in specified organisation.
     */
    List<Long> getFavoriteLessonsByOrgAndUser(Integer organisationId, Integer userId);
}
