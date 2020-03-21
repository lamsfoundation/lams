package org.lamsfoundation.lams.lesson.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.lesson.FavoriteLesson;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.IFavoriteLessonDAO;
import org.springframework.stereotype.Repository;

/**
 * Hibernate implementation of IFavoriteLessonDAO
 *
 * @author Andrey Balan
 */
@Repository
public class FavoriteLessonDAO extends LAMSBaseDAO implements IFavoriteLessonDAO {
    
    @Override
    public FavoriteLesson getFavoriteLesson(Long lessonId, Integer userId) {
	final String query = "SELECT fav from FavoriteLesson fav WHERE fav.lesson.lessonId=" + lessonId
		+ " AND fav.user.userId=" + userId;
	return (FavoriteLesson) getSession().createQuery(query).uniqueResult();
    }

    @Override
    public List<Long> getFavoriteLessonsByOrgAndUser(Integer organisationId, Integer userId) {
	final String query = "SELECT fav.lesson.lessonId from FavoriteLesson fav WHERE fav.user.userId=" + userId
		+ " AND fav.lesson.organisation.organisationId = " + organisationId
		+ " ORDER BY fav.lesson.lessonId";
	return find(query);
    }
}
