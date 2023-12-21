<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<title>
			<fmt:message key="label.reviewitem.title" />
		</title>	
		<%@ include file="/common/header.jsp"%>
	</lams:head>
	
	<body class="stripes">
	
		<c:set var="title"><fmt:message key="label.reviewitem.title" /></c:set>
		<lams:Page type="learner" title="${title}">
			
			<p><fmt:message key="label.reviewitem.spreadsheet.sent.by" />&nbsp;<strong><c:out value="${userName}" escapeXml="true"/></strong></p>
			<c:choose>
				<c:when test="${code == null}">
					<lams:Alert type="info" id="no-spreadsheet" close="false">
						<fmt:message key="label.reviewitem.user.hasnot.sent.spreadsheet" />
					</lams:Alert>
				</c:when>
				<c:otherwise>
				    <form:hidden path="code" id="spreadsheet-code" value="${code}"/>	
					<iframe
						id="externalSpreadsheet" name="externalSpreadsheet" src="${spreadsheetURL}"
						style="width:99%;" frameborder="no" height="385px"
						scrolling="no">
					</iframe> 

					<a href="javascript:window.close();" class="btn btn-secondary"><fmt:message key="button.close"/></a>
					
				</c:otherwise>
			</c:choose>	

		<div id="footer"></div>

		</lams:Page>
		
	</body>
</lams:html>
