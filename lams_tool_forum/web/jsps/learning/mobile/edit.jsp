<%@ include file="/common/taglibs.jsp"%>

<div data-role="page" data-cache="false">

	<html:form action="/learning/updateTopic.do" focus="message.subject" enctype="multipart/form-data">
		<html:hidden property="sessionMapID"/>	
		<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
		<c:set var="sessionMapID" value="${formBean.sessionMapID}"/>
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>

		<div data-role="header" data-theme="b">
			<h1>
				<c:out value="${sessionMap.title}" escapeXml="true"/>
			</h1>
		</div><!-- /header -->
		
		<div data-role="content">
			<h3>
				<fmt:message key="title.message.edit" />
			</h3>
				
			<html:errors property="error" />
			
			<%@ include file="/jsps/learning/mobile/message/topiceditform.jsp"%>
		</div>
		
	</html:form>

	<div data-role="footer" data-theme="b">
		<h2>&nbsp;</h2>
	</div><!-- /footer -->

</div>




