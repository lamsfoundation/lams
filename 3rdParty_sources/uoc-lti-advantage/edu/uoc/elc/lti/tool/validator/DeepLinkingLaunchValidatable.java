package edu.uoc.elc.lti.tool.validator;

import edu.uoc.elc.lti.tool.ToolDefinition;
import edu.uoc.elc.lti.tool.deeplinking.Settings;
import edu.uoc.lti.MessageTypesEnum;
import edu.uoc.lti.claims.ClaimAccessor;
import edu.uoc.lti.claims.ClaimsEnum;
import edu.uoc.lti.oidc.OIDCLaunchSession;

import java.util.List;

/**
 * @author xaracil@uoc.edu
 */
public class DeepLinkingLaunchValidatable extends LTICoreValidator {
	/**
	 * Validates request following https://www.imsglobal.org/spec/lti-dl/v2p0#deep-linking-request-message
	 * @param state saved state, if present
	 * @param toolDefinition {@link ToolDefinition} with the tool's data
	 * @param claimAccessor {@link ClaimAccessor} for accessing the claims
	 * @param oidcLaunchSession {@link OIDCLaunchSession} with the OIDC session
	 * @return true if the launch is valid, false otherwise
	 */
	@Override
	public boolean validate(String state, ToolDefinition toolDefinition, ClaimAccessor claimAccessor, OIDCLaunchSession oidcLaunchSession) {
		// Core validation
		if (!super.validate(state, toolDefinition, claimAccessor, oidcLaunchSession)) {
			return false;
		}

		// 3.4.1 Deep linking settings
		final Settings settings = claimAccessor.get(ClaimsEnum.DEEP_LINKING_SETTINGS, Settings.class);
		if (settings == null) {
			setReasonToMissingRequiredClaim(ClaimsEnum.DEEP_LINKING_SETTINGS);
			return false;
		}
		if (isEmpty(settings.getDeep_link_return_url())) {
			setReasonToInvalidClaim(ClaimsEnum.DEEP_LINKING_SETTINGS);
			return false;
		}
		final List<String> acceptTypes = settings.getAccept_types();
		if (acceptTypes == null || acceptTypes.size() == 0) {
			setReasonToInvalidClaim(ClaimsEnum.DEEP_LINKING_SETTINGS);
			return false;
		}
		final List<String> acceptPresentationDocumentTargets = settings.getAccept_presentation_document_targets();
		if (acceptPresentationDocumentTargets == null || acceptPresentationDocumentTargets.size() == 0) {
			setReasonToInvalidClaim(ClaimsEnum.DEEP_LINKING_SETTINGS);
			return false;
		}

		// 3.4.2 Message type
		final String messageTypeClaim = claimAccessor.get(ClaimsEnum.MESSAGE_TYPE);
		final MessageTypesEnum messageType = MessageTypesEnum.valueOf(messageTypeClaim);
		if (messageType != MessageTypesEnum.LtiDeepLinkingRequest) {
			setReasonToInvalidClaim(ClaimsEnum.MESSAGE_TYPE);
			return false;
		}

		// 3.4.3 LTI version (already in core)
		// 3.4.4 Deployment ID (already in core)
		// 3.4.5 User (already in core)
		// 3.4.6 Launch Presentation (already in core)
		// 3.4.7 Platform (already in core)
		// 3.4.8 Context (already in core)
		// 3.4.10 Role-scope mentor (already in core)

		return true;
	}
}
