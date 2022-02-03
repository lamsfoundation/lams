package edu.uoc.elc.lti.platform;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author xaracil@uoc.edu
 */
@Getter
@Setter
public class NamesRoleServiceResponse {
	private String id;
	private List<Member> members;
}
