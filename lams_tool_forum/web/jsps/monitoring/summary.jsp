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

	<table cellpadding="0">
		<tr><td colspan="3">
			<img src="${tool}/images/indicator.gif" style="display:none" id="messageArea_Busy" />
			<span id="messageArea"></span>
		</td>
		</tr>	
		<tr>
			<td colspan="3" >
				<h2>
					<fmt:message key="message.session.name" />:	<c:out value="${toolSessionDto.sessionName}" />
				</h2>
			</td>
		</tr>
	</table>
	<table cellpadding="0">
		<c:forEach var="user" items="${userlist}" varStatus="status">
			<c:if test="${status.first}">
				<tr>
					<th>
						<fmt:message key="monitoring.user.fullname"/>
					</th>
					<th>
						<fmt:message key="monitoring.user.loginname"/>
					</th>
					<c:if test="${user.hasRefection}">
						<th>
							<fmt:message key="monitoring.user.reflection"/>
						</th>
					</c:if>
					<th>&nbsp;</th>
				</tr>
			</c:if>
			<tr>
				<td>
					<c:out value="${user.fullName}" />
				</td>
				<td>
					<c:out value="${user.loginName}" />
				</td>
				<td>
					<c:if test="${user.hasRefection}">
						<c:set var="viewReflection">
							<c:url value="/monitoring/viewReflection.do?toolSessionID=${toolSessionDto.sessionID}&userUid=${user.userUid}"/>
						</c:set>
						<html:link href="javascript:launchPopup('${viewReflection}')">
							<fmt:message key="label.view" />
						</html:link>
					</c:if>
				</td>
				<td>
					<c:url value="/monitoring/viewUserMark.do" var="viewuserurl">
						<c:param name="userID" value="${user.userUid}" />
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
 	 </table>

	<table cellpadding="0">
		<tr>
			<td align="right">
				<html:form action="/learning/viewForum.do" target="_blank">
					<html:hidden property="mode" value="teacher"/>
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

