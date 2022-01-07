package edu.uoc.elc.lti.tool.oidc;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * @author xaracil@uoc.edu
 */
@Getter
@Builder
@ToString
public class LoginResponse {
	private final String scope = "openid";
	private final String response_type = "id_token";
	/** The Tool’s Client ID for this issuer. */
	private final String client_id;
	/** One of the registered redirect URIs. **/
	private final String redirect_uri;
	/** As passed in the initiate login request.*/
	private final String login_hint;
	/** Opaque value for the platform to maintain state between the request and callback and provide Cross-Site Request Forgery (CSRF) mitigation.**/
	private String state;
	/** The Token can be lengthy and thus should be passed over as a form POST. */
	private final String response_mode = "form_post";
	/** String value used to associate a Client session with an ID Token, and to mitigate replay attacks.
	 * The value is passed through unmodified from the Authentication Request to the ID Token.*/
	private final String nonce;
	/**
	 * Since the message launch is meant to be sent from a platform where the user is already logged in.
	 * If the user has no session, a platform must just fail the flow rather than ask the user to log in.
	 */
	private final String prompt = "none";
	private String lti_message_hint;
}
