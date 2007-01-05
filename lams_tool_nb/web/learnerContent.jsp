<%@ include file="/includes/taglibs.jsp"%>


<script type="text/javascript">
	function disableFinishButton() {
		var finishButton = document.getElementById("finishButton");
		if (finishButton != null) {
			finishButton.disabled = true;
		}
	}
	function submitForm(methodName){
		var f = document.getElementById('learnerForm');
		var m = document.getElementById('methodVar');
		m.value=methodName
		f.submit();
	}
</script>

<div id="content">

<h1><c:out value="${NbLearnerForm.title}" escapeXml="false" /></h1>

	<p>
		<c:out value="${NbLearnerForm.content}" escapeXml="false" />
	</p>
  	 
 	<html:form action="/learner" target="_self" onsubmit="disableFinishButton();" styleId="learnerForm"> 
		<html:hidden property="toolSessionID" />
		<html:hidden property="mode" />
		<input type="hidden" id="methodVar" name="method"">
  	 
		<c:if test="${userFinished and reflectOnActivity}">
			<div class="small-space-top">
		    	<h2>${reflectInstructions}</h2>
		 	
		 	    <c:choose>
		    	    <c:when test="${empty reflectEntry}">
		        	<p>
		            	<em>
		                	<fmt:message key="message.no.reflection.available" />
		                </em>
		            </p>
		            
		            </c:when>
		            
		            <c:otherwise>
		            	<p> <lams:out escapeXml="true" value="${reflectEntry}" />  </p>
					</c:otherwise>
		            
				</c:choose>

		    </div>
		</c:if>
	
		<div class="space-bottom-top align-right">
			<c:choose>
				<c:when test="${reflectOnActivity}">
					<html:button property="continueButton" styleClass="button" onclick="submitForm('reflect')">
						<fmt:message key="button.continue" />
					</html:button>
				</c:when>
				<c:otherwise>
					
						
					<html:button property="finishButton" styleClass="button" styleId="finishButton" onclick="submitForm('finish')">
						<fmt:message key="button.finish" />
					</html:button>
				</c:otherwise>
			</c:choose>
		</div>
	</html:form>
</div>




