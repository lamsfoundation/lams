<%@ include file="/common/taglibs.jsp"%>

	<lams:JSImport src="learning/includes/javascript/gate-check.js" />
	
	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${learningForm.toolSessionID}', '', function(){
			continueOrFinish('finishActivity');
		});
		
	    function disableFinishButton() {
	      var finishButton = document.getElementById("finishButton");
	      if (finishButton != null) {
	        finishButton.disabled = true;
	      }
	    }
	
	    function continueOrFinish(action) {
	      document.getElementById("learningForm").action = action + ".do";
	      document.getElementById("learningForm").submit();
	    }
	</script>

  <c:if test="${userDTO.finishedActivity and wikiDTO.reflectOnActivity}">
    <form:form action="openNotebook.do" method="get" id="learningForm" modelAttribute="learningForm">
      <form:hidden path="mode" value="${mode}" />	
      <form:hidden path="toolSessionID" id="toolSessionID"/>
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
          <input type="submit" class="btn btn-primary" value="<fmt:message key="button.edit" />"/>
        </div>

      </div>

    </form:form>
  </c:if>

  <form:form action="openNotebook.do" method="post" onsubmit="disableFinishButton();" modelAttribute="learningForm" id="learningForm">
    <form:hidden path="toolSessionID" id="toolSessionID"/>
    <form:hidden path="mode" value="${mode}" />	
    <div class="pull-right voffset5" id="finishButtonDiv">
      <c:choose>
        <c:when test="${!userDTO.finishedActivity and wikiDTO.reflectOnActivity}">

          <a href="javascript:continueOrFinish('openNotebook');" class="btn btn-primary"><fmt:message key="button.continue" /></a>

        </c:when>
        <c:otherwise>

          <div class="pull-right voffset5">
            <a href="#nogo" class="btn btn-primary na" id="finishButton">
              <c:choose>
                <c:when test="${isLastActivity}">
                  <fmt:message key="button.submit" />
                </c:when>
                <c:otherwise>
                  <fmt:message key="button.finish" />
                </c:otherwise>
              </c:choose>
            </a>
          </div>
        </c:otherwise>
      </c:choose>
    </div>
  </form:form>
