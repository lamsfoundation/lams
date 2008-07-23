<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="summaryList" value="${sessionMap.summaryList}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="title" value="${sessionMap.title}" />
<c:set var="instructions" value="${sessionMap.instructions}" />
<c:set var="spreadsheet" value="${sessionMap.spreadsheet}" />

<script>
	function popitup2(userUid) {
		window.open('./simple_spreadsheet/spreadsheet_exported.html?userUid=' + userUid, 'name', 'width=800, height=600');
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
	
		<c:set var='title' ><fmt:message key="label.reviewitem.title" /></c:set>
		<html:hidden property="title" styleId="title" value="${title}" />
		<c:set var='sentBy' ><fmt:message key='label.reviewitem.spreadsheet.sent.by' /></c:set>
		<html:hidden property="sentBy" styleId="sentBy" value="${sentBy}"/>
		<c:set var='userHasntSent' ><fmt:message key='label.reviewitem.user.hasnot.sent.spreadsheet' /></c:set>
		<html:hidden property="userHasntSent" styleId="userHasntSent" value="${userHasntSent}"/>
		<html:hidden property="lang" styleId="lang" value="<%=request.getLocale().getLanguage()%>"/>
		<c:set var='closeButton' ><fmt:message key='button.close'/></c:set>
		<html:hidden property="closeButton" styleId="closeButton" value="${closeButton}"/>	

		<h1>
			${title}
		</h1>
		
		<div>
			${instructions}
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
