<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">
<%@ include file="/common/taglibs.jsp"%>
<%--
<%@ page import="org.lamsfoundation.lams.contentrepository.client.IToolContentHandler"%>
<%@ page import="org.lamsfoundation.lams.tool.taskList.TaskListConstants"%>
 --%>
<script type="text/javascript">
    function doUpload() {
     	var myForm = $("reviewTaskForm");
       	myForm.action = "<c:url value='/reviewtask/uploadFile.do'/>";
       	myForm.submit();
    }
</script>

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
	
	<c:if test="${not empty param.sessionMapID}">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
	</c:if>
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
</lams:head>
	
<body class="stripes">
	
	<div id="content">
	

		<html:form action="reviewtask/addNewComment" method="post" styleId="reviewTaskForm" enctype="multipart/form-data">
			<html:hidden property="sessionMapID" />
			<html:hidden property="title" />
				

				<h1>${sessionMap.taskListItemTitle}</h1>

				<p>${sessionMap.taskListItemDescription}</p>


<%@ include file="topiclist.jsp"%>



<table class="forms">


	AAA<c:out value="${formBean}"></c:out>
	BBB<c:out value="${sessionMapID}"></c:out>
	CCC<c:out value="${sessionMap}"></c:out>

	<tr>
		<td>

		<hr />
		</td>
	</tr>


	<!-- Online Instructions -->
<%-- 	
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.authoring.online.instruction" />
			</div>
			<html:textarea property="taskList.onlineInstructions" rows="3" cols="75" />
		</td>
	</tr>
--%>	
	<tr>
		<td>
			<div>
				<c:set var="fileList" value="${sessionMap.taskListItemAttachmentList}" />

				<%-- Display target file type --%>
				<ul>
					<c:forEach var="file" items="${fileList}">
						<li>
							<c:out value="${file.fileName}" />
							
							<c:if test="${file.createBy != null}">
								[${file.createBy}]
							</c:if>
								
							<c:set var="downloadURL">
								<html:rewrite page="/download/?uuid=${file.fileUuid}&versionID=${file.fileVersionId}&preferDownload=true" />
							</c:set>
							<html:link href="${downloadURL}">
								<fmt:message key="label.download" />
							</html:link>
								
						</li>
					</c:forEach>
				</ul>
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<div class="field-name">
				<fmt:message key="label.preview.upload.file" />
			</div>
			<html:file property="uploadedFile">
				<fmt:message key="label.authoring.choosefile.button" />
			</html:file> 
			<a href="#" onclick="javascript:doUpload();" class="button">
				<fmt:message key="label.preview.upload.button" /> 
			</a>
		</td>
	</tr>

</table>
		</html:form>


		</div>
	</body>
</lams:html>
