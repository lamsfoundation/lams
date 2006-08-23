<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@include file="/common/taglibs.jsp"%>
<html>
<head>
	<title><fmt:message key="activity.title" /></title>
	<lams:headItems />
	<script type="text/javascript">
		function updateMark(detailId,reportId,sessionId,userId){
			var act = "<c:url value="/monitoring.do"/>";
			location.href=act + "?method=newMark&updateMode=listMark&userID="+userId+"&toolSessionID="+sessionId+"&detailID="+detailId+"&reportID="+reportId;
		}
	</script>
</head>
<body>
	<div id="page-learner">
		<h1 class="no-tabs-below">
			<fmt:message key="label.monitoring.heading.marking"/>
		</h1>
		<div id="header-no-tabs-learner">
		</div>
		<div id="content-learner">
				<table cellpadding="0">
					<c:forEach var="fileInfo" items="${report}" varStatus="status">
						<%@include file="filelist.jsp"%>
					</c:forEach>
					<tr>
						<td colspan="2">
							<html:link href="javascript:window.close();" property="submit" styleClass="button">
								<bean:message key="label.monitoring.done.button" />
							</html:link>
						</td>
					</tr>
				</table>
			</div>
		<div id="footer-learner"></div>
		</div>
	</div>
</body>
</html>