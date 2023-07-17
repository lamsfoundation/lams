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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
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
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %> 
<%@ taglib uri="tags-lams" prefix="lams"%>

<%@ attribute name="formID" required="true" rtexprvalue="true" %>
<%@ attribute name="toolSignature" required="true" rtexprvalue="true" %>
<%@ attribute name="toolContentID" required="true" rtexprvalue="true" %>
<%@ attribute name="contentFolderID" required="true" rtexprvalue="true" %>
<%@ attribute name="clearSessionActionUrl" required="true" rtexprvalue="true" %>

<%-- Optional attribute --%>
<%@ attribute name="accessMode" required="false" rtexprvalue="true" %>
<%@ attribute name="cancelButtonLabelKey" required="false" rtexprvalue="true" %>
<%@ attribute name="saveButtonLabelKey" required="false" rtexprvalue="true" %>
<%@ attribute name="cancelConfirmMsgKey" required="false" rtexprvalue="true" %>
<%@ attribute name="defineLater" required="false" rtexprvalue="true" %>
<%@ attribute name="customiseSessionID" required="false" rtexprvalue="true" %>

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
<c:if test="${not empty param.notifyCloseURL}">
	<c:set var="notifyCloseURL" value="${param.notifyCloseURL}" scope="session"/>
</c:if>

<lams:JSImport src="includes/javascript/common.js" />

<!-- begin tab content -->
<script type="text/javascript">
	//we set LAMS_AUTHORING_SUCCESS_FLAG to true in AuthoringAction.update() method 
	if(<c:choose><c:when test="${LAMS_AUTHORING_SUCCESS_FLAG == true}">true</c:when><c:otherwise>false</c:otherwise></c:choose>){
		
		//if defineLater is true close current window
		if (("${defineLater}" == "true") || ("${defineLater}" == "yes")) {
			closeWindow("defineLater");
			
		//show confirmation page otherwise
		} else {
			location.href= "<c:url value='${clearSessionActionUrl}?action=confirm&mode=${accessMode}&signature=${toolSignature}&toolContentID=${toolContentID}&customiseSessionID=${customiseSessionID}&contentFolderID=${contentFolderID}'/>";
		}
	}
    function doSubmit_Form_Only() {
    	var form = document.getElementById("${formID}");
    	//invoke onsubmit event if it's available, submit form afterwards
    	if (form.onsubmit == null || (form.onsubmit != null) && form.onsubmit()) {
    		form.submit();
    	}
    }
    function doCancel() {
    	if(confirm("<fmt:message key='${cancelConfirmMsgKey}'/>")){
    		closeWindow("cancel");
    	}
    }
    function closeWindow(nextAction) {
        // notifyCloseURL needs to be encoded in Java *twice*, otherwise it won't work
        // for both AuthoringButton.tag and authoringConfirm.jsp
		var notifyCloseURL = decodeURIComponent("<c:out value='${notifyCloseURL}' />");
		if (notifyCloseURL == ""){
			if (nextAction == "defineLater") {
				refreshParentMonitoringWindow();
			}
			var clearSessionUrl = "<c:url value='${clearSessionActionUrl}?action=" + nextAction + "&mode=${accessMode}&defineLater=${defineLater}&customiseSessionID=${customiseSessionID}&signature=${toolSignature}&toolContentID=${toolContentID}'/>";
			doAjaxCall(clearSessionUrl);
		} else {
			if (${param.noopener eq "true" or notifyCloseURL.indexOf("noopener=true") >= 0}) {
				window.location.href = notifyCloseURL + '&action=' + nextAction;
			} else if (window.parent.opener == null){
				doAjaxCall(notifyCloseURL);
			} else {
				window.parent.opener.location.href = notifyCloseURL;
			}
		}
    	
    	//just for depress alert window when call window.close()
    	//only available for IE browser
    	var userAgent=navigator.userAgent;
    	if(userAgent.indexOf('MSIE') != -1)
        	window.opener = "authoring"
    	window.close();
    }  				
</script>	
<div id="saveCancelButtons" >
		<a href="javascript:doSubmit_Form_Only();" id="saveButton" name="submit" class="btn btn-primary pull-right">
			<span class="okIcon"><fmt:message key="${saveButtonLabelKey}" /></span>
		</a>
		<a href="javascript:;" name="cancel" id="cancelButton" onclick="javascript:doCancel()" class="btn btn-default roffset5 pull-right">
			<span class="cancelIcon"><fmt:message key="${cancelButtonLabelKey}" /></span>
		</a>
</div>
<!-- end tab content -->