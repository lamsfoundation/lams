<%@ include file="/common/taglibs.jsp"%>

<%-- If you change this file, remember to update the copy made for CNG-28 --%>

<html:form action="/learning/replyTopic.do" onsubmit="return validateForm();" 
		focus="message.subject" enctype="multipart/form-data">
		
	<html:hidden property="sessionMapID"/>
	<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
	<c:set var="sessionMapID" value="${formBean.sessionMapID}"/>
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
	<c:set var="originalMessage" value="${sessionMap.originalMessage}"/>
	
	<div id="content">
	
		<h1>
			<c:out value="${sessionMap.title}" escapeXml="true"/>
		</h1>
	
		<h2>
			<fmt:message key="title.original.message.reply" />
	    </h2>

		<p><!--  Begins Original Message  -->
		
			<div>
				<table cellspacing="0" class="forum">
					<tr>
						<th>
							<c:out value='${originalMessage.message.subject}' escapeXml='true' />
						</th>
					</tr>
					<tr>
						<td class="posted-by">
							<fmt:message key="lable.topic.subject.by" />
							<c:if test="${empty originalMessage.author}">
								<fmt:message key="label.default.user.name" />
							</c:if> 
							<c:out value='${originalMessage.author}' /> - <lams:Date
								value="${originalMessage.message.updated}" />
						</td>
					</tr>
					<tr>
						<td>
							<c:out value="${originalMessage.message.body}" escapeXml="false" />
						</td>
					</tr>
			
				</table>
			</div>
		<!-- Ends Original Message -->
		</p>
	
		<h2>
			<fmt:message key="title.message.reply" />
		</h2>
		
		<html:errors property="error" />
				
		<%@ include file="/jsps/learning/message/topicreplyform.jsp"%>
	
	</div>
</html:form>
