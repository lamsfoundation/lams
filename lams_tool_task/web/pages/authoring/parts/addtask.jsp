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
			<input type="hidden" name="itemType" id="itemType" value="1" />
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

			<div class="space-top">
				<html:checkbox property="commentsAllowed" styleClass="noBorder"	styleId="isCommentsAllowed"
					onclick="document.taskListItemForm.selectedId[1].disabled = !document.taskListItemForm.selectedId[1].disabled;
					document.taskListItemForm.selectedId[0].disabled = !document.taskListItemForm.selectedId[0].disabled;">
				</html:checkbox>

				<label for="isCommentsAllowed">
					<fmt:message key="label.authoring.basic.task.isCommentsAllowed" />
				</label>
			</div>
					
			<div class="space-top">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	
				<input type="radio" name="selectedId" value="${false} }" 
					${1 == 2 ? "selected" : ""} styleId="showToMonitoring"/>
				<label for="showToMonitoring">
					<fmt:message key="label.authoring.basic.task.show.only.to.monitoring" />
				</label>
			</div>

			<div class="space-top">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	
				<input type="radio" name="selectedId" value="${true} }" 
					${1 == 2 ? "selected" : ""} styleId="showToLearners"  disabled = "${true}"/>
				<label for="showToLearners">
					<fmt:message key="label.authoring.basic.task.show.to.all.learners" />
				</label>
			</div>
					
			<div class="space-top" ">
				<html:checkbox property="childTask" styleClass="noBorder"
					styleId="isChildTask" onclick="document.taskListItemForm.parentTaskName.disabled = !document.taskListItemForm.parentTaskName.disabled">
				</html:checkbox>

				<label for="isChildTask">
					<fmt:message key="label.authoring.basic.task.isChildTask" />
				</label>

				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<label  for="parentTaskName">
					<fmt:message key="label.authoring.basic.task.parent.task.name" />
				</label>
				
				<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
				<html:select property="parentTaskName" styleId="parentTaskName" style="width:150px" disabled="${!formBean.childTask}" >
					
	                <c:set var="sessionMapID" value="${formBean.sessionMapID}" />				
				    <c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
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
