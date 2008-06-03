<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
<!--	
	function validateForm() {
	
		// Validates that there's input from the user. 
		
		// disables the Finish button to avoid double submittion 
		disableFinishButton();

 	<c:if test='${mode == "learner"}'>
		// if this is learner mode, then we add this validation see (LDEV-1319)
		
		if (document.learningForm.entryText.value == "") {
			
			// if the input is blank, then we further inquire to make sure it is correct
			if (confirm("\n<fmt:message>message.learner.blank.input</fmt:message>"))  {
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
		
	</c:if>
	
	}

	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}
-->
</script>

<div id="content">
	<h1>
		${gmapDTO.title}
	</h1>

	
	<html:form action="/learning" method="post" onsubmit="return validateForm();">
		<html:hidden property="dispatch" value="finishActivity" />
		<html:hidden property="toolSessionID" />

		<p>
			${gmapDTO.instructions}
		</p>

		<c:set var="lrnForm" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

		<c:choose>
			<c:when test="${contentEditable}">
				<table cellpadding='0' cellspacing='0' border='1'>
				<tr><td><div id="map_canvas" style="width: 400px; height: 300px;" ></div></td>
				<td><div id="sidebar" style="width:100px; 
										overflow:auto;
										height:320px; 
										background:WhiteSmoke; "></div></td></tr>
				</table>
				
				<a href="javascript:addMarkerToCenter()" class="button"/><fmt:message key="button.addMarker"/></a>
				<a href="javascript:fitMapMarkers()" class="button"/><fmt:message key="button.fitMarkers"/></a>
				<input type="button" onclick="test()"  value="Test" />
				
				<br><br>
				
				<input type="text" size="60" name="address" id="address" value="<fmt:message key="label.authoring.basic.sampleAddress"></fmt:message>" />
       			<a href="javascript:showAddress()" class="button"/><fmt:message key="button.go"/></a>
				

				<div class="space-bottom-top align-right">
					<html:submit styleClass="button" styleId="finishButton">
						<fmt:message>button.finish</fmt:message>
					</html:submit>
				</div>

			</c:when>

			<c:otherwise>
					<lams:out value="${lrnForm.entryText}" />
				</c:otherwise>
		</c:choose>
		
		<script type="text/javascript">
		<!--
			initLearnerGmap();
		//-->
		</script>
		
		
		   		
		<script type="text/javascript">
		<!--
			refreshSideBar();
		//-->
		</script>

	</html:form>
</div>
