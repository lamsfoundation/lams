package org.lamsfoundation.lams.usermanagement.service;

import org.lamsfoundation.lams.usermanagement.User;

import java.util.Comparator;

/**
 * Interface for all classes that mimic User class.
 */
public interface IUserDetails extends Comparable<IUserDetails> {
    Comparator<IUserDetails> COMPARATOR = Comparator.comparing(IUserDetails::getLastName,
		    Comparator.nullsLast(String::compareTo))
	    .thenComparing(IUserDetails::getFirstName, Comparator.nullsLast(String::compareTo))
	    .thenComparing(IUserDetails::getLogin, Comparator.nullsLast(String::compareTo));

    @Override
    default int compareTo(IUserDetails o) {
	return COMPARATOR.compare(this, o);
    }

    String getFirstName();

    String getLastName();

    String getLogin();
}