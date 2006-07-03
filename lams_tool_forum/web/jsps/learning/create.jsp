<%@ include file="/includes/taglibs.jsp"%>

<h1 class="no-tabs-below">
	${forum_title}
</h1>

<div id="header-no-tabs-learner"></div>

<div id="content-learner">
	
	<h2>
		<fmt:message key="title.message.edit" />
	</h2>
	
	<html:errors property="error" />
	<html:form action="/learning/createTopic.do" method="post" focus="message.subject" enctype="multipart/form-data">
		<%@ include file="/jsps/learning/message/topicform.jsp"%>
	</html:form>

</div>

<div id="footer-learner"></div>
