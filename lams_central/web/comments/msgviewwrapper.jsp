<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="isSticky" value="false"/> 
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<%-- Wraps up msgview.jsp for returning a single message - called when an edit is performed. It needs to do all the setup that topicview.jsp normally does. --%>

<script type="text/javascript">
	// The treetable code uses the clicks to expand and collapse the replies but we 
	// don't want to trigger based on the links/buttons. So stop the event propogating up the event chain. 
	$(".comment").click(function (e) {
		e.stopPropagation();
	});

    jQuery(document).ready(function() {
		jQuery("time.timeago").timeago();
    });
</script>

<c:forEach var="msgDto" items="${topicThread}">
	<c:set var="msgLevel" value="${msgDto.level}" />
	<%@ include file="msgview.jsp"%>
</c:forEach>
