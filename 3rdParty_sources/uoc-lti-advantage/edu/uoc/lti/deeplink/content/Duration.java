package edu.uoc.lti.deeplink.content;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

/**
 * @author Created by xaracil@uoc.edu
 */
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ToString
public class Duration {
	private Instant startDate;
	private Instant endDate;

	public String getStartDateTime() {
		return startDate != null ? startDate.toString() : null;
	}

	public String getEndDateTime() {
		return endDate != null ? endDate.toString() : null;
	}
}
