package edu.uoc.elc.lti.tool.validator;

import edu.uoc.elc.lti.tool.Context;
import edu.uoc.elc.lti.tool.Platform;
import edu.uoc.elc.lti.tool.RolesEnum;
import edu.uoc.elc.lti.tool.ToolDefinition;
import edu.uoc.lti.MessageTypesEnum;
import edu.uoc.lti.claims.ClaimAccessor;
import edu.uoc.lti.claims.ClaimsEnum;
import edu.uoc.lti.deeplink.content.DocumentTargetEnum;
import edu.uoc.lti.deeplink.content.Presentation;
import edu.uoc.lti.oidc.OIDCLaunchSession;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Core validator, validades only core requirements (common to all launches)
 * Intented to expand by subclasses
 * @author xaracil@uoc.edu
 */
public class LTICoreValidator implements LaunchValidatable {
	static final int ID_MAX_LENGTH = 255;
	final static String VERSION = "1.3.0";

	@Getter
	String reason;

	@Override
	public boolean validate(String state, ToolDefinition toolDefinition, ClaimAccessor claimAccessor, OIDCLaunchSession oidcLaunchSession) {
		if (!this.validateRequiredFields(toolDefinition, claimAccessor)) {
			return false;
		}

		if (!this.validateOptionalFields(claimAccessor)) {
			return false;
		}

		return true;
	}

	private boolean validateRequiredFields(ToolDefinition toolDefinition, ClaimAccessor claimAccessor) {
		// Message type
		final String messageTypeClaim = claimAccessor.get(ClaimsEnum.MESSAGE_TYPE);
		if (messageTypeClaim == null) {
			setReasonToMissingRequiredClaim(ClaimsEnum.MESSAGE_TYPE);
			return false;
		}

		try {
			MessageTypesEnum.valueOf(messageTypeClaim);
		} catch (IllegalArgumentException e) {
			setReasonToInvalidClaim(ClaimsEnum.MESSAGE_TYPE);
			return false;
		}

		// LTI version
		final String versionClaim = claimAccessor.get(ClaimsEnum.VERSION);
		if (versionClaim == null || !VERSION.equals(versionClaim)) {
			setReasonToInvalidClaim(ClaimsEnum.VERSION);
			return false;
		}

		// Deployment ID
		final String deploymentId = claimAccessor.get(ClaimsEnum.DEPLOYMENT_ID);
		if (isEmpty(deploymentId)) {
			setReasonToMissingRequiredClaim(ClaimsEnum.DEPLOYMENT_ID);
			return false;
		}
		if (deploymentId.trim().length() > ID_MAX_LENGTH) {
			setReasonToInvalidClaim(ClaimsEnum.DEPLOYMENT_ID);
			return false;
		}
		if (!toolDefinition.getDeploymentId().equals(deploymentId)) {
			setReasonToInvalidClaim(ClaimsEnum.DEPLOYMENT_ID);
			return false;
		}

		// User
		final String subject = claimAccessor.getSubject();
		if (isEmpty(subject)) {
			// check other user identity claims are empty as well
			List<ClaimsEnum> identityClaims = Arrays.asList(ClaimsEnum.GIVEN_NAME, ClaimsEnum.FAMILY_NAME, ClaimsEnum.NAME, ClaimsEnum.EMAIL);
			if (identityClaims.stream().anyMatch(claim -> !isEmpty(claimAccessor.get(claim)))) {
				this.reason = "Subject is required";
				return false;
			}
		} else {
			if (!isIdStringValid(subject)) {
				this.reason = "Subject is invalid";
				return false;
			}
		}

		// Roles
		Class<List<String>> rolesClass = (Class) List.class;
		final List<String> roles = claimAccessor.get(ClaimsEnum.ROLES, rolesClass);
		if (roles == null) {
			setReasonToMissingRequiredClaim(ClaimsEnum.ROLES);
			return false;
		}
		// check it contains, at least one valid role
		final List<String> filteredRoles = roles.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
		if (filteredRoles.size() > 0 && !filteredRoles.stream().anyMatch(role -> RolesEnum.from(role) != null)) {
			setReasonToInvalidClaim(ClaimsEnum.ROLES);
			return false;
		}

		return true;
	}

	private boolean validateOptionalFields(ClaimAccessor claimAccessor) {
		// Context
		final Context contextClaim = claimAccessor.get(ClaimsEnum.CONTEXT, Context.class);
		if (contextClaim != null) {
			if (!isIdStringValid(contextClaim.getId())) {
				setReasonToInvalidClaim(ClaimsEnum.CONTEXT);
				return false;
			}
		}

		// Platform
		final Platform platform = claimAccessor.get(ClaimsEnum.TOOL_PLATFORM, Platform.class);
		if (platform != null) {
			if (!isIdStringValid(platform.getGuid())) {
				setReasonToInvalidClaim(ClaimsEnum.TOOL_PLATFORM);
				return false;
			}
		}

		// Launch presentation claim
		final Presentation presentation = claimAccessor.get(ClaimsEnum.PRESENTATION, Presentation.class);
		if (presentation != null) {
			if (!isEmpty(presentation.getDocumentTarget())) {
				try {
					DocumentTargetEnum.valueOf(presentation.getDocumentTarget());
				} catch (IllegalArgumentException e) {
					setReasonToInvalidClaim(ClaimsEnum.PRESENTATION);
					return false;
				}
			}
		}

		// Role-scope mentor
		Class<List<String>> mentorRolesClass = (Class) List.class;
		final List<String> mentorRoles = claimAccessor.get(ClaimsEnum.ROLE_SCOPE_MENTOR, mentorRolesClass);
		if (mentorRoles != null && mentorRoles.size() > 0) {
			Class<List<String>> rolesClass = (Class) List.class;
			final List<String> roles = claimAccessor.get(ClaimsEnum.ROLES, rolesClass);
			if (!roles.contains(RolesEnum.MENTOR.getName())) {
				setReasonToInvalidClaim(ClaimsEnum.ROLE_SCOPE_MENTOR);
				return false;
			}
		}

		return true;
	}

	boolean isEmpty(String value) {
		return value == null || "".equals(value.trim());
	}
	boolean isIdStringValid(String value) {
		return !isEmpty(value) && value.trim().length() <= ID_MAX_LENGTH;
	}

	void setReasonToMissingRequiredClaim(ClaimsEnum claim) {
		reason = "Required claim " + claim.getName() + " not found";
	}

	void setReasonToInvalidClaim(ClaimsEnum claim) {
		reason = "Claim " + claim.getName() + " is invalid";
	}

}
