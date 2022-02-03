package edu.uoc.elc.lti.platform.deeplinking;

import edu.uoc.elc.lti.tool.deeplinking.Settings;
import edu.uoc.lti.deeplink.content.Item;
import edu.uoc.lti.deeplink.content.LinkItem;

/**
 * @author xaracil@uoc.edu
 */
public class LinkItemValidator extends ItemValidator {
	public LinkItemValidator(Settings settings) {
		super(settings);
	}

	@Override
	public boolean isValid(Item item) {
		boolean valid = super.isValid(item);
		valid = valid && linkItemIsValid((LinkItem) item);
		return valid;
	}

	private boolean linkItemIsValid(LinkItem item) {
		if (item.getEmbed() != null) {
			if (!documentTargetIsValid("embed")) {
				message = "Platform doesn't allow content items with document target embed";
				return false;
			}
		}
		if (item.getIframe() != null) {
			if (!documentTargetIsValid("iframe")) {
				message = "Platform doesn't allow content items with document target iframe";
				return false;
			}
		}
		if (item.getWindow() != null) {
			if (!documentTargetIsValid("window")) {
				message = "Platform doesn't allow content items with document target window";
				return false;
			}
		}

		return true;
	}
}
