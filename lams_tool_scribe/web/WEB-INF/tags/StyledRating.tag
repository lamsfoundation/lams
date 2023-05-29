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
				<table class="tablesorter">
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
									<lams:Portrait userId="${rating.itemId}"/><span class="portrait-sm-lineheight">&nbsp;<c:out value="${rating.itemDescription}" escapeXml="false"/></span>
								</td>
								<td class="rating">
									<div class="rating-stars-holder text-center center-block">
										<c:set var="objectId">${criteriaRatings.ratingCriteria.ratingCriteriaId}-${rating.itemId}</c:set>
										<div class="rating-stars-disabled rating-stars-new" data-average="${rating.averageRating}" data-id="${objectId}"></div>
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
				<table class="tablesorter" width="100%">
					<tbody>
					<c:forEach var="rating" items="${criteriaRatings.ratingDtos}" varStatus="status">
						<c:if test="${status.first && not empty rating.averageRating}">
							<tr>
								<td class="rating">
									<div class="rating-stars-holder text-center center-block">
										<c:set var="objectId">${criteriaRatings.ratingCriteria.ratingCriteriaId}-${rating.itemId}</c:set>
										<div class="rating-stars-disabled rating-stars-new" data-average="${rating.averageRating}" data-id="${objectId}"></div>
										<div class="rating-stars-caption" id="rating-stars-caption-${objectId}">
											<fmt:message key="label.avg.rating">
												<fmt:param><span id="average-rating-${objectId}">${rating.averageRating}</span></fmt:param>
												<fmt:param><span id="number-of-votes-${objectId}">${rating.numberOfVotes}</span></fmt:param>
											</fmt:message>
										</div>
								</td>
							</tr>
						</c:if>
						<c:if test="${criteriaRatings.ratingCriteria.commentsEnabled && not empty rating.comment}">
							<tr>
								<td>
									<div class="rating-comment"><c:out value="${rating.comment}" escapeXml="false"/></div>
								</td>
							</tr>
						</c:if>
					</c:forEach>
					</tbody>
				</table>
			</c:otherwise>
		</c:choose>

	</c:when>

	<c:when test="${criteriaRatings.ratingCriteria.ratingStyle == 2}">
		<fmt:message key="label.learning.average.rankings"/>&nbsp;<c:if test="${not currentUserDisplay}"><fmt:message key="label.learning.your.rankings.shown.in.brackets"/></c:if>
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
	</c:when>

	<c:when test="${criteriaRatings.ratingCriteria.ratingStyle == 3}">
		<fmt:message key="label.learning.average.marks"/>&nbsp;<c:if test="${not currentUserDisplay}"><fmt:message key="label.learning.your.marks.shown.in.brackets"/></c:if>
		<ul class="list-group">
			<c:forEach var="rating" items="${criteriaRatings.ratingDtos}">
				<c:if test="${alwaysShowAverage || not empty rating.averageRating }">
					<li class="list-group-item"><strong>
						<c:choose>
							<c:when test="${empty rating.averageRating}">-</c:when>
							<c:otherwise>${rating.averageRating}</c:otherwise>
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
		<c:if test="${showJustification && criteriaRatings.ratingCriteria.commentsEnabled}">
			<fmt:message key="label.learning.your.justification"/>&nbsp;
			<div class="rating-comment"><lams:out value="${criteriaRatings.justificationComment}" /></div>
		</c:if>
	</c:when>

	<c:when test="${criteriaRatings.ratingCriteria.ratingStyle == 4}">

		<%-- Some styles for the table and results --%>
		<style>
			.rubrics-user-panel .collapsable-icon-left a,
			.rubrics-row-panel .collapsable-icon-left a {
				text-decoration: none;
			}

			.rubrics-user-panel .collapsable-icon-left a:after {
				margin-top: 6px;
			}

			.rubrics-table.standard-view tr:first-child {
				background-color: initial !important;
			}

			.rubrics-table.standard-view th {
				font-style: normal;
				font-weight: bold;
			}

			.rubrics-table.standard-view td:first-child {
				font-weight: bold;
			}

			.rubrics-table.pivot-view td:not(:first-child) {
				text-align: center;
			}

			.rubrics-table.pivot-view th {
				font-style: normal;
				text-align: center;
			}

			.column-header-span {
				font-style: italic;
			}

			.rubrics-table .rubrics-rating-cell {
				border-top: none;

			}
			.rubrics-table .rubrics-rating-cell .rubrics-rating-count {
				text-align: right;
			}

			.rubrics-table .rubrics-rating-cell .rubrics-rating-count > .badge {
				color: gray;
				background-color: white;
				font-size: large;
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

				<table class="table table-bordered rubrics-table standard-view">
					<tr>
							<%-- Each answer column has the same length, all remaining space is taken by the question column --%>
						<th></th>
						<c:forEach var="columnHeader" items="${criteriaRatings.ratingCriteria.rubricsColumnHeaders}" varStatus="columnStatus">
							<th>
								(${columnStatus.count})&nbsp;<c:out value="${columnHeader}" escapeXml="false"/>
							</th>
							<c:if test="${not columnStatus.last and rubricsInBetweenColumns}">
								<th>
									<i>(${columnStatus.count + 0.5})&nbsp;<fmt:message key="label.rating.rubrics.in.between" /></i>
								</th>
							</c:if>
						</c:forEach>
						<th class="col-xs-1 text-center"><fmt:message key="label.average" /></th>
					</tr>
					<c:forEach var="criteriaDto" items="${criteriaRatings.criteriaGroup}">
						<c:set var="criteria" value="${criteriaDto.ratingCriteria}" />

						<tr>
							<td rowspan="2">
								<c:out value="${criteria.title}" escapeXml="false" />
							</td>

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

								<td class="rubrics-description-cell
								<c:if test="${rateCount > 0}">
									bg-success
								</c:if>
								"
								>
									<c:out value="${column.name}" escapeXml="false" />
								</td>

								<c:if test="${not columnOrderId.last and rubricsInBetweenColumns}">
									<%-- Check if any other learner rated this learner for this 0.5 column --%>
									<c:set var="rateCount" value="0" />
									<c:forEach var="ratingDto" items="${criteriaDto.ratingDtos}">
										<c:set var="rateCount" value="${rateCount + (ratingDto.userRating == (columnOrderId.count + 0.5) ? 1 : 0)}" />
									</c:forEach>
									<c:set var="rowRateCount" value="${rowRateCount + rateCount}" />
									<c:set var="rowRateValue" value="${rowRateValue + rateCount * (columnOrderId.count + 0.5)}" />

									<td class="rubrics-description-cell
									<c:if test="${rateCount > 0}">
										bg-success
									</c:if>
									"
									>
										<i><fmt:message key="label.rating.rubrics.in.between" /></i>
									</td>
								</c:if>

							</c:forEach>

								<%-- Cell with average rating --%>
							<td rowspan="2" class="text-center">
								<c:choose>
									<c:when test="${rowRateCount == 0}">
										-
									</c:when>
									<c:otherwise>
										<fmt:formatNumber value="${rowRateValue / rowRateCount}" type="number" maxFractionDigits="2" />
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
							<c:forEach var="column" items="${criteria.rubricsColumns}" varStatus="columnOrderId">
								<%-- Calculate again because we need the same cell background colour --%>
								<c:set var="rateCount" value="0" />
								<c:forEach var="ratingDto" items="${criteriaDto.ratingDtos}">
									<c:set var="rateCount" value="${rateCount + (ratingDto.userRating == columnOrderId.count? 1 : 0)}" />
								</c:forEach>

								<td class="rubrics-rating-cell
								<%-- Columns are ordered from 1 to 5, so rate value is also the order ID of the column --%>
								<c:if test="${rateCount > 0}">
									bg-success
								</c:if>
								"
								>

									<c:if test="${rateCount > 0}">
										<%-- learners see just how many rates they got from other learners --%>
										<div class="rubrics-rating-count">
											<span class="badge">x&nbsp;${rateCount}</span>
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
								</td>
								<c:if test="${not columnOrderId.last and rubricsInBetweenColumns}">
									<%-- Calculate again because we need the same cell background colour --%>
									<c:set var="rateCount" value="0" />
									<c:forEach var="ratingDto" items="${criteriaDto.ratingDtos}">
										<c:set var="rateCount" value="${rateCount + (ratingDto.userRating  == (columnOrderId.count + 0.5) ? 1 : 0)}" />
									</c:forEach>

									<td class="rubrics-rating-cell
									<%-- Columns are ordered from 1 to 5, so rate value is also the order ID of the column --%>
									<c:if test="${rateCount > 0}">
										bg-success
									</c:if>
									"
									>

										<c:if test="${rateCount > 0}">
											<%-- learners see just how many rates they got from other learners --%>
											<div class="rubrics-rating-count">
												<span class="badge">x&nbsp;${rateCount}</span>
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
									</td>
								</c:if>
							</c:forEach>
						</tr>
					</c:forEach>
				</table>
			</c:when>
			<c:when test="${rubricsPivotView}">
				<div id="rubrics-row-panels" class="panel-group" role="tablist" aria-multiselectable="true">
						<%-- It is sufficient to take user names and columns from the first row/criterion --%>
					<c:set var="columnHeaders" value="${criteriaRatings.ratingCriteria.rubricsColumnHeaders}" />
					<c:set var="columnHeaderCount" value="${fn:length(columnHeaders)}" />

					<c:forEach var="criteriaDto" items="${criteriaRatings.criteriaGroup}">
						<c:set var="criteria" value="${criteriaDto.ratingCriteria}" />

						<div class="panel panel-default rubrics-row-panel pivot-view">
							<div class="panel-heading" role="tab" id="heading${criteria.ratingCriteriaId}">
				       	<span class="panel-title collapsable-icon-left">
				       		<a class="collapsed" role="button" data-toggle="collapse" href="#collapse${criteria.ratingCriteriaId}"
							   aria-expanded="false" aria-controls="collapse${criteria.ratingCriteriaId}" data-parent="#rubrics-rows-panels">
								<%-- Criterion "row" --%>
								<c:out value="${criteria.title}" escapeXml="false" />
							</a>
						</span>
							</div>

							<div id="collapse${criteria.ratingCriteriaId}" class="panel-collapse collapse"
								 role="tabpanel" aria-labelledby="heading${criteria.ratingCriteriaId}">
								<table class="table table-hover table-bordered rubrics-table pivot-view">
									<thead>
									<tr>
											<%-- Learner profile pictures and names --%>
										<th></th>
										<c:forEach var="ratingDto" items="${criteriaDto.ratingDtos}" varStatus="learnerOrderId">
											<th>
												<lams:Portrait userId="${ratingDto.itemId}" hover="false" /><br>
												<strong><c:out value="${ratingDto.itemDescription}" escapeXml="false"/></strong>
											</th>
										</c:forEach>
									</tr>
									</thead>

									<tbody>
									<c:forEach items="${columnHeaders}" varStatus="columnStatus">
										<c:set var="columnHeader" value="${columnHeaders[columnHeaderCount - columnStatus.count]}" />
										<tr>
											<td>
													<%-- Criterion "column" --%>
												<span class="column-header-span"><c:out value="${columnHeader}" escapeXml="false"/></span><br>
													<%-- Criterion "cell" --%>
												<c:out value="${criteria.rubricsColumns[columnHeaderCount - columnStatus.count].name}" escapeXml="false" />
											</td>
											<c:forEach var="ratingDto" items="${criteriaDto.ratingDtos}" varStatus="learnerOrderId">

												<td
													<%-- Columns are ordered from 1 to 5, so rate value is also the order ID of the column --%>
														<c:if test="${criteriaDto.ratingDtos[learnerOrderId.index].userRating == (columnHeaderCount - columnStatus.index)}">
															class="bg-success"
														</c:if>
												>
														${columnHeaderCount - columnStatus.index}
												</td>
											</c:forEach>
										</tr>
										<c:if test="${not columnStatus.last and rubricsInBetweenColumns}">
											<tr>
												<td>
													<i><fmt:message key="label.rating.rubrics.in.between" /></i>
												</td>
												<c:forEach var="ratingDto" items="${criteriaDto.ratingDtos}" varStatus="learnerOrderId">
													<td
															<c:if test="${criteriaDto.ratingDtos[learnerOrderId.index].userRating == (columnHeaderCount - columnStatus.index - 0.5)}">
																class="bg-success"
															</c:if>
													>
															${columnHeaderCount - columnStatus.index - 0.5}
													</td>
												</c:forEach>
											</tr>
										</c:if>
									</c:forEach>
									</tbody>
								</table>
							</div>
						</div>

					</c:forEach>
				</div>
			</c:when>
			<c:otherwise>

				<c:set var="criteriaGroupId" value="${criteriaRatings.ratingCriteria.ratingCriteriaGroupId}" />

				<div id="rubrics-user-panels-${criteriaGroupId}" class="panel-group" role="tablist" aria-multiselectable="true">
						<%-- It is sufficient to take user names and columns from the first row/criterion --%>
					<c:set var="exampleRatings" value="${criteriaRatings.ratingDtos}" />

					<c:forEach var="ratingDto" items="${exampleRatings}" varStatus="learnerOrderId">

						<div class="panel panel-default rubrics-user-panel">
							<div class="panel-heading" role="tab" id="rubrics-heading-${criteriaGroupId}-${ratingDto.itemId}">
				       	<span class="panel-title collapsable-icon-left">
				       		<a class="collapsed" role="button" data-toggle="collapse" href="#rubrics-collapse-${criteriaGroupId}-${ratingDto.itemId}"
							   aria-expanded="false" aria-controls="rubrics-collapse-${criteriaGroupId}-${ratingDto.itemId}"
							   data-parent="#rubrics-users-panels-${criteriaGroupId}">
								<lams:Portrait userId="${ratingDto.itemId}" hover="false" />
								&nbsp;<c:out value="${ratingDto.itemDescription}" escapeXml="false"/>
							</a>
						</span>
							</div>

							<div id="rubrics-collapse-${criteriaGroupId}-${ratingDto.itemId}" class="panel-collapse collapse"
								 role="tabpanel" aria-labelledby="rubrics-heading-${criteriaGroupId}-${ratingDto.itemId}">
								<table class="table table-bordered rubrics-table standard-view">
									<tr>
											<%-- Each answer column has the same length, all remaining space is take by the question column --%>
										<th></th>
										<c:forEach var="columnHeader" items="${criteriaRatings.ratingCriteria.rubricsColumnHeaders}" varStatus="columnStatus">
											<th>
												(${columnStatus.count})&nbsp;<c:out value="${columnHeader}" escapeXml="false"/>
											</th>
											<c:if test="${not columnStatus.last and rubricsInBetweenColumns}">
												<th>
													<i>(${columnStatus.count + 0.5})&nbsp;<fmt:message key="label.rating.rubrics.in.between" /></i>
												</th>
											</c:if>
										</c:forEach>
									</tr>
									<c:forEach var="criteriaDto" items="${criteriaRatings.criteriaGroup}">
										<c:set var="criteria" value="${criteriaDto.ratingCriteria}" />
										<tr>
											<td>
												<c:out value="${criteria.title}" escapeXml="false" />
											</td>
											<c:forEach var="column" items="${criteria.rubricsColumns}" varStatus="columnOrderId">
												<td
													<%-- Columns are ordered from 1 to 5, so rate value is also the order ID of the column --%>
														<c:if test="${criteriaDto.ratingDtos[learnerOrderId.index].userRating == columnOrderId.count}">
															class="bg-success"
														</c:if>
												>
													<c:out value="${column.name}" escapeXml="false" />
												</td>
												<c:if test="${not columnOrderId.last and rubricsInBetweenColumns}">
													<td
															<c:if test="${criteriaDto.ratingDtos[learnerOrderId.index].userRating == (columnOrderId.count + 0.5)}">
																class="bg-success"
															</c:if>
													>
														<i><fmt:message key="label.rating.rubrics.in.between" /></i>
													</td>
												</c:if>
											</c:forEach>
										</tr>
									</c:forEach>
								</table>
							</div>
						</div>
					</c:forEach>
				</div>
			</c:otherwise>
		</c:choose>
	</c:when>
</c:choose>