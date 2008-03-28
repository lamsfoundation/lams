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

	<h1>
		<fmt:message key="label.monitoring.summary.overall.summary" />
	</h1>

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
