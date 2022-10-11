<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<lams:html>
<lams:head>
	<lams:css/>
	
	<script type="text/javascript" src="${lams}includes/javascript/getSysInfo.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/openUrls.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/profile.js"></script>
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
						<button type="button" class="btn btn-sm btn-default offset5" onclick="history.go(-1);">
							<fmt:message key="label.return.to.myprofile" />
						</button>
					</div>

				</div>
			</div>
		</div>
	</div>
</div>
</body>
</lams:html>
