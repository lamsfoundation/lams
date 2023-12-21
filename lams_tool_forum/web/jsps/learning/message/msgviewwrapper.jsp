<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<%-- Wraps up msgview.jsp for returning a single message - called when an edit is performed. It needs to do all the setup that topicview.jsp normally does. --%>

<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/jquery.timeago.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>
<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery("time.timeago").timeago();
	});
</script>

<c:forEach var="msgDto" items="${topicThread}">
	<c:set var="msgLevel" value="${msgDto.level}" />
	<%@ include file="msgview.jsp"%>
</c:forEach>


