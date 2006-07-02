<%@ include file="/includes/taglibs.jsp"%>

<h1 class="no-tabs-below">
	<fmt:message key="title.message.add"/>
</h1>

<div id="header-no-tabs-learner"></div>

<div id="content-learner">

	<html:errors property="error" />
	<html:form action="/learning/createTopic.do" method="post" focus="message.subject" enctype="multipart/form-data">
		<%@ include file="/jsps/learning/message/topicform.jsp"%>
	</html:form>

</div>

<div id="footer-learner"></div>
