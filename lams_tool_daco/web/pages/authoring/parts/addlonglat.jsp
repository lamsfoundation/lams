<!DOCTYPE html>
		

<%@ include file="/common/taglibs.jsp"%>
<%@page import="org.lamsfoundation.lams.tool.daco.DacoConstants;"%>

<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
	<lams:css />
	<!-- To use in external script files. -->
	<script type="text/javascript">
	   var msgShowAdditionalOptions = "<fmt:message key='label.authoring.basic.additionaloptions.show' />";
       var msgHideAdditionalOptions = "<fmt:message key='label.authoring.basic.additionaloptions.hide' />";
	   	//Initial behavior
	   	$(document).ready(function() {
	   		defaultShowAdditionaOptionsArea();
	   	});
	</script>
</lams:head>
<body class="tabpart">

<div class="panel panel-default">
<div class="panel-heading">
	<div class="panel-title"><fmt:message key="label.authoring.basic.longlat" /></div>
</div>

<div class="panel-body">

<!-- Add question form-->
<%@ include file="/common/messages.jsp"%>
<html:form action="/authoring/saveOrUpdateQuestion" method="post" styleId="dacoQuestionForm">
	<html:hidden property="sessionMapID" />
	<input type="hidden" id="questionType" name="questionType" value="10" />
	<html:hidden property="questionIndex" />
	<input type="hidden" id="longlatMapsSelected" name="longlatMapsSelected" />

	<p><fmt:message key="label.authoring.basic.longlat.help" /></p>

	<%@ include file="description.jsp"%>
  
	<!--  Options -->  
	<a href="javascript:toggleAdditionalOptionsArea()" class="fa-xs"><i id="faIcon" class="fa fa-plus-square-o"></i> <span id="toggleAdditionalOptionsAreaLink"><fmt:message key="label.authoring.basic.additionaloptions.show" /></span></a>
	<div id="additionalOptionsArea" style="display: none;" class="panel-body">
 		<div class="form-inline">
		<div class="checkbox">
		    <label>
	 	      <html:checkbox property="questionRequired" styleClass="input-sm form-control" styleId="questionRequired"/>&nbsp;<fmt:message key="label.authoring.basic.required" />
		    </label>
	  	</div>
	  	</div>
	</div>
 	<!--  end options -->

	<div class="voffset5">
	<form-group>
	<label for="longlatMaps"><fmt:message key="label.authoring.basic.longlat.maps" /></label>
	<select id="longlatMaps" name="longlatMaps" multiple="multiple" size="<%=DacoConstants.LONGLAT_MAPS_LIST.length %>" class="form-control">

		<c:forEach var="map" items="<%=DacoConstants.LONGLAT_MAPS_LIST %>">
			<option value="<c:out  value='${map}'/>"
				<c:forEach var="selectedMap" items="${longlatMapsSelected}">
					<c:if test="${selectedMap==map}">
						selected="selected"
					</c:if>
				</c:forEach>
			>${map}</option>
		</c:forEach>
	</select>
	</form-group>
	</div>

</html:form>

<c:set var="addButtonMessageKey" value="label.authoring.basic.longlat.add" />
<%@ include file="buttons.jsp"%>

</div>
</div>

</body>
</lams:html>
