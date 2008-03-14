<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<title><fmt:message key="label.learning.title" /></title>
	<%@ include file="/common/header.jsp"%>
	
	<c:if test="${not empty param.sessionMapID}">
		<c:set var="sessionMapID" value="${param.sessionMapID}" />
	</c:if>
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	
	<script type="text/javascript">
    	function doUpload() {
     		var myForm = $("reviewTaskForm");
       		myForm.action = "<c:url value='/reviewtask/uploadFile.do'/>";
       		myForm.submit();
    	}
    	
    	 function addNewComment() {
     		var myForm = $("reviewTaskForm");
       		myForm.action = "<c:url value='/reviewtask/addNewComment.do'/>";
       		myForm.submit();
    	}
		
		function completeItem(itemUid){
			//learner and author(preview mode) will mark the finish
			if(${sessionMap.mode == "learner"} || ${sessionMap.mode == "author"}){
			   var reqIDVar = new Date();
			    window.parent.opener.location.href="<c:url value="/learning/completeItem.do"/>?sessionMapID=${sessionMapID}&itemUid=" + itemUid;
			}
		   	if(window.parent.opener != null) {
				window.parent.opener=null;
				window.parent.close();
			}
		}
		
	</script>
</lams:head>
	
<body class="stripes">
	
	<div id="content">
	
		<html:form action="reviewtask/addNewComment" method="post" styleId="reviewTaskForm" enctype="multipart/form-data">
			<html:hidden property="sessionMapID" />
				
			<h1>${sessionMap.taskListItemTitle}</h1>

			<p>${sessionMap.taskListItemDescription}</p>

			<c:if test="${sessionMap.taskListItem.commentsAllowed}">
				
				<!-- Comments Part -->
				<br/><br/>
				<%@ include file="commentlist.jsp"%>
				
				<table class="forms">
					
					<c:if test="${sessionMap.mode != 'teacher'}">
						<tr>
							<td>
								<div class="field-name">
									<fmt:message key="label.preview.leave.your.comment" />
								</div>
								<html:textarea property="comment" rows="3" style="width: 99%;"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<div class="right-buttons">
									<html:button property="newcomment"
										onclick="javascript:addNewComment();" styleClass="button">
										<fmt:message key="label.preview.post" />
									</html:button>
								</div> 
							</td>
						</tr>
					</c:if>
						
					<!-- Uploaded Files -->	
					<tr>
						<td>
							<%@ include file="filelist.jsp"%>
						</td>
					</tr>
					<c:if test="${sessionMap.mode != 'teacher'}">
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
					</c:if>
				
				</table>
			</c:if>

		</html:form>
		
		<br/><br/><br/>
		<div class="right-buttons">		
			<a href="javascript:;"
				onclick="return completeItem(${sessionMap.itemUid})" class="button"> <fmt:message key="label.completed" /> 
			</a>
		</div>
		<br/>

	</div>
</body>
</lams:html>
