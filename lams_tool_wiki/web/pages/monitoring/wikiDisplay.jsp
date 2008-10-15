<%@ include file="/common/taglibs.jsp"%>





<html:form action="/monitoring" styleId="monitoringForm" method="post"
	enctype="multipart/form-data">

	<c:set var="formBean"
		value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<html:hidden property="dispatch" styleId="dispatch" />
	<html:hidden property="toolSessionID" />
	<!--<html:hidden property="mode"/>-->
	<html:hidden property="currentWikiPage" value="${currentWikiPage.uid}"
		styleId="currentWikiPage" />
	<input type="hidden" id="wikiLinks" />
	<html:hidden property="newPageName" styleId="newPageName" />
	<html:hidden property="historyPageContentId"
		styleId="historyPageContentId" />

	<div id="wikimenu">
	<h4>${sessionDTO.sessionName}</h4>
	<br />
	<div id="breadcrumb" style="float: left; width: 30%;"><c:if
		test="${currentWikiPage.title != mainWikiPage.title}">
		<a href="javascript:changeWikiPage('${mainWikiPage.javaScriptTitle}')">/${mainWikiPage.title}</a>
	</c:if> <a
		href="javascript:changeWikiPage('${currentWikiPage.javaScriptTitle}')">/${currentWikiPage.title}</a>
	</div>

	<div id="buttons" style="float: right; width: 70%;"><a
		href="javascript:changeDiv('view');" class="button"><fmt:message
		key="label.wiki.view"></fmt:message></a> <a
		href="javascript:changeDiv('edit');" class="button"><fmt:message
		key="label.wiki.edit"></fmt:message></a> <a
		href="javascript:cancelAdd();changeDiv('add');" class="button"><fmt:message
		key="label.wiki.add"></fmt:message></a> <a
		href="javascript:changeDiv('history');" class="button"><fmt:message
		key="label.wiki.history"></fmt:message></a> <c:if
		test="${currentWikiPage.title != mainWikiPage.title}">
		<a
			href="javascript:doRemove(&#39;<fmt:message key="label.wiki.remove.confirm"></fmt:message>&#39;)"
			class="button"><fmt:message key="label.wiki.remove"></fmt:message></a>
	</c:if></div>
	</div>

	<br />
	<br />
	<hr />
	<br />


	<div id="view">
	<h1>${currentWikiPage.title}</h1>
	<br />
	<i> <fmt:message key="label.wiki.last.edit">
		<fmt:param>
			<c:choose>
				<c:when
					test="${currentWikiPage.currentWikiContentDTO.editorDTO == null}">
					<fmt:message key="label.wiki.history.editor.author"></fmt:message>
				</c:when>
				<c:otherwise>
							${currentWikiPage.currentWikiContentDTO.editorDTO.firstName} ${currentWikiPage.currentWikiContentDTO.editorDTO.lastName}
						</c:otherwise>
			</c:choose>
		</fmt:param>
		<fmt:param>
			<lams:Date value="${currentWikiPage.currentWikiContentDTO.editDate}" />
		</fmt:param>
	</fmt:message> </i> <br />
	<br />
	<div id="viewBody">${currentWikiPage.currentWikiContentDTO.body}
	</div>
	</div>

	<div id="history" style="display: none;">

	<h1><fmt:message key="label.wiki.history" /> -
	${currentWikiPage.title}</h1>
	<br />
	<c:choose>
		<c:when test="${not empty wikiPageContentHistory}">
			<table class="alternative-color">
				<tr>
					<th><fmt:message key="label.wiki.history.version"></fmt:message>
					</th>
					<th><fmt:message key="label.wiki.history.date"></fmt:message>
					</th>
					<th><fmt:message key="label.wiki.history.editor"></fmt:message>
					</th>
					<th><fmt:message key="label.wiki.history.actions"></fmt:message>
					</th>
				</tr>
				<c:forEach var="wikiContentPageVersion"
					items="${wikiPageContentHistory}">
					<c:if
						test="${wikiContentPageVersion.version != currentWikiPage.currentWikiContentDTO.version}">
						<tr>
							<td>${wikiContentPageVersion.version}</td>
							<td><lams:Date value="${wikiContentPageVersion.editDate}" />
							</td>
							<td><c:choose>
								<c:when test="${wikiContentPageVersion.editorDTO != null}">
									${wikiContentPageVersion.editorDTO.firstName} ${wikiContentPageVersion.editorDTO.firstName}
								</c:when>
								<c:otherwise>
									<fmt:message key="label.wiki.history.editor.author"></fmt:message>
								</c:otherwise>
							</c:choose></td>
							<td><a
								href="javascript:doRevert('${wikiContentPageVersion.uid}');"
								title='<fmt:message key="label.wiki.history.actions.compare.tooltip" />'>
							<fmt:message key="label.wiki.history.actions.revert" /> </a> &nbsp;
							<a
								href="javascript:doCompareOrView('<lams:WebAppURL/>', '${wikiContentPageVersion.uid}', '${currentWikiPage.uid}', 'comparePage');"
								title='<fmt:message key="label.wiki.history.actions.compare.tooltip" />'>
							<fmt:message key="label.wiki.history.actions.compare" /> </a> &nbsp;
							<a
								href="javascript:doCompareOrView('<lams:WebAppURL/>', '${wikiContentPageVersion.uid}', '${currentWikiPage.uid}', 'viewPage');"
								title='<fmt:message key="label.wiki.history.actions.compare.tooltip" />'>
							<fmt:message key="label.wiki.history.actions.view" /> </a></td>
						</tr>
					</c:if>
				</c:forEach>
			</table>
		</c:when>
		<c:otherwise>
			<fmt:message key="label.wiki.history.empty" />
		</c:otherwise>
	</c:choose></div>

	<div id="edit" style="display: none;">
	<h1><fmt:message key="label.wiki.edit"></fmt:message> -
	${currentWikiPage.title}</h1>
	<table cellpadding="0">
		<tr>
			<td>
			<div class="field-name"><fmt:message
				key="label.authoring.basic.title"></fmt:message></div>
			<html:text property="title" styleId="title" style="width: 99%;"
				value="${currentWikiPage.title}"></html:text></td>
		</tr>
		<tr>
			<td>
			<div class="field-name"><fmt:message key="label.wiki.body"></fmt:message>
			</div>
			<lams:FCKEditor id="wikiBody"
				value="${currentWikiPage.currentWikiContentDTO.body}"
				contentFolderID="${contentFolderID}" toolbarSet="Custom-Learner"
				height="400px">
			</lams:FCKEditor></td>
		</tr>
		<tr>
			<td><html:checkbox property="isEditable" value="1"
				styleClass="noBorder" styleId="isEditable"></html:checkbox>&nbsp;<fmt:message
				key="label.authoring.basic.wikipagevisible"></fmt:message></td>
		</tr>
		<tr>
			<td align="right"><a href="javascript:doEditOrAdd('editPage');"
				class="button"><fmt:message key="label.wiki.savechanges"></fmt:message></a>
			<a href="javascript:changeDiv('view');" class="button"><fmt:message
				key="button.cancel"></fmt:message></a></td>
		</tr>
	</table>
	</div>

	<div id="add" style="display: none;">

	<h1><fmt:message key="label.wiki.add"></fmt:message></h1>
	<table cellpadding="0">
		<tr>
			<td>
			<div class="field-name"><fmt:message
				key="label.authoring.basic.title"></fmt:message></div>
			<html:text property="newPageTitle" styleId="newPageTitle"
				style="width: 99%;" value=""></html:text></td>
		</tr>
		<tr>
			<td>
			<div class="field-name"><fmt:message key="label.wiki.body"></fmt:message>
			</div>
			<lams:FCKEditor id="newPageWikiBody" value="" height="400px"
				contentFolderID="${contentFolderID}" toolbarSet="Custom-Learner"></lams:FCKEditor></td>
		</tr>
		<tr>
			<td><html:checkbox property="newPageIsEditable"
				styleClass="noBorder" value="1" styleId="newPageIsEditable"></html:checkbox>&nbsp;<fmt:message
				key="label.authoring.basic.wikipagevisible"></fmt:message></td>
		</tr>
		<tr>
			<td align="right"><a href="javascript:doEditOrAdd('addPage');"
				class="button"><fmt:message key="label.wiki.savechanges"></fmt:message></a>
			<a href="javascript:cancelAdd();changeDiv('view');" class="button"><fmt:message
				key="button.cancel"></fmt:message></a></td>
		</tr>
	</table>
	</div>

	<br />
	<hr />
	<br />

	<div class="field-name"><fmt:message key="label.wiki.pages"></fmt:message>
	</div>
	<c:forEach var="wikiPage" items="${wikiPages}">
		<a href="javascript:changeWikiPage('${wikiPage.javaScriptTitle}')">
		${wikiPage.title} <c:if test="${wikiPage.title == mainWikiPage.title}">
			<fmt:message key="label.wiki.main"></fmt:message>
		</c:if> </a>
		<br />
	</c:forEach>

	<!-- Reflections -->
	<c:if
		test="${not empty sessionDTO.userDTOs && sessionDTO.reflectOnActivity}">

		<br />
		<hr />
		<br />

		<h4><fmt:message key="monitor.notebooks"></fmt:message></h4>
		<br />
		<table class="alternative-color">
			<c:forEach var="user" items="${sessionDTO.userDTOs}">
				<c:if test="${not empty user.notebookEntry}">
					<tr>
						<td>${user.firstName} ${user.lastName}</td>
						<td><lams:out escapeHtml="true" value="${user.notebookEntry}" />
						</td>
					</tr>
				</c:if>
			</c:forEach>
		</table>
	</c:if>

	<div id="finishButtonDiv"></div>
</html:form>


<script type="text/javascript">
<!--
	var wikiLinkArray = new Array();
	populateWikiLinkArray();
	function populateWikiLinkArray()
	{
		<c:forEach var="wikiPage" items="${wikiPages}">
			wikiLinkArray[wikiLinkArray.length] = '${wikiPage.javaScriptTitle}';
		</c:forEach>
		document.getElementById("wikiLinks").value = wikiLinkArray.toString();
	}
	
	function doEditOrAdd(dispatch)
	{
		var title="";
		if(dispatch == "editPage")
		{
			title = document.getElementById("title").value;
		}
		else
		{
			title = document.getElementById("newPageTitle").value;
		}
		
		var i;
		
		if (title == null || trim(title).length == 0)
		{
			alert('<fmt:message key="label.wiki.add.title.required"></fmt:message>');
			return;
		}
		
		for (i=0; i<wikiLinkArray.length; i++)
		{
			if(dispatch == "editPage" && wikiLinkArray[i] == '${currentWikiPage.javaScriptTitle}')
			{
				continue;
			}
			
			if (trim(title) == wikiLinkArray[i])
			{
				alert('<fmt:message key="label.wiki.add.title.exists"><fmt:param>' + title + '</fmt:param></fmt:message>');
				return;
			}
		}
		
		// if all validation fulfilled, we can continue
		document.getElementById("title").value = trim(title);
		submitWiki(dispatch);
	}
	
	function submitWiki(dispatch)
	{
		document.getElementById("dispatch").value=dispatch;
		document.getElementById("monitoringForm").submit();
	}
	
-->
</script>




