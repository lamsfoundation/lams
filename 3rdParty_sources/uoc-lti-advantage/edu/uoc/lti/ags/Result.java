package edu.uoc.lti.ags;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Created by xaracil@uoc.edu
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Result {
	/**
	 * URL end point for the resource. It must be present on all responses containing the resource and may be used for subsequent operations on that resource.
	 */
	private String id;

	/**
	 * Recipient of the result, usually a student
	 */
	private String userId;

	/**
	 * Current score for this line item and user, in scale with resultMaximum
	 */
	private double resultScore;

	/**
	 * Maximum possible score for this result; 1 is the default value and will be assumed if not specified otherwise.
	 */
	private double resultMaximum;

	/**
	 * Comment visible to the student about the Result.
	 */
	private String comment;

	/**
	 * URL of the line item this result belongs to; must be the same as the url property of the line item.
	 */
	private String scoreOf;
}
