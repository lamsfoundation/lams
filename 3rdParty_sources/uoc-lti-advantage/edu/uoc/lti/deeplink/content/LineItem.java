package edu.uoc.lti.deeplink.content;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Created by xaracil@uoc.edu
 */
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ToString(callSuper = true)
public class LineItem {
	/**
	 * Positive decimal value indicating the maximum score possible for this activity
	 */
	private final double scoreMaximum;

	/**
	 * label for the line item. If not present, the title of the content item must be used.
	 */
	private String label;

	/**
	 * String, tool provided ID for the resource.
	 */
	private String resourceId;

	/**
	 *  String, additional information about the line item; may be used by the tool to identify line items attached
	 *  to the same resource or resource link (example: grade, originality, participation).
	 */
	private String tag;
}
