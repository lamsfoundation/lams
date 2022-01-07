package edu.uoc.elc.lti.tool;

import edu.uoc.elc.lti.exception.BadToolProviderConfigurationException;
import edu.uoc.elc.lti.platform.accesstoken.AccessTokenRequestHandler;
import edu.uoc.elc.lti.platform.accesstoken.AccessTokenResponse;
import edu.uoc.elc.lti.platform.ags.AgsClientFactory;
import edu.uoc.elc.lti.platform.deeplinking.DeepLinkingClient;
import edu.uoc.elc.lti.tool.deeplinking.Settings;
import edu.uoc.elc.lti.tool.oidc.AuthRequestUrlBuilder;
import edu.uoc.elc.lti.tool.oidc.LoginRequest;
import edu.uoc.elc.lti.tool.oidc.LoginResponse;
import edu.uoc.lti.MessageTypesEnum;
import edu.uoc.lti.claims.ClaimAccessor;
import edu.uoc.lti.claims.ClaimsEnum;
import edu.uoc.lti.oidc.OIDCLaunchSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author xaracil@uoc.edu
 */
@RequiredArgsConstructor
public class Tool {
	@Getter
	String issuer;
	@Getter
	String audience;

	@Getter
	String kid;

	@Getter
	Date issuedAt;

	@Getter
	Date expiresAt;

	@Getter
	private User user;

	@Getter
	private String locale;

	@Getter
	private boolean valid;

	@Getter
	private String reason;

	private AccessTokenResponse accessTokenResponse;

	private final ToolDefinition toolDefinition;
	private final ClaimAccessor claimAccessor;
	private final OIDCLaunchSession oidcLaunchSession;
	private final ToolBuilders toolBuilders;

	public boolean validate(String token, String state) {
		LaunchValidator launchValidator = new LaunchValidator(toolDefinition, claimAccessor, oidcLaunchSession);
		this.valid = launchValidator.validate(token, state);
		if (!this.valid) {
			this.reason = launchValidator.getReason();
			return false;
		}

		// get the standard JWT payload claims
		this.issuer = this.claimAccessor.getIssuer();
		this.audience = this.claimAccessor.getAudience();
		this.issuedAt = this.claimAccessor.getIssuedAt();
		this.expiresAt = this.claimAccessor.getExpiration();

		// create the user attribute
		createUser(this.claimAccessor.getSubject());

		// update locale attribute
		this.locale = this.claimAccessor.get(ClaimsEnum.LOCALE);

		return this.valid;
	}

	public AccessTokenResponse getAccessToken() throws IOException, BadToolProviderConfigurationException {
		if (!this.isValid()) {
			return null;
		}

		if (accessTokenResponse == null) {
			AccessTokenRequestHandler accessTokenRequestHandler = new AccessTokenRequestHandler(kid,
							toolDefinition,
							toolBuilders.getClientCredentialsTokenBuilder(),
							toolBuilders.getAccessTokenRequestBuilder());
			accessTokenResponse = accessTokenRequestHandler.getAccessToken();
		}

		return accessTokenResponse;
	}

	private void createUser(String subject) {
		this.user = User.builder()
						.id(subject)
						.givenName(this.claimAccessor.get(ClaimsEnum.GIVEN_NAME))
						.familyName(this.claimAccessor.get(ClaimsEnum.FAMILY_NAME))
						.middleName(this.claimAccessor.get(ClaimsEnum.MIDDLE_NAME))
						.picture(this.claimAccessor.get(ClaimsEnum.PICTURE))
						.email(this.claimAccessor.get(ClaimsEnum.EMAIL))
						.name(this.claimAccessor.get(ClaimsEnum.NAME))
						.build();
	}


	// general claims getters
	public Platform getPlatform() {
		return this.claimAccessor.get(ClaimsEnum.TOOL_PLATFORM, Platform.class);
	}

	public Context getContext() {
		return this.claimAccessor.get(ClaimsEnum.CONTEXT, Context.class);
	}

	public ResourceLink getResourceLink() {
		return this.claimAccessor.get(ClaimsEnum.RESOURCE_LINK, ResourceLink.class);
	}

	public NamesRoleService getNameRoleService() {
		return this.claimAccessor.get(ClaimsEnum.NAMES_ROLE_SERVICE, NamesRoleService.class);
	}

	public AssignmentGradeService getAssignmentGradeService() {
		return this.claimAccessor.get(ClaimsEnum.ASSIGNMENT_GRADE_SERVICE, AssignmentGradeService.class);
	}

	public String getDeploymentId() {
		if (!isDeepLinkingRequest()) {
			return null;
		}
		return this.claimAccessor.get(ClaimsEnum.DEPLOYMENT_ID);
	}

	public Settings getDeepLinkingSettings() {
		if (!isDeepLinkingRequest()) {
			return null;
		}
		return this.claimAccessor.get(ClaimsEnum.DEEP_LINKING_SETTINGS, Settings.class);
	}


	public List<String> getRoles() {
		Class<List<String>> rolesClass = (Class) List.class;
		return this.claimAccessor.get(ClaimsEnum.ROLES, rolesClass);
	}

	public String getCustomParameter(String name) {
		Class<Map<String, String>> customClass = (Class) Map.class;
		final Map<String, String> claim = this.claimAccessor.get(ClaimsEnum.CUSTOM, customClass);
		if (claim != null) {
			return claim.get(name);
		}
		return null;
	}

	public MessageTypesEnum getMessageType() {
		try {
			return MessageTypesEnum.valueOf(this.claimAccessor.get(ClaimsEnum.MESSAGE_TYPE));
		} catch (IllegalArgumentException ignored) {
			return null;
		}
	}

	public boolean isDeepLinkingRequest() {
		return MessageTypesEnum.LtiDeepLinkingRequest == getMessageType();
	}

	public boolean isResourceLinkLaunch() {
		return MessageTypesEnum.LtiResourceLinkRequest == getMessageType();
	}

	public DeepLinkingClient getDeepLinkingClient() {
		if (!isDeepLinkingRequest()) {
			return null;
		}

		return new DeepLinkingClient(
						toolBuilders.getDeepLinkingTokenBuilder(),
						getIssuer(),
						toolDefinition.getClientId(),
						this.claimAccessor.getAzp(),
						getDeploymentId(),
						this.claimAccessor.get(ClaimsEnum.NONCE),
						getDeepLinkingSettings());
	}

	public AgsClientFactory getAssignmentGradeServiceClientFactory() {
		return new AgsClientFactory(getAssignmentGradeService(),
						getResourceLink());
	}

	// roles commodity methods
	public boolean isLearner() {
		return getRoles() != null && getRoles().contains(RolesEnum.LEARNER.getName());
	}

	public boolean isInstructor() {
		return getRoles() != null && getRoles().contains(RolesEnum.INSTRUCTOR.getName());
	}

	// openid methods
	public String getOidcAuthUrl(LoginRequest loginRequest) throws URISyntaxException {
		final LoginResponse loginResponse = LoginResponse.builder()
						.client_id(loginRequest.getClient_id() != null ? loginRequest.getClient_id() : toolDefinition.getClientId())
						.redirect_uri(loginRequest.getTarget_link_uri())
						.login_hint(loginRequest.getLogin_hint())
						.state(new BigInteger(50, new SecureRandom()).toString(16))
						.nonce(new BigInteger(50, new SecureRandom()).toString(16))
						.lti_message_hint(loginRequest.getLti_message_hint())
						.build();

		final URI uri = new URI(loginRequest.getTarget_link_uri());

		// save in session
		this.oidcLaunchSession.setState(loginResponse.getState());
		this.oidcLaunchSession.setNonce(loginResponse.getNonce());
		this.oidcLaunchSession.setTargetLinkUri(loginResponse.getRedirect_uri());

		// return url
		return AuthRequestUrlBuilder.build(toolDefinition.getOidcAuthUrl(), loginResponse);
	}
}
