<%@ include file="/common/taglibs.jsp"%>

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<script type="text/javascript" src="${tool}includes/javascript/authoring.js"></script>
<script type="text/javascript" src="${tool}includes/javascript/mapFunctions.js"></script>

<!--  
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery-latest.pack.js"></script>
<script src="http://gmaps-utility-library.googlecode.com/svn/trunk/markermanager/release/src/markermanager.js"></script>
-->

<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAvPAE96y1iioFQOnrP1RCBxS3S_A0Q4kgEfsHF6TMv6-oezFszBTPVN72_75MGlxr3nP_6ixxWd30jw" type="text/javascript"></script>

<script type="text/javascript">
<!--
	var webAppUrl = "${tool}";
	var errorMissingTitle = '<fmt:message key="error.missingMarkerTitle"/>';
	var errorCantFindLocation = '<fmt:message key="error.cantFindAddress"/>'
	var confirmDelete = '<fmt:message key="label.authoring.basic.confirmDelete"/>'
//-->
</script>
