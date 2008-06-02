<script language="JavaScript" type="text/javascript" src="/lams//includes/javascript/jquery-1.1.4.pack.js"></script>
<script language="JavaScript" type="text/javascript" src="/lams//includes/javascript/jquery.dimensions.pack.js"></script>
 
<script language="JavaScript" type="text/JavaScript">
	<!--
	    function doUpload(myForm, itemUid) {
     		
       		myForm.action = "<c:url value='/learning/uploadFile.do?sessionMapID=${sessionMapID}&mode=${mode}&itemUid='/>" + itemUid;
       		myForm.submit();
    	}
    	
    	 function addNewComment(myForm, itemUid) {
     		
       		myForm.action = "<c:url value='/learning/addNewComment.do?sessionMapID=${sessionMapID}&mode=${mode}&itemUid='/>" + itemUid;
       		myForm.submit();
    	}
	
		$(document).ready(function() {

			$("a.toggling-link").click(function() {
				executeToggling(this.id);
			});
				
			$("span.toggling-icon").click(function() {
				var id = this.id.substring(4,this.id.length);
				executeToggling(id);
			});
			
			$("a.toggling-link").each(function() {
				var id = this.id;
				var display = $("div#div" + id).css("display");
				if (display == "block") {
					var imagePath = "<html:rewrite page='/includes/images/tree_open.gif'/>";
					jQuery("span.toggling-icon#icon" + id).html("<img src='" + imagePath + "'/>");
				} else if (display == "none") {
					var imagePath = "<html:rewrite page='/includes/images/tree_closed.gif'/>";
					jQuery("span.toggling-icon#icon" + id).html("<img src='" + imagePath + "'/>");
				} else {
				}
			});
		});
		
		function executeToggling(id) {
			var display = $("div#div" + id).css("display");
			if (display == "none") {
				var imagePath = "<html:rewrite page='/includes/images/tree_open.gif'/>";
				jQuery("span.toggling-icon#icon" + id).html("<img src='" + imagePath + "'/>");
			} else if (display == "block") {
				var imagePath = "<html:rewrite page='/includes/images/tree_closed.gif'/>";
				jQuery("span.toggling-icon#icon" + id).html("<img src='" + imagePath + "'/>");
			} else {
			}
			
			$("div#div" + id).toggle("slow");
		}
			

	-->		
</script>

<table cellspacing="0" class="alternative-color">
	<tr>
		<th width="70%">
			<fmt:message key="label.learning.tasks.to.do" />
		</th>
		<th align="center">
			<fmt:message key="label.completed" />
		</th>
	</tr>
		
	<c:forEach var="itemDTO" items="${sessionMap.itemDTOs}" varStatus="status">
		<c:set var="item" value="${itemDTO.taskListItem}" />
				
		<c:if test="${itemDTO.allowedByParent}">
			<tr>
				<td>
					<span id="icon${status.index}" class="toggling-icon" style="vertical-align:middle;">
					</span>
					&nbsp;
					<a class="toggling-link" id="${status.index}" href="#">
						${item.title} 
						<c:if test="${!item.createByAuthor && item.createBy != null}">
								[${item.createBy.loginName}]
						</c:if>
						<c:if test="${item.required}">
							*
						</c:if>
					</a>
					
					<c:if test="${mode != 'teacher' && (!itemDTO.commentRequirementsMet || !itemDTO.attachmentRequirementsMet)}">
						<div class="info space-bottom">
							<c:choose>
								<c:when test="${!itemDTO.commentRequirementsMet && !itemDTO.attachmentRequirementsMet}">
									<fmt:message key="label.learning.info.comment.and.attachment.required" />		
								</c:when>
								<c:when test="${!itemDTO.commentRequirementsMet}">
									<fmt:message key="label.learning.info.post.comment.required" />		
								</c:when>							
								<c:when test="${!itemDTO.attachmentRequirementsMet}">
									<fmt:message key="label.learning.info.upload.attachment.required" />	
								</c:when>							
							</c:choose>
						</div>
					</c:if>
					
					<c:choose>
						<c:when test="${itemDTO.displayedOpen}">
							<c:set var="displayStyle" value="block" />
						</c:when>
						<c:otherwise>
							<c:set var="displayStyle" value="none" /> 
						</c:otherwise>
					</c:choose>
					
					<div id="div${status.index}" class="toggling-div" style="display:${displayStyle};">
						<%@ include file="/pages/learning/parts/itemdetails.jsp"%>
					</div>
					
				</td>

				<td align="center">
					<c:choose>
						<c:when test="${item.complete}">
							<img src="<html:rewrite page='/includes/images/tick.gif'/>"	border="0">
						</c:when>

						<c:when	test="${(mode != 'teacher') && (not finishedLock) && (not taskList.sequentialOrder || itemDTO.previousTaskCompleted) 
									&& itemDTO.commentRequirementsMet && itemDTO.attachmentRequirementsMet}">
							<a href="javascript:;" onclick="return completeItem(${item.uid})">
								<img src="<html:rewrite page='/includes/images/empty_checkbox.jpeg'/>" title="<fmt:message key='label.completed' />" border="0" > 
							</a>
						</c:when>
								
						<c:otherwise>
							<%--<fmt:message key="label.completed" /> --%>
							<img src="<html:rewrite page='/includes/images/cross.gif'/>" border="0"> 
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</c:if>
		
	</c:forEach>
	
	<c:if test="${taskList.numberTasksToComplete > 0}">
		<tr>
			<td colspan="3" align="left">
				<fmt:message key="label.learning.notification.you.must.complete.tasks.1" /> 
				<b>${taskList.numberTasksToComplete}</b>
				<fmt:message key="label.learning.notification.you.must.complete.tasks.2" />
			</td>
		</tr>
	</c:if>
</table>
