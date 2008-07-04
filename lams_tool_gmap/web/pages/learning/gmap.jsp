<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
<!--	
	function validateForm() {
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
		<html:hidden property="toolSessionID" styleId="toolSessionID"/>
		<html:hidden property="markersXML" value="" styleId="markersXML" />
		<p>
			${gmapDTO.instructions}
		</p>

		<c:set var="lrnForm" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

		<c:choose>
			<c:when test="${contentEditable}">
			
				<table style="cellpadding:0; cellspacing:0; border:0; width:500px;">
					<tr>
						<td width="80%">
						<div id="map_canvas" style="width:400px;height:300px;" ></div><fmt:message key="error.cantLoadMap"></fmt:message></td>
						<td width="10%">
						<div id="usersidebar" style="width:100px; overflow:auto;height:320px; background:WhiteSmoke; "></div>
						</td>
					</tr>
					<tr>
						<td>
						<a href="javascript:addMarkerToCenter()" class="button"/><fmt:message key="button.addMarker"/></a>
						<a href="javascript:fitMapMarkers()" class="button"/><fmt:message key="button.fitMarkers"/></a>
						</td>
					</tr>
				</table>

				
				
				<br><br>
				
				<input type="text" size="60" name="address" id="address" value="<fmt:message key="label.authoring.basic.sampleAddress"></fmt:message>" />
       			<a href="javascript:showAddress()" class="button"/><fmt:message key="button.go"/></a>
				

				<div class="space-bottom-top align-right">
					<html:submit styleClass="button" styleId="finishButton" onclick="javascript:serialiseMarkers();">
						<fmt:message>button.finish</fmt:message>
					</html:submit>
				</div>

			</c:when>

			<c:otherwise>
					<lams:out value="${lrnForm.entryText}" />
			</c:otherwise>
		</c:choose>
		
		

	</html:form>
</div>
