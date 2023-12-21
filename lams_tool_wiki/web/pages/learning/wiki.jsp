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
		<lams:css/>
	
		<lams:JSImport src="includes/javascript/common.js" />
		<lams:JSImport src="includes/javascript/wikiCommon.js" relative="true" />
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
		
		<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	    <script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	    <script language="JavaScript" type="text/javascript" src="${tool}includes/javascript/validation.js"></script>
	    <script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/jquery.timeago.js"></script>
	    <c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
	    <script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>
	
	    <script type="text/javascript">
	      <!--
	        var mode = "${mode}";
	      var formName = "learningForm"
	
	   	  $(document).ready(function() {
	  			$("time.timeago").timeago();
	 		});
	      
	      function disableFinishButton() {
	        document.getElementById("finishButton").disabled = true;
	      }
	
	      function validateForm() {
	
	        // Validates that there's input from the user. 
	
	        // disables the Finish button to avoid double submission 
	        disableFinishButton();
	
	        if (mode == "learner") {
	          // if this is learner mode, then we add this validation see (LDEV-1319)
	
	          if (document.forms.learningForm.entryText.value == "") {
	
	            // if the input is blank, then we further inquire to make sure it is correct
	            if (confirm("<fmt:message>message.learner.blank.input</fmt:message>"))  {
	              // if correct, submit form
	              return true;
	            } else {
	              // otherwise, focus on the text area
	              document.forms.learningForm.entryText.focus();
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
	</lams:head>

	
	<body class="stripes">
	
 	<lams:Page type="learner" usePanel="false" formID="learningForm">
     
    <form:form action="learning.do" method="post" id="learningForm" modelAttribute="learningForm" enctype="multipart/form-data">

            <div class="panel panel-default">
              <div class="panel-heading panel-learner-title">
                <c:if test="${currentWikiPage.title != mainWikiPage.title}">
                  <a class="panel-title panel-learner-title float-start" href="javascript:changeWikiPage('${fn:escapeXml(mainWikiPage.javaScriptTitle)}')"> ${fn:escapeXml(mainWikiPage.title)}</a>&nbsp;/&nbsp;
                </c:if> 
                <a class="panel-title panel-learner-title"  href="javascript:changeWikiPage('${fn:escapeXml(currentWikiPage.javaScriptTitle)}')">${fn:escapeXml(currentWikiPage.title)}</a>


                <!-- Set up edit and add flags -->
                <c:set var="editableFlag" value="${currentWikiPage.editable and not maxEditsReached}" />
                <c:set var="addFlag" value="${wikiDTO.allowLearnerCreatePages and not maxEditsReached}" />

                <!-- begin wiki menu-->

                <div class="btn-group float-end">
                  <c:if test="${contentEditable}">

                    <a href="javascript:refreshPage();" title="<fmt:message key='label.wiki.refresh.toolTip'/>" type="button" class="btn btn-sm btn-secondary"><i class="fa fa-xm fa-refresh"></i> <fmt:message key="label.wiki.refresh"/></a>

                    <c:if test="${editableFlag or addFlag}">
                      <a href="javascript:changeDiv('view');" title="<fmt:message key='label.wiki.view.toolTip'/>" type="button" class="btn btn-sm btn-secondary"><i class="fa fa-xm fa-play"></i> <fmt:message key="label.wiki.view"/></a>
                    </c:if>

                    <c:if test="${editableFlag and not currentWikiPage.deleted}">
                      <a href="javascript:changeDiv('edit');" title="<fmt:message key='label.wiki.edit.toolTip'/>" type="button" class="btn btn-sm btn-secondary"><i class="fa fa-xm fa-pencil"></i> <fmt:message key="label.wiki.edit"/></a>
                    </c:if>

                    <c:if test="${addFlag}">
                      <a href="javascript:cancelAdd();changeDiv('add');" title="<fmt:message key='label.wiki.add.toolTip'/>" type="button" class="btn btn-sm btn-secondary"><i class="fa fa-xm fa-plus"></i> 
                        <fmt:message key="label.wiki.add"/></a>
                    </c:if>

                    <c:if test="${editableFlag}">
                      <a href="javascript:changeDiv('history');" title="<fmt:message key='label.wiki.history.toolTip'/>" 
                         type="button" class="btn btn-sm btn-secondary"><i class="fa fa-xm fa-history"></i> 
                        <fmt:message key="label.wiki.history"/>
                      </a>
                    </c:if>

                    <c:if test="${currentWikiPage.title != mainWikiPage.title and not currentWikiPage.deleted}">

                      <a type="button" class="btn btn-sm btn-secondary" href="javascript:doRemove('<fmt:message key="label.wiki.remove.mark.confirm"/>')"
                         title="<fmt:message key="label.wiki.remove.mark.toolTip"/>"
                         ><i class="fa fa-xm fa-trash"></i> 
                        <fmt:message key="label.wiki.remove"></fmt:message>
                      </a>
                    </c:if>
                    <c:if test="${currentWikiPage.deleted and editableFlag}">

                      <a type="button" class="btn btn-sm btn-secondary" href="javascript:submitWiki('restorePage')"
                         title="<fmt:message key="label.wiki.restore.toolTip"/>"
                         ><i class="fa fa-xm fa-backward"></i> 
                        <fmt:message key="label.wiki.restore"/>
                      </a>	
                    </c:if>                    

                  </c:if>

                </div> <!-- end wiki top menu -->               
              </div>
              <div class="panel-body">
              
                <!--
<div style="float: right; margin-left: 10px; position:relative; padding-top: 4px" class="help">
<lams:help toolSignature="<%=WikiConstants.TOOL_SIGNATURE%>" module="learning" />
</div>
-->
                <!-- Display the advanced option warnings -->
                    <c:if test="${wikiDTO.lockOnFinish and mode != 'teacher'}">
                      <lams:Alert type="info" close="true" id="lockWhenFinished">
                        <c:choose>
                          <c:when test="${finishedActivity}">
                            <fmt:message key="message.activityLocked" />
                          </c:when>
                          <c:otherwise>
                            <fmt:message key="message.warnLockOnFinish" />
                          </c:otherwise>
                        </c:choose>
                      </lams:Alert>
                    </c:if> 

                    <c:if test="${not (wikiDTO.lockOnFinish and finishedActivity) }">
                      <c:choose>
                        <c:when test="${currentWikiPage.editable}">
                          <c:if test="${not minEditsReached}">
                            <lams:Alert id="limitEdits" close="true" type="info">
                              <fmt:message key="message.minumumEditsNotReached">
                                <fmt:param>
                                  ${wikiDTO.minimumEdits}
                                </fmt:param>
                              </fmt:message>
                            </lams:Alert>
                          </c:if>

                          <c:if test="${wikiDTO.maximumEdits > 0}">
                            <lams:Alert id="maxEditsReached" close="true" type="info">
                              <c:choose>
                                <c:when test="${maxEditsReached}">
                                  <fmt:message key="message.maxEditsReached" />
                                </c:when>
                                <c:otherwise>
                                  <fmt:message key="message.warnMaxEdits">
                                    <fmt:param>
                                      ${editsLeft} 
                                    </fmt:param>
                                  </fmt:message>
                                </c:otherwise>
                              </c:choose>
                            </lams:Alert>
                          </c:if>
                        </c:when>
                        <c:otherwise>
                          <lams:Alert id="pageNotEditable" type="danger" close="false">
                            <fmt:message key="message.pageNotEditable" />
                          </lams:Alert>
                        </c:otherwise>

                      </c:choose>


                    </c:if>
                    <c:if test="${mode == 'learner' || mode == 'author'}">
                    <c:if test="${not empty submissionDeadline}">

			<lams:Alert id="submissionDeadline" type="info" close="true">
				<fmt:message key="authoring.info.teacher.set.restriction">
					<fmt:param>
						<lams:Date value="${submissionDeadline}" />
					</fmt:param>
				</fmt:message>
			</lams:Alert>
		    </c:if>
		     </c:if>

                    <c:if test="${currentWikiPage.deleted}">
                      <lams:Alert id="pageRemoved" type="danger" close="false">
                        <fmt:message key="label.wiki.removed" />
                      </lams:Alert>
                    </c:if>
                    
                <!-- end annoucements -->

                <!-- Wiki main -->
                <div class="row g-0">
                  <div class="col-12">

                    <div class="panel panel-default" id="view">
                      <div class="panel-heading">
                        <!-- Title and Last edited -->
                        <c:choose>
                            <c:when test="${currentWikiPage.currentWikiContentDTO.editorDTO == null}">
                                <c:set var="lastEditName"><fmt:message key="label.wiki.history.editor.author"/></c:set>
                            </c:when>
                            <c:otherwise>
                                <c:set var="lastEditPortrait"><div class="float-end"><lams:Portrait userId="${currentWikiPage.currentWikiContentDTO.editorDTO.userId}"/></div></c:set>
                                <c:set var="lastEditName"><c:out value="${currentWikiPage.currentWikiContentDTO.editorDTO.firstName} ${currentWikiPage.currentWikiContentDTO.editorDTO.lastName}" escapeXml="true"/></c:set>
                            </c:otherwise>
                        </c:choose>
                        ${lastEditPortrait}
                        <h4 class="panel-title">${fn:escapeXml(currentWikiPage.title)}</h4> 
                        <div class="mt-2" style="font-size: 12px">
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
                      <div class="panel-footer" style="font-size: 12px">
                        <i class="fa fa-envelope"></i> 
                        <fmt:message key="notify.learner.query">
                          <fmt:param>
                            <a href="javascript:submitWiki('toggleLearnerSubsciption');">
                              <c:choose>
                                <c:when test="${userDTO.notificationEnabled}">
                                  <fmt:message key="notify.learner.unsubscribe" />
                                </c:when>
                                <c:otherwise>
                                  <fmt:message key="notify.learner.subscribe" />
                                </c:otherwise>
                              </c:choose>
                            </a>
                          </fmt:param>
                        </fmt:message> 

                      </div>
                    </div>


                  </div>
                </div> <!-- end Wiki main content -->

                <!-- Begin form -->
                <form:hidden path="toolSessionID" id = "toolSessionID"  />
                <form:hidden path="mode" />
                <input type="hidden" name="userID" value="${userDTO.userId}"/>
                <input type="hidden" name="currentWikiPage" value="${currentWikiPage.uid}" id="currentWikiPage" />
                <input type="hidden" id="wikiLinks" />
                <form:hidden path="newPageName" id="newPageName" />
                <form:hidden path="historyPageContentId" id="historyPageContentId" />


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
                                <td>${wikiContentPageVersion.version}</td>

                                <td>
                                  <lams:Date value="${wikiContentPageVersion.editDate}" timeago="true"/>
                                </td>

                                <td>
                                  <c:choose>
                                    <c:when test="${wikiContentPageVersion.editorDTO != null}">
                                     	<lams:Portrait userId="${wikiContentPageVersion.editorDTO.userId}"/>
                                      <c:out value="${wikiContentPageVersion.editorDTO.firstName} ${wikiContentPageVersion.editorDTO.lastName}" escapeXml="true"/>
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

                          <input type="text" class="form-control" name="title" id="title" style="width: 99%;" value="${currentWikiPage.title}"/><span style="display: none;'" class="title error"><fmt:message key="error.title.invalid.characters"/>
                          </td>
                      </tr>
                      <tr>
                        <td>
                          <div class="field-name">
                            <fmt:message key="label.wiki.body"></fmt:message>
                          </div>
                          <lams:CKEditor id="wikiBody"
                                         value="${currentWikiPage.currentWikiContentDTO.body}"
                                         contentFolderID="${contentFolderID}" toolbarSet="CustomWikiLearner"
                                         height="400px">
                          </lams:CKEditor>
                        </td>
                      </tr>
                      <tr>
                        <td>
                          <div class="mt-2 float-end">
                            <a href="javascript:changeDiv('view');" class="btn btn-primary"><fmt:message key="button.cancel"/></a> 
                            <a href="javascript:doEditOrAdd('editPage');" class="btn btn-primary"><fmt:message key="label.wiki.savechanges"></fmt:message></a>
                          </div>
                        </td>
                      </tr>
                    </table>
                  </div>
                </div>

                <div id="add" style="display: none;">
                  <div class="row g-0">
                    <div class="col-12">

                      <div class="panel panel-default" id="view">
                        <div class="panel-heading">

                          <h4 class="panel-title">
                            <fmt:message key="label.wiki.add"></fmt:message>
                          </h4>
                        </div>
                        <div class="panel-body">
                          <div class="field-name">
                            <fmt:message key="label.authoring.basic.title"/>
                          </div>
                          <input type="text" name="newPageTitle" id="newPageTitle" class="form-control" value=""/><span style="display: none;'" class="newPageTitle error">
                          <fmt:message key="error.title.invalid.characters"/>
                          </span>

                          <div class="field-name">
                            <fmt:message key="label.wiki.body"></fmt:message>
                          </div>
                          <lams:CKEditor id="newPageWikiBody" value="" height="400px"
                                         contentFolderID="${contentFolderID}" toolbarSet="CustomWikiLearner">
                          </lams:CKEditor>

                          <a href="javascript:doEditOrAdd('addPage');" class="btn btn-primary float-end mt-2"><fmt:message key="label.wiki.savechanges"></fmt:message></a> 
                          <a href="javascript:cancelAdd();changeDiv('view');" class="btn btn-primary float-end mt-2 me-2"><fmt:message key="button.cancel"></fmt:message></a>

                        </div>
                      </div>



                    </div>
                  </div>
                </div>
                </form:form> 

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

              <c:if test="${ (mode == 'learner' && minEditsReached ) || mode == 'author'}">
                <%@ include file="parts/finishButton.jsp"%>
                  </c:if>

                </div>


            </div><!-- end panel body -->
          </div> <!-- end panel -->

	</lams:Page>

    <script type="text/javascript">
      <!--
        var wikiLinkArray = new Array();
      populateWikiLinkArray();
      function populateWikiLinkArray()
      {
        <c:forEach var="wikiPage" items="${wikiPages}">
          wikiLinkArray[wikiLinkArray.length] = '${fn:escapeXml(wikiPage.javaScriptTitle)}';
      </c:forEach>
      document.getElementById("wikiLinks").value = wikiLinkArray.toString();
      }

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

      function submitWiki(actionMethod)
	  	{
	  		document.forms.learningForm.action=actionMethod+".do"; 
	  		replaceJavascriptTokenAndSubmit("learningForm");
	  	}

      function hideToolbarItem(editorInstance, itemName) {

        editorInstance.ui.items[itemName].setState(CKEDITOR.TRISTATE_DISABLED);
        /*
			toolbarItem = editorInstance.EditorWindow.parent.FCKToolbarItems.LoadedItems[itemName]._UIButton.MainElement;
			toolbarItem.style.display = 'none';
		*/
      }

      CKEDITOR.on('instanceCreated',function (editorInstance) 
                  { 	
        if (!${wikiDTO.allowLearnerAttachImages}) {	
          hideToolbarItem(editorInstance, 'Image');
        }
        if (!${wikiDTO.allowLearnerInsertLinks}) {	
          hideToolbarItem(editorInstance, 'Link');
        }

        editorInstance.wikiLinkArray = wikiLinkArray;
      });

      function refreshPage()
      {
    	var toolSessionID = $("#toolSessionID").val();
        var url = "<lams:WebAppURL/>/learning/learning.do?mode=${mode}&toolSessionID="+toolSessionID+"&currentWikiPageId=${currentWikiPage.uid}"
        window.location=url;
      }

      -->
    </script>
    <script type="text/javascript">

      jQuery(document).ready(function() {
        jQuery("time.timeago").timeago();
      });
    </script>

		<div class="footer">
		</div>					
	</body>
</lams:html>