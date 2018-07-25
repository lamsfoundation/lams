<%@ include file="/common/taglibs.jsp"%>


<c:set var="title" scope="request">
	<fmt:message key="message.endMeeting" />
</c:set>

<lams:Page type="learner" title="${title}">
	<h4><fmt:message key="message.endMeeting" /></h4>



<a href="#" class="button" onclick="window.close();"
	style="float: right"> <fmt:message key="button.close" /> </a> <br>

</lams:Page>