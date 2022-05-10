package edu.uoc.elc.lti.tool.validator;

import edu.uoc.elc.lti.tool.*;
import edu.uoc.lti.MessageTypesEnum;
import edu.uoc.lti.claims.ClaimAccessor;
import edu.uoc.lti.claims.ClaimsEnum;
import edu.uoc.lti.oidc.OIDCLaunchSession;
import lombok.RequiredArgsConstructor;

/**
 * @author xaracil@uoc.edu
 */
@RequiredArgsConstructor
public class LTIResourceLinkLaunchValidatable extends LTICoreValidator {
	private static final int ID_MAX_LENGTH = 255;

	@Override
	public boolean validate(String state, ToolDefinition toolDefinition, ClaimAccessor claimAccessor, OIDCLaunchSession oidcLaunchSession) {
		// Core validation
		if (!super.validate(state, toolDefinition, claimAccessor, oidcLaunchSession)) {
			return false;
		}

		// LTI required claims
		if (!validateRequiredClaims(state, toolDefinition, claimAccessor, oidcLaunchSession)) {
			return false;
		}

		// LTI optional claims
		if (!validateOptionalClaims()) {
			return false;
		}

		// state
		if (state != null) {
			if (!state.equals(oidcLaunchSession.getState())) {
				reason = "Invalid state";
				return false;
			}
			if (claimAccessor.get(ClaimsEnum.NONCE) != null) {
				if (!claimAccessor.get(ClaimsEnum.NONCE).equals(oidcLaunchSession.getNonce())) {
					setReasonToInvalidClaim(ClaimsEnum.NONCE);
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Validates the required claims of the LTI launch following https://www.imsglobal.org/spec/lti/v1p3/#required-message-claims
	 * @param state saved state, if present
	 * @return true if the required claims of the LTI launch are valid, false otherwise
	 */
	private boolean validateRequiredClaims(String state, ToolDefinition toolDefinition, ClaimAccessor claimAccessor, OIDCLaunchSession oidcLaunchSession) {
		// 5.3.1 message type claim
		final String messageTypeClaim = claimAccessor.get(ClaimsEnum.MESSAGE_TYPE);
		final MessageTypesEnum messageType = MessageTypesEnum.valueOf(messageTypeClaim);
		if (messageType != MessageTypesEnum.LtiResourceLinkRequest) {
			setReasonToInvalidClaim(ClaimsEnum.MESSAGE_TYPE);
			return false;
		}

		// 5.3.2 version (already in core)
		// 5.3.3 LTI Deployment ID claim (already in core)

		// 5.3.4 Target Link URI
		if (state != null) {
			final String targetLinkUri = claimAccessor.get(ClaimsEnum.TARGET_LINK_URI);
			if (isEmpty(targetLinkUri)) {
				setReasonToMissingRequiredClaim(ClaimsEnum.TARGET_LINK_URI);
				return false;
			}
			final String targetLinkUriFromOidcSession = oidcLaunchSession.getTargetLinkUri();
			if (!targetLinkUri.equals(targetLinkUriFromOidcSession)) {
				setReasonToInvalidClaim(ClaimsEnum.TARGET_LINK_URI);
				return false;
			}
		}

		// 5.3.5 Resource link claim
		final ResourceLink resourceLink = claimAccessor.get(ClaimsEnum.RESOURCE_LINK, ResourceLink.class);
		if (resourceLink == null) {
			setReasonToMissingRequiredClaim(ClaimsEnum.RESOURCE_LINK);
			return false;
		}

		if (!isIdStringValid(resourceLink.getId())) {
			setReasonToInvalidClaim(ClaimsEnum.RESOURCE_LINK);
			return false;
		}

		// 5.3.6 User Identity claims (already in core)
		// 5.3.7 Roles claim (already in core)

		return true;
	}

	/**
	 * Validates the optional claims of the LTI launch following https://www.imsglobal.org/spec/lti/v1p3/#optional-message-claims
	 * @return true if the optional claims of the LTI launch are valid, false otherwise
	 */
	private boolean validateOptionalClaims() {
		// 5.4.1 Context claim (already in core)
		// 5.4.2 Platform instance claim (already in core)
		// 5.4.3 Role-scope mentor claims (already in core)
		// 5.4.4 Launch presentation claim (already in core)

		// 5.4.5 Learning Information Services LIS claim: Nothing to do here
		// 5.4.6 Custom properties and variable substitution: Nothing to do here (values are gotten as string in Tool)
		// 5.4.7 Vendor-specific extension claims: Nothing to do here
		return true;
	}

}
