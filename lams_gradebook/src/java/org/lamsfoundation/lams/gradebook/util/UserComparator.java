package org.lamsfoundation.lams.gradebook.util;

import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * Sorts users by last names.
 */
public class UserComparator implements Comparator<User> {
    @Override
    public int compare(User o1, User o2) {
	return new CompareToBuilder().append(o1.getLastName(), o2.getLastName())
		.append(o1.getFirstName(), o2.getFirstName()).append(o1.getLogin(), o2.getLogin()).toComparison();
    }
}
