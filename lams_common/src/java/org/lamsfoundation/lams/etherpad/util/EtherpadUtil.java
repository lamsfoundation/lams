package org.lamsfoundation.lams.etherpad.util;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.etherpad.service.IEtherpadService;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.WebUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EtherpadUtil {

    private static Pattern RELATIVE_UPLOAD_DIR_PATTERN = null;

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
	String cleanContent = "";
	if (StringUtils.isNotBlank(rawContent)) {
	    cleanContent = rawContent.trim().replaceAll("[\n\r\f]", "");

	    if (RELATIVE_UPLOAD_DIR_PATTERN == null) {
		RELATIVE_UPLOAD_DIR_PATTERN = Pattern.compile(
			"\"/" + Configuration.get(ConfigurationKeys.SERVER_URL_CONTEXT_PATH) + "/"
				+ CommonConstants.LAMS_WWW_FOLDER + FileUtil.LAMS_WWW_SECURE_DIR + "/[^\"]+\"");
	    }
	    Matcher matcher = RELATIVE_UPLOAD_DIR_PATTERN.matcher(cleanContent);
	    while (matcher.find()) {
		String match = matcher.group();
		cleanContent = cleanContent.replace(match, match.replace(" ", "%20"));
	    }
	}

	return "<html><body>" + cleanContent + "</body></html>";
    }
}