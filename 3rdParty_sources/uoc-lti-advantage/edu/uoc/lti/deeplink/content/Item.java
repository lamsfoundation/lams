package edu.uoc.lti.deeplink.content;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Created by xaracil@uoc.edu
 */
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ToString
public abstract class Item {
	@Getter
	private final String type;


	// extending a type https://www.imsglobal.org/spec/lti-dl/v2p0#extending-a-type
	// gotten from http://tutorials.jenkov.com/java-json/jackson-annotations.html
	private Map<String, Object> properties = new HashMap<>();

	@JsonAnyGetter
	public Map<String, Object> getProperties() {
		return properties;
	}

	@JsonAnySetter
	public void set(String fieldName, Object value){
		this.properties.put(fieldName, value);
	}

	public Object get(String fieldName){
		return this.properties.get(fieldName);
	}
}
