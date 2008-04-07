<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css style="tabbed" />

	</lams:head>
	<body class="tabpart">
		<!-- Basic Info Form-->
		<%@ include file="/common/messages.jsp"%>
		<html:form action="/authoring/saveOrUpdateItem" method="post" styleId="taskListItemForm" focus="title" >

			<html:hidden property="sessionMapID" />
			<html:hidden property="itemIndex" />
			<h2 class="no-space-left">
				<fmt:message key="label.authoring.basic.add.task" />
			</h2>

			<div class="field-name">
            	<fmt:message key="label.authoring.basic.resource.title.input" />
			</div>

			<div class="small-space-bottom">
         			<html:text property="title" size="55"/>
			</div>

			<div class="field-name">
            	<fmt:message key="label.authoring.basic.resource.description.input" />
			</div>
			
			<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
			<c:set var="sessionMap"	value="${sessionScope[formBean.sessionMapID]}" />
			<div class="small-space-bottom" >
				<lams:FCKEditor id="description" value="${formBean.description}" contentFolderID="${sessionMap.taskListForm.contentFolderID}" />
			</div>

			<div class="space-top">
				<html:checkbox property="required" styleClass="noBorder"
					styleId="isRequired">
				</html:checkbox>

				<label for="isRequired">
					<fmt:message key="label.authoring.basic.task.isRequired" />
				</label>
			</div>
			
			<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />					
	        <c:set var="sessionMapID" value="${formBean.sessionMapID}" />				
		    <c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

			<div class="space-top">
				<html:checkbox property="commentsAllowed" styleClass="noBorder"	styleId="isCommentsAllowed"
					onclick="document.taskListItemForm.showCommentsToAll[0].disabled = !document.taskListItemForm.showCommentsToAll[0].disabled;
					document.taskListItemForm.showCommentsToAll[1].disabled = !document.taskListItemForm.showCommentsToAll[1].disabled">
				</html:checkbox>

				<label for="isCommentsAllowed">
					<fmt:message key="label.authoring.basic.task.isCommentsAllowed" />
				</label>
			</div>
					
			<div class="space-top">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	
				<input type="radio" name="showCommentsToAll" value="${false}" styleId="showToMonitoring"
					<c:if test="${not formBean.showCommentsToAll}">checked="checked"</c:if>
					<c:if test="${not formBean.commentsAllowed}">disabled="disabled"</c:if>/>
					
				<label for="showToMonitoring">
					<fmt:message key="label.authoring.basic.task.show.only.to.monitoring" />
				</label>
			</div>
			
			<div class="space-top">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	
				<input type="radio" name="showCommentsToAll" value="${true}" styleId="showToLearners"
					<c:if test="${formBean.showCommentsToAll}">checked="checked"</c:if>
					<c:if test="${not formBean.commentsAllowed}">disabled="disabled"</c:if>/>
					
				<label for="showToLearners">
					<fmt:message key="label.authoring.basic.task.show.to.all.learners" />
				</label>
			</div>
			
			<div class="space-top" ">
				<html:checkbox property="childTask" styleClass="noBorder" 
					disabled="${(fn:length(sessionMap.taskListList) == 0) || (fn:length(sessionMap.taskListList) == 1) && (formBean.itemIndex > -1)}"
					styleId="isChildTask" onclick="document.taskListItemForm.parentTaskName.disabled = !document.taskListItemForm.parentTaskName.disabled;">
				</html:checkbox>

				<label for="isChildTask">
					<fmt:message key="label.authoring.basic.task.isChildTask" />
				</label>

				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<label  for="parentTaskName">
					<fmt:message key="label.authoring.basic.task.parent.task.name" />
				</label>

				<html:select property="parentTaskName" styleId="parentTaskName" style="width:150px" disabled="${!formBean.childTask}" >
					<c:forEach var="taskListItem" items="${sessionMap.taskListList}">
						<c:choose>
							<c:when	test="${formBean.parentTaskName == taskListItem.title}">
								<option value="${taskListItem.title}" selected="true">
									${taskListItem.title}
								</option>
							</c:when>
							<c:when	test="${formBean.title == taskListItem.title}">
							</c:when>
							<c:otherwise>
								<option value="${taskListItem.title}">
									${taskListItem.title}
								</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</html:select>
			</div>

		</html:form>

		<lams:ImgButtonWrapper>
			<a href="#" onclick="taskListItemForm.submit();" class="button-add-item"><fmt:message
					key="button.add" /> </a>
			<a href="javascript:;" onclick="window.top.hideMessage();"
				class="button space-left"><fmt:message key="label.cancel" /> </a>
		</lams:ImgButtonWrapper>
	</body>
</lams:html>
