<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<title>
		<fmt:message key="label.learning.title" />
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
		
		function completeItem(itemUid){
			document.location.href = "<c:url value="/learning/completeItem.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&itemUid=" + itemUid;
			return false;
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
	
		<!--Task Information-->
	
		<h1>
			<c:out value="${taskList.title}" escapeXml="true"/>
		</h1>
		
 	   	<%@ include file="/common/messages.jsp"%>

		<c:if test="${not empty taskList.submissionDeadline}">
			 <div class="info">
			 	<fmt:message key="authoring.info.teacher.set.restriction" >
			 		<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
			 	</fmt:message>
			 </div>
		</c:if>
		

		<p>
			<c:out value="${taskList.instructions}" escapeXml="false"/>
		</p>
		
		<c:if test="${(mode != 'teacher') && taskList.lockWhenFinished}">
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
	   
	   	<c:if test="${(mode != 'teacher') && taskList.sequentialOrder}">
	    	<div class="info space-bottom">
				<fmt:message key="label.learning.info.sequential.order" />								
			</div>
	   </c:if>
		
		<!--TaskListItems table-->
		
		<%@ include file="/pages/learning/parts/itemlist.jsp"%>
		
		<!--"Check for new" button-->
		
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
		
		<!--"Add new task" Area-->

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
		
		<!--Reflection-->
		
		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<div class="small-space-top">
				<h3><fmt:message key="label.monitoring.summary.title.reflection" /></h3>
				<strong><lams:out escapeHtml="true" value="${sessionMap.reflectInstructions}"/></strong>
			
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

		<!--Bottom buttons-->

		<c:set var="isRequiredTasksCompleted" value="${true}" />
		<c:forEach var="itemDTO" items="${sessionMap.itemDTOs}">
			<c:if test="${itemDTO.taskListItem.required && not itemDTO.taskListItem.complete}">
				<c:set var="isRequiredTasksCompleted" value="${false}" />
			</c:if>
		</c:forEach>

		<c:if test="${(mode != 'teacher') && isRequiredTasksCompleted}">
			<div class="space-bottom-top align-right">

				<c:choose>
					<c:when	test="${taskList.monitorVerificationRequired && !sessionMap.userVerifiedByMonitor && (mode != 'author')}">
						<fmt:message key="label.learning.wait.for.monitor.verification" />
					</c:when>
				
					<c:when	test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
						<html:button property="FinishButton" onclick="return continueReflect()" styleClass="button">
							<fmt:message key="label.continue" />							
						</html:button>
					</c:when>
					
					<c:otherwise>
						<html:link href="#nogo" property="FinishButton" styleId="finishButton"
							onclick="return finishSession()" styleClass="button">
							<span class="nextActivity">
								<c:choose>
									<c:when test="${sessionMap.activityPosition.last}">
										<fmt:message key="label.submit" />
									</c:when>
									<c:otherwise>
										<fmt:message key="label.finished" />
									</c:otherwise>
								</c:choose>
							</span>
						</html:link>
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
