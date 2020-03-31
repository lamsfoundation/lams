<%@ include file="/common/taglibs.jsp"%>

 <table id="lessons-table-${org.id}" class="lessons-table table cards dt-responsive <c:if test="${isUserMonitor}">user-monitor</c:if>" style="width:100%" 
 		data-orgid="${org.id}"
 		data-is-user-monitor="${isUserMonitor}"
 		data-row-reordering-enabled="${isRowReorderingEnabled}">
	<thead style="display:none;"></thead>
	<tbody>
	<c:forEach var="lesson" items="${org.lessons}" varStatus="i">
	<tr data-lessonid="${lesson.id}"
			<c:if test="${not empty lesson.url && !isUserMonitor}">
				onclick="<c:out value="${lesson.url}"/>"
				style="cursor:pointer;"
			</c:if>>

		<td>
			${i.index}
		</td>	
		<td>
			${lesson.startDate}
		</td>
		<td>
			${lesson.started}
		</td>
		<td>
			${lesson.completed}
		</td>
		<td id="favorite-lesson-td-${lesson.id}" style="display:none;"
			>${lesson.favorite}</td>
		
		<td class="lesson-image lesson-image-${lesson.id % 10 + 1}">
			<div class="lesson-name">
				<c:if test="${isFavouriteLessonEnabled}">
					<a href="#nogo" onclick="javascript:toggleFavoriteLesson(${org.id}, ${lesson.id});" class="tour-favorite-lesson">
						<c:choose>
							<c:when test="${lesson.favorite}">
								<i id="favorite-lesson-star-${lesson.id}" class="fa fa-star" title="<fmt:message key='label.remove.lesson.favorite'/>"></i>
							</c:when>
							<c:otherwise>
								<i id="favorite-lesson-star-${lesson.id}" class="fa fa-star-o" title="<fmt:message key='label.mark.lesson.favorite'/>"></i>
							</c:otherwise>
						</c:choose>
					</a>
				</c:if>
			
				<c:choose>
					<c:when test="${empty lesson.url}">
						<a class="text-danger"> 
							<c:out value="${lesson.name}" />
						</a>
					</c:when>
					<c:otherwise>
						<a href="<c:out value="${lesson.url}"/>"> 
							<c:out value="${lesson.name}" />
						</a>
					</c:otherwise>
				</c:choose>
				
				<c:if test="${lesson.state eq 4}">
					<i class="fa fa-minus-circle text-danger loffset5" title="<fmt:message key="label.disabled"/>"></i>
				</c:if>
				<c:if test="${lesson.state eq 6}">
					<i class="fa fa-archived text-danger loffset5" title="<fmt:message key="label.archived"/>"></i>
				</c:if>
				<c:if test="${lesson.dependent or lesson.scheduledFinish}">
					 <i class="fa fa-code-fork text-warning loffset5" title="<fmt:message key="index.conditions.flag.tooltip"/>"></i>
				</c:if>
				<c:if test="${lesson.completed}">
					<i class="fa fa-check-circle text-success loffset5" title="<fmt:message key="label.completed"/>"></i>
				</c:if>
			</div>
			
			<c:if test="${!isUserMonitor}">
				<div class="learner-bottom-card">
					<c:if test="${not empty lesson.startDate}">
						<lams:Date value="${lesson.startDate}" timeago="true"/>
					</c:if>
				
					<div class="progress">
			  			<div class="progress-bar" role="progressbar" style="width: ${lesson.progressAsLearner}%;" aria-valuenow="${lesson.progressAsLearner}" aria-valuemin="0" aria-valuemax="100">
			  				<span class="aaa">${lesson.progressAsLearner}% completed</span>
			  			</div>
					</div>
				</div>
			</c:if>
		</td>
		
		<td class="chart-td">
			<c:if test="${isUserMonitor}">
			<span class="learners-count text-secondary" title="<fmt:message key="lesson.ratio.learners.tooltip"/>">
				<i class="fa fa-users roffset5" title="<fmt:message key="lesson.ratio.learners.tooltip"/>"></i>
				${lesson.countAttemptedLearners}/${lesson.countTotalLearners}
			</span>	
			</c:if>
		</td>
	
		<td class="chart-td">
			<c:if test="${isUserMonitor}">
				<div class="chart-holder">
					<canvas class="chart-area" 
						data-count-completed-learners="${lesson.countCompletedLearners}"
						data-count-attempted-learners="${lesson.countAttemptedLearners}"
						data-count-not-started-learners="${lesson.countTotalLearners - lesson.countAttemptedLearners}">
					</canvas>
				</div>
			</c:if>
		</td>
	
		<!-- Buttons -->
	    <td class="buttons-td">
	    	<c:if test="${isUserMonitor}">
			<c:if test="${not empty lesson.auxiliaryLinks}">
				<div class="dropdown auxiliary-links-menu">
					<button class="btn btn-sm btn-light" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				    	<i class="fa fa-ellipsis-h" title="<fmt:message key="label.completed"/>"></i>
					</button>
					
					<div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenuButton">
						<c:forEach var="lessonlink" items="${lesson.auxiliaryLinks}">
							<a href="<c:out value='${lessonlink.url}'/>" style="padding-left: 5px; padding-right:5px;" class="dropdown-item"
								<c:if test="${not empty lessonlink.tooltip}">
									title="<fmt:message key='${lessonlink.tooltip}'/>"
								</c:if>>
								<i class="${lessonlink.style} lesson-action-label" title="<fmt:message key='${lessonlink.name}'/>"></i>
								<fmt:message key='${lessonlink.tooltip}'/>
							</a>
						</c:forEach>
				  	</div>
				</div>
			</c:if>

			<div class="lesson-actions" style="transform: rotate(180deg);">
			
				<span class="auxiliary-links">
					<c:forEach var="lessonlink" items="${lesson.auxiliaryLinks}">
						<a href="<c:out value='${lessonlink.url}'/>"  style="padding-left: 5px; padding-right:5px;"
							<c:if test="${not empty lessonlink.tooltip}">
								title="<fmt:message key='${lessonlink.tooltip}'/>"
							</c:if>>
							<i style="transform: rotate(-180deg);" class="${lessonlink.style} lesson-action-label" title="<fmt:message key='${lessonlink.name}'/>"></i>
						</a>
					</c:forEach>
				</span>
				
				<c:forEach var="lessonlink" items="${lesson.links}">
					<c:choose><c:when test="${addTourClass}"><c:set var="tourClass">class="tour-${lessonlink.id}"</c:set></c:when>
					<c:otherwise><c:set var="tourClass"></c:set></c:otherwise></c:choose>
					
					<a href="<c:out value='${lessonlink.url}'/>"  ${tourClass} style="padding-left: 5px; padding-right:5px;"
						<c:if test="${not empty lessonlink.tooltip}">
							title="<fmt:message key='${lessonlink.tooltip}'/>"
						</c:if>>
						<i style="transform: rotate(-180deg);" class="${lessonlink.style} lesson-action-label" title="<fmt:message key='${lessonlink.name}'/>"></i>
					</a>
				</c:forEach>
				
				<c:if test="${not empty lesson.requiredTasksUrl}">
					<a href="<c:out value='${lesson.requiredTasksUrl}'/>" style="padding-left: 5px; padding-right:5px;"
							title="<fmt:message key='${lessonlink.tooltip}'/>">
						<i style="transform: rotate(-180deg);" class="fa fa-fw fa-exclamation-triangle lesson-action-label" title="<fmt:message key='${lessonlink.name}'/>"></i>
					</a>
				</c:if>
			</div>
			<c:set var="addTourClass" value="false" />
			</c:if>
	    </td>

		<!-- order -->
		<td class="row-reorder text-center tour-sorting">
			<i class="fa fa-sort"></i>
		</td>	
	</tr>
	</c:forEach>
	</tbody>
</table>