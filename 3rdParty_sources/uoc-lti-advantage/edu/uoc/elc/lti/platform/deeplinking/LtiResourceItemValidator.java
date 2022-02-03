package edu.uoc.elc.lti.platform.deeplinking;

import edu.uoc.elc.lti.tool.deeplinking.Settings;
import edu.uoc.lti.deeplink.content.Item;
import edu.uoc.lti.deeplink.content.LtiResourceItem;

/**
 * @author xaracil@uoc.edu
 */
public class LtiResourceItemValidator extends ItemValidator {
	public LtiResourceItemValidator(Settings settings) {
		super(settings);
	}

	@Override
	public boolean isValid(Item item) {
		boolean valid = super.isValid(item);
		valid = valid && ltiItemIsValid((LtiResourceItem) item);
		return valid;
	}

	private boolean ltiItemIsValid(LtiResourceItem item) {
		if (item.getIFrame() != null) {
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
