<%@ include file="/common/taglibs.jsp"%>
<c:set var="tool"><lams:WebAppURL /></c:set>
<c:set var="lrnForm" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
<script type="text/javascript">	
	function validateForm() {
	}

	function disableFinishButton() {
		document.getElementById("finishButton").disabled = true;
	}
	
	function refreshPage() {
		var url = "<lams:WebAppURL/>/learning.do?mode=${mode}&toolSessionID=${gmapSessionDTO.sessionID}";
		window.location = url;
	}
</script>

<lams:Page type="learner" title="${gmapDTO.title}">

	<div class="panel">
		<c:out value="${gmapDTO.instructions}" escapeXml="false" />
	</div>

	<!-- map UI -->
	<div class="panel">
     
		<%@ include file="../../common/mapLegend.jsp"%>

		<div class="container-fluid">
		<div class="row no-gutter">
			<div class="col-sm-9">
				<div id="map_canvas" style="height:400px"><fmt:message key="error.cantLoadMap"></fmt:message></div>
			</div>
			<div class="col-sm-3">
				<div id="usersidebar"></div>		
			</div>	
		</div>
		</div>
		
	<html:form action="/learning" method="post" styleId="learningForm">		
		<div class="btn-group-sm">
			<c:if test="${contentEditable}">
				<a href="javascript:addMarkerToCenter()" class="btn btn-default voffset5" role="button"/><fmt:message key="button.addMarker"/></a>
			</c:if>
			
			<a href="javascript:fitMapMarkers()" class="btn btn-default voffset5" role="button"/><fmt:message key="button.fitMarkers"/></a>
			<a href="javascript:if(confirmLeavePage()){refreshPage();}" class="btn btn-default voffset5" role="button"/><fmt:message key="button.refresh"/></a>
			<div class="btn-group-sm col-md-4 pull-right">
		        &nbsp;&nbsp;&nbsp;
				<html:hidden property="toolSessionID" styleId="toolSessionID"/>
		        <html:hidden property="markersXML" value="" styleId="markersXML" />
		        <html:hidden property="mode" value="${mode}" />	
				<html:submit styleClass="btn btn-primary voffset5" onclick="javascript:document.getElementById('dispatch').value = 'saveMarkers'; return serialiseMarkers();">
			           	<fmt:message>button.save</fmt:message>
		        </html:submit>
			</div>
		</div>
	
		<div class="voffset5">
			<input type="text" onkeypress="javascript:if (event.keyCode==13){showAddress();return false;}" size="55" name="address" id="address" value="${gmapDTO.defaultGeocoderAddress}" />
			<a href="javascript:showAddress()" class="btn btn-default btn-xs" role="button"><span class="fa fa-search" aria-hidden="true"></span></a>
		</div>

	</div>
	<!-- end map UI -->
	
		<c:if test="${mode == 'learner' || mode == 'author'}">
			<%@ include file="parts/finishButton.jsp"%>
		</c:if>
	 </html:form>
</lams:Page>
