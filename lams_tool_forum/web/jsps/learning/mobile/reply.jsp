<%@ include file="/common/taglibs.jsp"%>

<div data-role="page" data-cache="false">

	<html:form action="/learning/replyTopic.do" styleId="topic-form"
			method="post" focus="message.subject" enctype="multipart/form-data">
			
		<html:hidden property="sessionMapID"/>
		<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
		<c:set var="sessionMapID" value="${formBean.sessionMapID}"/>
		<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
		<c:set var="originalMessage" value="${sessionMap.originalMessage}"/>

		<div data-role="header" data-theme="b" data-nobackbtn="true">
			<h1>
				<c:out value="${sessionMap.title}" escapeXml="true"/>
			</h1>
		</div><!-- /header -->
			
		<div data-role="content">
			<h3>
				<fmt:message key="title.original.message.reply" />	
		    </h3>
		
			<p><!--  Begins Original Message  -->
			<div>
			<table cellspacing="0" class="forum">
				<tr>
					<th class="ui-bar-c ui-corner-top">
						<c:out value='${originalMessage.message.subject}' escapeXml='true' />
					</th>
				</tr>
				<tr class="ui-btn-up-d">
					<td class="posted-by"><fmt:message key="lable.topic.subject.by" />
					<c:if test="${empty originalMessage.author}">
						<fmt:message key="label.default.user.name" />
					</c:if> <c:out value='${originalMessage.author}' /> - <lams:Date
						value="${originalMessage.message.updated}" /></td>
				</tr>
				<tr class="ui-btn-up-d">
					<td><c:out value="${originalMessage.message.body}"
						escapeXml="false" /></td>
				</tr>
				<tr class="ui-btn-up-d">
					<td>&nbsp;</td>
				</tr>				
			</table>
			</div>
			<!-- Ends Original Message -->
			</p>
			
			
			<h3>
				<fmt:message key="title.message.reply" />
			</h3>
			<html:errors property="error" />
						
			<%@ include file="/jsps/learning/mobile/message/topicreplyform.jsp"%>
			
		</div>
	</html:form>
	
	<div data-role="footer" data-theme="b">
		<h2>&nbsp;</h2>
	</div><!-- /footer -->

</div>

