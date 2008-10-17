<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	
	<lams:head>
		<title>
			<fmt:message key="label.wiki.history.actions.view" />
		</title>
		
		<lams:headItems />
		
		<link href="<lams:WebAppURL />/includes/css/wiki_style.css" rel="stylesheet" type="text/css">
	</lams:head>
	
	<div id="page">
	<body class="stripes">
		<div id="header">
		</div>
		
		<div id="content" style="margin-right:75px;">
			<h1>
				${currentWikiPage.title}
			</h1>
			
			<h3>
				<fmt:message key="label.wiki.history.version"></fmt:message>: ${currentWikiPage.currentWikiContentDTO.version}<br />
				<fmt:message key="label.wiki.history.date"></fmt:message>: <lams:Date value="${currentWikiPage.currentWikiContentDTO.editDate}"/><br />
				<fmt:message key="label.wiki.history.editor"></fmt:message>: 
				<c:choose>
					<c:when test="${currentWikiPage.currentWikiContentDTO.editorDTO != null}">
						${currentWikiPage.currentWikiContentDTO.editorDTO.firstName} ${currentWikiPage.currentWikiContentDTO.editorDTO.firstName}
					</c:when>
					<c:otherwise>
						<fmt:message key="label.wiki.history.editor.author"></fmt:message>
					</c:otherwise>
				</c:choose>
			</h3>
			
			<br />	
			<div id="viewBody">
				${currentWikiPage.currentWikiContentDTO.body}
			</div>
			
			<p id="saveCancelButtons" >
				<a href="javascript:window.close()" class="button right-buttons space-left"><fmt:message key="button.close" /></a>
			</p>
		</div>
		
		<div id="footer">
		</div>
	</div>
	</body>
</lams:html>