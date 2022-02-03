package edu.uoc.lti.accesstoken;

import lombok.Builder;
import lombok.Getter;

/**
 * @author Created by xaracil@uoc.edu
 */
@Getter
@Builder
public class AccessTokenRequest {
	private String grant_type;
	private String client_assertion_type;
	private String scope;
	private String client_assertion;
}
