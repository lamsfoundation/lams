<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<title><fmt:message key="activity.title" /></title>
		<%@ include file="/common/header.jsp"%>
	</lams:head>
	<body class="stripes">
			
			<lams:Page type="monitor" title="${page.title.monitoring.view.user.mark}">

			<style media="screen,projection" type="text/css">
				.field-name {
					padding-bottom: 0px;
				}
			</style>
			<script type="text/javascript">
				function closeAndRefreshParentMonitoringWindow() {
					refreshParentMonitoringWindow();
					window.close();
				}
			</script>
			
			<c:if test="${isMarksReleased}">
 				<div class="alert alert-success">
 					<fmt:message key="label.marks.released" />
 				</div>
 			</c:if>

			<p>
				<strong><c:out value="${user.getFullName()}" /></strong>&nbsp;
				<fmt:message key="monitoring.user.post.topic" />
			</p>
			
			<c:forEach items="${messages}" var="topic">
			
			<div class="panel panel-default ${highlightClass} msg" >
			
				<c:choose>
					<c:when test="${topic.message.isMonitor}">
			            	<c:set var="textClass" value="text-info"/>
			                <c:set var="bgClass" value="bg-info"/>
			                <c:set var="iconClass" value ="fa-mortar-board ${textClass}"/>
			            </c:when>
					<c:otherwise>
			            	<c:set var="textClass" value=""/>
			                <c:set var="bgClass" value=""/>
			                <c:set var="iconClass" value ="fa-user ${textClass}"/>
			        </c:otherwise>
			    </c:choose>
			
				<div class="panel-heading">
					<h4 class="panel-title">
					<span class="${textClass}">
						<span style="float: right"> <i class="fa fa-xs ${iconClass}"></i> 
							<lams:Date value="${topic.message.updated}" />
						</span>
						<c:out value="${topic.message.subject}" />
					</span>
					</h4>
				</div>
			
				<div class="panel-body ${bgClass}" >
			
				<div class="pull-right">
						<form:form action="editMark.do" id="markForm" modelAttribute="markForm" method="post">
						<form:hidden path="sessionMapID" value="${sessionMapID}"/>
						<form:hidden path="topicID" value="${topic.message.uid}"/>
						<input type="submit" value="<fmt:message key="lable.update.mark"/>" class="btn btn-default btn-sm" />
						</form:form>
				</div>
			
					<div class="row">
			  		<div class="col-sm-2"><strong><fmt:message key="label.number.of.replies" /></strong></div>
					<div class="col-sm-3">${topic.message.replyNumber}</div>
				</div>
				<div class="row">
					<div class="col-sm-2"><strong><fmt:message key="lable.topic.title.mark" /></strong></div>
					<div class="col-sm-3">
					<c:choose>
					<c:when test="${empty topic.message.report.mark}">
						<fmt:message key="message.not.avaliable" />
					</c:when>
					<c:otherwise>
						<fmt:formatNumber value="${topic.message.report.mark}"  maxFractionDigits="2"/>
					</c:otherwise>
					</c:choose>
					</div>
				</div>
				
				<div class="row">
					<div class="col-sm-2"><strong><fmt:message key="lable.topic.title.comment" /></strong></div>
				  	<div class="col-sm-10">
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
				
				<hr class="msg-hr">
				
				<span><c:out value="${topic.message.body}" escapeXml="false" /></span>
			
				<c:forEach var="file" items="${topic.message.attachments}">
					<c:set var="downloadURL">
						<lams:WebAppURL />download/?uuid=${file.fileDisplayUuid}&versionID=${file.fileVersionId}&preferDownload=true
					</c:set>
					<a href="<c:out value='${downloadURL}' escapeXml='false'/>"><i class="fa fa-paperclip" title="<fmt:message key='message.label.attachment'/>"></i></a>
				</c:forEach>
			
				</div>
			</div>			
			
			</c:forEach>
			
			<div style="padding:0 15px 25px;">
				<a href="javascript:window.close();" class="btn btn-default btn-sm pull-right">
					<fmt:message key="button.close"/>
				</a>
			</div>


			<div id="footer">
			</div>

			</lams:Page>
	</body>
</lams:html>