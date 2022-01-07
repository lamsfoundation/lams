package edu.uoc.lti.ags;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Created by xaracil@uoc.edu
 */
@RequiredArgsConstructor
public enum ContentTypes {
	LINE_ITEM("application", "vnd.ims.lis.v2.lineitem+json"),
	LINE_ITEM_CONTAINER("application", "vnd.ims.lis.v2.lineitemcontainer+json"),
	RESULT("application", "vnd.ims.lis.v2.resultcontainer+json"),
	SCORE("application", "vnd.ims.lis.v1.score+json")
	;

	@Getter
	private final String type;
	@Getter
	private final String subtype;
}
