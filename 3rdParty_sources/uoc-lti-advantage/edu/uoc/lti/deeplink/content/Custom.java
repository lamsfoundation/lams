package edu.uoc.lti.deeplink.content;

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
@ToString
public class Custom {
	private String quiz_id;
	private String duedate;
}
