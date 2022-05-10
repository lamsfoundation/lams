package edu.uoc.lti.ags;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Setter;

import java.time.Instant;

/**
 * @author Created by xaracil@uoc.edu
 */
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Submission {
	/**
	 * Date and time in ISO 8601 format when a submission can start being submitted by learner
	 */
	private Instant startDateTime;

	public String getStartDateTime() {
		return startDateTime != null ? startDateTime.toString() : null;
	}

	/**
	 * Date and time in ISO 8601 format when a submission can last be submitted by learner
	 */
	private Instant endDateTime;

	public String getEndDateTime() {
		return endDateTime != null ? endDateTime.toString() : null;
	}
}
