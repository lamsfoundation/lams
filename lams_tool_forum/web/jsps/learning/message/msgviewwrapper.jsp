<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%-- Wraps up msgview.jsp for returning a single message - called when an edit is performed. It needs to do all the setup that topicview.jsp normally does. --%>

<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">

	// The treetable code uses the clicks to expand and collapse the replies but then 
	// the buttons will not work. So stop the event propogating up the event chain. 
	$(".button").click(function (e) {
		e.stopPropagation();
	});
	$(".rating-stars-div").click(function (e) {
		e.stopPropagation();
	});
	$("#attachments").click(function (e) {
    	e.stopPropagation();
	});


</script>

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:forEach var="msgDto" items="${topicThread}">

	<c:set var="msgLevel" value="${msgDto.level}" />
	<%@ include file="msgview.jsp"%>

</c:forEach>
