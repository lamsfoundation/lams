<!DOCTYPE html>
        

<%@include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
	<title><fmt:message key="activity.title" /></title>
	<lams:css/>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/common.js"></script>
	<script type="text/javascript">
		function updateMark(detailId,reportId,sessionId,userId){
			location.href="<lams:WebAppURL/>mark.do?method=newMark&updateMode=listAllMarks&userID="+userId+"&toolSessionID="+sessionId+"&detailID="+detailId+"&reportID="+reportId;
		}
	</script>
</lams:head>
<body class="stripes">

		<div id="content">
		<h1>
			<fmt:message key="label.monitoring.heading.marking"/>
		</h1>

			<table cellpadding="0" class="alternative-color">
				<c:forEach var="user" items="${reports}">
					<c:forEach items="${user.value}" var="fileInfo" varStatus="status">
						<%@include file="filelist.jsp"%>
					</c:forEach>
				</c:forEach>
				<tr>
					<td colspan="2">
						<html:link href="javascript:window.close();" property="submit" styleClass="button">
							<fmt:message key="label.monitoring.done.button" />
						</html:link>
					</td>
				</tr>
			</table>
		</div>
		<div id="footer"></div>

</body>
</lams:html>