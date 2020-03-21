package org.lamsfoundation.lams.lesson;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;

@Entity
@Table(name = "lams_favorite_lesson")
public class FavoriteLesson implements Serializable {
    private static final long serialVersionUID = 6320034459017328098L;

    @Id
    @Column(name = "favorite_lesson_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer favoriteLessonId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    public FavoriteLesson(User user, Lesson lesson) {
	this.user = user;
	this.lesson = lesson;
    }

    public FavoriteLesson() {
    }

    public Integer getFavoriteLessonId() {
	return this.favoriteLessonId;
    }

    public void setFavoriteLessonId(Integer favoriteLessonId) {
	this.favoriteLessonId = favoriteLessonId;
    }

    public User getUser() {
	return this.user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public Lesson getlesson() {
	return this.lesson;
    }

    public void setlesson(Lesson lesson) {
	this.lesson = lesson;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("favoriteLessonId", getFavoriteLessonId()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if (!(other instanceof FavoriteLesson)) {
	    return false;
	}
	FavoriteLesson castOther = (FavoriteLesson) other;
	return new EqualsBuilder().append(this.getFavoriteLessonId(), castOther.getFavoriteLessonId())
		.isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getFavoriteLessonId()).toHashCode();
    }
}