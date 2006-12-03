<%@ include file="/includes/taglibs.jsp"%>


<script type="text/javascript">
	function disableFinishButton() {
		var finishButton = document.getElementById("finishButton");
		if (finishButton != null) {
			finishButton.disabled = true;
		}
	}
</script>

<div id="content">

<h1><c:out value="${NbLearnerForm.title}" escapeXml="false" /></h1>

	<p>
		<c:out value="${NbLearnerForm.content}" escapeXml="false" />
	</p>
  	 
 	<html:form action="/learner" target="_self" onsubmit="disableFinishButton();"> 
		<html:hidden property="toolSessionID" />
		<html:hidden property="mode" />
  	 
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
		 
				<html:submit property="method" styleClass="button">
					<fmt:message key="button.edit" />
				</html:submit>
		    </div>
		</c:if>
	
		<c:if test="${!NbLearnerForm.readOnly}">
			<div class="space-bottom-top align-right">
				<c:choose>
					<c:when test="${reflectOnActivity}">
						<html:submit property="method" styleClass="button">
							<fmt:message key="button.continue" />
						</html:submit>
					</c:when>
					<c:otherwise>
						
						<html:hidden property="method" value="Finish"/>
							
						<html:submit styleClass="button" styleId="finishButton">
							<fmt:message key="button.finish" />
						</html:submit>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>
	</html:form>
</div>




