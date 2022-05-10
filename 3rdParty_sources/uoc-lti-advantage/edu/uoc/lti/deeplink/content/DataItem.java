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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ToString(callSuper = true)
public class DataItem extends Item {
	private String data;

	@Builder
	public DataItem(String type, String data) {
		super(type);
		this.data = data;
	}
}
