<!DOCTYPE html>
            
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.wiki.util.WikiConstants"%>

<lams:html>

	<c:set var="lams"> <lams:LAMSURL /> </c:set>
	<c:set var="tool"> <lams:WebAppURL /> </c:set>
	
	<lams:head>
		<title>
			<fmt:message key="activity.title" />
		</title>
	
		<lams:headItems />
		<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>

		<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.timeago.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/portrait.js"></script>
		
		<script type="text/javascript" src="${tool}includes/javascript/monitoring.js">
		</script>
		
		<lams:JSImport src="includes/javascript/wikiCommon.js" relative="true" />
		
		<script type="text/javascript">
			$(document).ready(function() {$("time.timeago").timeago();});
		</script>

		
	</lams:head>
	
	<body class="stripes">
		<script type="text/javascript">
		$(document).ready(function(){
				initializePortraitPopover('<lams:LAMSURL />');
		});
		</script>
		
		<form:form id="monitoringForm" modelAttribute="monitoringForm" method="post" enctype="multipart/form-data">
		
		<form:hidden path="toolSessionID" />
		<form:hidden path="contentFolderID" />
		<!--<form:hidden path="mode"/>-->
		<input type="hidden" name="currentWikiPage" value="${currentWikiPage.uid}" id="currentWikiPage" />
		<input type="hidden" id="wikiLinks" />
		<form:hidden path="newPageName" id="newPageName" />
		<form:hidden path="historyPageContentId" id="historyPageContentId" />
		
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
			         <!-- Title & Last edited -->
		             <c:choose>
		               <c:when test="${currentWikiPage.currentWikiContentDTO.editorDTO == null}">
		                  <c:set var="lastEditName"><fmt:message key="label.wiki.history.editor.author"/></c:set>
		               </c:when>
		               <c:otherwise>
		                <c:set var="lastEditPortrait"><div class="pull-right"><lams:Portrait userId="${currentWikiPage.currentWikiContentDTO.editorDTO.userId}"/></div></c:set>
		                  <c:set var="lastEditName"><c:out value="${currentWikiPage.currentWikiContentDTO.editorDTO.firstName} ${currentWikiPage.currentWikiContentDTO.editorDTO.lastName}" escapeXml="true"/></c:set>
		               </c:otherwise>
		             </c:choose>
		             ${lastEditPortrait}
			         <h4 class="panel-title">${fn:escapeXml(currentWikiPage.title)}</h4>
			         <div class="voffset5" style="font-size: 12px">
			           <fmt:message key="label.wiki.last.edit">
			             <fmt:param>${lastEditName}</fmt:param>
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
			                                     	<lams:Portrait userId="${wikiContentPageVersion.editorDTO.userId}"/>
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
							<input type="text" class="form-control" name="title" id="title" style="width: 99%;" value="${currentWikiPage.title}"/>
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
							<form:checkbox path="isEditable" value="1" cssClass="noBorder" id="isEditable"/>
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
						<input type="text" class="form-control" name="newPageTitle" id="newPageTitle" style="width: 99%;" value=""/>
						<div class="field-name">
							<fmt:message key="label.wiki.body"></fmt:message>
						</div>
						<lams:CKEditor id="newPageWikiBody" value="" height="400px"
							contentFolderID="${contentFolderID}" toolbarSet="CustomWiki">
						</lams:CKEditor>
						<form:checkbox path="newPageIsEditable" cssClass="noBorder" value="1" id="newPageIsEditable"/>
							&nbsp;
						<fmt:message key="label.authoring.basic.wikipagevisible"></fmt:message>
						
							<a href="javascript:doEditOrAdd('addPage');" class="btn btn-primary pull-right voffset5"><fmt:message key="label.wiki.savechanges"></fmt:message></a>
							<a href="javascript:cancelAdd();changeDiv('view');" class="btn default pull-right voffset5"><fmt:message key="button.cancel"></fmt:message></a>
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
		
			<div id="finishButtonDiv"></div>
		
		</div></div></div></div></div>
		
		</form:form>
		
		<script type="text/javascript">
		
			var wikiLinkArray = new Array();
			populateWikiLinkArray();
			function populateWikiLinkArray() {
				<c:forEach var="wikiPage" items="${wikiPages}">
					wikiLinkArray[wikiLinkArray.length] = '${wikiPage.javaScriptTitle}';
				</c:forEach>
				document.getElementById("wikiLinks").value = wikiLinkArray.toString();
			}
			
			function doEditOrAdd(actionMethod) {
				var title="";
				if(actionMethod == "editPage") {
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
					if(actionMethod == "editPage" && wikiLinkArray[i] == '${currentWikiPage.javaScriptTitle}') {
						continue;
					}
					
					if (trim(title) == wikiLinkArray[i]) {
						alert("<fmt:message key='label.wiki.add.title.exists'><fmt:param>" + title + "</fmt:param></fmt:message>");
						return;
					}
				}
				
				// if all validation fulfilled, we can continue
				document.getElementById("title").value = trim(title);
				submitWiki(actionMethod);
			}

		    function submitWiki(actionMethod)
			{
			  		document.forms.monitoringForm.action=actionMethod+".do"; 
			  		replaceJavascriptTokenAndSubmit("monitoringForm");
			}
			
			CKEDITOR.on('instanceCreated',function (editorInstance) { 	
				editorInstance.wikiLinkArray = wikiLinkArray;
			});
			
			function refreshPage() {
				var url = "<lams:WebAppURL/>monitoring/showWiki.do?toolSessionID=${sessionDTO.sessionID}&currentWikiPageId=${currentWikiPage.uid}&contentFolderID=${contentFolderID}"
				window.location=url;
			}
			
		</script>

	</body>
</lams:html>



