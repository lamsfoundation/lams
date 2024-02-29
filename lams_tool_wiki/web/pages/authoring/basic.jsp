<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Basic Tab ========== -->

<div id="wikimenu" class="panel panel-default">
  <div class="panel-heading  panel-learner-title">
		<c:if test="${currentWikiPage.title != mainWikiPage.title}">
			<a href='javascript:changeWikiPage("${fn:escapeXml(mainWikiPage.javaScriptTitle)}")'>${fn:escapeXml(mainWikiPage.title)}</a> :
		</c:if> 
		<a href='javascript:changeWikiPage("${fn:escapeXml(currentWikiPage.javaScriptTitle)}")'>${fn:escapeXml(mainWikiPage.title)}</a>

    <!-- begin wiki menu-->
    <div class="btn-group pull-right">

		<a href="javascript:changeDiv('view');" title="<fmt:message key="label.wiki.view.toolTip"></fmt:message>" type="button" class="btn btn-xs btn-default">
			<i class="fa fa-xm fa-play"></i> <fmt:message key="label.wiki.view"></fmt:message>
		</a> 
		<c:if test="${not currentWikiPage.deleted}">
			<a href="javascript:changeDiv('edit');" title="<fmt:message key="label.wiki.edit.toolTip"></fmt:message>" type="button" class="btn btn-xs btn-default">
			<i class="fa fa-xm fa-pencil"></i> <fmt:message key="label.wiki.edit"></fmt:message>
			</a> 
		</c:if>
		<a href="javascript:cancelAdd();changeDiv('add');" title="<fmt:message key="label.wiki.add.toolTip"></fmt:message>" type="button" class="btn btn-xs btn-default">
			<i class="fa fa-xm fa-plus"></i> <fmt:message key="label.wiki.add"></fmt:message>
		</a> 
		<a href="javascript:changeDiv('history');" title="<fmt:message key="label.wiki.history.toolTip"></fmt:message>" type="button" class="btn btn-xs btn-default">
			<i class="fa fa-xm fa-history"></i> <fmt:message key="label.wiki.history"></fmt:message>
		</a>
		<c:if test="${currentWikiPage.title != mainWikiPage.title and not currentWikiPage.deleted}">
			<c:choose>
				<c:when test="${defineLater eq 'yes'}">
					<a href="javascript:doRemove('<fmt:message key="label.wiki.remove.mark.confirm"></fmt:message>')"
					   title="<fmt:message key="label.wiki.remove.mark.toolTip"></fmt:message>" type="button" class="btn btn-xs btn-default">
					</a>
				</c:when>
				<c:otherwise>
					<a href="javascript:doRemove('<fmt:message key="label.wiki.remove.confirm"></fmt:message>')"
					   title="<fmt:message key="label.wiki.remove.toolTip"></fmt:message>" type="button" class="btn btn-xs btn-default">
				</c:otherwise>
			</c:choose>
					<i class="fa fa-xm fa-trash"></i> <fmt:message key="label.wiki.remove"></fmt:message>
					</a>
		</c:if>
		<c:if test="${currentWikiPage.deleted}">
			<a href="javascript:submitWiki('restorePage')"
				title="<fmt:message key="label.wiki.restore.toolTip"></fmt:message>"
			>
				<i class="fa fa-xm fa-backward"></i> <fmt:message key="label.wiki.restore"></fmt:message>
			</a>	
		</c:if>
	</div> <!-- end wiki menu -->
	</div> <!--  end panel-heading -->
</div> <!--  end panel for overall heading & buttons -->

<div class="panel panel-default" id="view">
   <div class="panel-heading">
     <h4 class="panel-title"><c:out value="${currentWikiPage.title}" escapeXml="true"/></h4>
     <!-- Last edited -->
     <div class="voffset5" style="font-size: 12px">
     	<!-- Last edited -->
		<fmt:message key="label.wiki.last.edit">
			<fmt:param>
				<c:choose>
					<c:when test="${currentWikiPage.currentWikiContentDTO.editorDTO == null}">
						<fmt:message key="label.wiki.history.editor.author"></fmt:message>
					</c:when>
					<c:otherwise>
						<c:out value="${currentWikiPage.currentWikiContentDTO.editorDTO.getFullName()}" />
					</c:otherwise>
				</c:choose>
			</fmt:param>
            <fmt:param>
	            <lams:Date value="${currentWikiPage.currentWikiContentDTO.editDate}" timeago="true"/>
             </fmt:param>
		</fmt:message> 
		</div> <!-- End last edited -->
	</div>
    <div class="panel-body" id="viewBody">
		<c:out value="${currentWikiPage.currentWikiContentDTO.body}" escapeXml="false"/>
    </div>
</div>

<div class="panel panel-default" id="history" style="display: none">
  <div class="panel-heading">
    <h4 class="panel-title">
      <fmt:message key="label.wiki.history" /> - <c:out value="${currentWikiPage.title}" />
    </h4>
  </div>
  <div class="panel-body">
	<c:choose>
		<c:when test="${not empty wikiPageContentHistory}">
			<table class="table table-striped table-condensed">
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
								<lams:Date value="${wikiContentPageVersion.editDate}" timeago="true"/>
							</td>
							<td>
								<c:choose>
									<c:when test="${wikiContentPageVersion.editorDTO != null}">
										<c:out value="${wikiContentPageVersion.editorDTO.getFullName()}" escapeXml="true"/>
									</c:when>
									<c:otherwise>
										<fmt:message key="label.wiki.history.editor.author"></fmt:message>
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<a href="javascript:doRevert('${wikiContentPageVersion.uid}');" title="<fmt:message key="label.wiki.history.actions.compare.tooltip" />">
									<fmt:message key="label.wiki.history.actions.revert" /> 
								</a>
								&nbsp; 
								<a href="javascript:doCompareOrView('<lams:WebAppURL/>', '${wikiContentPageVersion.uid}', '${currentWikiPage.uid}', 'comparePage');" title="<fmt:message key="label.wiki.history.actions.compare.tooltip" />">
									<fmt:message key="label.wiki.history.actions.compare" /> 
								</a>
								&nbsp;
								<a href="javascript:doCompareOrView('<lams:WebAppURL/>', '${wikiContentPageVersion.uid}', '${currentWikiPage.uid}', 'viewPage');" title="<fmt:message key="label.wiki.history.actions.compare.tooltip" />">
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
</div>

<div class="panel panel-default" id="edit" style="display: none">
	<div class="panel-heading">
		<h4 clas="panel-title">
			<fmt:message key="label.wiki.edit"></fmt:message> -  <c:out value="${currentWikiPage.title}" escapeXml="true"/>
		</h4>
	</div>
	<div class="panel-body">
	  	<div class="form-group">
	  		<label for="title"><fmt:message key="label.authoring.basic.title"></fmt:message></label>
			<input type="text" name="title" id="title" style="width: 99%;" value="${currentWikiPage.title}" class="form-control"/>
			<span style="display: none;'" class="title error"><fmt:message key="error.title.invalid.characters"/>
		</div>
	  	<div class="form-group">
			<label for="wikiBody"><fmt:message key="label.wiki.body"></fmt:message></label>
			<lams:CKEditor id="wikiBody"
				value="${currentWikiPage.currentWikiContentDTO.body}"
				contentFolderID="${sessionMap.contentFolderID}"
				toolbarSet="CustomWiki" height="400px">
			</lams:CKEditor>
		</div>
		<div class="checkbox">
			<label for="isEditable">
				<form:checkbox path="isEditable" value="1" id="isEditable"/>
				<fmt:message key="label.authoring.basic.wikipagevisible"></fmt:message>
			</label>
		</div>
		<div class="pull-right">
		    <a href="javascript:changeDiv('view');" class="btn btn-default"><fmt:message key="button.cancel"></fmt:message></a>
			<a href="javascript:doEditOrAdd('editPage');" class="btn btn-primary"><fmt:message key="label.wiki.savechanges"></fmt:message></a>
		</div>
	</div>
</div>

<div class="panel panel-default" id="add" style="display: none">
  <div class="panel-heading">
    <h4 clas="panel-title">
      <fmt:message key="label.wiki.add"></fmt:message>
    </h4>
  </div>
  <div class="panel-body">
  	  	<div class="form-group">
	  		<label for="newPageTitle"><fmt:message key="label.authoring.basic.title"></fmt:message></label>
			<input type="text" name="newPageTitle" id="newPageTitle" style="width: 99%;" value="" class="form-control"/>
			<span style="display: none;'" class="newPageTitle error"><fmt:message key="error.title.invalid.characters"/>
		</div>
	  	<div class="form-group">
			<label for=newPageWikiBody><fmt:message key="label.wiki.body"></fmt:message></label>
			<lams:CKEditor id="newPageWikiBody" value="" height="400px"
				contentFolderID="${sessionMap.contentFolderID}"
				toolbarSet="CustomWiki">
			</lams:CKEditor>
		</div>
		<div class="checkbox">
			<label for=newPageIsEditable>
				<form:checkbox path="newPageIsEditable" value="1" id="newPageIsEditable"/>
				<fmt:message key="label.authoring.basic.wikipagevisible"></fmt:message>
			</label>
		</div>
		<div class="pull-right">
		    <a href="javascript:cancelAdd();changeDiv('view');" class="btn btn-default"><fmt:message key="button.cancel"></fmt:message></a>
			<a href="javascript:doEditOrAdd('addPage');" class="btn btn-primary"><fmt:message key="label.wiki.savechanges"></fmt:message></a>
		</div>
  </div>
</div>

<!-- Wiki pages folders -->
<h4>
  <fmt:message key="label.wiki.pages"></fmt:message>
</h4>
    
<i id="iconToggle" class="fa fa-sm fa-plus-square-o"  onclick="javascript:toggleWikiList('<lams:WebAppURL />')" aria-hidden="true"></i>
<a href="javascript:changeWikiPage('${fn:escapeXml(mainWikiPage.javaScriptTitle)}')">${fn:escapeXml(mainWikiPage.title)}</a>
<div id="wikiList" style="display:none;">
	<c:forEach var="wikiPage" items="${wikiPages}">
		<c:if test="${wikiPage.title != mainWikiPage.title}">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:changeWikiPage('${fn:escapeXml(wikiPage.javaScriptTitle)}')">${fn:escapeXml(wikiPage.title)}</a>
			<br />
		</c:if> 
	</c:forEach>
</div>                
<!-- end wiki pages folders -->

<script type="text/javascript">
<!--
	var wikiLinkArray = new Array();
	populateWikiLinkArray();
	
	
	function populateWikiLinkArray()
	{
		<c:forEach var="wikiPage" items="${wikiPages}">
			wikiLinkArray[wikiLinkArray.length] = '${fn:escapeXml(wikiPage.javaScriptTitle)}';
		</c:forEach>
		
		
	}
	
	function submitWiki(actionMethod)
	{
		document.forms.authoringForm.action=actionMethod+".do"; 
		replaceJavascriptTokenAndSubmit("authoringForm");
	}
	
	CKEDITOR.on('instanceCreated', function(editorInstance)
	{
		editorInstance.wikiLinkArray = wikiLinkArray;
	});

	function doEditOrAdd(actionMethod)
	{
		var title="";
		if(actionMethod == "editPage")
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
			alert("<fmt:message key='label.wiki.add.title.required'></fmt:message>");
			return;
		}
		
		for (i=0; i<wikiLinkArray.length; i++)
		{
			if(actionMethod == "editPage" && wikiLinkArray[i] == '${fn:escapeXml(currentWikiPage.javaScriptTitle)}')
			{
				continue;
			}
			
			if (trim(title) == wikiLinkArray[i])
			{
				alert("<fmt:message key='label.wiki.add.title.exists'><fmt:param>" + title + "</fmt:param></fmt:message>");
				return;
			}
		}
		
		// if all validation fulfilled, we can continue
		document.getElementById("title").value = trim(title);
		submitWiki(actionMethod);
	}

	
-->
</script>