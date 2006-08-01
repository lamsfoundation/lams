<%@include file="/common/taglibs.jsp"%>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<script type="text/javascript" src="${lams}includes/javascript/prototype.js"></script>
<script type="text/javascript">
	function launchPopup(url,title) {
		var wd = null;
		if(wd && wd.open && !wd.closed){
			wd.close();
		}
		wd = window.open(url,title,'resizable,width=796,height=570,scrollbars');
		wd.window.focus();
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
	
	function viewMark(userId,sessionId){
		var act = "<c:url value="/monitoring.do"/>";
		launchPopup(act + "?method=listMark&userID="+userId+"&toolSessionID="+sessionId,"mark");
	}
	function viewAllMarks(sessionId){
		var act = "<c:url value="/monitoring.do"/>";
		launchPopup(act + "?method=listAllMarks&toolSessionID="+sessionId,"mark");
	}
	
	var messageTargetDiv = "messageArea";
	function releaseMarks(sessionId){
		var url = "<c:url value="/monitoring.do"/>";
	    var reqIDVar = new Date();
		var param = "method=releaseMarks&toolSessionID=" + sessionId +"&reqID="+reqIDVar.getTime();
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
	
	function downloadMarks(sessionId){
		var url = "<c:url value="/monitoring.do"/>";
	    var reqIDVar = new Date();
		var param = "?method=downloadMarks&toolSessionID=" + sessionId +"&reqID="+reqIDVar.getTime();
		url = url + param;
		location.href=url;
	}
	
	function messageLoading(){
		showBusy(messageTargetDiv);
	}
	function messageComplete(){
		hideBusy(messageTargetDiv);
	}
	
	
	
</script>
<table cellpadding="0">
<tr><td>
	<img src="${tool}/images/indicator.gif" style="display:none" id="messageArea_Busy" />
	<span id="messageArea"></span>
</td></tr>
</table>
<c:forEach var="element" items="${sessionUserMap}">
	<c:set var="sessionDto" value="${element.key}" />
	<c:set var="userlist" value="${element.value}" />
	<table cellpadding="0">
		<tr>
			<th colspan="3"><fmt:message key="label.session.name" /> : 
				<c:out value="${sessionDto.sessionName}" /></th>
		</tr>
		<c:forEach var="user" items="${userlist}">
			<tr>
				<td><c:out value="${user.firstName}" /> 
					<c:out value="${user.lastName}" />
				</td>
				<td><c:out value="${user.login}" /></td>
				<td><html:link
					href="javascript:viewMark(${user.userID},${sessionDto.sessionID});"
					property="Mark" styleClass="button">
					<fmt:message key="label.monitoring.Mark.button" />
				</html:link></td>
			</tr>
		</c:forEach>
		<c:if test="${empty userlist}">
			<tr>
				<td colspan="3"><fmt:message key="label.no.user.available" />
				</td>
			</tr>
		</c:if>
		<tr>
			<td><html:link href="javascript:viewAllMarks(${sessionDto.sessionID});"
				property="viewAllMarks" styleClass="button">
				<fmt:message key="label.monitoring.viewAllMarks.button" />
			</html:link></td>
			<td><html:link href="javascript:releaseMarks(${sessionDto.sessionID});"
				property="releaseMarks" styleClass="button">
				<fmt:message key="label.monitoring.releaseMarks.button" />
			</html:link></td>
			<td><html:link href="javascript:downloadMarks(${sessionDto.sessionID});"
				property="downloadMarks" styleClass="button">
				<fmt:message key="label.monitoring.downloadMarks.button" />
			</html:link></td>
		</tr>
	</table>
</c:forEach>
