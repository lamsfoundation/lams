package edu.uoc.elc.lti.platform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author xaracil@uoc.edu
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Member {
	@JsonProperty("context_id")
	private String contextId;
	@JsonProperty("context_label")
	private String contextLabel;
	@JsonProperty("context_title")
	private String contextTitle;
	@JsonProperty("name")
	private String name;
	@JsonProperty("picture")
	private String picture;
	@JsonProperty("given_name")
	private String givenName;
	@JsonProperty("family_name")
	private String familyName;
	@JsonProperty("middle_name")
	private String middleName;
	@JsonProperty("email")
	private String email;
	@JsonProperty("user_id")
	private String userId;
	@JsonProperty("lis_person_sourcedid")
	private String lisPersonSourceid;
	@JsonProperty("roles")
	private List<String> roles;
}
