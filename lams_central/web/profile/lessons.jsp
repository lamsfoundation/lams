<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>
<lams:html>
<lams:head>
	<link rel="stylesheet" href="${lams}css/components.css">
    <link rel="stylesheet" href="${lams}includes/font-awesome6/css/all.css">

	<lams:JSImport src="includes/javascript/getSysInfo.js" />
	<lams:JSImport src="includes/javascript/openUrls.js" />
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap5.bundle.min.js"></script>
	<lams:JSImport src="includes/javascript/profile.js" />
	<script type="text/javascript">
		const LAMS_URL = "${lams}";
		
		$(document).ready(function () {
			//update dialog's height and title
			updateMyProfileDialogSettings('<spring:escapeBody javaScriptEscape="true"><fmt:message key="title.all.my.lessons" /></spring:escapeBody>', '80%');
		});
	</script>
</lams:head>

<body>
	<div style="clear: both;"></div>
	<div class="container">
		<div class="col-12 col-sm-8 col-sm-offset-2 col-md-8 col-md-offset-2 mx-auto">
			<c:if test="${not empty beans}">
				<c:forEach var="group" items="${beans}">
					<br />
					<h4>
						<c:out value="${group.name}" />
					</h4>
								
					<div class="list-group">
						<c:forEach var="lesson" items="${group.lessons}">
							<a href="<c:out value="${lesson.url}"/>" class="list-group-item list-group-item-action sequence-name-link"> 
								<c:out value="${lesson.name}" />
							</a>
						</c:forEach>
					</div>
								
					<div class="ms-5 mt-3">
						<c:forEach var="subgroup" items="${group.childIndexOrgBeans}">
							<h5>
								<c:out value="${subgroup.name}" />
							</h5>
							
							<div class="list-group">
								<c:forEach var="s_lesson" items="${subgroup.lessons}">
									<a href="<c:out value="${s_lesson.url}"/>" class="list-group-item list-group-item-action sequence-name-link"> 
										<c:out value="${s_lesson.name}" />
									</a>
								</c:forEach>
							</div>
						</c:forEach>
					</div>
				</c:forEach>
			</c:if>
						
			<c:if test="${empty beans}">
				<p>
					<fmt:message key="msg.no.lessons" />
				</p>
			</c:if>
					
			<div class="float-end">
				<button type="button" class="btn btn-sm btn-light mt-4" onclick="history.go(-1);">
					<i class="fa-solid fa-rotate-left me-1"></i>
					<fmt:message key="label.return.to.myprofile" />
				</button>
			</div>
		</div>
	</div>
</body>
</lams:html>
