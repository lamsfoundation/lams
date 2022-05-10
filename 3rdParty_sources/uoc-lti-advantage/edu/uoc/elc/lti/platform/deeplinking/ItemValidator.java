package edu.uoc.elc.lti.platform.deeplinking;

import edu.uoc.elc.lti.tool.deeplinking.Settings;
import edu.uoc.lti.deeplink.content.Item;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author xaracil@uoc.edu
 */
@RequiredArgsConstructor
public class ItemValidator {
	final Settings settings;

	@Getter
	String message;

	public boolean isValid(Item item) {
		if (!accessTypeIsValid(item.getType())) {
			message = "Platform doesn't allow content items of type " + item.getType();
			return false;
		}
		return true;
	}

	private boolean accessTypeIsValid(String type) {
		return settings.getAccept_types().contains(type);
	}

	protected boolean documentTargetIsValid(String presentation) {
		final List<String> acceptPresentationDocumentTargets = settings.getAccept_presentation_document_targets();
		return (acceptPresentationDocumentTargets != null && acceptPresentationDocumentTargets.size() > 0 ? acceptPresentationDocumentTargets.contains(presentation) : true);
	}

}
