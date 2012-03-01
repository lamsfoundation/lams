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
		<table style="table-layout: fixed">
			<c:forEach var="lesson" items="${orgBean.lessons}">
				<c:set var="hasLessonToSort">true</c:set>
				<tr id="<c:out value="${lesson.id}"/>" class="j-single-lesson">
					<td style="word-wrap: break-word;">
						<c:choose>
							<c:when test="${empty lesson.url}">
								<a title="<c:out value="${lesson.description}"/>" class="disabled-sequence-name-link"> <c:out value="${lesson.name}" /></a> 
							</c:when>
							<c:otherwise>
								<a title="<c:out value="${lesson.description}"/>" href="<c:out value="${lesson.url}"/>" class="sequence-name-link"> <c:out value="${lesson.name}" /></a> 
							</c:otherwise>
						</c:choose>
					<td style="width: 150px;">
						<c:if test="${lesson.completed}"> <span class="mycourses-completed-img" title="<fmt:message key="label.completed"/>" >&nbsp;</span> </c:if>
						<c:if test="${lesson.state eq 4}"> <span class="mycourses-stop-img" title="<fmt:message key="label.disabled"/>" >&nbsp;</span> </c:if>
						<c:if test="${lesson.state eq 6}"> <span class="mycourses-stop-img" title="<fmt:message key="label.archived"/>" >&nbsp;</span> </c:if>
						<c:if test="${lesson.dependent or lesson.scheduledFinish}"> <span class="mycourses-conditions-img" title="<fmt:message key="index.conditions.flag.tooltip"/>" >&nbsp;</span> </c:if>
					</td>
					<c:choose>
						<c:when test="${empty lesson.links}">
							<td style="width: 170px;"></td>
						</c:when>
						<c:otherwise>
							<td class="split-menu-button" style="width: 170px;">
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
													<a href="#" class="${lessonlink.style}">
						                        		<span onclick="<c:out value='${lessonlink.url}'/>" title="${tooltip}" 
						                        		class="${lessonlink.spanStyle}" style="margin-right:0px">
						                        			<fmt:message key="${lessonlink.name}" /> &nbsp;
						                        		</span>
														<em class="">
															<img src="<lams:LAMSURL/>/images/icons/bullet_arrow_down.png" alt="dropdown">
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
			<div id="<c:out value="${childOrg.id}"/>" class="j-subgroup-lessons"><c:forEach var="childLesson" items="${childOrg.lessons}">
				<c:set var="hasLessonToSort">true</c:set>
				<table style="table-layout: fixed">
					<tr id="<c:out value="${childLesson.id}"/>" class="j-single-subgroup-lesson">
						<td style="word-wrap: break-word;">
							<c:choose>
								<c:when test="${empty childlesson.url}">
									<a title="<c:out value="${childlesson.description}"/>" class="disabled-sequence-name-link"> <c:out value="${childlesson.name}" /></a> 
								</c:when>
								<c:otherwise>
									<a title="<c:out value="${childlesson.description}"/>" href="<c:out value="${childlesson.url}"/>" class="sequence-name-link"> <c:out value="${childlesson.name}" /></a> 
								</c:otherwise>
							</c:choose>
						</td>
						<td style="width: 150px;">
							<c:if test="${childLesson.completed}"> <span class="mycourses-completed-img" title="<fmt:message key="label.completed"/>" >&nbsp;</span> </c:if>
							<c:if test="${childLesson.state eq 4}"> <span class="mycourses-stop-img" title="<fmt:message key="label.disabled"/>" >&nbsp;</span> </c:if>
							<c:if test="${childLesson.state eq 6}"> <span class="mycourses-stop-img" title="<fmt:message key="label.archived"/>" >&nbsp;</span> </c:if>
							<c:if test="${lesson.dependent or lesson.scheduledFinish}"> <span class="mycourses-conditions-img" title="<fmt:message key="index.conditions.flag.tooltip"/>" >&nbsp;</span> </c:if>
						</td>
						<c:choose>
							<c:when test="${empty childlesson.links}">
								<td style="width: 170px;"></td>
							</c:when>
							<c:otherwise>
								<td class="split-menu-button" style="width: 170px;">
									<ul>
										<li>
											<c:forEach var="childlessonlink" items="${childLesson.links}">
												<c:set var="tooltip" value="" />
												<c:if test="${childlessonlink.tooltip ne null}">
													<c:set var="tooltip">
														<fmt:message key="${childlessonlink.tooltip}" />
													</c:set>
												</c:if>
												
												<c:choose>
													<c:when test="${status.first}">
														<a href="#" class="${childlessonlink.style}">
							                        		<span onclick="<c:out value='${childlessonlink.url}'/>" title="${tooltip}" 
							                        		class="${childlessonlink.spanStyle}" style="margin-right:0px">
							                        			<fmt:message key="${childlessonlink.name}" /> &nbsp;
							                        		</span>
															<em class="">
																<img src="<lams:LAMSURL/>/images/icons/bullet_arrow_down.png" alt="dropdown">
															</em>
														</a>
														<ul class="button-menu">
													</c:when>
													<c:otherwise>
														<li>
															<a href="<c:out value='${childlessonlink.url}'/>" class="${childlessonlink.style}">
																<span class="${childlessonlink.spanStyle}" title="${tooltip}"><fmt:message key="${childlessonlink.name}" /> &nbsp;</span>
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
