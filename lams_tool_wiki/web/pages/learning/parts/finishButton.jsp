<%@ include file="/common/taglibs.jsp"%>
  <script type="text/javascript">
    function disableFinishButton() {
      var finishButton = document.getElementById("finishButton");
      if (finishButton != null) {
        finishButton.disabled = true;
      }
    }

    function continueOrFinish(dispatch) {
      document.getElementById("learningButtonForm").action += "?dispatch=" + dispatch;
      document.getElementById("learningButtonForm").submit();
    }
  </script>

  <c:if test="${userDTO.finishedActivity and wikiDTO.reflectOnActivity}">
    <html:form action="/learning" method="get" styleId="reflectEditForm">
      <html:hidden property="dispatch" value="openNotebook" />
      <html:hidden property="mode" value="${mode}" />	
      <html:hidden property="toolSessionID" styleId="toolSessionID"/>
      <div class="panel panel-default voffset10">
        <div class="panel-heading">
          <h4 class="panel-title">
            <lams:out value="${wikiDTO.reflectInstructions}" escapeHtml="true"/>
          </h4>
        </div>
        <div class="panel-body">
          <c:choose>
            <c:when test="${not empty userDTO.notebookEntry}">
              <lams:out escapeHtml="true" value="${userDTO.notebookEntry}" />
            </c:when>

            <c:otherwise>
              <fmt:message key="message.no.reflection.available" />
            </c:otherwise>
          </c:choose>
          <hr class="mgs-hr"/>
          <html:submit styleClass="btn btn-primary">
            <fmt:message key="button.edit" />
          </html:submit>	
        </div>

      </div>

    </html:form>
  </c:if>

  <html:form action="/learning" method="post" onsubmit="disableFinishButton();" styleId="learningButtonForm">
    <html:hidden property="toolSessionID" styleId="toolSessionID"/>
    <html:hidden property="mode" value="${mode}" />	
    <div class="pull-right voffset5" id="finishButtonDiv">
      <c:choose>
        <c:when test="${!userDTO.finishedActivity and wikiDTO.reflectOnActivity}">

          <a href="javascript:continueOrFinish('openNotebook');" class="btn btn-primary"><fmt:message key="button.continue" /></a>

        </c:when>
        <c:otherwise>

          <div class="pull-right voffset5">
            <html:link href="#nogo" styleClass="btn btn-primary na" styleId="finishButton"
                       onclick="javascript:continueOrFinish('finishActivity'); return false">
              <c:choose>
                <c:when test="${activityPosition.last}">
                  <fmt:message key="button.submit" />
                </c:when>
                <c:otherwise>
                  <fmt:message key="button.finish" />
                </c:otherwise>
              </c:choose>
            </html:link>
          </div>
        </c:otherwise>
      </c:choose>
    </div>
  </html:form>
