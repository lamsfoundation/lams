<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

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
		function closeAndRefreshParentMonitoringWindow() {
			refreshParentMonitoringWindow();
			window.close();
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
						<html:link href="javascript:closeAndRefreshParentMonitoringWindow();" property="submit" styleClass="button">
							<fmt:message key="label.monitoring.done.button" />
						</html:link>
					</td>
				</tr>
			</table>
		</div>
		<div id="footer"></div>

</body>
</lams:html>