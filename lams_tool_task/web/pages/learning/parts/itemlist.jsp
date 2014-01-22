<%@ include file="/common/taglibs.jsp"%>

<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
 
<script language="JavaScript" type="text/JavaScript">
	<!--
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
					<span id="icon${status.index}" class="toggling-icon" style="vertical-align:middle;"> </span>&nbsp;
					
					<c:choose>
						<c:when test="${taskList.sequentialOrder && !itemDTO.previousTaskCompleted}">
							<a class="toggling-link" id="${status.index}" href="#" style="color:#AAAAAA; font-style:italic">
						</c:when>
						<c:otherwise>
							<a class="toggling-link" id="${status.index}" href="#">
						</c:otherwise>
					</c:choose>
										
						${item.title} 
						<c:if test="${!item.createByAuthor && item.createBy != null}">
								[${item.createBy.loginName}]
						</c:if>
						<c:if test="${item.required}">
							*
						</c:if>
					</a>
					
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
							<%--<img src="<html:rewrite page='/includes/images/cross.gif'/>" border="0"> --%>
							<img src="<html:rewrite page='/includes/images/dash.gif'/>" border="0">
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</c:if>
		
	</c:forEach>
	
	<c:if test="${fn:length(taskList.minimumNumberTasksErrorStr) > 0}">
		<tr>
			<td colspan="3" align="left">
				<b>${taskList.minimumNumberTasksErrorStr}</b>
			</td>
		</tr>
	</c:if>
</table>
