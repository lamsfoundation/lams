<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>

<link href="${lams}css/jquery-ui-bootstrap-theme.css" rel="stylesheet" type="text/css" />

<lams:JSImport src="includes/javascript/authoring.js" relative="true" />
<lams:JSImport src="includes/javascript/wikiCommon.js" relative="true" />
<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.timeago.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>

<script type="text/javascript">
	$(document).ready(function() {$("time.timeago").timeago();});
</script>