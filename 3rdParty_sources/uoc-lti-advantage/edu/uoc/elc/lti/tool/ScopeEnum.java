package edu.uoc.elc.lti.tool;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xaracil@uoc.edu
 */
@Getter
@AllArgsConstructor
public enum ScopeEnum {

	AGS_SCOPE_LINE_ITEM ("https://purl.imsglobal.org/spec/lti-ags/scope/lineitem"),
	AGS_SCOPE_LINE_ITEM_READONLY ("https://purl.imsglobal.org/spec/lti-ags/scope/lineitem.readonly"),
	AGS_SCOPE_RESULT ("https://purl.imsglobal.org/spec/lti-ags/scope/result.readonly"),
	AGS_SCOPE_SCORE ("https://purl.imsglobal.org/spec/lti-ags/scope/score"),
	NAMES_AND_ROLES_SCOPE ("https://purl.imsglobal.org/spec/lti-nrps/scope/contextmembership.readonly"),
	CALIPER_SCOPE ("https://purl.imsglobal.org/spec/lti-ces/v1p0/scope/send")
	;

	private String scope;
}
