<%@ include file="/common/taglibs.jsp"%>

<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>

<script language="JavaScript" type="text/JavaScript">
	<!--
	-->		
</script>

<h4>
	<fmt:message key="label.learning.tasks.to.do" />
</h4>

<div class="panel-group" id="accordion">


	<c:forEach var="itemDTO" items="${sessionMap.itemDTOs}" varStatus="status">
		<c:set var="item" value="${itemDTO.taskListItem}" />

		<c:if test="${itemDTO.allowedByParent}">

			<div class="panel panel-default">
				<div class="panel-heading clearfix">
					<div class="panel-title pull-left" data-toggle="collapse" data-target="#collapse${item.uid}">
						<c:out value="${item.title}" escapeXml="true" /> 
						<c:if test="${!item.createByAuthor && item.createBy != null}">
								[<c:out value="${item.createBy.firstName} ${item.createBy.lastName}" escapeXml="true" />]
						</c:if>
						<c:if test="${item.required}">
							*
						</c:if>
						</a>
					</div>
					<div class="pull-right">
						<c:choose>
							<c:when test="${item.complete}">
								<i title="<fmt:message key='label.completed' />" class="fa fa-lg fa-check text-success"></i>
							</c:when>

							<c:when
								test="${(mode != 'teacher') && (not finishedLock) && (not taskList.sequentialOrder || itemDTO.previousTaskCompleted) 
									&& itemDTO.commentRequirementsMet && itemDTO.attachmentRequirementsMet}">
								<a href="javascript:;" onclick="return completeItem(${item.uid})"> <i class="fa fa-lg fa-square-o"></i>

								</a>
							</c:when>

							<c:otherwise>
								<i class="fa fa-lg fa-minus"></i>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				
				<div id="collapse${item.uid}" class="panel-collapse collapse <c:if test="${not item.complete}">in</c:if>">
					<div class="panel-body">
						<!-- tasks details -->
							<%@ include file="/pages/learning/parts/itemdetails.jsp"%>
						<!-- end task details -->
					</div>
				</div>
			</div>
		</c:if>
	</c:forEach>




	<c:if test="${fn:length(taskList.minimumNumberTasksErrorStr) > 0}">
		<p class="help-block">
			<c:out value="${taskList.minimumNumberTasksErrorStr}" />
		</p>
	</c:if>

</div>