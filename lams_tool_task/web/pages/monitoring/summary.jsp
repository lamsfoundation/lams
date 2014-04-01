<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>
<c:set var="taskList" value="${sessionMap.taskList}"/>

<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>
<link type="text/css" href="${lams}/css/jquery-ui-smoothness-theme.css" rel="stylesheet">
<link type="text/css" href="${lams}/css/jquery-ui.timepicker.css" rel="stylesheet">

<script type="text/javascript">
	//pass settings to monitorToolSummaryAdvanced.js
	var submissionDeadlineSettings = {
		lams: '${lams}',
		submissionDeadline: '${sessionMap.submissionDeadline}',
		setSubmissionDeadlineUrl: '<c:url value="/monitoring/setSubmissionDeadline.do"/>',
		toolContentID: '${toolContentID}',
		messageNotification: '<fmt:message key="monitor.summary.notification" />',
		messageRestrictionSet: '<fmt:message key="monitor.summary.date.restriction.set" />',
		messageRestrictionRemoved: '<fmt:message key="monitor.summary.date.restriction.removed" />'
	};
</script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.blockUI.js"></script>  
<script type="text/javascript" src="${lams}/includes/javascript/monitorToolSummaryAdvanced.js" ></script>

<script type="text/javascript">
<!--
	function summaryTask(taskUid){
		var myUrl = "<c:url value="/monitoring/itemSummary.do"/>?toolContentID=${toolContentID}&taskListItemUid=" + taskUid;
		launchPopup(myUrl,"LearnerView");
	}
	
	function setVerifiedByMonitor(userUid){
		document.location.href = "<c:url value="/monitoring/setVerifiedByMonitor.do"/>?toolContentID=${toolContentID}&contentFolderID=${sessionMap.contentFolderID}&userUid=" + userUid;
		return false;
	}

-->	
</script>

<h1>
	<c:out value="${taskList.title}" escapeXml="true"/>
</h1>
<div class="instructions space-top space-bottom">
	<c:out value="${taskList.instructions}" escapeXml="false"/>
</div>
<%-- Summary list  --%>

<c:if test="${empty summaryList}">
	<div align="center">
		<b> <fmt:message key="message.monitoring.summary.no.session" /> </b>
	</div>
</c:if>

<c:forEach var="summary" items="${summaryList}">

	<h1><fmt:message key="monitoring.label.group" /> ${summary.sessionName}	</h1>
	<h2 style="color:black; margin-left: 20px;"><fmt:message key="label.monitoring.summary.overall.summary" />	</h2>
	<table cellpadding="0" class="alternative-color" >
		
		<tr>
			<th width="30%" style="background-repeat: repeat">
				<fmt:message key="label.monitoring.summary.user" />
			</th>
			<c:forEach var="item" items="${summary.taskListItems}">
				<th width="30px"  align="center" style="background-repeat: repeat">
					<a href="javascript:;" onclick="return summaryTask(${item.uid})"> 
						<c:out value="${item.title}" escapeXml="true"/> 
					</a>
				</th>
			</c:forEach>					
			
			<c:if test="${summary.monitorVerificationRequired}">
				<th width="60px" align="center" style="background-repeat: repeat">
					<fmt:message key="label.monitoring.summary.confirm.completion" />
				</th>
			</c:if>			
		</tr>
	
	
		<c:forEach var="user" items="${summary.userNames}" varStatus="userStatus">
		
			<tr>
				<td>
					${user.loginName}
				</td>
				
				<c:forEach var="item" items="${summary.taskListItems}" varStatus="itemStatus">
					<td align="center">
						<c:choose>
							<c:when test="${summary.completeMap[userStatus.index][itemStatus.index]}">
								<img src="<html:rewrite page='/includes/images/completeitem.gif'/>"	border="0">
							</c:when>
							
							<c:otherwise>
								<img src="<html:rewrite page='/includes/images/dash.gif'/>" border="0">
							</c:otherwise>
						</c:choose>
					</td>
				</c:forEach>					
							
				<c:if test="${summary.monitorVerificationRequired}">
					<td align="center">
						<c:choose>
							<c:when test="${user.verifiedByMonitor}">
								<img src="<html:rewrite page='/includes/images/tick.gif'/>"	border="0">
							</c:when>
							
							<c:otherwise>
								<a href="javascript:;"
									onclick="return setVerifiedByMonitor(${user.uid})"> [<fmt:message key="label.completed" /> for ${user.loginName}] 
								</a>
							</c:otherwise>
						</c:choose>
					</td>
				</c:if>
					
			</tr>
			
		</c:forEach>
	</table>
	
	<%-- Reflection list  --%>
	
	<c:if test="${sessionMap.taskList.reflectOnActivity}">
	
		<h2 style="color:black; margin-left: 20px; " ><fmt:message key="label.monitoring.summary.title.reflection"/>	</h2>
		<table cellpadding="0"  class="alternative-color"  >

			<tr>
				<th>
					<fmt:message key="label.monitoring.summary.user"/>
				</th>
				<th>
					<fmt:message key="label.monitoring.summary.reflection"/>
				</th>
			</tr>				
						
			<c:forEach var="user" items="${summary.userNames}" varStatus="userStatus">
		
				<tr>
					<td>
						${user.loginName}
					</td>
					<td >
						<c:set var="viewReflection">
							<c:url value="/monitoring/viewReflection.do?userUid=${user.uid}"/>
						</c:set>
						<html:link href="javascript:launchPopup('${viewReflection}')">
							<fmt:message key="label.view" />
						</html:link>
					</td>
				</tr>
			</c:forEach>
						
		</table>
	</c:if>
	<br>
	
</c:forEach>				
				
				
<br/>				
<%-- Overall TaskList information  --%>
<h1 style="padding-bottom: 10px;">
	<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="treeIcon" onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'), '<lams:LAMSURL/>');" />

	<a href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'),'<lams:LAMSURL/>');" >
		<fmt:message key="monitor.summary.th.advancedSettings" />
	</a>
</h1>
<br />

<div class="monitoring-advanced" id="advancedDiv" style="display:none">
<table class="alternative-color">

	<tr>
		<td>
			<fmt:message key="label.monitoring.summary.lock.when.finished" ><fmt:param> </fmt:param></fmt:message>
		</td>
		<td>
			<c:choose>
				<c:when test="${taskList.lockWhenFinished}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.monitoring.summary.sequential.order" ><fmt:param> </fmt:param></fmt:message>
		</td>
		<td>
			<c:choose>
				<c:when test="${taskList.sequentialOrder}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.monitoring.summary.min.number.tasks" ><fmt:param> </fmt:param></fmt:message>
		</td>
		<td>
			<c:choose>
				<c:when test="${taskList.minimumNumberTasks > 0}">
					${taskList.minimumNumberTasks}
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.monitoring.summary.allowed.contribute.tasks" ><fmt:param> </fmt:param></fmt:message>
		</td>
		<td>
			<c:choose>
				<c:when test="${taskList.allowContributeTasks}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.monitoring.summary.monitor.verification" ><fmt:param> </fmt:param></fmt:message>
		</td>
		<td>
			<c:choose>
				<c:when test="${taskList.monitorVerificationRequired}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	
	<tr>
		<td>
			<fmt:message key="label.monitoring.summary.notebook.reflection" ><fmt:param> </fmt:param></fmt:message>
		</td>
		<td>
			<c:choose>
				<c:when test="${taskList.reflectOnActivity}">
					<fmt:message key="label.on" />
				</c:when>
				<c:otherwise>
					<fmt:message key="label.off" />
				</c:otherwise>
			</c:choose>	
		</td>
	</tr>
</table>
</div>

<%@include file="daterestriction.jsp"%>
				