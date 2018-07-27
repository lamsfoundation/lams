package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * Learner that uses a gate.
 * It contains information of the user and the status can he/she pass the gate.
 * 
 * @author Marcin Cieslak
 *
 */
public class GateUser implements Serializable {
    private User user;
    private Boolean allowedToPass;

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public Boolean getAllowedToPass() {
	return allowedToPass;
    }

    public void setAllowedToPass(Boolean passed) {
	allowedToPass = passed;
    }

    @Override
    public boolean equals(Object o) {
	return o instanceof GateUser && ((GateUser) o).user.equals(user)
		&& ((GateUser) o).allowedToPass.equals(allowedToPass);
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(user).append(allowedToPass).toHashCode();
    }
}
