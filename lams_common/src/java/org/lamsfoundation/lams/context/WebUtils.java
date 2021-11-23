package org.lamsfoundation.lams.context;

import org.springframework.web.util.UriUtils;

public class WebUtils {

    /**
     * Extract the URL filename from the given request URL path.
     * Correctly resolves nested paths such as "/products/view.html" as well.
     *
     * @param urlPath
     *            the request URL path (e.g. "/index.html")
     * @return the extracted URI filename (e.g. "index")
     * @deprecated as of Spring 4.3.2, in favor of custom code for such purposes
     */
    @Deprecated
    public static String extractFilenameFromUrlPath(String urlPath) {
	String filename = WebUtils.extractFullFilenameFromUrlPath(urlPath);
	int dotIndex = filename.lastIndexOf('.');
	if (dotIndex != -1) {
	    filename = filename.substring(0, dotIndex);
	}
	return filename;
    }

    /**
     * Extract the full URL filename (including file extension) from the given
     * request URL path. Correctly resolve nested paths such as
     * "/products/view.html" and remove any path and or query parameters.
     *
     * @param urlPath
     *            the request URL path (e.g. "/products/index.html")
     * @return the extracted URI filename (e.g. "index.html")
     * @deprecated as of Spring 4.3.2, in favor of custom code for such purposes
     *             (or {@link UriUtils#extractFileExtension} for the file extension use case)
     */
    @Deprecated
    public static String extractFullFilenameFromUrlPath(String urlPath) {
	int end = urlPath.indexOf('?');
	if (end == -1) {
	    end = urlPath.indexOf('#');
	    if (end == -1) {
		end = urlPath.length();
	    }
	}
	int begin = urlPath.lastIndexOf('/', end) + 1;
	int paramIndex = urlPath.indexOf(';', begin);
	end = (paramIndex != -1 && paramIndex < end ? paramIndex : end);
	return urlPath.substring(begin, end);
    }
}
