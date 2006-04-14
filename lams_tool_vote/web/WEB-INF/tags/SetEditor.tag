<% 
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
 
 /**
  * SetEditor.tag
  *	Author: Mitchell Seaton
  *	Description: Creates a dynamic textarea field using a HTML Editor.
  * Wiki: 
  */
 
 %>
<%@ tag body-content="empty" %>
<%@ attribute name="id" required="true" rtexprvalue="true" %>
<%@ attribute name="text" required="true" rtexprvalue="true" %>
<%@ attribute name="small" required="false" rtexprvalue="true" %>
<%@ taglib uri="tags-function" prefix="fn" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

<c:set var="previewClassName" value="previewPanel"/>
<c:set var="txtClassName" value="textareaPanel"/>
<c:set var="id_a" value="${fn:substring(id, 0, 1)}"/>
<c:set var="id_b" value="${fn:substring(id, 1, fn:length(id))}"/>

	<c:if test="${small}">
		<c:set var="previewClassName" value="smallPreviewPanel"/>
		<c:set var="txtClassName" value="smallTextareaPanel"/>
	</c:if>
	<span id="preview${id}" style="visibility: hidden; display: none;">
		<div>
	    	<a href="javascript:doWYSWYGEdit('${id}'<c:if test="${small}">,'small'</c:if>)"><img src="${lams}images/html.gif" border="0" alt="Open HTML Editor"></a>
	    </div>
	    <div class="${previewClassName}" id="preview${id}.text"></div>
	</span>
	<span id="tx${id}">
		<div>
	    	<a href="javascript:doTextToHTML('${id}'); doWYSWYGEdit('${id}'<c:if test="${small}">,'small'</c:if>)"><img src="${lams}images/html.gif" border="0" alt="Open HTML Editor"></a>
	    </div>
		<textarea class="${txtClassName}" name="${fn:toLowerCase(id_a)}${id_b}" id="tx${id}.textarea"><c:out value="${text}" escapeXml="false" /></textarea>
	</span>