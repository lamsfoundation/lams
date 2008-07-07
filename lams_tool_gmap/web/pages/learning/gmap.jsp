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
				<br />
				
				<table>
					<tr>
						<td>
							<div id="map_canvas" style="float: left; width:80%; height:400px"><fmt:message key="error.cantLoadMap"></fmt:message></div>
							<div id="usersidebar" style="float: right; width:20%; overflow:auto; height:400px; background:WhiteSmoke; "></div>		
						</td>
					</tr>
					<tr>
						<td>	
							<a href="javascript:addMarkerToCenter()" class="button"/><fmt:message key="button.addMarker"/></a>
							<a href="javascript:fitMapMarkers()" class="button"/><fmt:message key="button.fitMarkers"/></a>
						</td>
					</tr>
				</table>
				<p />
				
				<input type="text" onkeypress="javascript:if (event.keyCode==13){showAddress();return false;}" size="60" name="address" id="address" value="<fmt:message key="label.authoring.basic.sampleAddress"></fmt:message>" />
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


