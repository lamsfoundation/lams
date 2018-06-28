<!DOCTYPE html>

<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-bean" prefix="bean"%>
<%@ taglib uri="tags-logic" prefix="logic"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<lams:html>
<lams:head>
	<lams:css/>
	
	<script type="text/javascript" src="includes/javascript/getSysInfo.js"></script>
	<script type="text/javascript" src="loadVars.jsp"></script>
	<script type="text/javascript" src="includes/javascript/openUrls.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="includes/javascript/profile.js"></script>
	<script type="text/javascript">
		$(document).ready(function () {
			//update dialog's height and title
			updateMyProfileDialogSettings('<fmt:message key="title.all.my.lessons" />', '80%');
		});
	</script>
</lams:head>

<body>
<div style="clear: both;"></div>
<div class="container">
	<div class="row vertical-center-row">
		<div
			class="col-xs-12 col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3">
			<div class="panel">
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
							onclick="javascript:document.location='index.do?method=profile'" />
					</div>

				</div>
			</div>
		</div>
	</div>
</div>
</body>
</lams:html>
