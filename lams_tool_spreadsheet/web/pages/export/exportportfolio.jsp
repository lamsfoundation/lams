<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.spreadsheet.SpreadsheetConstants"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="summaryList" value="${sessionMap.summaryList}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="title" value="${sessionMap.title}" />
<c:set var="instructions" value="${sessionMap.instructions}" />
<c:set var="spreadsheet" value="${sessionMap.spreadsheet}" />

<script type="text/javascript">
	function popitup2(userUid) {
		
		var userName = encodeURIComponent(document.getElementById("userName" + userUid).value);
		var code = encodeURIComponent(document.getElementById("spreadsheet.code" + userUid).value);
		
		var url = "./simple_spreadsheet/spreadsheet_exported.html" 
			    + "?userName=" + userName
				+ "&code=" + code
				+ "&lang=<%=request.getLocale().getLanguage()%>"
				+ "&labelTitle=<fmt:message key='label.reviewitem.title' />"
				+ "&labelSentBy=<fmt:message key='label.reviewitem.spreadsheet.sent.by' />" 
				+ "&labelUserHasntSent=<fmt:message key='label.reviewitem.user.hasnot.sent.spreadsheet' />"
				+ "&labelCloseButton=<fmt:message key='button.close'/>";
		window.open(url, 'name', 'width=800, height=600');
	}
</script>

<lams:html>
<lams:head>
	<title>
		<fmt:message key="export.title" />
	</title>
	
	<c:set var="lams">
		<lams:LAMSURL />
	</c:set>
	
	<lams:css localLinkPath="../" />

</lams:head>
<body class="stripes">

	<div id="content">

		<h1>
			<c:out value="${title}" escapeXml="true"/>
		</h1>
		
		<div>
			<c:out value="${instructions}" escapeXml="false"/>
		</div>
		
		<c:if test="${spreadsheet.lockWhenFinished || spreadsheet.learnerAllowedToSave || spreadsheet.markingEnabled || spreadsheet.reflectOnActivity}">
			<br>
			<ul>
				<c:if test="${spreadsheet.lockWhenFinished}">
					<li>
						<fmt:message key="label.authoring.advance.lock.on.finished" />
					</li>
				</c:if>
				
				<c:if test="${spreadsheet.learnerAllowedToSave}">
					<li>
						<fmt:message key="label.authoring.advanced.learners.are.allowed.to.save" />
					</li>
				</c:if>
							
				<c:if test="${spreadsheet.markingEnabled}">
				    <ul>
						<li>
							<fmt:message key="label.export.marking.enabled" />
						</li>
					</ul>
				</c:if>
							
				<c:if test="${spreadsheet.reflectOnActivity}">
					<li>
						<fmt:message key="label.export.notebook.reflection.enabled" />
					</li>
				</c:if>
				
			</ul>
		</c:if>
		<br>
		<br>
		
		<c:forEach var="summary" items="${summaryList}">
			<%@ include file="summarylist.jsp"%>
		</c:forEach>

	</div>  <!--closes content-->

	<div id="footer">
	</div><!--closes footer-->
	
</body>
</lams:html>
