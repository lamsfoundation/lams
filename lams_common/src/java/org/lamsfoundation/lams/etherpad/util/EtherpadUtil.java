package org.lamsfoundation.lams.etherpad.util;

import org.lamsfoundation.lams.etherpad.service.IEtherpadService;

public class EtherpadUtil {

    /**
     * Produces a standard, pretty meaningless Etherpad ID.
     */
    public static String getPadId(String groupId) {
	return groupId + "$" + IEtherpadService.DEFAULT_PAD_NAME;
    }

    /**
     * If Etherpad intial content is not well formed, Etherpad server throws an exception.
     */
    public static String preparePadContent(String rawContent) {
	String cleanContent = rawContent == null ? ""
		: rawContent.trim().replaceAll("[\n\r\f]", "");
	return "<html><body>" + cleanContent + "</body></html>";
    }
}
