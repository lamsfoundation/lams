package edu.uoc.lti.ags;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Created by xaracil@uoc.edu
 */
@RequiredArgsConstructor
public enum ActivityProgressEnum {
	INITIALIZED("Initialized"),
	STARTED("Started"),
	IN_PROGRESS("InProgress"),
	SUBMITTED("Submitted"),
	COMPLETED("Completed")
	;

	@Getter
	private final String value;
}
