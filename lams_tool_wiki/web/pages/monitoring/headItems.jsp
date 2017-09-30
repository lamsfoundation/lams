<%@ include file="/common/taglibs.jsp"%>

<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>

<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/jquery.timeago.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/portrait.js"></script>

<script type="text/javascript" src="${tool}includes/javascript/monitoring.js">
</script>

<script type="text/javascript" src="<lams:WebAppURL />/includes/javascript/wikiCommon.js"></script>

<script type="text/javascript">
	$(document).ready(function() {$("time.timeago").timeago();});
</script>
