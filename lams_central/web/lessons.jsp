<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<link rel="stylesheet" href="css/defaultHTML_learner.css"
	type="text/css" />

<div style="clear: both;"></div>
<div class="container">
	<div class="row vertical-center-row">
		<div
			class="col-xs-12 col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3">
			<div class="panel panel-default">
				<div class="panel-heading">
					<div class="panel-title">
						<fmt:message key="title.all.my.lessons" />
					</div>
				</div>
				<div class="panel-body">
					<div class="text-left">
						<c:if test="${not empty beans}">
							<c:forEach var="group" items="${beans}">
								<br />
								<h4>
									<c:out value="${group.name}" />
								</h4>
								<ul>
									<c:forEach var="lesson" items="${group.lessons}">
										<li><a href="<c:out value="${lesson.url}"/>"
											class="sequence-name-link"> <c:out value="${lesson.name}" />
										</a></li>
									</c:forEach>
								</ul>
								<ul>
									<c:forEach var="subgroup" items="${group.childIndexOrgBeans}">
										<c:out value="${subgroup.name}" />
										<ul>
											<c:forEach var="s_lesson" items="${subgroup.lessons}">
												<li><a href="<c:out value="${s_lesson.url}"/>"
													class="sequence-name-link"> <c:out
															value="${s_lesson.name}" />
												</a></li>
											</c:forEach>
										</ul>
									</c:forEach>
								</ul>
							</c:forEach>
						</c:if>
						<c:if test="${empty beans}">
							<p class="align-left">
								<fmt:message key="msg.no.lessons" />
							</p>
						</c:if>
					</div>
					<div align="center">
						<input class="btn btn-sm btn-default offset5" type="button"
							value="<fmt:message key="label.return.to.myprofile" />"
							onclick="javascript:document.location='index.do?state=active&tab=profile'" />
					</div>

				</div>
			</div>
		</div>
	</div>
</div>
