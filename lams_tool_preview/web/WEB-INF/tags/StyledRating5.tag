<%
	/**
	 * StyledRating.tag
	 *	Author: Andrey Balan
	 *	Description: Shows the results of a styled rating. Does not allow for update.
	 */
%>
<%@ tag body-content="scriptless" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-function" prefix="fn" %>
<c:set var="lams"><lams:LAMSURL/></c:set>

<%@ attribute name="criteriaRatings" required="true" rtexprvalue="true" type="org.lamsfoundation.lams.rating.dto.StyledCriteriaRatingDTO" %>
<%@ attribute name="showJustification" required="true" %>
<%@ attribute name="alwaysShowAverage" required="true" %>
<%@ attribute name="rubricsPivotView" required="false" %>
<%@ attribute name="rubricsInBetweenColumns" required="false" %>

<c:if test="${empty rubricsPivotView}">
	<c:set var="rubricsPivotView" value="false" />
</c:if>
<c:if test="${empty rubricsInBetweenColumns}">
	<c:set var="rubricsInBetweenColumns" value="false" />
</c:if>

<!-- On the results page, set to true for current users summary at the bottom, set to false "other users results".
When true, hides the names and groups the comments.  -->
<%@ attribute name="currentUserDisplay" required="true" %>

<c:choose>
	<c:when test="${criteriaRatings.ratingCriteria.ratingStyle == 0}">

		<table class="tablesorter">
			<thead>
			<tr>
				<c:if test="${not currentUserDisplay}">
					<th class="username" title="<fmt:message key='label.sort.by.user.name'/>" >
						<fmt:message key="label.user.name" />
					</th>
				</c:if>
				<th class="comment">
					<fmt:message key="label.comment" />
				</th>
			</tr>
			</thead>
			<tbody>

			<c:forEach var="rating" items="${criteriaRatings.ratingDtos}">
				<c:if test="${ not empty rating.comment }">
					<tr>
						<c:if test="${not currentUserDisplay}">
							<td>
								<lams:Portrait userId="${rating.itemId}"/><span class="portrait-sm-lineheight">&nbsp;<c:out value="${rating.itemDescription}" escapeXml="false"/></span>
							</td>
						</c:if>
						<td>
							<c:out value="${rating.comment}" escapeXml="false"/>
						</td>
					</tr>
				</c:if>
			</c:forEach>
			</tbody>
		</table>
	</c:when>

	<c:when test="${criteriaRatings.ratingCriteria.ratingStyle == 1}">

		<c:choose>
			<c:when test="${not currentUserDisplay}">
				<table class="tablesorter jRating">
					<thead>
					<tr>
						<th class="username" title="<fmt:message key='label.sort.by.user.name'/>" width="${criteriaRatings.ratingCriteria.commentsEnabled ? '25%' : '40%'}" >
							<fmt:message key="label.user.name" />
						</th>
						<th class="rating text-center ">
							<fmt:message key="label.rating" />
						</th>
						<c:if test="${criteriaRatings.ratingCriteria.commentsEnabled}">
							<th class="comment">
								<fmt:message key="label.comment" />
							</th>
						</c:if>
					</tr>
					</thead>
					<tbody>
					<c:forEach var="rating" items="${criteriaRatings.ratingDtos}">
						<c:if test="${not empty rating.averageRating}">
							<tr>
								<td>
									<lams:Portrait userId="${rating.itemId}"/>
									<span class="portrait-sm-lineheight">&nbsp;<c:out value="${rating.itemDescription}" escapeXml="false"/></span>
								</td>
								
								<td class="rating">
									<div class="rating-stars-holder text-center center-block">
										<c:set var="objectId">${criteriaRatings.ratingCriteria.ratingCriteriaId}-${rating.itemId}</c:set>
										<div class="rating-stars-disabled rating-stars-new" data-average="${rating.averageRating}" data-id="${objectId}">
										</div>
										<c:set var="userRating">${rating.userRating}</c:set>
										<div class="rating-stars-caption" id="rating-stars-caption-${objectId}">
											<c:choose>
												<c:when test="${empty userRating}">
													<fmt:message key="label.not.rated"></fmt:message>
												</c:when>
												<c:otherwise>
													<fmt:message key="label.you.gave.rating"><fmt:param><span id="user-rating-${objectId}">${userRating}</span></fmt:param></fmt:message>
												</c:otherwise>
											</c:choose>
											<br/>
											<fmt:message key="label.avg.rating">
												<fmt:param><span id="average-rating-${objectId}">${rating.averageRating}</span></fmt:param>
												<fmt:param><span id="number-of-votes-${objectId}">${rating.numberOfVotes}</span></fmt:param>
											</fmt:message>
										</div>
									</div>
								</td>
								
								<c:if test="${criteriaRatings.ratingCriteria.commentsEnabled}">
									<td>
										<c:out value="${rating.comment}" escapeXml="false"/>
									</td>
								</c:if>
							</tr>
						</c:if>
					</c:forEach>
					</tbody>
				</table>
			</c:when>

			<c:otherwise>
				<c:forEach var="rating" items="${criteriaRatings.ratingDtos}" varStatus="status">
					<c:if test="${status.first && not empty rating.averageRating}">
						<div class="rating">
							<div class="rating-stars-holder text-center center-block">
								<c:set var="objectId">${criteriaRatings.ratingCriteria.ratingCriteriaId}-${rating.itemId}</c:set>
								<div class="rating-stars-disabled rating-stars-new" data-average="${rating.averageRating}" data-id="${objectId}"></div>
								<div class="rating-stars-caption" id="rating-stars-caption-${objectId}-currentUser">
									<fmt:message key="label.avg.rating">
										<fmt:param><span id="average-rating-${objectId}-currentUser">${rating.averageRating}</span></fmt:param>
										<fmt:param><span id="number-of-votes-${objectId}-currentUser">${rating.numberOfVotes}</span></fmt:param>
									</fmt:message>
								</div>
							</div>
						</div>
					</c:if>
						
					<c:if test="${criteriaRatings.ratingCriteria.commentsEnabled && not empty rating.comment}">
						<div>
							<div class="rating-comment"><c:out value="${rating.comment}" escapeXml="false"/></div>
						</div>
					</c:if>
				</c:forEach>
			</c:otherwise>
		</c:choose>

	</c:when>

	<c:when test="${criteriaRatings.ratingCriteria.ratingStyle == 2}">
		<div class="card-body">
			<div class="fst-italic mb-2">
				<fmt:message key="label.learning.average.rankings"/>&nbsp;
				<c:if test="${not currentUserDisplay}">
					<fmt:message key="label.learning.your.rankings.shown.in.brackets"/>
				</c:if>
			</div>
			
			<ul class="list-group">
				<c:forEach var="rating" items="${criteriaRatings.ratingDtos}">
					<c:if test="${alwaysShowAverage || not empty rating.averageRating }">
						<li class="list-group-item"><strong>
							<c:choose>
								<c:when test="${empty rating.averageRating}">-</c:when>
								<c:otherwise><fmt:formatNumber value="${rating.averageRating}" type="number" maxFractionDigits="0" /></c:otherwise>
							</c:choose>
							:</strong>
							&nbsp;<c:if test="${not currentUserDisplay}"><lams:Portrait userId="${rating.itemId}"/></c:if>
							<span class="portrait-sm-lineheight">&nbsp;<c:out value="${rating.itemDescription}" escapeXml="false"/>&nbsp;</span>
							<c:if test="${not currentUserDisplay && not empty rating.userRating}">
								(${rating.userRating})
							</c:if>
						</li>
					</c:if>
				</c:forEach>
			</ul>
		</div>
	</c:when>

	<c:when test="${criteriaRatings.ratingCriteria.ratingStyle == 3}">
		<div class="card-body">
			<div class="fst-italic mb-2">
				<fmt:message key="label.learning.average.marks"/>
				&nbsp;
				<c:if test="${not currentUserDisplay}">
					<fmt:message key="label.learning.your.marks.shown.in.brackets"/>
				</c:if>
			</div>
			
			<ul class="list-group">
				<c:forEach var="rating" items="${criteriaRatings.ratingDtos}">
					<c:if test="${alwaysShowAverage || not empty rating.averageRating }">
						<li class="list-group-item">
							<strong>
							<c:choose>
								<c:when test="${empty rating.averageRating}">-</c:when>
								<c:otherwise>${rating.averageRating}</c:otherwise>
							</c:choose>
							:
							</strong>
							
							&nbsp;
							<c:if test="${not currentUserDisplay}">
								<lams:Portrait userId="${rating.itemId}"/>
							</c:if>
							<span class="portrait-sm-lineheight">
								&nbsp;<c:out value="${rating.itemDescription}" escapeXml="false"/>&nbsp;
							</span>
							<c:if test="${not currentUserDisplay && not empty rating.userRating}">
								(${rating.userRating})
							</c:if>
						</li>
					</c:if>
				</c:forEach>
			</ul>
			
			<c:if test="${showJustification && criteriaRatings.ratingCriteria.commentsEnabled}">
				<div class="fst-italic mt-2">
					<fmt:message key="label.learning.your.justification"/>
				</div>
				<lams:out value="${criteriaRatings.justificationComment}" />
			</c:if>
		</div>
	</c:when>

	<c:when test="${criteriaRatings.ratingCriteria.ratingStyle == 4}">
		<style>
			.rubrics-user-card .collapsable-icon-left button,
			.rubrics-row-card .collapsable-icon-left button {
				text-decoration: none;
			}

			.rubrics-user-card .collapsable-icon-left button:after {
				margin-top: 6px;
			}

			.rubrics-table.standard-view .row:first-child {
				background-color: initial !important;
			}

			.rubrics-table.standard-view .rubrics-table-header {
				font-style: normal;
				font-weight: bold;
			}

			.rubrics-table.standard-view .col:first-child {
				font-weight: bold;
			}

			.rubrics-table.pivot-view .row:not(.rubrics-table-header) .col:not(:first-child) {
				text-align: center;
			}

			.rubrics-table.pivot-view .rubrics-table-header {
				font-style: normal;
				text-align: center;
			}
			.rubrics-table.pivot-view .row .col:first-child {
				font-weight: bold;
			}

			.column-header-span {
				font-style: italic;
			}
			
			.rubrics-user-card .collapsable-icon-left button, .collapsable-icon-left button:after,
			.rubrics-row-card .collapsable-icon-left button, .collapsable-icon-left button:after {
				color: inherit !important;
			}
			
			.rubrics-table .col.text-bg-success {
				font-weight: bold;
				line-height: 40px;
				margin: -0.5em 0;
			}

			.rubrics-table .rubrics-rating-cell {
				border-top: none;

			}
			.rubrics-rating-count {
				text-align: right;
			}

			.rubrics-table .rubrics-rating-cell .rubrics-rating-learner {
				margin-top: 10px;
			}

			.rubrics-table .rubrics-description-cell {
				border-bottom: none;
			}
		</style>

		<c:choose>
			<c:when test="${currentUserDisplay}">
				<div class="ltable table-sm rubrics-table standard-view">
					<div class="row rubrics-table-header">
						<%-- Each answer column has the same length, all remaining space is taken by the question column --%>
						<div class="col"></div>
						<c:forEach var="columnHeader" items="${criteriaRatings.ratingCriteria.rubricsColumnHeaders}" varStatus="columnStatus">
							<div class="col">
								(${columnStatus.count})&nbsp;<c:out value="${columnHeader}" escapeXml="false"/>
							</div>
							<c:if test="${not columnStatus.last and rubricsInBetweenColumns}">
								<div class="col">
									<i>(${columnStatus.count + 0.5})&nbsp;<fmt:message key="label.rating.rubrics.in.between" /></i>
								</div>
							</c:if>
						</c:forEach>
						<div class="col text-center"><fmt:message key="label.average" /></div>
					</div>
					
					<c:forEach var="criteriaDto" items="${criteriaRatings.criteriaGroup}">
						<c:set var="criteria" value="${criteriaDto.ratingCriteria}" />

						<div class="row align-items-center g-0">
							<div class="col">
								<c:out value="${criteria.title}" escapeXml="false" />
							</div>

							<%-- These variables are for counting average rating for the given row --%>
							<c:set var="rowRateCount" value="0" />
							<c:set var="rowRateValue" value="0" />
							<c:forEach var="column" items="${criteria.rubricsColumns}" varStatus="columnOrderId">

								<%-- Check if any other learner rated this learner for this column --%>
								<c:set var="rateCount" value="0" />
								<c:forEach var="ratingDto" items="${criteriaDto.ratingDtos}">
									<c:set var="rateCount" value="${rateCount + (ratingDto.userRating == columnOrderId.count ? 1 : 0)}" />
								</c:forEach>
								<c:set var="rowRateCount" value="${rowRateCount + rateCount}" />
								<c:set var="rowRateValue" value="${rowRateValue + rateCount * columnOrderId.count}" />

								<div class="col rubrics-description-cell <c:if test="${rateCount > 0}">text-bg-success</c:if>">
									<c:out value="${column.name}" escapeXml="false" />
									
									<c:if test="${rateCount > 0}">
										<%-- learners see just how many rates they got from other learners --%>
										<div class="rubrics-rating-count">
											<span class="badge text-bg-light">x&nbsp;${rateCount}</span>
										</div>

										<%-- teachers see also who gave the rating --%>
										<c:if test="${showJustification}">
											<c:forEach var="ratingDto" items="${criteriaDto.ratingDtos}">
												<c:if test="${ratingDto.userRating == columnOrderId.count}">
													<div class="rubrics-rating-learner">
														<lams:Portrait userId="${ratingDto.itemDescription2}" hover="false" />
														&nbsp;<c:out value="${ratingDto.itemDescription}" escapeXml="false"/>
													</div>
												</c:if>
											</c:forEach>
										</c:if>
									</c:if>
								</div>

								<c:if test="${not columnOrderId.last and rubricsInBetweenColumns}">
									<%-- Check if any other learner rated this learner for this 0.5 column --%>
									<c:set var="rateCount" value="0" />
									<c:forEach var="ratingDto" items="${criteriaDto.ratingDtos}">
										<c:set var="rateCount" value="${rateCount + (ratingDto.userRating == (columnOrderId.count + 0.5) ? 1 : 0)}" />
									</c:forEach>
									<c:set var="rowRateCount" value="${rowRateCount + rateCount}" />
									<c:set var="rowRateValue" value="${rowRateValue + rateCount * (columnOrderId.count + 0.5)}" />

									<div class="col rubrics-description-cell <c:if test="${rateCount > 0}">text-bg-success</c:if>">
										<i><fmt:message key="label.rating.rubrics.in.between" /></i>
										
										<c:if test="${rateCount > 0}">
											<%-- learners see just how many rates they got from other learners --%>
											<div class="rubrics-rating-count">
												<span class="badge text-bg-light">x&nbsp;${rateCount}</span>
											</div>

											<%-- teachers see also who gave the rating --%>
											<c:if test="${showJustification}">
												<c:forEach var="ratingDto" items="${criteriaDto.ratingDtos}">
													<c:if test="${ratingDto.userRating == (columnOrderId.count + 0.5)}">
														<div class="rubrics-rating-learner">
															<lams:Portrait userId="${ratingDto.itemDescription2}" hover="false" />
															&nbsp;<c:out value="${ratingDto.itemDescription}" escapeXml="false"/>
														</div>
													</c:if>
												</c:forEach>
											</c:if>
										</c:if>
									</div>
								</c:if>
							</c:forEach>

							<%-- Cell with average rating --%>
							<div class="col text-center">
								<c:choose>
									<c:when test="${rowRateCount == 0}">
										-
									</c:when>
									<c:otherwise>
										<fmt:formatNumber value="${rowRateValue / rowRateCount}" type="number" maxFractionDigits="2" />
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</c:forEach>
				</div>
			</c:when>
			
			<c:when test="${rubricsPivotView}">
				<div id="rubrics-row-cards">
						<%-- It is sufficient to take user names and columns from the first row/criterion --%>
					<c:set var="columnHeaders" value="${criteriaRatings.ratingCriteria.rubricsColumnHeaders}" />
					<c:set var="columnHeaderCount" value="${fn:length(columnHeaders)}" />

					<c:forEach var="criteriaDto" items="${criteriaRatings.criteriaGroup}">
						<c:set var="criteria" value="${criteriaDto.ratingCriteria}" />

						<div class="lcard card rubrics-row-card pivot-view">
							<div class="card-header text-bg-secondary collapsable-icon-left" id="heading${criteria.ratingCriteriaId}">
						       	<button type="button" class="btn collapsed" data-bs-toggle="collapse" data-bs-target="#collapse${criteria.ratingCriteriaId}"
								   aria-expanded="false" aria-controls="collapse${criteria.ratingCriteriaId}" data-parent="#rubrics-rows-panels">
									<%-- Criterion "row" --%>
									<c:out value="${criteria.title}" escapeXml="false" />
								</button>
							</div>

							<div id="collapse${criteria.ratingCriteriaId}" class="collapse"
								 	role="tabpanel" aria-labelledby="heading${criteria.ratingCriteriaId}">
								<div class="ltable rubrics-table pivot-view">
									<div class="row rubrics-table-header m-0">
										<%-- Learner profile pictures and names --%>
										<div class="col"></div>
										<c:forEach var="ratingDto" items="${criteriaDto.ratingDtos}" varStatus="learnerOrderId">
											<div class="col">
												<lams:Portrait userId="${ratingDto.itemId}" hover="false" /><br>
												<strong><c:out value="${ratingDto.itemDescription}" escapeXml="false"/></strong>
											</div>
										</c:forEach>
									</div>

									<c:forEach items="${columnHeaders}" varStatus="columnStatus">
										<c:set var="columnHeader" value="${columnHeaders[columnHeaderCount - columnStatus.count]}" />
										<div class="row align-items-center m-0">
											<div class="col">
													<%-- Criterion "column" --%>
												<span class="column-header-span"><c:out value="${columnHeader}" escapeXml="false"/></span><br>
													<%-- Criterion "cell" --%>
												<c:out value="${criteria.rubricsColumns[columnHeaderCount - columnStatus.count].name}" escapeXml="false" />
											</div>
											<c:forEach var="ratingDto" items="${criteriaDto.ratingDtos}" varStatus="learnerOrderId">
												<%-- Columns are ordered from 1 to 5, so rate value is also the order ID of the column --%>
												<div class='col <c:if test="${criteriaDto.ratingDtos[learnerOrderId.index].userRating == (columnHeaderCount - columnStatus.index)}">text-bg-success</c:if>'>
													${columnHeaderCount - columnStatus.index}
												</div>
											</c:forEach>
										</div>
										
										<c:if test="${not columnStatus.last and rubricsInBetweenColumns}">
											<div class="row align-items-center m-0">
												<div class="col">
													<i><fmt:message key="label.rating.rubrics.in.between" /></i>
												</div>
												<c:forEach var="ratingDto" items="${criteriaDto.ratingDtos}" varStatus="learnerOrderId">
													<div class='col <c:if test="${criteriaDto.ratingDtos[learnerOrderId.index].userRating == (columnHeaderCount - columnStatus.index - 0.5)}">text-bg-success</c:if>'>
														${columnHeaderCount - columnStatus.index - 0.5}
													</div>
												</c:forEach>
											</div>
										</c:if>
									</c:forEach>
								</div>
							</div>
						</div>

					</c:forEach>
				</div>
			</c:when>
			
			<c:otherwise>
				<c:set var="criteriaGroupId" value="${criteriaRatings.ratingCriteria.ratingCriteriaGroupId}" />

				<div id="rubrics-user-cards-${criteriaGroupId}" role="tablist" aria-multiselectable="true">
					<%-- It is sufficient to take user names and columns from the first row/criterion --%>
					<c:set var="exampleRatings" value="${criteriaRatings.ratingDtos}" />

					<c:forEach var="ratingDto" items="${exampleRatings}" varStatus="learnerOrderId">

						<div class="card lcard rubrics-user-card">
							<div class="card-header text-bg-secondary collapsable-icon-left" id="rubrics-heading-${criteriaGroupId}-${ratingDto.itemId}">
					       		<button type="button" class="btn collapsed" data-bs-toggle="collapse" data-bs-target="#rubrics-collapse-${criteriaGroupId}-${ratingDto.itemId}"
									   aria-expanded="false" aria-controls="rubrics-collapse-${criteriaGroupId}-${ratingDto.itemId}"
									   data-parent="#rubrics-users-panels-${criteriaGroupId}">
									<lams:Portrait userId="${ratingDto.itemId}" hover="false" />
									&nbsp;<c:out value="${ratingDto.itemDescription}" escapeXml="false"/>
								</button>
							</div>

							<div id="rubrics-collapse-${criteriaGroupId}-${ratingDto.itemId}" class="collapse"
									 role="tabpanel" aria-labelledby="rubrics-heading-${criteriaGroupId}-${ratingDto.itemId}">
								<div class="ltable rubrics-table mb-0 standard-view">
									<div class="row rubrics-table-header m-0">
											<%-- Each answer column has the same length, all remaining space is take by the question column --%>
										<div class="col"></div>
										<c:forEach var="columnHeader" items="${criteriaRatings.ratingCriteria.rubricsColumnHeaders}" varStatus="columnStatus">
											<div class="col">
												(${columnStatus.count})&nbsp;<c:out value="${columnHeader}" escapeXml="false"/>
											</div>
											<c:if test="${not columnStatus.last and rubricsInBetweenColumns}">
												<div class="col">
													<i>(${columnStatus.count + 0.5})&nbsp;<fmt:message key="label.rating.rubrics.in.between" /></i>
												</div>
											</c:if>
										</c:forEach>
									</div>
									
									<c:forEach var="criteriaDto" items="${criteriaRatings.criteriaGroup}">
										<c:set var="criteria" value="${criteriaDto.ratingCriteria}" />
										<div class="row m-0">
											<div class="col">
												<c:out value="${criteria.title}" escapeXml="false" />
											</div>
											<c:forEach var="column" items="${criteria.rubricsColumns}" varStatus="columnOrderId">
												<%-- Columns are ordered from 1 to 5, so rate value is also the order ID of the column --%>
												<div class='col <c:if test="${criteriaDto.ratingDtos[learnerOrderId.index].userRating == columnOrderId.count}">text-bg-success</c:if>'>
													<c:out value="${column.name}" escapeXml="false" />
												</div>
												<c:if test="${not columnOrderId.last and rubricsInBetweenColumns}">
													<div class='col <c:if test="${criteriaDto.ratingDtos[learnerOrderId.index].userRating == (columnOrderId.count + 0.5)}">text-bg-success</c:if>'>
														<i><fmt:message key="label.rating.rubrics.in.between" /></i>
													</div>
												</c:if>
											</c:forEach>
										</div>
									</c:forEach>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</c:otherwise>
		</c:choose>
	</c:when>
</c:choose>
