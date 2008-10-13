<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
<!--
	var mode = "${mode}";
	var formName = "learningForm"

	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}

	function validateForm() {
	
		// Validates that there's input from the user. 
		
		// disables the Finish button to avoid double submittion 
		disableFinishButton();

 		if (mode == "learner") {
			// if this is learner mode, then we add this validation see (LDEV-1319)
		
			if (document.learningForm.entryText.value == "") {
				
				// if the input is blank, then we further inquire to make sure it is correct
				if (confirm("<fmt:message>message.learner.blank.input</fmt:message>"))  {
					// if correct, submit form
					return true;
				} else {
					// otherwise, focus on the text area
					document.learningForm.entryText.focus();
					document.getElementById("finishButton").disabled = false;
					return false;      
				}
			} else {
				// there was something on the form, so submit the form
				return true;
			}
		}
	}

-->
</script>


<div id="content">
	<!-- Display the advanced option warnings -->
	<div class="info" id="messageDiv" style="display:none;">
		<c:if test="${wikiDTO.lockOnFinish and mode == 'learner'}">
				<p>
				<c:choose>
					<c:when test="${finishedActivity}">
						<fmt:message key="message.activityLocked" />
					</c:when>
					<c:otherwise>
						<fmt:message key="message.warnLockOnFinish" />
					</c:otherwise>
				</c:choose>
				</p>
		</c:if>
		
		<c:if test="${not (wikiDTO.lockOnFinish and finishedActivity) }">
			<c:choose>
				<c:when test="${currentWikiPage.editable}">
					<c:if test="${not minEditsReached}">
						<p>
						<fmt:message key="message.minumumEditsNotReached">
							<fmt:param>
								${wikiDTO.minimumEdits}
							</fmt:param>
						</fmt:message>
						</p>
					</c:if>
					
					<c:if test="${wikiDTO.maximumEdits > 0}">
						<p>
							<c:choose>
								<c:when test="${maxEditsReached}">
									<fmt:message key="message.maxEditsReached" />
								</c:when>
								<c:otherwise>
									<fmt:message key="message.warnMaxEdits" >
										<fmt:param>
										${editsLeft} 
										</fmt:param>
									</fmt:message>
								</c:otherwise>
							</c:choose>
						</p>
					</c:if>
				</c:when>
				<c:otherwise>
					<p>		
						<fmt:message key="message.pageNotEditable" />
					</p>
				</c:otherwise>
			</c:choose>
		</c:if>
	</div>

	&nbsp;

	<html:form action="/learning" method="post" styleId="learningForm" enctype="multipart/form-data">
		<html:hidden property="dispatch" styleId="dispatch" value="finishActivity" />
		<html:hidden property="toolSessionID" />
		<html:hidden property="mode" />
		<html:hidden property="currentWikiPage" value="${currentWikiPage.uid}" styleId="currentWikiPage" />
		<input type="hidden" id="wikiLinks"/>
		<html:hidden property="newPageName" styleId="newPageName"/>
		<html:hidden property="historyPageContentId" styleId="historyPageContentId"/>
		<c:set var="lrnForm" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
		
		<!-- Set up edit and add flags -->
		<c:set var="editableFlag" value="${currentWikiPage.editable and not maxEditsReached}" />
		<c:set var="addFlag" value="${wikiDTO.allowLearnerCreatePages and not maxEditsReached}" />
		
		
		<div id="wikimenu">
			<div id="breadcrumb" style="float: left; width: 30%;">
				<c:if test="${currentWikiPage.title != mainWikiPage.title}">
					<a href="javascript:changeWikiPage('${mainWikiPage.javaScriptTitle}')">/${mainWikiPage.title}</a>
				</c:if>
				<a href="javascript:changeWikiPage('${currentWikiPage.javaScriptTitle}')">/${currentWikiPage.title}</a>
			</div>
			
			<div id="buttons" style="float: right; width: 70%;">
			
				<c:if test="${contentEditable}">
					<c:if test="${editableFlag or addFlag}">	
						<a href="javascript:changeDiv('view');" class="button"><fmt:message key="label.wiki.view"></fmt:message></a>
					</c:if>
					<c:if test="${editableFlag}">	
						<a href="javascript:changeDiv('edit');" class="button"><fmt:message key="label.wiki.edit"></fmt:message></a>
					</c:if>
					<c:if test="${addFlag}">
						<a href="javascript:cancelAdd();changeDiv('add');" class="button"><fmt:message key="label.wiki.add"></fmt:message></a>
					</c:if>
					<c:if test="${editableFlag}">
						<a href="javascript:changeDiv('history');" class="button"><fmt:message key="label.wiki.history"></fmt:message></a>
					</c:if>
					<c:if test="${currentWikiPage.title != mainWikiPage.title && editableFlag}">
						<a href="javascript:doRemove(&#39;<fmt:message key="label.wiki.remove.confirm"></fmt:message>&#39;)" class="button"><fmt:message key="label.wiki.remove"></fmt:message></a>
					</c:if>
				</c:if>
			</div>
		</div>
		
		<br />
		<br />
		<hr />
		<br />
		
		<div id="view" >
			<h1>
			${currentWikiPage.title}
			</h1>
			<i>
				<fmt:message key="label.wiki.last.edit">
					<fmt:param>
						<c:choose>
							<c:when test="${currentWikiPage.currentWikiContentDTO.editorDTO == null}">
								<fmt:message key="label.wiki.history.editor.author"></fmt:message>
							</c:when>
							<c:otherwise>
								${currentWikiPage.currentWikiContentDTO.editorDTO.firstName} ${currentWikiPage.currentWikiContentDTO.editorDTO.lastName}
							</c:otherwise>
						</c:choose>
					</fmt:param>
					<fmt:param><lams:Date value="${currentWikiPage.currentWikiContentDTO.editDate}" /></fmt:param>
				</fmt:message>
			</i>
			<br />
			<br />
			<div id="viewBody">
			${currentWikiPage.currentWikiContentDTO.body}
			</div>
		</div>
			
		<div id="history" style="display: none;">
			<h1>
			<fmt:message key="label.wiki.history" /> - ${currentWikiPage.title}
			</h1>
			<br />
			<c:choose>
			<c:when test="${not empty wikiPageContentHistory}">
				<table class="alternative-color">
					<tr>
						<th>
							<fmt:message key="label.wiki.history.version"></fmt:message>
						</th>	
						<th>
							<fmt:message key="label.wiki.history.date"></fmt:message>
						</th>
						<th>
							<fmt:message key="label.wiki.history.editor"></fmt:message>
						</th>
						<th>
							<fmt:message key="label.wiki.history.actions"></fmt:message>
						</th>
					</tr>
					<c:forEach var="wikiContentPageVersion" items="${wikiPageContentHistory}">
						<c:if test="${wikiContentPageVersion.version != currentWikiPage.currentWikiContentDTO.version}">
						<tr>
							<td>
								${wikiContentPageVersion.version}
							</td>
							<td>
								<lams:Date value="${wikiContentPageVersion.editDate}" />
							</td>
							<td>
								<c:choose>
									<c:when test="${wikiContentPageVersion.editorDTO != null}">
										${wikiContentPageVersion.editorDTO.firstName} ${wikiContentPageVersion.editorDTO.firstName}
									</c:when>
									<c:otherwise>
										<fmt:message key="label.wiki.history.editor.author"></fmt:message>
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<a href="javascript:doRevert('${wikiContentPageVersion.uid}');" class="button" title='<fmt:message key="label.wiki.history.actions.compare.tooltip" />'>
									<fmt:message key="label.wiki.history.actions.revert" />
								</a>
								&nbsp;
								<a href="javascript:doCompareOrView('<lams:WebAppURL/>', '${wikiContentPageVersion.uid}', '${currentWikiPage.uid}', 'comparePage');" class="button" title='<fmt:message key="label.wiki.history.actions.compare.tooltip" />'>
									<fmt:message key="label.wiki.history.actions.compare" />
								</a>
								&nbsp;
								<a href="javascript:doCompareOrView('<lams:WebAppURL/>', '${wikiContentPageVersion.uid}', '${currentWikiPage.uid}', 'viewPage');" class="button" title='<fmt:message key="label.wiki.history.actions.compare.tooltip" />'>
									<fmt:message key="label.wiki.history.actions.view" />
								</a>
							</td>
						</tr>
						</c:if>
					</c:forEach>
				</table>
			</c:when>
			<c:otherwise>
				<fmt:message key="label.wiki.history.empty" />
			</c:otherwise>
			</c:choose>
		</div>
		
		
		<div id="edit" style="display:none;"  >
			<h1>
				<fmt:message key="label.wiki.edit"></fmt:message> - ${currentWikiPage.title}
			</h1>
			<table cellpadding="0">
				<tr>
					<td>
						<div class="field-name">
							<fmt:message key="label.authoring.basic.title"></fmt:message>
						</div>
						<html:text property="title" styleId="title" style="width: 99%;" value="${currentWikiPage.title}"></html:text>
					</td>
				</tr>
				<tr>
					<td>
						<div class="field-name">
							<fmt:message key="label.wiki.body"></fmt:message>
						</div>
						<lams:FCKEditor id="wikiBody"
							value="${currentWikiPage.currentWikiContentDTO.body}"
							contentFolderID="${sessionMap.contentFolderID}"
							toolbarSet="Custom-Learner"
							height="400px"
							>
						</lams:FCKEditor>
					</td>
				</tr>
				<tr>
					<td align="right">
						<br />
						<a href="javascript:doEditOrAdd('editPage');" class="button"><fmt:message key="label.wiki.savechanges"></fmt:message></a>	
						<a href="javascript:changeDiv('view');" class="button"><fmt:message key="button.cancel"></fmt:message></a>
					</td>
				</tr>
			</table>
		</div>
		
		<div id="add" style="display:none;"  >
			<h1><fmt:message key="label.wiki.add"></fmt:message></h1>
			<table cellpadding="0">
				<tr>
					<td>
						<div class="field-name">
							<fmt:message key="label.authoring.basic.title"></fmt:message>
						</div>
						<html:text property="newPageTitle" styleId="newPageTitle" style="width: 99%;" value=""></html:text>
					</td>
				</tr>
				<tr>
					<td>
						<div class="field-name">
							<fmt:message key="label.wiki.body"></fmt:message>
						</div>
						<lams:FCKEditor id="newPageWikiBody"
							value=""
							height="400px"
							contentFolderID="${sessionMap.contentFolderID}"
							toolbarSet="Custom-Learner"></lams:FCKEditor>
					</td>
				</tr>
				<tr>
					<td align="right">
						<br />
						<a href="javascript:doEditOrAdd('addPage');" class="button"><fmt:message key="label.wiki.savechanges"></fmt:message></a>	
						<a href="javascript:cancelAdd();changeDiv('view');" class="button"><fmt:message key="button.cancel"></fmt:message></a>
					</td>
				</tr>
			</table>
		</div>
		
		<br />
		<hr />
		<br />
		
		
		<div class="field-name">
			<fmt:message key="label.wiki.pages"></fmt:message>
		</div>
		<c:forEach var="wikiPage" items="${wikiPages}">
			<a href="javascript:changeWikiPage('${wikiPage.javaScriptTitle}')">
				${wikiPage.title}
				<c:if test="${wikiPage.title == mainWikiPage.title}">
					<fmt:message key="label.wiki.main"></fmt:message>
				</c:if>
			</a><br />
		</c:forEach>

		<!-- 
		<div id="finishButtonDiv">
			
			<c:choose>
				<c:when test="${contentEditable and minEditsReached}">
					<div class="space-bottom-top align-right" id="finishButtonDiv">
						<a href="javascript:submitWiki('finishActivity');" class="button"><fmt:message key="button.finish"></fmt:message></a>
					</div>
				</c:when>
			</c:choose>
			
			
			
		</div>
		-->
	</html:form>
		
	<c:if test="${mode == 'learner' || mode == 'author'}">
		<%@ include file="parts/finishButton.jsp"%>
	</c:if>
	
</div>

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
	
	displayMessageDiv();
	function displayMessageDiv()
	{
		var messageDiv = document.getElementById("messageDiv");

		if (trim(messageDiv.innerHTML) != "")
		{
			messageDiv.style.display="block";
		}
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
		document.getElementById("learningForm").submit();
	}
-->
</script>
