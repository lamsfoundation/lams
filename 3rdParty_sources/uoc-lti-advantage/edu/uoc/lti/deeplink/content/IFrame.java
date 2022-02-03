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
@ToString
public class IFrame {
	private final String url;
	private int width;
	private int height;

}
