<%@ include file="/common/taglibs.jsp"%>
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

<p>
	<strong><c:out value="${user.lastName}" />&nbsp;<c:out value="${user.firstName}" /></strong>&nbsp;
	<fmt:message key="monitoring.user.post.topic" />
</p>

<c:forEach items="${messages}" var="topic">

<div class="panel panel-default ${highlightClass} msg" >
	<div class="panel-heading">
		<h4 class="panel-title">
			<span style="float: right"> <i class="fa fa-xs fa-user"></i> 
				<lams:Date value="${topic.message.updated}" />
			</span>
			<c:out value="${topic.message.subject}" />
		</h4>
	</div>

	<div class="panel-body" >

	<div class="pull-right"><html:form action="/monitoring/editMark" method="post">
			<html:hidden property="sessionMapID" value="${sessionMapID}"/>
			<html:hidden property="topicID" value="${topic.message.uid}"/>
			<input type="submit" value="<fmt:message key="lable.update.mark"/>" class="btn btn-default btn-sm" />
			</html:form>
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
			<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true" />
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
