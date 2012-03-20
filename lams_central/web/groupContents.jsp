<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<c:set var="hasLessonToSort">false</c:set>

<div class="j-course-contents">
		
	<div class="sequence-name">
		<div id="<c:out value="${orgBean.id}"/>-lessons" class="j-lessons">
		<c:if test="${allowSorting}">
			<div class="mycourses-right-buttons" style="display:none;">
				<a class="sorting" onclick="makeOrgSortable(<c:out value="${orgBean.id}"/>)" title="<fmt:message key="label.enable.lesson.sorting"/>">
					<img src="<lams:LAMSURL/>/images/sorting_disabled.gif">
				</a>
			</div>
		</c:if>
		<table class="lesson-table" style="table-layout: fixed">
			<c:forEach var="lesson" items="${orgBean.lessons}">
				<c:set var="hasLessonToSort">true</c:set>
			<tr id="<c:out value="${lesson.id}"/>" class="j-single-lesson">
					<td class="td-lesson" style="width: 25px;">
					<c:if test="${lesson.completed}"> <span class="mycourses-completed-img" title="<fmt:message key="label.completed"/>" >&nbsp;</span> </c:if>
                    </td>
					<td class="td-lesson" style="word-wrap: break-word;">
						<c:choose>
							<c:when test="${empty lesson.url}">
								<a class="disabled-sequence-name-link"> <c:out value="${lesson.name}" /></a> 
							</c:when>
							<c:otherwise>
								<a href="<c:out value="${lesson.url}"/>" class="sequence-name-link"> <c:out value="${lesson.name}" /></a> 
							</c:otherwise>
						</c:choose>
                        </td>
					<td class="td-lesson" style="width: 150px;">
						<c:if test="${lesson.state eq 4}"> <span class="mycourses-stop-img" title="<fmt:message key="label.disabled"/>" >&nbsp;</span> </c:if>
						<c:if test="${lesson.state eq 6}"> <span class="mycourses-stop-img" title="<fmt:message key="label.archived"/>" >&nbsp;</span> </c:if>
						<c:if test="${lesson.dependent or lesson.scheduledFinish}"> <span class="mycourses-conditions-img" title="<fmt:message key="index.conditions.flag.tooltip"/>" >&nbsp;</span> </c:if>
					</td>
					<c:choose>
						<c:when test="${empty lesson.links}">
							<td class="td-lesson" style="width: 220px;"></td>
						</c:when>
						<c:otherwise>
							<td class="split-menu-button" style="width: 220px;">
								<ul>
									<li>
										<c:forEach var="lessonlink" items="${lesson.links}" varStatus="status">
											<c:set var="tooltip" value="" />
											<c:if test="${lessonlink.tooltip ne null}">
												<c:set var="tooltip">
													<fmt:message key="${lessonlink.tooltip}" />
												</c:set>
											</c:if>
											
											<c:choose>
												<c:when test="${status.first}">
													<a href="#nogo" class="${lessonlink.style}">
						                        		<span onclick="<c:out value='${lessonlink.url}'/>" title="${tooltip}" 
						                        		class="${lessonlink.spanStyle}" style="margin-right:0px">
						                        			<fmt:message key="${lessonlink.name}" /> &nbsp;
						                        		</span>
														<em class="em-menu-button">
															<img src="<lams:LAMSURL/>/images/icons/bullet_arrow_down.png" alt="dropdown" style="border-style: none;">
														</em>
													</a>
													<ul class="button-menu">
												</c:when>
												<c:otherwise>
													<li>
														<a href="<c:out value='${lessonlink.url}'/>" class="${lessonlink.style}">
															<span class="${lessonlink.spanStyle}" title="${tooltip}"><fmt:message key="${lessonlink.name}" /> &nbsp;</span>
														</a>
													</li>
													<c:if test="${status.last}">
														</ul>
													</c:if>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</li>
								</ul>
			        		</td>
						</c:otherwise>
					</c:choose>
				</tr>
			</c:forEach>
		</table>
		</div>
	</div>
		
	<c:forEach var="childOrg" items="${orgBean.childIndexOrgBeans}">
	<div class="group-name">
		<p>
			<c:out value="${childOrg.name}" />
			<c:if test="${not empty childOrg.archivedDate}">
				<small>(<fmt:message key="label.archived"/> <lams:Date value="${childOrg.archivedDate}"/>)</small>
			</c:if>
			<c:forEach var="childlink" items="${childOrg.links}">
				<a href="<c:out value='${childlink.url}'/>" class="sequence-action-link thickbox<c:out value="${orgBean.id}"/>"> 
					<span class="mycourses-addlesson-img" >
						<fmt:message key="${childlink.name}" />
					</span>
				</a>
			</c:forEach>
			<c:forEach var="childlink" items="${childOrg.moreLinks}">
				<a href="${childlink.url}" class="sequence-action-link" title="${childlink.tooltip}"> 
					<span class="${childlink.style}" >
						<fmt:message key="${childlink.name}" />
					</span>
				</a>
			</c:forEach>    
		</p>
		
		<div class="sequence-name">
			<div id="<c:out value="${childOrg.id}"/>" class="j-subgroup-lessons">
			<c:forEach var="childLesson" items="${childOrg.lessons}">
				<c:set var="hasLessonToSort">true</c:set>
				<table class="lesson-table" style="table-layout: fixed">
					<tr id="<c:out value="${childLesson.id}"/>" class="j-single-subgroup-lesson">
						<td class="td-lesson" style="width: 25px;">
							<c:if test="${childLesson.completed}"> <span class="mycourses-completed-img" title="<fmt:message key="label.completed"/>" >&nbsp;</span> </c:if>
	                    </td>
						<td class="td-lesson" style="word-wrap: break-word;">
							<c:choose>
								<c:when test="${empty childLesson.url}">
									<a class="disabled-sequence-name-link"> <c:out value="${childLesson.name}" /></a> 
								</c:when>
								<c:otherwise>
									<a href="<c:out value="${childLesson.url}"/>" class="sequence-name-link"> <c:out value="${childLesson.name}" /></a> 
								</c:otherwise>
							</c:choose>
						</td>
						<td class="td-lesson" style="width: 150px;">
							<c:if test="${childLesson.state eq 4}"> <span class="mycourses-stop-img" title="<fmt:message key="label.disabled"/>" >&nbsp;</span> </c:if>
							<c:if test="${childLesson.state eq 6}"> <span class="mycourses-stop-img" title="<fmt:message key="label.archived"/>" >&nbsp;</span> </c:if>
							<c:if test="${childLesson.dependent or childLesson.scheduledFinish}"> <span class="mycourses-conditions-img" title="<fmt:message key="index.conditions.flag.tooltip"/>" >&nbsp;</span> </c:if>
						</td>
						<c:choose>
							<c:when test="${empty childLesson.links}">
							<td class="td-lesson" style="width: 220px;"></td>
							</c:when>
							<c:otherwise>
								<td class="split-menu-button" style="width: 220px;">
									<ul>
										<li>
											<c:forEach var="childLessonlink" items="${childLesson.links}" varStatus="status">
												<c:set var="tooltip" value="" />
												<c:if test="${childLessonlink.tooltip ne null}">
													<c:set var="tooltip">
														<fmt:message key="${childLessonlink.tooltip}" />
													</c:set>
												</c:if>
												
												<c:choose>
													<c:when test="${status.first}">
														<a href="#nogo" class="${childLessonlink.style}">
							                        		<span onclick="<c:out value='${childLessonlink.url}'/>" title="${tooltip}" 
							                        		class="${childLessonlink.spanStyle}" style="margin-right:0px">
							                        			<fmt:message key="${childLessonlink.name}" /> &nbsp;
							                        		</span>
															<em class="em-menu-button">
																<img src="<lams:LAMSURL/>/images/icons/bullet_arrow_down.png" alt="dropdown">
															</em>
														</a>
														<ul class="button-menu">
													</c:when>
													<c:otherwise>
														<li>
															<a href="<c:out value='${childLessonlink.url}'/>" class="${childLessonlink.style}">
																<span class="${childLessonlink.spanStyle}" title="${tooltip}"><fmt:message key="${childLessonlink.name}" /> &nbsp;</span>
															</a>
														</li>
														<c:if test="${status.last}">
															</ul>
														</c:if>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</li>
									</ul>
			        			</td>
		   					</c:otherwise>
						</c:choose>
					</tr>
				</table>
			</c:forEach></div>
		</div>
	</div>
	</c:forEach>
	
</div>

<c:if test="${allowSorting}"><c:if test="${hasLessonToSort eq 'true'}">
<script>
	jQuery("div.mycourses-right-buttons").show();
</script>
</c:if></c:if>
