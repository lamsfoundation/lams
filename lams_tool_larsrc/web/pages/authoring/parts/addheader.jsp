<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>

 	<!-- ********************  CSS ********************** -->
	<lams:css />

 	<!-- ********************  javascript ********************** -->
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.validate.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.tabcontroller.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/upload.js"></script>
	<script type="text/javascript" src="${tool}includes/javascript/rsrccommon.js"></script>

	<script type="text/javascript">
	   <%-- used by rsrcresourceitem.js --%>
		var removeInstructionUrl = "<c:url value='/authoring/removeInstruction.do'/>";
		var addInstructionUrl = "<c:url value='/authoring/newInstruction.do'/>";
       	var removeItemAttachmentUrl = "<c:url value='/authoring/removeItemAttachment.do'/>";
	</script>
