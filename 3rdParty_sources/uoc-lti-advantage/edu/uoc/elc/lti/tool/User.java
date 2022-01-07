package edu.uoc.elc.lti.tool;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xaracil@uoc.edu
 */
@Getter
@Setter
@Builder
public class User {
	private String id;
	private String givenName;
	private String familyName;
	private String middleName;
	private String picture;
	private String email;
	private String name;
}
