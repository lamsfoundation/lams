<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summary" value="${sessionMap.summary}"/>
<script type="text/javascript">

	function viewItem(itemUid){
		var myUrl = "<c:url value="/reviewtask/reviewTask.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&itemUid=" + itemUid;
		launchPopup(myUrl,"LearnerView");
	}
	
	function verifyUser(userUid){
		document.location.href = "<c:url value="/monitoring/verifyUser.do"/>?sessionMapID=${sessionMapID}&userUid=" + userUid;
		return false;
	}
	
</script>

<table cellpadding="0" class="alternative-color" >

	<h1>
		Overall Summary
	</h1>

	<tr>
		<th width="30%">
			USER
		</th>
		<c:forEach var="item" items="${summary.taskListItems}">
			<th width="30px"  align="center">
				${item.title}
			</th>
		</c:forEach>					
		
		<c:if test="${summary.monitorVerificationRequired}">
			<th width="60px" align="center">
				Complete Activity?
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
						<c:when test="${1==0}">
							<img src="<html:rewrite page='/includes/images/tick.gif'/>"	border="0">
						</c:when>
						
						<c:otherwise>
							<a href="javascript:;"
								onclick="return verifyUser(${user.uid})"> [<fmt:message key="label.completed" /> for ${user.loginName}] 
							</a>
						</c:otherwise>
					</c:choose>
				</td>
			</c:if>
				
		</tr>
		
			
	</c:forEach>
</table>

<%-- 


		<c:forEach var="item" items="${summary.taskListItems}" varStatus="itemStatus">
			<c:if test="${userStatus.first}">
			</c:if>
						
					
		</c:forEach>						
						
							
							<c:set var="lastTaskCompletion" value="${true}" />
							<c:forEach var="item" items="${sessionMap.taskListList}">
							
								<c:set var="isAllowedByParent" value="${true}" />
								<c:if test="${item.childTask}">
									<c:forEach var="parent" items="${sessionMap.taskListList}">
										<c:if test="${(parent.title == item.parentTaskName) && not parent.complete}">
											<c:set var="isAllowedByParent" value="${false}" />
										</c:if>
									</c:forEach>
								</c:if>
								
								<c:if test="${isAllowedByParent}">
									<tr>
										<td>
											<c:choose>
												<c:when test="${(not finishedLock) && (not taskList.sequentialOrder || lastTaskCompletion)}">
													<a href="javascript:;" onclick="viewItem(${item.uid})">
														${item.title} </a>
												</c:when>
					
												<c:otherwise>
													${item.title}
												</c:otherwise>
											</c:choose>
				
											<c:if test="${!item.createByAuthor && item.createBy != null}">
													[${item.createBy.loginName}]
											</c:if>
											
											<c:if test="${item.required}">
												*
											</c:if>
										</td>
										<td align="center">
											<c:choose>
												<c:when test="${item.complete}">
													<img src="<html:rewrite page='/includes/images/tick.gif'/>"
														border="0">
												</c:when>
												<c:otherwise>
													<c:if test="${(mode != 'teacher') && (not finishedLock) && (not taskList.sequentialOrder || lastTaskCompletion)}">
														<a href="javascript:;"
															onclick="return completeItem(${item.uid})"> <fmt:message key="label.completed" /> 
														</a>
													</c:if>
												</c:otherwise>
											</c:choose>
										</td>
									</tr>

				<tr>
					<th width="44%">
						<fmt:message key="monitoring.label.title" />
					</th>
					<th width="34%">
						<fmt:message key="monitoring.label.suggest" />
					</th>
					<th width="22%" align="center">
						<fmt:message key="monitoring.label.number.learners" />
					</th>
				</tr>
			</c:if>
			<c:if test="${item.itemUid == -1}">
				<tr>
					<td colspan="5">
						<div class="align-left">
							<b> <fmt:message key="message.monitoring.summary.no.resource.for.group" /> </b>
						</div>
					</td>
				</tr>
			</c:if>
			<c:if test="${item.itemUid != -1}">
				<tr>
					<td>
						<a href="javascript:;" onclick="viewItem(${item.itemUid},'${sessionMapID}')">${item.itemTitle}</a>
					</td>
					<td>
						<c:if test="${!item.itemCreateByAuthor}">
											${item.username}
										</c:if>
					</td>
					<td align="center">
						<c:choose>
							<c:when test="${item.viewNumber > 0}">
								<c:set var="listUrl">
									<c:url value='/monitoring/listuser.do?toolSessionID=${item.sessionId}&itemUid=${item.itemUid}' />
								</c:set>
								<a href="#" onclick="launchPopup('${listUrl}','listuser')"> ${item.viewNumber}<a>
							</c:when>
							<c:otherwise>
									0
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:if>
			

				<c:if test="${sessionMap.taskList.reflectOnActivity && status.last}">
					<c:set var="userList" value="${sessionMap.reflectList[item.sessionId]}"/>
					<c:forEach var="user" items="${userList}" varStatus="refStatus">
						<c:if test="${refStatus.first}">
							<tr>
								<td colspan="5">
									<h2><fmt:message key="title.reflection"/>	</h2>
								</td>
							</tr>
							<tr>
								<th colspan="2">
									<fmt:message key="monitoring.user.fullname"/>
								</th>
								<th colspan="2">
									<fmt:message key="monitoring.label.user.loginname"/>
								</th>
								<th>
									<fmt:message key="monitoring.user.reflection"/>
								</th>
							</tr>
						</c:if>
						<tr>
							<td colspan="2">
								${user.fullName}
							</td>
							<td colspan="2">
								${user.loginName}
							</td>
							<td >
								<c:set var="viewReflection">
									<c:url value="/monitoring/viewReflection.do?toolSessionID=${item.sessionId}&userUid=${user.userUid}"/>
								</c:set>
								<html:link href="javascript:launchPopup('${viewReflection}')">
									<fmt:message key="label.view" />
								</html:link>
							</td>
						</tr>
					</c:forEach>
				</c:if>
			
		</c:forEach>
		
	</c:forEach>
</table>

--%>
