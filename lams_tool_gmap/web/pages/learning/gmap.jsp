<%@ include file="/common/taglibs.jsp"%>
<c:set var="tool"><lams:WebAppURL /></c:set>
<c:set var="lrnForm" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<script type="text/javascript">
<!--	
	function validateForm() {
	}

	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}
	
	function refreshPage()
	{
		var url = "<lams:WebAppURL/>/learning.do?mode=${mode}&toolSessionID=${gmapSessionDTO.sessionID}";
		window.location = url;
	}
-->
</script>

<div id="content">
	<h1>
		<c:out value="${gmapDTO.title}" escapeXml="true"/>
	</h1>

	
	<p>
		<c:out value="${gmapDTO.instructions}" escapeXml="false"/>
	</p>

	<br />
	
	<table>
		<tr>
			<td>
				<div id="map_legend" style="width:100%;" >
					<iframe marginwidth="0" align="left" height="60px" width="100%" frameborder="0" src="${tool}/common/mapLegend.jsp"></iframe>
				</div>
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
				<a href="javascript:if(confirmLeavePage()){refreshPage();}" class="button"/><fmt:message key="button.refresh"/></a>
			</td>
		</tr>
	</table>
	<p />
	
	<input type="text" onkeypress="javascript:if (event.keyCode==13){showAddress();return false;}" size="55" name="address" id="address" value="${gmapDTO.defaultGeocoderAddress}" />
    			<a href="javascript:showAddress()" class="button"/><fmt:message key="button.go"/></a>
	
	
	<c:if test="${mode == 'learner' || mode == 'author'}">
		<%@ include file="parts/finishButton.jsp"%>
	</c:if>
	
</div>


