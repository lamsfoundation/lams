package org.lamsfoundation.lams.tool;

import org.lamsfoundation.lams.tool.exception.ToolException;

/**
 * Special content manager designed for tool adapters
 * 
 * @author lfoxton
 *
 */
public interface ToolAdapterContentManager extends ToolContentManager {
    /**
     * Make a copy of requested tool content. It will be needed by LAMS to
     * create a copy of learning design and start a new tool session. If
     * no content exists with the given fromToolContentId or if fromToolContent is
     * null, then use the default content id. This is a special implementation
     * of copyToolContent for tool adapters tools
     *
     * @param fromContentId
     *            the original tool content id.
     * @param toContentId
     *            the destination tool content id.
     * @param customCSV
     *            the comma-separate string to help construct the call the external LMS
     * @throws ToolException
     *             if an error occurs e.g. defaultContent is missing
     */
    public void copyToolContent(Long fromContentId, Long toContentId, String customCSV) throws ToolException;

    /**
     * Import the XML fragment for the tool's content, along with any files needed
     * for the content.
     *
     * Specially implemented for tool adapters so the customCSV can be passed, and used
     * to create the new content on the external LMS server
     * 
     * @throws ToolException
     *             if any other error occurs
     */
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion, String customCSV) throws ToolException;

}
