<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<c:set var="title"><fmt:message key="page.title.monitoring.view.user.mark" /></c:set>
<lams:PageMonitor title="${title}" hideHeader="true">
	<script type="text/javascript">
		function closeAndRefreshParentMonitoringWindow() {
			refreshParentMonitoringWindow();
			window.close();
		}
	</script>
			
	<h1>
		${title}
	</h1>
			
	<c:if test="${isMarksReleased}">
 		<div class="alert alert-success">
 			<fmt:message key="label.marks.released" />
 		</div>
 	</c:if>

	<p>
		<strong><c:out value="${user.lastName}" />&nbsp;<c:out value="${user.firstName}" /></strong>&nbsp;
		<fmt:message key="monitoring.user.post.topic" />
	</p>
			
	<c:forEach items="${messages}" var="topic">
		<div class="lcard ${highlightClass} msg" >
			<c:choose>
				<c:when test="${topic.message.isMonitor}">
		           	<c:set var="textClass" value="text-warning"/>
		            <c:set var="bgClass" value="bg-warning"/>
		            <c:set var="iconClass" value ="fa-mortar-board ${textClass}"/>
		        </c:when>
				<c:otherwise>
		          	<c:set var="textClass" value=""/>
		            <c:set var="bgClass" value=""/>
		            <c:set var="iconClass" value ="fa-user ${textClass}"/>
		        </c:otherwise>
		    </c:choose>
			
			<div class="card-header">
				<span class="${textClass}">
					<span class="font-size-init float-end"> 
						<i class="fa fa-xs ${iconClass}"></i> 
						<lams:Date value="${topic.message.updated}" />
					</span>
					<c:out value="${topic.message.subject}" />
				</span>
			</div>
			
			<div class="card-body ${bgClass} p-2">
				<span><c:out value="${topic.message.body}" escapeXml="false" /></span>
			
				<c:forEach var="file" items="${topic.message.attachments}">
					<span class="badge text-bg-warning bg-opacity-50 me-1 mt-3" title="<fmt:message key='message.label.attachment'/>">
						<fmt:message key='message.label.attachment'/>&nbsp;<c:out value="${file.fileName}" />
					</span>
				
					<c:set var="downloadURL">
						<lams:WebAppURL />download/?uuid=${file.fileDisplayUuid}&versionID=${file.fileVersionId}&preferDownload=true
					</c:set>
					<a href="<c:out value='${downloadURL}' escapeXml='false'/>" class="btn btn-sm btn-light">
						<fmt:message key="label.download" />
						<i class="fa-solid fa-download ms-1"></i>
					</a>
				</c:forEach>
				
				<div class="row mt-2">
			  		<div class="col-2">
			  			<strong><fmt:message key="label.number.of.replies" /></strong>
			  		</div>
					<div class="col">
						${topic.message.replyNumber}
					</div>
				</div>
				
				<div class="row">
					<div class="col-2">
						<strong><fmt:message key="lable.topic.title.mark" /></strong>
					</div>
					<div class="col d-flex flex-row align-items-center">
						<c:choose>
							<c:when test="${empty topic.message.report.mark}">
								<fmt:message key="message.not.avaliable" />
							</c:when>
							<c:otherwise>
								<fmt:formatNumber value="${topic.message.report.mark}"  maxFractionDigits="2"/>
							</c:otherwise>
						</c:choose>
						

				<span>
					<form:form action="editMark.do" id="markForm" modelAttribute="markForm" method="post">
						<form:hidden path="sessionMapID" value="${sessionMapID}"/>
						<form:hidden path="topicID" value="${topic.message.uid}"/>
						<button type="submit" class="btn btn-light btn-sm ms-2">
							<i class="fa-solid fa-marker me-1"></i>
							<fmt:message key="lable.update.mark"/>
						</button>
					</form:form>
				</span>	
					</div>
				</div>
				
				<div class="row">
					<div class="col-2">
						<strong><fmt:message key="lable.topic.title.comment" /></strong>
					</div>
				  	<div class="col">
					<c:choose>
					<c:when test="${empty topic.message.report.comment}">
						<fmt:message key="message.not.avaliable" />
					</c:when>
					<c:otherwise>
						<c:out value="${topic.message.report.comment}" escapeXml="false" />
					</c:otherwise>
					</c:choose>
					</div>
				</div>
			</div>
		</div>
	</c:forEach>
			
	<div class="activity-bottom-buttons">
		<button onclick="window.close();" class="btn btn-primary">
			<i class="fa-regular fa-circle-xmark me-1"></i>
			<fmt:message key="button.close"/>
		</button>
	</div>
</lams:PageMonitor>
