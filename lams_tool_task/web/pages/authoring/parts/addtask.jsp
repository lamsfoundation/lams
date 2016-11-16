<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
	<lams:css />

	<script type="text/javascript">
		function uncheckIsFilesRequired() {
			document.getElementById("isFilesRequired").checked=false;
		}
			
		function uncheckIsCommentsRequired() {
			document.getElementById("isCommentsRequired").checked=false;
		}
	</script>

</lams:head>
<body>

	<div class="panel panel-default add-file">
		<div class="panel-heading panel-title">
			<fmt:message key="label.authoring.basic.add.task" />
		</div>
			
		<div class="panel-body">

			<%@ include file="/common/messages.jsp"%>

			<html:form action="/authoring/saveOrUpdateItem" method="post" styleId="taskListItemForm" focus="title" >
	
				<html:hidden property="sessionMapID" />
				<html:hidden property="itemIndex" />
				
				<html:hidden property="commentsFilesAllowed" value="${true}"/>
				<html:hidden property="showCommentsToAll" value="${true}"/>
				
				<div class="form-group">
	            	<label for="title"><fmt:message key="label.authoring.basic.resource.title.input" /></label>
	       			<html:text property="title" size="55" styleClass="form-control"/>
				</div>
	
				<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
				<c:set var="sessionMap"	value="${sessionScope[formBean.sessionMapID]}" />
	
				<div class="form-group">
					<label for="description"><fmt:message key="label.authoring.basic.resource.description.input" /></label>
					<lams:CKEditor id="description" value="${formBean.description}" contentFolderID="${sessionMap.taskListForm.contentFolderID}"/>
				</div>
	
				<div class="checkbox">
					<label for="isRequired">
					<html:checkbox property="required" styleId="isRequired">
					</html:checkbox>
					<fmt:message key="label.authoring.basic.task.isRequired" />
					</label>
				</div>
				
				<div class="checkbox">
					<label for="isCommentsAllowed">
					<html:checkbox property="commentsAllowed" styleId="isCommentsAllowed"
						onclick="uncheckIsCommentsRequired(); document.taskListItemForm.commentsRequired.disabled = !document.taskListItemForm.commentsRequired.disabled;">
					</html:checkbox>
					<fmt:message key="label.authoring.basic.task.isCommentsAllowed" />
					</label>
				</div>
						
				<div class="checkbox loffset10">
					<label for="isCommentsRequired">
					<html:checkbox property="commentsRequired" styleId="isCommentsRequired" 
						disabled="${not formBean.commentsAllowed}" >
					</html:checkbox>
					<fmt:message key="label.authoring.basic.task.isCommentsRequired" />
					</label>
				</div>
				
				<div class="checkbox">
					<label for="isFilesAllowed">
					<html:checkbox property="filesAllowed" styleId="isFilesAllowed"
						onclick="uncheckIsFilesRequired(); document.taskListItemForm.filesRequired.disabled = !document.taskListItemForm.filesRequired.disabled; ">
					</html:checkbox>
					<fmt:message key="label.authoring.basic.task.isFilesAllowed" />
					</label>
				</div>
						
				<div class="checkbox loffset10">
					<label for="isFilesRequired">
					<html:checkbox property="filesRequired" styleId="isFilesRequired"  disabled="${not formBean.filesAllowed}" >
					</html:checkbox>
					<fmt:message key="label.authoring.basic.task.isFilesRequired" />
					</label>
				</div>
				
				<div class="checkbox">
					<label for="isChildTask">
					<html:checkbox property="childTask" 
						disabled="${(fn:length(sessionMap.taskListList) == 0) || (fn:length(sessionMap.taskListList) == 1) && (formBean.itemIndex > -1)}"
						styleId="isChildTask" onclick="document.taskListItemForm.parentTaskName.disabled = !document.taskListItemForm.parentTaskName.disabled;">
					</html:checkbox>
					<fmt:message key="label.authoring.basic.task.isChildTask" />
					</label>
	
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<label  for="parentTaskName">
						<fmt:message key="label.authoring.basic.task.parent.task.name" />
					</label>
	
					<html:select property="parentTaskName" styleId="parentTaskName" disabled="${!formBean.childTask}" styleClass="form-control form-control-inline input-sm">
						<c:forEach var="taskListItem" items="${sessionMap.taskListList}">
							<c:choose>
								<c:when	test="${formBean.parentTaskName == taskListItem.title}">
									<option value="${fn:escapeXml(taskListItem.title)}" selected="true">
										<c:out value="${taskListItem.title}"/>
									</option>
								</c:when>
								<c:when	test="${formBean.title == taskListItem.title}">
								</c:when>
								<c:otherwise>
									<option value="${fn:escapeXml(taskListItem.title)}">
										<c:out value="${taskListItem.title}"/>
									</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</html:select>
				</div>
	
			</html:form>
	
			<a href="#" onclick="javascript:submitTask();" class="btn btn-default btn-sm pull-right">
				<i class="fa fa-plus"></i>&nbsp;<fmt:message key="button.add" /> 
			</a>
			<a href="#" onclick="javascript:hideMessage()" class="btn btn-default btn-sm pull-right roffset5">
				<fmt:message key="label.cancel" /> 
			</a>
			
		</div>
	</div>

</body>
</lams:html>
