<%@ include file="/common/taglibs.jsp"%>
<html:form action="/learning/createTopic.do" method="post" focus="message.subject" enctype="multipart/form-data">
	<html:hidden property="sessionMapID"/>
	<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
	<c:set var="sessionMapID" value="${formBean.sessionMapID}"/>
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
	
	<h1 class="no-tabs-below">
		${sessionMap.title}
	</h1>
	
	<div id="header-no-tabs-learner"></div>
	
	<div id="content-learner">
		
		<h2>
			<fmt:message key="title.message.edit" />
		</h2>
		
		<html:errors property="error" />
			<%@ include file="/jsps/learning/message/topicform.jsp"%>
	
	</div>

</html:form>
<div id="footer-learner"></div>
