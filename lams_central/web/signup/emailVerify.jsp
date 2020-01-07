<%@ include file="/common/taglibs.jsp"%>
<lams:css/>

<script type="text/javascript" src="/lams/includes/javascript/jquery.js"></script>
<script type="text/javascript" src="/lams/includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="/lams/includes/javascript/jquery.validate.js"></script>
<script type="text/javascript" src="/lams/includes/javascript/bootstrap.min.js"></script>

<div class="panel-heading text-center">
	<img src="<lams:LAMSURL/>/images/svg/lams_logo_black.svg" class="img-center" width="200px"></img>
</div>
<div class="panel-body text-center">
	<c:choose>
		<c:when test="${empty error}">
			<lams:Alert type="info" close="false">
				<fmt:message key="signup.email.verify.sent">
					<fmt:param value='${email}' />
				</fmt:message>
			</lams:Alert>
		</c:when>
		<c:otherwise>
			<lams:Alert type="danger" close="false">
				<fmt:message key="success.errors" />,
					<c:out value="${error}" />
			</lams:Alert>
		</c:otherwise>
	</c:choose>

</div>



