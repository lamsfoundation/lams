/**
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.qa.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsLookupDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;


/**
 *  @author <a href="mailto:anthony.xiao@lamsinternational.com">Anthony Xiao</a>
 */
public class QaAuthoringAction extends LamsLookupDispatchAction {
    private Logger log = Logger.getLogger(QaAuthoringAction.class);

    /**
     * The unspecified method will be called as the first entry into the authoring environment)
     */
    protected ActionForward unspecified(
            ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws Exception {
        
        Long contentID = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
        
        return null;
    }
    
    
    
    protected Map getKeyMethodMap()
    {
        Map map = new HashMap();
//        map.put(NoticeboardConstants.BUTTON_SAVE, "save");
//        map.put(NoticeboardConstants.BUTTON_UPLOAD, "upload");
//        map.put(NoticeboardConstants.LINK_DELETE, "deleteAttachment");
        
        return map;
    }
}
