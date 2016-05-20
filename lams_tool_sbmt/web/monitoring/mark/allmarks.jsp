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
	<script type="text/javascript">
		function updateMark(detailId,reportId,sessionId,userId){
			location.href="<lams:WebAppURL/>mark.do?method=newMark&updateMode=listAllMarks&userID="+userId+"&toolSessionID="+sessionId+"&detailID="+detailId+"&reportID="+reportId;
		}
	</script>
</lams:head>

<body class="stripes">
<c:set var="title"><fmt:message key="label.monitoring.heading.marking" /></c:set>
<lams:Page title="${title}" type="learner">

	<table class="table table-condensed">
		<c:forEach var="user" items="${reports}">
			<c:forEach items="${user.value}" var="fileInfo" varStatus="status">
				<%@include file="filelist.jsp"%>
			</c:forEach>
		</c:forEach>
		<tr>
			<td colspan="3">
				<html:link href="javascript:window.close();" property="submit" styleClass="btn btn-default">
					<fmt:message key="label.monitoring.done.button" />
				</html:link>
			</td>
		</tr>
	</table>
	
	<div id="footer"></div>
	
</lams:Page>
</body>
</lams:html>