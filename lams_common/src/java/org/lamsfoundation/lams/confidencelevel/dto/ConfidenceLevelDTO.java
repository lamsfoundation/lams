package org.lamsfoundation.lams.confidencelevel.dto;

import org.lamsfoundation.lams.usermanagement.User;

public class ConfidenceLevelDTO {

    private User learner;

    private String rating;

    /**
     */
    public User getLearner() {
	return learner;
    }

    public void setLearner(User learner) {
	this.learner = learner;
    }

    /**
     */
    public void setRating(String rating) {
	this.rating = rating;
    }

    public String getRating() {
	return this.rating;
    }
}
