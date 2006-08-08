<%@ include file="/includes/taglibs.jsp"%>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<script type="text/javascript">
<!--

	var messageTargetDiv = "messageArea";
	function releaseMarks(sessionId){
		var url = "<c:url value="/monitoring/releaseMark.do"/>";
	    var reqIDVar = new Date();
		var param = "toolSessionID=" + sessionId +"&reqID="+reqIDVar.getTime();
		messageLoading();
	    var myAjax = new Ajax.Updater(
		    	messageTargetDiv,
		    	url,
		    	{
		    		method:'get',
		    		parameters:param,
		    		onComplete:messageComplete,
		    		evalScripts:true
		    	}
	    );
		
	}
	
	function showBusy(targetDiv){
		if($(targetDiv+"_Busy") != null){
			Element.show(targetDiv+"_Busy");
		}
	}
	function hideBusy(targetDiv){
		if($(targetDiv+"_Busy") != null){
			Element.hide(targetDiv+"_Busy");
		}				
	}
	function messageLoading(){
		showBusy(messageTargetDiv);
	}
	function messageComplete(){
		hideBusy(messageTargetDiv);
	}
	
	
//-->
</script>

<c:forEach var="element" items="${sessionUserMap}">
	<c:set var="toolSessionDto" value="${element.key}" />
	<c:set var="userlist" value="${element.value}" />
	<c:set var="toolAccessMode" value="${mode}" />

	<table cellpadding="0">
		<tr><td colspan="3">
			<img src="${tool}/images/indicator.gif" style="display:none" id="messageArea_Busy" />
			<span id="messageArea"></span>
		</td></tr>	
		<tr>
			<th colspan="3">
				<fmt:message key="message.session.name" />
				:
				<c:out value="${toolSessionDto.sessionName}" />
			</th>
		</tr>
		<c:forEach var="user" items="${userlist}">
			<tr>
				<td>
					<c:out value="${user.firstName}" />
					<c:out value="${user.lastName}" />
				</td>
				<td>
					<c:out value="${user.loginName}" />
				</td>
				<td>
					<c:url value="/monitoring/viewUserMark.do" var="viewuserurl">
						<c:param name="userID" value="${user.uid}" />
						<c:param name="toolSessionID" value="${toolSessionDto.sessionID}" />
					</c:url>
					<html:link href="javascript:launchPopup('${viewuserurl}')" styleClass="button">
						<fmt:message key="lable.topic.title.mark" />
					</html:link>
				</td>
			</tr>
		</c:forEach>

		<c:if test="${empty userlist}">
			<tr>
				<td colspan="3">
					<b><fmt:message key="message.monitoring.summary.no.users" /></b>
				</td>
			</tr>
		</c:if>

		<tr>
			<td align="right">
				<html:form action="/learning/viewForum.do" target="_blank">
					<html:hidden property="mode" value="${toolAccessMode}" />
					<html:hidden property="toolSessionID" value="${toolSessionDto.sessionID}" />
					<html:submit property="viewForum" styleClass="button">
						<fmt:message key="label.monitoring.summary.view.forum" />
					</html:submit>
				</html:form>
			</td>

			<td align="center">
				<html:form action="/monitoring/viewAllMarks" target="_blank">
					<html:hidden property="toolSessionID" value="${toolSessionDto.sessionID}" />
					<html:submit property="Mark" styleClass="button">
						<fmt:message key="lable.topic.title.mark" />
					</html:submit>
				</html:form>
			</td>
			<td align="left">
				<html:button property="releaseMarks" onclick="releaseMarks(${toolSessionDto.sessionID})" styleClass="button">
					<fmt:message key="button.release.mark" />
				</html:button>
			</td>
			<td align="left">
				<html:form action="/monitoring/downloadMarks">
					<html:hidden property="toolSessionID" value="${toolSessionDto.sessionID}" />
					<html:submit property="downloadMarks" styleClass="button">
						<fmt:message key="message.download.marks" />
					</html:submit>
				</html:form>
			</td>
		</tr>
	</table>
</c:forEach>

<c:if test="${empty sessionUserMap}">
	<p>
		<fmt:message key="message.monitoring.summary.no.session" />
	</p>
</c:if>

