<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<%@page import="org.lamsfoundation.lams.tool.daco.DacoConstants;"%>

<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
	<lams:css style="main" />
	<!-- To use in external script files. -->
	<script type="text/javascript">
	   var msgShowAdditionalOptions = "<fmt:message key='label.authoring.basic.additionaloptions.show' />";
       var msgHideAdditionalOptions = "<fmt:message key='label.authoring.basic.additionaloptions.hide' />";
	</script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/dacoAuthoring.js'/>"></script>

</lams:head>
<body class="tabpart">
<!-- Add question form-->
<%@ include file="/common/messages.jsp"%>
<html:form action="/authoring/saveOrUpdateQuestion" method="post" styleId="dacoQuestionForm">
	<html:hidden property="sessionMapID" />
	<input type="hidden" id="questionType" name="questionType" value="10" />
	<html:hidden property="questionIndex" />
	<input type="hidden" id="longlatMapsSelected" name="longlatMapsSelected" />

	<h2 class="no-space-left"><fmt:message key="label.authoring.basic.longlat" /></h2>
	<div><fmt:message key="label.authoring.basic.longlat.help" /></div>

	<div class="field-name space-top"><fmt:message key="label.authoring.basic.description" /></div>

	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />
	<lams:CKEditor id="description" value="${formBean.description}" 
		contentFolderID="${sessionMap.contentFolderID}"
                width="100%"
                resizeParentFrameName="questionInputArea">
	</lams:CKEditor>
	<div class="space-bottom-top"><a id="toggleAdditionalOptionsAreaLink" href="javascript:toggleAdditionalOptionsArea()"><fmt:message
		key="label.authoring.basic.additionaloptions.show" /> </a><br />
	</div>
	<div id="additionalOptionsArea" style="display: none;"><html:checkbox property="questionRequired"
		styleId="questionRequired" styleClass="noBorder">
		<fmt:message key="label.authoring.basic.required" />
	</html:checkbox></div>
	<div class="space-top" style="vertical-align: top">
	<div class="field-name space-right" style="float: left;"><fmt:message key="label.authoring.basic.longlat.maps" /></div>
	<select id="longlatMaps" name="longlatMaps" multiple="multiple" size="<%=DacoConstants.LONGLAT_MAPS_LIST.length %>">

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
	</div>


</html:form>
<lams:ImgButtonWrapper>
	<a href="#" onclick="javascript:submitDacoQuestion()" class="button-add-item"><fmt:message
		key="label.authoring.basic.longlat.add" /> </a>
	<a href="#" onclick="javascript:cancelDacoQuestion()" class="button space-left"><fmt:message key="label.common.cancel" /> </a>
</lams:ImgButtonWrapper>
</body>
</lams:html>
