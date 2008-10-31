<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<html>
	<lams:head>
		<title><c:out value="${mainWikiPage.title}" escapeXml="false" />
		</title>
		<lams:css localLinkPath="../" />
	
	
		<script type="text/javascript">
		<!--
			var currentPage;
			
			function init(mainPage)
			{
				mainPage.replace("&quot;", '"');
				document.getElementById(mainPage).style.display="block";
				currentPage = mainPage;
			}
			
			function changeWikiPage(javaScriptTitle)
			{
				javaScriptTitle.replace("&quot;", '"');
				document.getElementById(currentPage).style.display="none";
				document.getElementById(javaScriptTitle).style.display="block";
				currentPage = javaScriptTitle;
			}
	
		-->
		</script>
	
	</lams:head>

	<body class="stripes" onload="javascript:init('${mainWikiPage.javaScriptTitle}');">

		<div id="content">
			<div id="breadcrumb" style="float: left; width: 30%;">
				<c:if test="${currentWikiPage.title != mainWikiPage.title}">
					<a href="javascript:changeWikiPage('${mainWikiPage.javaScriptTitle}')">/${mainWikiPage.title}</a>
				</c:if>
				<a href="javascript:changeWikiPage('${currentWikiPage.javaScriptTitle}')">/${currentWikiPage.title}</a>
			</div>
			
			<br />
			<br />
			<hr />
			<br />
			
			
			<c:forEach var="wikiPage" items="${wikiPages}">
				
				<div id='${wikiPage.javaScriptTitle}' style="display:none">
					<h1>
						${wikiPage.title}
					</h1>
					<i>
						<fmt:message key="label.wiki.last.edit">
							<fmt:param>
								<c:choose>
									<c:when test="${wikiPage.currentWikiContentDTO.editorDTO == null}">
										<fmt:message key="label.wiki.history.editor.author"></fmt:message>
									</c:when>
									<c:otherwise>
										${wikiPage.currentWikiContentDTO.editorDTO.firstName} ${currentWikiPage.currentWikiContentDTO.editorDTO.lastName}
									</c:otherwise>
								</c:choose>
							</fmt:param>
							<fmt:param><lams:Date value="${wikiPage.currentWikiContentDTO.editDate}" /></fmt:param>
						</fmt:message>
					</i>
					<br />
					<br />
					
					<div>
						${wikiPage.currentWikiContentDTO.body}
					</div>
				
				</div>
			</c:forEach>	
			
			<br />
			<hr />
			<br />
			
			<div class="field-name">
			<fmt:message key="label.wiki.pages"></fmt:message>
			</div>
			<c:forEach var="wikiPage" items="${wikiPages}">
				<a href="javascript:changeWikiPage('${wikiPage.javaScriptTitle}')">
					${wikiPage.title}
				</a><br />
			</c:forEach>
		
		
			<!-- Reflections -->
			<c:choose>
				<c:when test="${mode=='teacher'}">
					<c:if test="${not empty sessionDTO.userDTOs && sessionDTO.reflectOnActivity}">
						<br />
						<hr />
						<br />
						
						<h4>
							<fmt:message key="monitor.notebooks"></fmt:message>
						</h4>
						<table class="alternative-color">
							<c:forEach var="user" items="${sessionDTO.userDTOs}">
								<c:if test="${not empty user.notebookEntry}">
								<tr>
									<td>
										${user.firstName} ${user.lastName}
									</td>
									<td> 
										<lams:out escapeHtml="true" value="${user.notebookEntry}" />
									</td>
								</tr>
								</c:if>
							</c:forEach>
						</table>
					</c:if>
				</c:when>
				<c:otherwise>
					<c:if test="${userDTO.notebookEntry != null && wikiDTO.reflectOnActivity}">
						<br />
						<hr />
						<br />
						
						<h4>
							${wikiDTO.reflectInstructions}
						</h4>
						<br />
						
							<lams:out escapeHtml="true" value="${userDTO.notebookEntry}" />	
					</c:if>
				</c:otherwise>
			</c:choose>
			
		
		</div>
		<!--closes content-->

		<div id="footer">
		</div>
		<!--closes footer-->
	</body>
</html>

