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

  <form:form action="finishActivity.do" method="post" onsubmit="disableFinishButton();" modelAttribute="learningForm" id="learningForm">
    <form:hidden path="toolSessionID" id="toolSessionID"/>
    <form:hidden path="mode" value="${mode}" />	
    
    <div class="activity-bottom-buttons5" id="finishButtonDiv">
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
  </form:form>
