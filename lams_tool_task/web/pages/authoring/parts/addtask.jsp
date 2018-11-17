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

			<lams:errors/>

			<form:form action="saveOrUpdateItem.do" modelAttribute="taskListItemForm" method="post" id="taskListItemForm" focus="title" >
				<form:hidden path="sessionMapID" />
				<form:hidden path="itemIndex" />
				
				<div class="form-group">
	            	<label for="title">
	            	<fmt:message key="label.authoring.basic.resource.title.input" />
	            	</label>
	       			<form:input path="title" size="55" cssClass="form-control"/>
				</div>
	
				<c:set var="sessionMap" value="${sessionScope[taskListItemForm.sessionMapID]}" />
	
				<div class="form-group">
					<label for="description"><fmt:message key="label.authoring.basic.resource.description.input" /></label>
					<lams:CKEditor id="description" value="${taskListItemForm.description}" contentFolderID="${sessionMap.taskListForm.contentFolderID}"/>
				</div>
	
				<div class="checkbox">
					<label for="isRequired">
					<form:checkbox path="required" id="isRequired"/>
					<fmt:message key="label.authoring.basic.task.isRequired" />
					</label>
				</div>
				
				<div class="checkbox">
					<label for="isCommentsAllowed">
					<form:checkbox path="commentsAllowed" id="isCommentsAllowed"
						onclick="uncheckIsCommentsRequired(); document.forms.taskListItemForm.commentsRequired.disabled = !document.forms.taskListItemForm.commentsRequired.disabled;"/>
					<fmt:message key="label.authoring.basic.task.isCommentsAllowed" />
					</label>
				</div>
						
				<div class="checkbox loffset10">
					<label for="isCommentsRequired">
					<form:checkbox path="commentsRequired" id="isCommentsRequired" 
						disabled="${not taskListItemForm.commentsAllowed}"/>
					<fmt:message key="label.authoring.basic.task.isCommentsRequired" />
					</label>
				</div>
				
				<div class="checkbox">
					<label for="isFilesAllowed">
					<form:checkbox path="filesAllowed" id="isFilesAllowed"
						onclick="uncheckIsFilesRequired(); document.forms.taskListItemForm.filesRequired.disabled = !document.forms.taskListItemForm.filesRequired.disabled; "/>
					<fmt:message key="label.authoring.basic.task.isFilesAllowed" />
					</label>
				</div>
						
				<div class="checkbox loffset10">
					<label for="isFilesRequired">
					<form:checkbox path="filesRequired" id="isFilesRequired"  disabled="${not taskListItemForm.filesAllowed}"/>
					<fmt:message key="label.authoring.basic.task.isFilesRequired" />
					</label>
				</div>
				
				<div class="checkbox">
					<label for="isChildTask">
					<form:checkbox path="childTask" 
						disabled="${(fn:length(sessionMap.taskListList) == 0) || (fn:length(sessionMap.taskListList) == 1) && (taskListItemForm.itemIndex > -1)}"
						id="isChildTask" onclick="document.forms.taskListItemForm.parentTaskName.disabled = !document.forms.taskListItemForm.parentTaskName.disabled;"/>
					<fmt:message key="label.authoring.basic.task.isChildTask" />
					</label>
	
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<label  for="parentTaskName">
						<fmt:message key="label.authoring.basic.task.parent.task.name" />
					</label>
	
					<form:select path="parentTaskName" id="parentTaskName" disabled="${!taskListForm.childTask}" cssClass="form-control form-control-inline input-sm">
						<c:forEach var="taskListItem" items="${sessionMap.taskListList}">
							<c:choose>
								<c:when	test="${taskListItemForm.parentTaskName == taskListItem.title}">
									<option value="${fn:escapeXml(taskListItem.title)}" selected="true">
										<c:out value="${taskListItem.title}"/>
									</option>
								</c:when>
								<c:when	test="${taskListItemForm.title == taskListItem.title}">
								</c:when>
								<c:otherwise>
									<option value="${fn:escapeXml(taskListItem.title)}">
										<c:out value="${taskListItem.title}"/>
									</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</form:select>
				</div>
	
			</form:form>
	
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
