/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.contentrepository.client;

import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Implements the Download servlet, using the ToolContentHandler to manage the connection to the repository.
 * <p>
 * Tool's can use this class "as is" to download single files previously saved in the content repository.
 * <p>
 * The servlet accesses the content repository via a tool's ToolContentHandler implementation. It looks for the bean
 * that implements IToolContentHandler in the web based Spring context. The name of the bean is specified using the
 * "toolContentHandlerBeanName" parameter in the servlet definition in web.xml.
 *
 * If you do not have a ToolContentHandler implementation then this servlet will not work. If you need to set up the
 * content repository access differently to the implementation in the Tool Content Handler, then derive a new concrete
 * class from the Download servlet.
 * <p>
 * Sample servlet definition:<BR>
 *
 * <pre>
 *  &lt;servlet&gt;
 *      &lt;description&gt;Noticeboard Instructions Download&lt;/description&gt;
 *      &lt;display-name&gt;Noticeboard Instructions Download&lt;/display-name&gt;
 *      &lt;servlet-name&gt;download&lt;/servlet-name&gt;
 *      &lt;servlet-class&gt;org.lamsfoundation.lams.contentrepository.client.ToolDownload&lt;/servlet-class&gt;
 *      &lt;init-param&gt;
 *           &lt;param-name&gt;toolContentHandlerBeanName&lt;/param-name&gt;
 *           param-value&gt;nbToolContentHandler&lt;/param-value&gt;
 *      &lt;/init-param&gt;
 *      &lt;load-on-startup&gt;3&lt;/load-on-startup&gt;
 *  &lt;/servlet&gt;
 * </pre>
 *
 * <p>
 * Sample mapping definition:<BR>
 *
 * <pre>
 * 	&lt;servlet-mapping&gt;
 * 	&lt;servlet-name&gt;download&lt;/servlet-name&gt;
 * 	&lt;url-pattern&gt;/download/*&lt;/url-pattern&gt;
 * 	&lt;/servlet-mapping&gt;
 * </pre>
 *
 * <p>
 * Sample HTML calls:<BR>
 *
 * <pre>
 *  &lt;table&gt;
 *  &lt;tr&gt;
 *   &lt;td&gt;Filename&lt;/td&gt;
 *   &lt;td&gt;&lt;/td&gt;
 *   &lt;td&gt;&lt;/td&gt;
 *  &lt;/tr&gt;
 *  &lt;tr&gt;
 *   &lt;td&gt;SomeFile.jpg&lt;/td&gt;
 *   &lt;td&gt;&lt;a href='javascript:launchInstructionsPopup(&quot;/lams/tool/lafrum11/download/?uuid=19&amp;preferDownload=false&quot;)' class=&quot;button&quot;&gt;View&lt;/a&gt;&lt;/td&gt;
 *   &lt;td&gt;&lt;a href=&quot;/lams/tool/lafrum11/download/?uuid=19&amp;preferDownload=true&quot;&gt;Download&lt;/a&gt;&lt;/td&gt;
 *  &lt;/tr&gt;
 * </pre>
 *
 * <p>
 * The launchInstructionsPopup() method is defined in common.js, available as
 * http://.../lams/includes/javascript/common.js.
 * <p>
 * For an example of this servlet being used, have a look at the lams_tool_imscp project. The
 * jsps/authoring/forum/instructions.jsp calls the servlet and includes/header.jsp loads the common.js file.
 * <p>
 * For more details on the request parameters recognised by the ToolDownload servlet, see the parent class (Download).
 *
 * @author Fiona Malikoff
 * @see org.lamsfoundation.lams.contentrepository.client.IToolContentHandler
 * @see org.lamsfoundation.lams.contentrepository.client.Download
 */
public class ToolDownload extends Download {

    /** The name of the servlet parameter used to define the implementation bean name */
    public final static String TOOL_CONTENT_HANDLER_BEAN_NAME = "toolContentHandlerBeanName";

    @Override
    public ITicket getTicket() throws RepositoryCheckedException {
	IToolContentFullHandler toolContentHandler = getToolContentHandler(); // make sure it is set up
	return toolContentHandler != null ? toolContentHandler.getTicket(false) : null;
    }

    @Override
    public ITicket getTicket(String toolContentHandlerName) throws RepositoryCheckedException {
	IToolContentFullHandler toolContentHandler = getToolContentHandler(toolContentHandlerName); // make sure it is set up
	return toolContentHandler != null ? toolContentHandler.getTicket(false) : null;
    }

    @Override
    public IRepositoryService getRepositoryService() throws RepositoryCheckedException {
	IToolContentFullHandler toolContentHandler = getToolContentHandler(); // make sure it is set up
	return toolContentHandler != null ? toolContentHandler.getRepositoryService() : null;
    }

    protected IToolContentFullHandler getToolContentHandler() {
	log.debug("ToolDownload servlet calling context and getting repository singleton.");

	String toolContentHandlerBeanName = getInitParameter(ToolDownload.TOOL_CONTENT_HANDLER_BEAN_NAME);
	if (toolContentHandlerBeanName == null) {
	    log.error("Accessing Download servlet but tool content handler bean has not been defined. Please define init parameter"
			    + ToolDownload.TOOL_CONTENT_HANDLER_BEAN_NAME + ".");
	    return null;
	}
	return getToolContentHandler(toolContentHandlerBeanName);
    }

    protected IToolContentFullHandler getToolContentHandler(String toolContentHandlerName) {
	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
	return (IToolContentFullHandler) wac.getBean(toolContentHandlerName);
    }
}