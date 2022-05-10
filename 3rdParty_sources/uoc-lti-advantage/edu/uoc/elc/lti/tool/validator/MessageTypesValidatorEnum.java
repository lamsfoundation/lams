package edu.uoc.elc.lti.tool.validator;

import edu.uoc.lti.MessageTypesEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author xaracil@uoc.edu
 */
@RequiredArgsConstructor
public enum MessageTypesValidatorEnum {
	LTI_RESOURCE_LINK(MessageTypesEnum.LtiResourceLinkRequest.name(), new LTIResourceLinkLaunchValidatable()),
	DEEP_LINKING(MessageTypesEnum.LtiDeepLinkingRequest.name(), new DeepLinkingLaunchValidatable());

	@Getter
	private final String messageType;

	@Getter
	private final LaunchValidatable validator;

	public static LaunchValidatable getValidator(String name) {
		for (MessageTypesValidatorEnum messageTypesValidator : values()) {
			if (messageTypesValidator.getMessageType().equals(name)) {
				return messageTypesValidator.getValidator();
			}
		}
		return null;
	}
}
