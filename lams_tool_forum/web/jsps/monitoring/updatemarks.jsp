<%@ include file="/common/taglibs.jsp"%>

<html:form action="/monitoring/updateMark" method="post">
	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<c:set var="sessionMap" value="${sessionScope[formBean.sessionMapID]}" />
	<html:hidden property="sessionMapID"/>
	<html:hidden property="topicID"/>
	<html:hidden property="hideReflection" value="${sessionMap.hideReflection}"/>

	<p>
		<fmt:message key="message.assign.mark" />&nbsp;
		<c:out value="${user.firstName}" />&nbsp;
		<c:out value="${user.lastName}" />&nbsp;
		(<lams:Date value="${topic.message.updated}"/>):
	</p>		
	
	<p>
		<c:set var="viewtopic">
			<html:rewrite page="/monitoring/viewTopic.do?topicID=${topic.message.uid}&create=${topic.message.created.time}&hideReflection=${sessionMap.hideReflection}" />
		</c:set>
		<html:link href="javascript:launchPopup('${viewtopic}','viewtopic')">
			<c:out value="${topic.message.subject}" />
		</html:link>
		<c:if test="${topic.hasAttachment}">
			<img src="<html:rewrite page="/images/paperclip.gif"/>">
		</c:if>
	</p>
	
	<%@ include file="/common/messages.jsp"%>

	<div class="form-group">
		<label for="mark"><fmt:message key="lable.topic.title.mark" />*</label>
		<html:text property="mark" styleClass="form-control form-control-inline"/>
	</div>
	<div class="form-group">
		<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />		
		<label for="comment"><fmt:message key="lable.topic.title.comment" /></label>
		<lams:CKEditor id="comment"	value="${formBean.comment}" toolbarSet="DefaultMonitor">
		</lams:CKEditor>
	</div>


	<div class="pull-right">
		<input type="submit" value="<fmt:message key="lable.update.mark"/>" class="btn btn-default" />
					
		<c:if test="${sessionMap.updateMode == 'listMarks'}">
			<c:set var="cancelUrl">
				<c:url value="/monitoring/viewUserMark.do"/>?sessionMapID=${formBean.sessionMapID}&userUid=${user.uid}&toolSessionID=${sessionMap.toolSessionID}
			</c:set>
		</c:if>
					
		<c:if test="${sessionMap.updateMode == 'viewForum'}">
			<c:set var="cancelUrl">
					<c:url value="/learning/viewTopic.do"/>?sessionMapID=${formBean.sessionMapID}&topicID=${sessionMap.rootUid}&hideReflection=${sessionMap.hideReflection}
			</c:set>
		</c:if>
	
		<input type="button" onclick="location.href='${cancelUrl}';" class="btn btn-default" value="<fmt:message key="button.cancel"/>">
	</div>

</html:form>
