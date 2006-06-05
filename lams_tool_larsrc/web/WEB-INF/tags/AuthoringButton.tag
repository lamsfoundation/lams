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
  * AuthoringButton.tag
  *	Author: Dapeng Ni
  *	Description: Creates the save/cancel button for authoring page
  */
 
 %>
<%@ tag body-content="scriptless" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-html" prefix="html" %>

<%@ attribute name="formID" required="true" rtexprvalue="true" %>
<%@ attribute name="toolSignature" required="true" rtexprvalue="true" %>
<%@ attribute name="toolContentID" required="true" rtexprvalue="true" %>
<%@ attribute name="clearSessionActionUrl" required="true" rtexprvalue="true" %>

<%-- Optional attribute --%>
<%@ attribute name="accessMode" required="false" rtexprvalue="true" %>
<%@ attribute name="cancelButtonLabelKey" required="false" rtexprvalue="true" %>
<%@ attribute name="saveButtonLabelKey" required="false" rtexprvalue="true" %>
<%@ attribute name="cancelConfirmMsgKey" required="false" rtexprvalue="true" %>
<%@ attribute name="defineLater" required="false" rtexprvalue="false" %>

<%-- Default value for message key --%>
<c:if test="${empty cancelButtonLabelKey}">
	<c:set var="cancelButtonLabelKey" value="label.authoring.cancel.button" scope="request"/>
</c:if>
<c:if test="${empty saveButtonLabelKey}">
	<c:set var="saveButtonLabelKey" value="label.authoring.save.button" scope="request"/>
</c:if>
<c:if test="${empty cancelConfirmMsgKey}">
	<c:set var="cancelConfirmMsgKey" value="authoring.msg.cancel.save" scope="request"/>
</c:if>
<c:if test="${empty accessMode}">
	<c:set var="accessMode" value="author" scope="request"/>
</c:if>

<!-- begin tab content -->
<script type="text/javascript">
	if(<c:choose><c:when test="${LAMS_AUTHORING_SUCCESS_FLAG == true}">true</c:when><c:otherwise>false</c:otherwise></c:choose>){
       	location.href="<c:url value='${clearSessionActionUrl}?action=confirm&mode=${accessMode}&signature=${toolSignature}&toolContentID=${toolContentID}&defineLater=${defineLater}'/>";
	}
    function doSubmit_Form_Only() {
    	document.getElementById("${formID}").submit();
    }
    function doCancel() {
    	if(confirm("<fmt:message key='${cancelConfirmMsgKey}'/>")){
        	location.href="<c:url value='${clearSessionActionUrl}?action=cancel&mode=${accessMode}'/>";
        	//just for depress alert window when call window.close()
        	//only available for IE browser
        	var userAgent=navigator.userAgent;
        	if(userAgent.indexOf('MSIE') != -1)
	        	window.opener = "authoring"
        	window.close();
		}
    }  				
</script>				
<p align="right">
	<html:link href="javascript:doSubmit_Form_Only();" property="submit" styleClass="button">
		<fmt:message key="${saveButtonLabelKey}" /> 
	</html:link>
	<html:link href="javascript:;" property="cancel" onclick="javascript:doCancel()" styleClass="button">
		<fmt:message key="${cancelButtonLabelKey}" />
	</html:link>
</p>
<!-- end tab content -->