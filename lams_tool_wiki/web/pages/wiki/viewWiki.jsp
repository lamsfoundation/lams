<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	
	<lams:head>
		<title>
			<fmt:message key="label.wiki.history.actions.view" />
		</title>
		
		<lams:headItems />
		
	</lams:head>
	
	<body class="stripes">
	
		<lams:Page title="${currentWikiPage.title}" type="learner">
			
			<div>
				<strong><fmt:message key="label.wiki.history.version"></fmt:message>:</strong> ${currentWikiPage.currentWikiContentDTO.version}<br />
				<strong><fmt:message key="label.wiki.history.date"></fmt:message>:</strong> <lams:Date value="${currentWikiPage.currentWikiContentDTO.editDate}" timeago="true"/><br />
				<strong><fmt:message key="label.wiki.history.editor"></fmt:message>:</strong> 
				<c:choose>
					<c:when test="${currentWikiPage.currentWikiContentDTO.editorDTO != null}">
						${currentWikiPage.currentWikiContentDTO.editorDTO.firstName} ${currentWikiPage.currentWikiContentDTO.editorDTO.firstName}
					</c:when>
					<c:otherwise>
						<fmt:message key="label.wiki.history.editor.author"></fmt:message>
					</c:otherwise>
				</c:choose>
			</div>
			
			<div id="viewBody">
				<strong><fmt:message key="label.wiki.body"></fmt:message>:</strong> ${currentWikiPage.currentWikiContentDTO.body}
			</div>
			
			<div id="saveCancelButtons" class="voffset10"> 
				<a href="javascript:window.close()" class="btn btn-secondary"><fmt:message key="button.close" /></a>
			</div>
		
		<div id="footer"></div>
		</lams:Page>
		
	</body>
</lams:html>
