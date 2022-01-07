package edu.uoc.elc.lti.tool.oidc;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * @author xaracil@uoc.edu
 */
@Builder
@Getter
@ToString
public class LoginRequest {
	/** REQUIRED. The issuer identifier identifying the learning platform. */
	private final String iss;

	/** REQUIRED. Hint to the Authorization Server about the login identifier the End-User might use to log in.
	 * The permitted values will be defined in the host specification.*/
	private final String login_hint;

	/**
	 * REQUIRED. The actual end-point that should be executed at the end of the OpenID Connect authentication flow.
	 */
	private final String target_link_uri;

	/**
	 * The new optional parameter lti_message_hint may be used alongside the login_hint to carry information about
	 * the actual LTI message that is being launched.
	 *
	 * Similarly to the login_hint parameter, lti_message_hint value is opaque to the tool.
	 * If present in the login initiation request, the tool MUST include it back in the authentication request unaltered.
	 */
	private String lti_message_hint;

	/**
	 * The new optional parameter lti_deployment_id that if included, MUST contain the same deployment id that would be
	 * passed in the https://purl.imsglobal.org/spec/lti/claim/deployment_id claim for the subsequent LTI message launch.
	 *
	 * This parameter may be used by the tool to perform actions that are dependant on a specific deployment.
	 * An example of this would be, using the deployment id to identify the region in which a tenant linked to the
	 * deployment lives. Subsequently changing the redirect_url the final launch will be directed to.
	 */
	private String lti_deployment_id;

	/**
	 * The new optional parameter client_id specifies the client id for the authorization server that should be used to
	 * authorize the subsequent LTI message request. This allows for a platform to support multiple registrations
	 * from a single issuer, without relying on the initiate_login_uri as a key
	 */
	private String client_id;
}
