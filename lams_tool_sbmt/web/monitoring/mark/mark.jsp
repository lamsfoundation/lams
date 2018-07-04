<!DOCTYPE html>
        
<%@include file="/common/taglibs.jsp"%>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<lams:html>
<lams:head>
	<title><fmt:message key="activity.title" /></title>
	<lams:css/>
	
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/readmore.min.js"></script>	
	<script type="text/javascript">
		function updateMark(detailId,reportId,sessionId,userId){
			location.href="<lams:WebAppURL/>mark.do?method=newMark&updateMode=listMark&userID="+userId+"&toolSessionID="+sessionId+"&detailID="+detailId+"&reportID="+reportId;
		}

		function removeLearnerFile(detailId,sessionId,userId,filename) {
			var msg = '<fmt:message key="message.monitor.confirm.original.learner.file.delete"/>';
			msg = msg.replace('{0}', filename)
			var answer = confirm(msg);
			if (answer) {	
				location.href="<c:url value="/monitoring.do"/>?method=removeLearnerFile&userID="+userId+"&toolSessionID="+sessionId+"&detailID="+detailId;
			}
		}

		function restoreLearnerFile(detailId,sessionId,userId,filename) {
			var msg = '<fmt:message key="message.monitor.confirm.original.learner.file.restore"/>';
			msg = msg.replace('{0}', filename)
			var answer = confirm(msg);
			if (answer) {	
				location.href="<c:url value="/monitoring.do"/>?method=restoreLearnerFile&userID="+userId+"&toolSessionID="+sessionId+"&detailID="+detailId;
			}
		}

	</script>
</lams:head>

<body class="stripes">
<c:set var="title"><fmt:message key="label.monitoring.heading.marking" /></c:set>
<lams:Page title="${title}" type="monitor">

	<c:if test="${isMarksReleased}">
		<div class="alert alert-success">
            <i class="fa fa-check-circle"></i>
			<fmt:message key="label.marks.released" />
		</div>
	</c:if>

	<c:forEach var="fileInfo" items="${report}" varStatus="status">
		<%@include file="fileinfo.jsp"%>
		<hr width="100%">
	</c:forEach>
	
	<html:link href="javascript:window.close();" property="submit" styleClass="btn btn-primary pull-right">
		<fmt:message key="label.monitoring.done.button" />
	</html:link>
	
	<div id="footer"></div>
	
</lams:Page>
</body>
</lams:html>
