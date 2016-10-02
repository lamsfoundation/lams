<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.wiki.util.WikiConstants"%>

<html:form action="/monitoring" styleId="monitoringForm" method="post" enctype="multipart/form-data">

<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<html:hidden property="dispatch" styleId="dispatch" />
<html:hidden property="toolSessionID" />
<html:hidden property="contentFolderID" />
<!--<html:hidden property="mode"/>-->
<html:hidden property="currentWikiPage" value="${currentWikiPage.uid}" styleId="currentWikiPage" />
<input type="hidden" id="wikiLinks" />
<html:hidden property="newPageName" styleId="newPageName" />
<html:hidden property="historyPageContentId" styleId="historyPageContentId" />

<c:set var="title"><fmt:message key="activity.title" /></c:set>

<div class="container" id="content">
  <div class="row no-gutter">
    <div class="col-xs-12">
      <div class="panel panel-default">
		<!-- begin wiki main heading-->
        <div class="panel-heading  panel-learner-title">
			<c:if test="${isGroupedActivity}">
				${sessionDTO.sessionName}:
			</c:if>
			<c:if test="${currentWikiPage.title != mainWikiPage.title}">
				<a href="javascript:changeWikiPage('${mainWikiPage.javaScriptTitle}')">${mainWikiPage.title}</a> :
			</c:if> 
			<a href="javascript:changeWikiPage('${currentWikiPage.javaScriptTitle}')">${currentWikiPage.title}</a>

		    <div class="btn-group pull-right">
				<a href="javascript:refreshPage();" title="<fmt:message key="label.wiki.refresh.toolTip"></fmt:message>" type="button" class="btn btn-xs btn-default"><i class="fa fa-xm fa-refresh"></i> 
					<fmt:message key="label.wiki.refresh"></fmt:message>
				</a> 
				<a href="javascript:changeDiv('view');" title="<fmt:message key="label.wiki.view.toolTip"></fmt:message>" type="button" class="btn btn-xs btn-default"><i class="fa fa-xm fa-play"></i> 
					<fmt:message key="label.wiki.view"></fmt:message>
				</a> 
				<a href="javascript:changeDiv('edit');" title="<fmt:message key="label.wiki.edit.toolTip"></fmt:message>" type="button" class="btn btn-xs btn-default"><i class="fa fa-xm fa-pencil"></i> 
					<fmt:message key="label.wiki.edit"></fmt:message>
				</a> 
				<a href="javascript:cancelAdd();changeDiv('add');" title="<fmt:message key="label.wiki.add.toolTip"></fmt:message>" type="button" class="btn btn-xs btn-default"><i class="fa fa-xm fa-plus"></i> 
					<fmt:message key="label.wiki.add"></fmt:message>
				</a> 
				<a href="javascript:changeDiv('history');" title="<fmt:message key="label.wiki.history.toolTip"></fmt:message>" 
					type="button" class="btn btn-xs btn-default"><i class="fa fa-xm fa-history"></i> 
					<fmt:message key="label.wiki.history"></fmt:message>
				</a> 
				<c:if test="${currentWikiPage.title != mainWikiPage.title}">
					<a href="javascript:doRemove(&#39;<fmt:message key="label.wiki.remove.confirm"></fmt:message>&#39;)"
						title="<fmt:message key="label.wiki.remove.toolTip"></fmt:message>"
					 	type="button" class="btn btn-xs btn-default">
					 	<i class="fa fa-xm fa-trash"></i> <fmt:message key="label.wiki.remove"></fmt:message>
					</a>
				</c:if>
			</div>
		</div> <!--  end of wiki main heading -->
		<div class="panel-body">
		
		<!-- Wiki main -->
	     <div class="panel panel-default" id="view">
	       <div class="panel-heading">
	         <h4 class="panel-title">${fn:escapeXml(currentWikiPage.title)}</h4>
	         <!-- Last edited -->
	         <div class="voffset5" style="font-size: 12px">
	           <fmt:message key="label.wiki.last.edit">
	             <fmt:param>
	               <c:choose>
	                 <c:when test="${currentWikiPage.currentWikiContentDTO.editorDTO == null}">
	                   <fmt:message key="label.wiki.history.editor.author"/>
	                 </c:when>
	                 <c:otherwise>
	                   <c:out value="${currentWikiPage.currentWikiContentDTO.editorDTO.firstName} ${currentWikiPage.currentWikiContentDTO.editorDTO.lastName}" escapeXml="true"/>
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
		<!--  End of Wiki main -->

         <div class="panel panel-default" id="history" style="display: none">
           <div class="panel-heading">
             <h4 class="panel-title">
               <fmt:message key="label.wiki.history" /> - ${fn:escapeXml(currentWikiPage.title)}
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
											${wikiContentPageVersion.editorDTO.firstName} ${wikiContentPageVersion.editorDTO.lastName}
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
               <fmt:message key="label.wiki.edit"></fmt:message> - ${fn:escapeXml(currentWikiPage.title)}
             </h4>
           </div>
           <div class="panel-body">
             <table class="table table-striped table-condensed">
			<tr>
				<td>
					<div class="field-name">
						<fmt:message key="label.authoring.basic.title"></fmt:message>
					</div>
					<html:text styleClass="form-control" property="title" styleId="title" style="width: 99%;" value="${currentWikiPage.title}"></html:text>
				</td>
			</tr>
			<tr>
				<td>
					<div class="field-name">
						<fmt:message key="label.wiki.body"></fmt:message>
					</div>
					<lams:CKEditor id="wikiBody"
						value="${currentWikiPage.currentWikiContentDTO.body}"
						contentFolderID="${contentFolderID}" toolbarSet="CustomWiki"
						height="400px">
					</lams:CKEditor>
				</td>
			</tr>
			<tr>
				<td>
					<html:checkbox property="isEditable" value="1" styleClass="noBorder" styleId="isEditable"></html:checkbox>
					&nbsp;
					<fmt:message key="label.authoring.basic.wikipagevisible"></fmt:message>
				</td>
			</tr>
			<tr>
				<div class="voffset5 pull-right">
					<a href="javascript:doEditOrAdd('editPage');" class="btn btn-primary"><fmt:message key="label.wiki.savechanges"></fmt:message></a>
					<a href="javascript:changeDiv('view');" class="btn btn-default"><fmt:message key="button.cancel"></fmt:message></a>
				</td>
			</tr>
			</table>
		</div>
	</div>

     	<div class="panel panel-default" id="add"  style="display: none">
            <div class="panel-heading">
              <h4 class="panel-title">
                <fmt:message key="label.wiki.add"></fmt:message>
              </h4>
            </div>

			<div class="panel-body">
				<div class="field-name">
					<fmt:message key="label.authoring.basic.title"></fmt:message>
				</div>
				<html:text styleClass="form-control" property="newPageTitle" styleId="newPageTitle" style="width: 99%;" value=""></html:text>
				<div class="field-name">
					<fmt:message key="label.wiki.body"></fmt:message>
				</div>
				<lams:CKEditor id="newPageWikiBody" value="" height="400px"
					contentFolderID="${contentFolderID}" toolbarSet="CustomWiki">
				</lams:CKEditor>
				<html:checkbox property="newPageIsEditable" styleClass="noBorder" value="1" styleId="newPageIsEditable"></html:checkbox>
					&nbsp;
				<fmt:message key="label.authoring.basic.wikipagevisible"></fmt:message>
				
					<a href="javascript:doEditOrAdd('addPage');" class="class="btn btn-primary pull-right voffset5""><fmt:message key="label.wiki.savechanges"></fmt:message></a>
					<a href="javascript:cancelAdd();changeDiv('view');" class="class="btn default pull-right voffset5""><fmt:message key="button.cancel"></fmt:message></a>
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
	
	<!-- Reflections -->
	<c:if test="${not empty sessionDTO.userDTOs && sessionDTO.reflectOnActivity}">
     	<div class="panel panel-default voffset10" id="add">
            <div class="panel-heading">
              <h4 class="panel-title">
                <fmt:message key="monitor.notebooks"></fmt:message>
              </h4>
            </div>

			<table class="table table-condensed table-striped">
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
		</div>
	</c:if>

	<div id="finishButtonDiv"></div>

</div></div></div></div></div>

</html:form>

<script type="text/javascript">

	var wikiLinkArray = new Array();
	populateWikiLinkArray();
	function populateWikiLinkArray() {
		<c:forEach var="wikiPage" items="${wikiPages}">
			wikiLinkArray[wikiLinkArray.length] = '${wikiPage.javaScriptTitle}';
		</c:forEach>
		document.getElementById("wikiLinks").value = wikiLinkArray.toString();
	}
	
	function doEditOrAdd(dispatch) {
		var title="";
		if(dispatch == "editPage") {
			title = document.getElementById("title").value;
		}
		else {
			title = document.getElementById("newPageTitle").value;
		}
		
		var i;
		
		if (title == null || trim(title).length == 0) {
			alert("<fmt:message key='label.wiki.add.title.required'></fmt:message>");
			return;
		}
		
		for (i=0; i<wikiLinkArray.length; i++) {
			if(dispatch == "editPage" && wikiLinkArray[i] == '${currentWikiPage.javaScriptTitle}') {
				continue;
			}
			
			if (trim(title) == wikiLinkArray[i]) {
				alert("<fmt:message key='label.wiki.add.title.exists'><fmt:param>" + title + "</fmt:param></fmt:message>");
				return;
			}
		}
		
		// if all validation fulfilled, we can continue
		document.getElementById("title").value = trim(title);
		submitWiki(dispatch);
	}
	
	function submitWiki(dispatch) {
		document.getElementById("dispatch").value=dispatch;
		replaceJavascriptTokenAndSubmit("monitoringForm");
	}
	
	CKEDITOR.on('instanceCreated',function (editorInstance) { 	
		editorInstance.wikiLinkArray = wikiLinkArray;
	});
	
	function refreshPage() {
		var url = "<lams:WebAppURL/>/monitoring.do?dispatch=showWiki&toolSessionID=${sessionDTO.sessionID}&currentWikiPageId=${currentWikiPage.uid}&contentFolderID=${contentFolderID}"
		window.location=url;
	}
	
</script>
