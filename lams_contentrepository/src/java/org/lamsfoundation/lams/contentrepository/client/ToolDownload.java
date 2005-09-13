/* 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt 
*/
package org.lamsfoundation.lams.contentrepository.client;

import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Implements the Download servlet, using the ToolContentHandler
 * to manage the connection to the repository.
 * <p>
 * Tool's can use this class "as is" to download single files 
 * previously saved in the content repository.
 * <p>
 * The servlet accesses the content repository via a tool's ToolContentHandler 
 * implementation. It looks for the bean IToolContentHandler.SPRING_BEAN_NAME
 * in the web based Spring context. If you do not have a ToolContentHandler
 * implementation then this servlet will not work, or if you use a different 
 * name for the bean in the Spring context, then you should derive a new
 * concrete class from the Download servlet.
 * <p>
 * Sample servlet definition:<BR><pre>
 *  &lt;servlet&gt;
 *      &lt;description&gt;Instructions Download&lt;/description&gt;
 *      &lt;display-name&gt;Instructions Download&lt;/display-name&gt;
 *      &lt;servlet-name&gt;download&lt;/servlet-name&gt;
 *      &lt;servlet-class&gt;org.lamsfoundation.lams.contentrepository.client.ToolDownload&lt;/servlet-class&gt;
 *      &lt;load-on-startup&gt;3&lt;/load-on-startup&gt;
 *  &lt;/servlet&gt;
 * </pre>
 * <p>
 * Sample mapping definition:<BR><pre>
 * 	&lt;servlet-mapping&gt;
 *		&lt;servlet-name&gt;download&lt;/servlet-name&gt;
 *		&lt;url-pattern&gt;/download/*&lt;/url-pattern&gt;
 * 	&lt;/servlet-mapping&gt;
 * </pre>
 * <p>
 * Sample HTML calls:<BR><pre>
 *  &lt;table&gt;
 *  &lt;tr&gt;
 *   &lt;td&gt;Filename&lt;/td&gt;
 *   &lt;td&gt;&lt;/td&gt;
 *   &lt;td&gt;&lt;/td&gt;
 *  &lt;/tr&gt;
 *  &lt;tr&gt;
 *   &lt;td&gt;SomeFile.jpg&lt;/td&gt;
 *   &lt;td&gt;&lt;a href='javascript:launchInstructionsPopup("/lams/tool/lafrum11/download/?uuid=19&preferDownload=false")' class="button"&gt;View&lt;/a&gt;&lt;/td&gt;
 *   &lt;td&gt;&lt;a href="/lams/tool/lafrum11/download/?uuid=19&preferDownload=true"&gt;Download&lt;/a&gt;&lt;/td&gt;
 *  &lt;/tr&gt;
 * </pre>
 * <p>
 * The launchInstructionsPopup() method is defined in common.js, available as http://.../lams/includes/javascript/common.js.
 * <p>
 * For an example of this servlet being used, have a look at the lams_tool_imscp project. 
 * The jsps/authoring/forum/instructions.jsp calls the servlet and includes/header.jsp 
 * loads the common.js file.
 * <p>
 * For more details on the request parameters recognised by the ToolDownload servlet, see 
 * the parent class (Download).
 * 
 * @author Fiona Malikoff
 * @see org.lamsfoundation.lams.contentrepository.client.IToolContentHandler
 * @see org.lamsfoundation.lams.contentrepository.client.Download
 */
public class ToolDownload extends Download {

	protected static IToolContentHandler toolContentHandler = null;

    /* (non-Javadoc)
     * @see org.lamsfoundation.lams.contentrepository.client.Download#getTicket()
     */
    public ITicket getTicket() throws RepositoryCheckedException {
        getToolContentHandler(); // make sure it is set up
        return toolContentHandler != null ? toolContentHandler.getTicket(false):null;
    }

    /* (non-Javadoc)
     * @see org.lamsfoundation.lams.contentrepository.client.Download#getRepositoryService()
     */
    public IRepositoryService getRepositoryService() throws RepositoryCheckedException {
        getToolContentHandler(); // make sure it is set up
        return toolContentHandler != null? toolContentHandler.getRepositoryService() : null;
    }

    protected IToolContentHandler getToolContentHandler() {
	    if ( toolContentHandler == null ) {
	    	log.debug("ToolDownload servlet calling context and getting repository singleton.");
	        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
	    	toolContentHandler = (IToolContentHandler)wac.getBean(IToolContentHandler.SPRING_BEAN_NAME);
	    }
		return toolContentHandler;
    }


}
