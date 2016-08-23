<!DOCTYPE html>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>

<%-- Wraps up msgview.jsp for returning a single message - called when an edit is performed. It needs to do all the setup that topicview.jsp normally does. --%>

<script type="text/javascript">

	// The treetable code uses the clicks to expand and collapse the replies but we 
	// don't want to trigger based on the links/buttons. So stop the event propogating up the event chain. 
	$(".comment").click(function (e) {
		e.stopPropagation();
	});

</script>

<c:set var="isSticky" value="false"/> 

<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:forEach var="msgDto" items="${topicThread}">

	<c:set var="msgLevel" value="${msgDto.level}" />
	<%@ include file="msgview.jsp"%>

</c:forEach>

  <script type="text/javascript">
    jQuery(document).ready(function() {
      jQuery("time.timeago").timeago();
    });
  </script>
