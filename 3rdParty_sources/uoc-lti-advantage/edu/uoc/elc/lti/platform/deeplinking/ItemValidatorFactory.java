package edu.uoc.elc.lti.platform.deeplinking;

import edu.uoc.elc.lti.tool.deeplinking.Settings;
import edu.uoc.lti.deeplink.content.FileItem;
import edu.uoc.lti.deeplink.content.Item;
import edu.uoc.lti.deeplink.content.LinkItem;
import edu.uoc.lti.deeplink.content.LtiResourceItem;

/**
 * @author xaracil@uoc.edu
 */
public class ItemValidatorFactory {
	public static ItemValidator itemValidatorFor(Item item, Settings settings) {
		if (item instanceof FileItem) {
			return new FileItemValidator(settings);
		}
		if (item instanceof LinkItem) {
			return new LinkItemValidator(settings);
		}
		if (item instanceof LtiResourceItem) {
			return new LtiResourceItemValidator(settings);
		}

		return new ItemValidator(settings);
	}
}
