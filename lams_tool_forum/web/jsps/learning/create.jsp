<%@ include file="/includes/taglibs.jsp"%>

<html:errors property="error" />
<html:form action="/learning/createTopic.do" method="post" focus="message.subject" enctype="multipart/form-data">
	<%@ include file="/jsps/learning/message/topicform.jsp"%>
</html:form>
