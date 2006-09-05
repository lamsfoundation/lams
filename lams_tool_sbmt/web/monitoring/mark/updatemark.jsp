<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@include file="/common/taglibs.jsp"%>
<html>
<head>
	<title><fmt:message key="activity.title" /></title>
	<lams:headItems />
	<!-- ********************  CSS ********************** -->
	<link href="<html:rewrite page='/includes/css/tool_custom.css'/>" rel="stylesheet" type="text/css">	
</head>

<body>
	<div id="page-learner">
		<h1 class="no-tabs-below">
			<fmt:message key="label.monitoring.updateMarks.button"/>
		</h1>
		<div id="header-no-tabs-learner">
		</div>
		<div id="content-learner">
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
									<c:set var="language"><lams:user property="localeLanguage"/></c:set>
									<span  class="field-name"><fmt:message key="label.learner.comments" /><BR></span>
									<FCK:editor id="comments" basePath="/lams/fckeditor/"
												imageBrowserURL="/FCKeditor/editor/filemanager/browser/default/browser.html?Type=Image&amp;Connector=connectors/jsp/connector"
												linkBrowserURL="/FCKeditor/editor/filemanager/browser/default/browser.html?Connector=connectors/jsp/connector"
												flashBrowserURL="/FCKeditor/editor/filemanager/browser/default/browser.html?Type=Flash&amp;Connector=connectors/jsp/connector"
												imageUploadURL="/FCKeditor/editor/filemanager/upload/simpleuploader?Type=Image"
												linkUploadURL="/FCKeditor/editor/filemanager/upload/simpleuploader?Type=File"
												flashUploadURL="/FCKeditor/editor/filemanager/upload/simpleuploader?Type=Flash"
												toolbarSet="Default-Learner" defaultLanguage="${language}" autoDetectLanguage="false">
												<c:out value="${fileInfo.comments}" escapeXml="false"/>
									</FCK:editor>			
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

		<div id="footer-learner"></div>
		</div>
	</div>
</body>
</html>