<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<title><fmt:message key="activity.title" /></title>
		<%@ include file="/common/header.jsp"%>
	</lams:head>
	<body class="stripes">
			
			<lams:Page type="monitor" title="${page.title.monitoring.edit.user.mark}">
            <c:set var="csrfToken"><csrf:token/></c:set>

			<form:form action="updateMark.do?${csrfToken}" id="markForm" modelAttribute="markForm" method="post">

			<c:set var="sessionMap" value="${sessionScope[markForm.sessionMapID]}" />
			<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>
		    <script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/jquery.timeago.js"></script>
		    <script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>
			<form:hidden path="sessionMapID"/>
			<form:hidden path="topicID"/>
			<input type="hidden" name="hideReflection" value="${sessionMap.hideReflection}"/>
		
			<p>
				<fmt:message key="message.assign.mark" />&nbsp;
				<c:out value="${user.firstName}" />&nbsp;
				<c:out value="${user.lastName}" />&nbsp;
				<lams:Date value="${topic.message.updated}" timeago="true"/>
			</p>		
			
			<p>
				<c:set var="viewtopic">
				    <lams:WebAppURL />learning/viewTopic.do?topicID=${topic.message.uid}&create=${topic.message.created.time}&sessionMapID=${markForm.sessionMapID}&hideReflection=${sessionMap.hideReflection}
				</c:set>
				<a href="javascript:launchPopup('${viewtopic}','viewtopic')">
					<c:out value="${topic.message.subject}" />
				</a>
				<c:if test="${topic.hasAttachment}">
					<i class="fa fa-paperclip loffset5" title="<fmt:message key='message.label.attachment'/>"></i>
				</c:if>
			</p>
			<lams:errors/>
		
			<div class="form-group">
				<label for="mark"><fmt:message key="lable.topic.title.mark" />*</label>
				<input type="text" name="mark" value="${markForm.mark}" class="form-control form-control-inline"/>
			</div>
			<div class="form-group">
				<label for="comment"><fmt:message key="lable.topic.title.comment" /></label>
				<lams:CKEditor id="comment"	value="${markForm.comment}" toolbarSet="DefaultMonitor">
				</lams:CKEditor>
			</div>
		
		
			<div class="pull-right">
				<input type="submit" value="<fmt:message key="lable.update.mark"/>" class="btn btn-default" />
							
				<c:if test="${sessionMap.updateMode == 'listMarks'}">
					<c:set var="cancelUrl">
						<c:url value="/monitoring/viewUserMark.do"/>?sessionMapID=${markForm.sessionMapID}&userUid=${user.uid}&toolSessionID=${sessionMap.toolSessionID}
					</c:set>
				</c:if>
							
				<c:if test="${sessionMap.updateMode == 'viewForum'}">
					<c:set var="cancelUrl">
							<c:url value="/learning/viewTopic.do"/>?sessionMapID=${markForm.sessionMapID}&topicID=${sessionMap.rootUid}&hideReflection=${sessionMap.hideReflection}
					</c:set>
				</c:if>
			
				<input type="button" onclick="location.href='${cancelUrl}';" class="btn btn-default" value="<fmt:message key="button.cancel"/>">
			</div>
			
			<script type="text/javascript">
			jQuery(document).ready(function() {
				jQuery("time.timeago").timeago();
			});
		</script>
		
		</form:form>
		

			<div id="footer">
			</div>

			</lams:Page>
	</body>
</lams:html>



