<!DOCTYPE html>

<%-- Wraps up msgview.jsp for returning a single message - called when an edit is performed. It needs to do all the setup that topicview.jsp normally does. --%>

<%@ include file="/common/taglibs.jsp"%>
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/jquery.timeago.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>
<script type="text/javascript">
	// The treetable code uses the clicks to expand and collapse the replies but then 
	// the buttons will not work. So stop the event propogating up the event chain. 
	$(".button").click(function (e) {
		e.stopPropagation();
	});
	$(".msg-footer").click(function (e) {
		e.stopPropagation();
	});
	$(".attachments").click(function (e) {
		e.stopPropagation();
	});
</script>

<c:forEach var="msgDto" items="${topicThread}">
	<c:set var="msgLevel" value="${msgDto.level}" />
	<%@ include file="msgview.jsp"%>
</c:forEach>

<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery("time.timeago").timeago();
	});
</script>
