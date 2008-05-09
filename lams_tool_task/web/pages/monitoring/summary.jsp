<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summary" value="${sessionMap.summary}"/>
<script type="text/javascript">

	function summaryTask(taskUid){
		var myUrl = "<c:url value="/monitoring/summaryTask.do"/>?toolContentID=${toolContentID}&taskListItemUid=" + taskUid;
		launchPopup(myUrl,"LearnerView");
	}
	
	function setVerifiedByMonitor(userUid){
		document.location.href = "<c:url value="/monitoring/setVerifiedByMonitor.do"/>?toolContentID=${toolContentID}&contentFolderID=${sessionMap.contentFolderID}&userUid=" + userUid;
		return false;
	}
	
</script>

<table cellpadding="0" class="alternative-color" >

	<tr>
		<td colspan="3">
			<h1><fmt:message key="label.monitoring.summary.overall.summary" />	</h1>
		</td>
	</tr>

	<tr>
		<th width="30%">
			<fmt:message key="label.monitoring.summary.user" />
		</th>
		<c:forEach var="item" items="${summary.taskListItems}">
			<th width="30px"  align="center">
				<a href="javascript:;" onclick="return summaryTask(${item.uid})"> 
					${item.title} 
				</a>
			</th>
		</c:forEach>					
		
		<c:if test="${summary.monitorVerificationRequired}">
			<th width="60px" align="center">
				<fmt:message key="label.monitoring.summary.complete.activity" />
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
							<img src="<html:rewrite page='/includes/images/incompleteitem.gif'/>" border="0">
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
	<table cellpadding="0" class="alternative-color" >

		<tr>
			<td colspan="5">
				<h1><fmt:message key="label.monitoring.summary.title.reflection"/>	</h1>
			</td>
		</tr>
		<tr>
			<th colspan="2">
				<fmt:message key="label.monitoring.summary.user"/>
			</th>
			<th>
				<fmt:message key="label.monitoring.summary.reflection"/>
			</th>
		</tr>				
					
		<c:forEach var="user" items="${summary.userNames}" varStatus="userStatus">
	
			<tr>
				<td colspan="2">
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
				
				
