<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@include file="/common/taglibs.jsp"%>
<html>
<head>
	<title><fmt:message key="activity.title" /></title>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<lams:css/>
</head>

<body class="stripes">
		<div id="content">
		<h1>
			<fmt:message key="label.monitoring.updateMarks.button"/>
		</h1>
			<c:forEach var="fileInfo"  items="${report}" varStatus="status">
				<form id="updateMarkForm" method="post" action="<c:url value='/monitoring.do'/>">
					<input type="hidden" name="method" value="updateMark" />
					<input type="hidden" name="toolSessionID" value="${toolSessionID}" />
					<input type="hidden" name="reportID" value="${fileInfo.reportID}" />
					<input type="hidden" name="detailID" value="${fileInfo.submissionID}" />
					<input type="hidden" name="userID" value="${fileInfo.owner.userID}" />
					<input type="hidden" name="updateMode" value="${updateMode}" />
						<table cellpadding="0">
							<%@include file="fileinfo.jsp"%>
							<tr>
								<td colspan="2">
									<%@include file="/common/messages.jsp"%>
								</td>
							</tr>
						
							<tr>
								<td class="field-name"  style="text-align: left;">
									<fmt:message key="label.learner.marks" />
								</td>
								<td>
									<input type="text" name="marks" value=<c:out value="${fileInfo.marks}"  escapeXml="false"/>>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<span  class="field-name"><fmt:message key="label.learner.comments" /><BR></span>
									<lams:FCKEditor id="comments"
										value="${fileInfo.comments}"
										toolbarSet="Default-Learner"></lams:FCKEditor>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<a href="javascript:history.back();" class="button">
										<fmt:message key="label.cancel" />
									</a>
									&nbsp;&nbsp;
									<a href="javascript:document.getElementById('updateMarkForm').submit();" class="button">
										<fmt:message key="label.monitoring.saveMarks.button" />
									</a>
								</td>
							</tr>
						</table>
				</form>
			</c:forEach>
			</div>

		<div id="footer"></div>
		</div>
</body>
</html>