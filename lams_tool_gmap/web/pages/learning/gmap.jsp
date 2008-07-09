<%@ include file="/common/taglibs.jsp"%>
<c:set var="tool"><lams:WebAppURL /></c:set>
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
		<html:hidden property="dispatch" styleId = "dispatch" value="finishActivity" />
		<html:hidden property="toolSessionID" styleId="toolSessionID"/>
		<html:hidden property="markersXML" value="" styleId="markersXML" />
		<html:hidden property="mode" value="${mode}" />
		<p>
			${gmapDTO.instructions}
		</p>

		<c:set var="lrnForm" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />

		<br />
		
		<table>
			<tr>
				<td>
					<iframe marginwidth="0" align="left" height="60px" width="100%" frameborder="0" src="${tool}/common/mapLegend.jsp"></iframe>
					<div id="map_canvas" style="float: left; width:80%; height:400px"><fmt:message key="error.cantLoadMap"></fmt:message></div>
					<div id="usersidebar" style="float: right; width:20%; overflow:auto; height:400px; background:WhiteSmoke; "></div>		
				</td>
			</tr>
			<tr>
				<td>	
					<c:choose>
						<c:when test="${contentEditable}">
					<a href="javascript:addMarkerToCenter()" class="button"/><fmt:message key="button.addMarker"/></a>
						</c:when>
					</c:choose>
					<a href="javascript:fitMapMarkers()" class="button"/><fmt:message key="button.fitMarkers"/></a>
				</td>
			</tr>
		</table>
		<p />
		
		<input type="text" onkeypress="javascript:if (event.keyCode==13){showAddress();return false;}" size="60" name="address" id="address" value="<fmt:message key="label.authoring.basic.sampleAddress"></fmt:message>" />
	    			<a href="javascript:showAddress()" class="button"/><fmt:message key="button.go"/></a>
		
		<div class="space-bottom-top align-right">
			<html:submit styleClass="button" styleId="finishButton" onclick="javascript:serialiseMarkers();document.getElementById('dispatch').value = 'saveMarkers';">
				<fmt:message>button.save</fmt:message>
			</html:submit>
			
			<html:submit styleClass="button" styleId="finishButton" onclick="javascript:serialiseMarkers();document.getElementById('dispatch').value = 'finishActivity';">
				<fmt:message>button.finish</fmt:message>
			</html:submit>
		</div>
	</html:form>
</div>


