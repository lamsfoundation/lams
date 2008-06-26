<%@ include file="/common/taglibs.jsp"%>

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<script type="text/javascript" src="${tool}includes/javascript/authoring.js"></script>

<!--  
<script type="text/javascript" src="${tool}includes/javascript/mapFunctions.js"></script>
-->

<%@ include file="/includes/jsp/mapFunctions.jsp"%>

<script type="text/javascript" src="${tool}includes/javascript/mapFunctionsAuthoring.js"></script>
<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAvPAE96y1iioFQOnrP1RCBxS3S_A0Q4kgEfsHF6TMv6-oezFszBTPVN72_75MGlxr3nP_6ixxWd30jw" type="text/javascript"></script>

<script type="text/javascript">
<!--
	//var webAppUrl = "${tool}";
	var errorMissingTitle = '<fmt:message key="error.missingMarkerTitle"/>';
	var confirmDelete = '<fmt:message key="label.authoring.basic.confirmDelete"/>';
	//var errorCantFindLocation = '<fmt:message key="error.cantFindAddress"/>';
	//var createdByMsg = '<fmt:message key="label.createdBy"/>';
	//var latitudeLongitudeMsg = '<fmt:message key="label.latitudeLongitude"/>';
	//var titleMsg = '<fmt:message key="label.authoring.basic.title"/>';
	//var newInfoWindowTextMsg = '<fmt:message key="label.newInfoWindowText"/>';
	//var editMsg = '<fmt:message key="button.edit"/>';
	//var saveMsg = '<fmt:message key="button.save"/>';
	//var removeMsg = '<fmt:message key="button.remove"/>';
	// var cancelMsg = '<fmt:message key="button.cancel"/>';
	var map;
	var markers;
	var geocoder = null;
	var currUser;
	var currUserId;
//-->
</script>

