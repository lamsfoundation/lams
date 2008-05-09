<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" />
	</title>
	<%@ include file="/common/header.jsp"%>

	<%-- param has higher level for request attribute --%>
	<c:if test="${not empty param.sessionMapID}">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
	</c:if>

	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

	<c:set var="mode" value="${sessionMap.mode}" />
	<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
	<c:set var="taskList" value="${sessionMap.taskList}" />
	<c:set var="finishedLock" value="${sessionMap.finishedLock}" />

	<script type="text/javascript">
	<!--
		function checkNew(){
			document.location.href = "<c:url value="/learning/start.do"/>?mode=${mode}&toolSessionID=${toolSessionID}";
 		    return false;
		}
		function viewItem(itemUid){
			var myUrl = "<c:url value="/reviewtask/reviewTask.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&itemUid=" + itemUid;
			launchPopup(myUrl,"LearnerView");
		}
		function finishSession(){
			document.getElementById("finishButton").disabled = true;
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
			return false;
		}
		function continueReflect(){
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		
		function showMessage(url) {
			var area=document.getElementById("reourceInputArea");
			if(area != null){
				area.style.width="100%";
				area.style.height="100%";
				area.src=url;
				area.style.display="block";
			}
			var elem = document.getElementById("saveCancelButtons");
			if (elem != null) {
				elem.style.display="none";
			}
			location.hash = "resourceInputArea";
		}
		
		function hideMessage(){
			var area=document.getElementById("reourceInputArea");
			if(area != null){
				area.style.width="0px";
				area.style.height="0px";
				area.style.display="none";
			}
			var elem = document.getElementById("saveCancelButtons");
			if (elem != null) {
				elem.style.display="block";
			}
	}
		
	-->        
    </script>
</lams:head>
<body class="stripes">


	<div id="content">
	
		<h1>
			${taskList.title}
		</h1>
		
		<c:if test="${mode == 'author' || mode == 'learner'}">
			<c:if test="${taskList.lockWhenFinished}">
		    	<div class="info space-bottom">
			   		<c:choose>
				    	<c:when test = "${finishedLock}">
							<fmt:message key="label.learning.responses.locked.reminder" />								
					    </c:when>
					    <c:otherwise>
							<fmt:message key="label.learning.responses.locked" />								
					    </c:otherwise>
				   </c:choose>
				</div>
			</c:if>
	   </c:if>

		<p>
			${taskList.instructions}
		</p>

<!--		<%@ include file="/common/messages.jsp"%> -->

		<table cellspacing="0" class="alternative-color">
			<tr>
				<th width="70%">
					<fmt:message key="label.learning.tasks.to.do" />
				</th>
				<th align="center">
					<fmt:message key="label.completed" />
				</th>
			</tr>
			
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
									<img src="<html:rewrite page='/includes/images/cross.gif'/>"
										border="0">
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:if>
				<c:set var="lastTaskCompletion" value="${item.complete}" />				
			</c:forEach>
		</table>
		
		* - <fmt:message key="label.learning.required.tasks" />
		<br><br>

		<c:if test="${mode != 'teacher'}">
			<p>
				<a href="#" onclick="return checkNew()" class="button"> 
					<fmt:message key="label.learning.check.for.new" /> 
				</a>
			</p>
			<br><br>
		</c:if>

		<c:if test="${mode != 'teacher' && (not finishedLock)}">
			<c:if test="${taskList.allowContributeTasks}">
		
				<p>
					<a href="javascript:showMessage('<html:rewrite page="/learning/addtask.do?sessionMapID=${sessionMapID}&mode=${mode}"/>');" class="button-add-item">
						<fmt:message key="label.authoring.basic.add.task" />
					</a> 
				<p>

				<iframe
					onload="javascript:this.style.height=this.contentWindow.document.body.scrollHeight+'px'"
					id="reourceInputArea" name="reourceInputArea"
					style="width:0px;height:0px;border:0px;display:none"
					frameborder="no" scrolling="no">
				</iframe>
				
			</c:if>
		</c:if>
		
		
		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<div class="small-space-top">
				<h2>${sessionMap.reflectInstructions}</h2>
			
				<c:choose>
					<c:when test="${empty sessionMap.reflectEntry}">
						<p>
							<em>
								<fmt:message key="message.no.reflection.available" />
							</em>
						</p>
					</c:when>
					<c:otherwise>
						<p> <lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />  </p>				
					</c:otherwise>
				</c:choose>
				
				<c:if test="${mode != 'teacher'}">
					<html:button property="FinishButton"
						onclick="return continueReflect()" styleClass="button">
						<fmt:message key="label.edit" />
					</html:button>											
				</c:if>
			</div>
		</c:if>


		<c:set var="isRequiredTasksCompleted" value="${true}" />
		<c:forEach var="item" items="${sessionMap.taskListList}">
			<c:if test="${item.required && not item.complete}">
				<c:set var="isRequiredTasksCompleted" value="${false}" />
			</c:if>
		</c:forEach>

		<c:if test="${(mode != 'teacher') && isRequiredTasksCompleted}">
			<div class="space-bottom-top align-right">

				<c:choose>
					<c:when	test="${taskList.monitorVerificationRequired && !sessionMap.userVerifiedByMonitor}">
						<fmt:message key="label.learning.wait.for.monitor.verification" />
					</c:when>
				
					<c:when	test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
						<html:button property="FinishButton" onclick="return continueReflect()" styleClass="button">
							<fmt:message key="label.continue" />							
						</html:button>
					</c:when>
					
					<c:otherwise>
						<html:button property="FinishButton" styleId="finishButton"
							onclick="return finishSession()" styleClass="button">
							<fmt:message key="label.finished" />
						</html:button>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>




	</div>
	<!--closes content-->

	<div id="footer">
	</div>
	<!--closes footer-->

</body>
</lams:html>
