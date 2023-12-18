<%@ include file="/common/taglibs.jsp"%>

<div class="card-subheader">
	<fmt:message key="label.learning.tasks.to.do" />
</div>

<c:forEach var="itemDTO" items="${sessionMap.itemDTOs}" varStatus="status">
	<c:set var="item" value="${itemDTO.taskListItem}" />

	<c:if test="${itemDTO.allowedByParent}">

			<div class="card lcard">
				<div class="card-header">
					<span class="card-title collapsable-icon-left">
						<button type="button" class="btn btn-secondary-darker no-shadow <c:if test="${item.complete}">collapsed</c:if>" data-bs-toggle="collapse" data-bs-target="#collapse${item.uid}"
								aria-controls="collapse${item.uid}" >
							<c:out value="${item.title}" escapeXml="true" /> 
							
							<c:if test="${!item.createByAuthor && item.createBy != null}">
								<span class="badge text-bg-warning rounded-pill mx-2">
									[<c:out value="${item.createBy.firstName} ${item.createBy.lastName}" escapeXml="true" />]
								</span>
							</c:if>
						</button>
					</span>
					
					<div class="float-end">
						<c:if test="${item.required}">
							<span class="badge text-bg-danger p-2 me-2">
								<i class="fa-solid fa-asterisk" title="<fmt:message key='label.monitoring.tasksummary.task.required.to.finish'/>"></i>
							</span>
						</c:if>
							
						<c:choose>
							<c:when test="${item.complete}">
								<span class="text-bg-success p-2 shadow"> 
									<i class="fa-regular fa-square-check fa-xl" title='<fmt:message key="label.completed" />'></i>
								</span>
							</c:when>

							<c:when test="${(mode != 'teacher') && (not finishedLock) && (not taskList.sequentialOrder || itemDTO.previousTaskCompleted) 
									&& itemDTO.commentRequirementsMet && itemDTO.attachmentRequirementsMet}">
								<button type="button" onClick="javascript:completeItem(${item.uid})"
										class="complete-item-button btn btn-success no-shadow">
									<i class="fa-solid fa-pen-to-square fa-xl me-1"></i>
									<fmt:message key="label.mark.completed" />
								</button>
							</c:when>

							<c:otherwise>
								<span class="text-bg-danger p-1 rounded-pill">
									<i id="item-faminus-${item.uid}" class="fa fa-lg fa-minus" 
											title="<fmt:message key="label.complete.required.activities" />"
											data-waiting-for-comment="${(mode != 'teacher') && (not finishedLock) && (not taskList.sequentialOrder || itemDTO.previousTaskCompleted) 
											&& !itemDTO.commentRequirementsMet && itemDTO.attachmentRequirementsMet}"></i>
								</span>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				
				<div id="collapse${item.uid}" class="collapse <c:if test="${not item.complete}">show</c:if>">
					<div class="card-body">
						<!-- task details -->
						<%@ include file="/pages/learning/parts/itemdetails.jsp"%>
					</div>
				</div>
			</div>
	</c:if>
</c:forEach>
