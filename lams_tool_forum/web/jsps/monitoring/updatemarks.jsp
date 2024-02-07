<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[markForm.sessionMapID]}" />
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>

<c:set var="title"><fmt:message key="page.title.monitoring.edit.user.mark" /></c:set>
<lams:PageMonitor title="${title}" hideHeader="true">
	<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/jquery.timeago.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>			
	<script type="text/javascript">
		jQuery(document).ready(function() {
			jQuery("time.timeago").timeago();
		});
	</script>
	
	<h1>
		${title}
	</h1>

	<form:form action="updateMark.do" id="markForm" modelAttribute="markForm" method="post">
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
		<form:hidden path="sessionMapID"/>
		<form:hidden path="topicID"/>
		
		<p>
			<fmt:message key="message.assign.mark" />&nbsp;
			<span class="alert alert-warning">
				<c:out value="${user.firstName}" />&nbsp;
				<c:out value="${user.lastName}" />&nbsp;
				<lams:Date value="${topic.message.updated}" timeago="true"/>
			</span>
		</p>		
			
		<div class="clearfix mb-3">
			<c:out value="${topic.message.subject}" />
			<c:set var="viewtopic">
			    <lams:WebAppURL />learning/viewTopic.do?topicID=${topic.message.uid}&create=${topic.message.created.time}&sessionMapID=${markForm.sessionMapID}&mode=teacher
			</c:set>
			<button type="button" onclick="launchPopup('${viewtopic}','viewtopic')" class="btn btn-sm btn-light ms-2">
				<fmt:message key="page.title.monitoring.view.topic"/>
				<i class="fa-solid fa-arrow-up-right-from-square ms-2"></i>
			</button>
			
			<c:if test="${topic.hasAttachment}">
				<div>			
					<i class="fa fa-paperclip me-2 mt-2" title="<fmt:message key='message.label.attachment'/>"></i>
					<c:forEach var="file" items="${topic.message.attachments}">
						<span class="badge text-bg-warning bg-opacity-50 me-1 mt-3" title="<fmt:message key='message.label.attachment'/>">
							<fmt:message key='message.label.attachment'/>&nbsp;<c:out value="${file.fileName}" />
						</span>
					</c:forEach>
				</div>
			</c:if>
		</div>
		
		<lams:errors5/>
		
		<div class="mb-3">
			<label for="mark">
				<fmt:message key="lable.topic.title.mark" />*
			</label>
			<input type="text" name="mark" id="mark" value="${markForm.mark}" class="form-control form-control-inline"/>
		</div>
		<div class="mb-3">
			<label for="comment">
				<fmt:message key="lable.topic.title.comment" />
			</label>
			<lams:CKEditor id="comment"	value="${markForm.comment}" toolbarSet="DefaultMonitor">
			</lams:CKEditor>
		</div>
		
		<div class="activity-bottom-buttons">
			<button type="submit" class="btn btn-primary">
				<i class="fa-regular fa-circle-check me-1"></i>
				<fmt:message key="lable.update.mark"/>
			</button>
							
			<c:if test="${sessionMap.updateMode == 'listMarks'}">
				<c:set var="cancelUrl">
					<c:url value="/monitoring/viewUserMark.do"/>?sessionMapID=${markForm.sessionMapID}&userUid=${user.uid}&toolSessionID=${sessionMap.toolSessionID}
				</c:set>
			</c:if>
			<c:if test="${sessionMap.updateMode == 'viewForum'}">
				<c:set var="cancelUrl">
					<c:url value="/learning/viewTopic.do"/>?sessionMapID=${markForm.sessionMapID}&topicID=${sessionMap.rootUid}
				</c:set>
			</c:if>
			<button type="button" onclick="location.href='${cancelUrl}';" class="btn btn-secondary btn-icon-cancel me-2">
				<fmt:message key="button.cancel"/>
			</button>
		</div>
	</form:form>
</lams:PageMonitor>
