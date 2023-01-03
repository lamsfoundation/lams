<%@ include file="/common/taglibs.jsp"%>
<lams:css/>

<script type="text/javascript" src="/lams/includes/javascript/jquery.js"></script>
<script type="text/javascript" src="/lams/includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="/lams/includes/javascript/jquery.validate.js"></script>
<script type="text/javascript" src="/lams/includes/javascript/bootstrap.min.js"></script>

<div class="panel-heading text-center">
	<img src="<lams:LAMSURL/>/images/svg/lamsv5_logo.svg" class="img-center" width="100px"></img>
</div>
<div class="panel-body text-center">
	<c:if test="${not empty error}">
		<lams:Alert type="danger" close="false">
			<fmt:message key="success.errors" />,
					<c:out value="${error}" />
		</lams:Alert>
	</c:if>
	<c:choose>
		<c:when test="${emailVerified}">
			<lams:Alert type="info" close="false">
				<fmt:message key="signup.email.verify.success" />
			</lams:Alert>
			<div class="panel">
				<fmt:message key="success.msg.1" />
				<p class="voffset20">
					<a class="btn btn-sm btn-primary" href="<lams:LAMSURL />"><fmt:message key="success.login" /></a>
				</p>
			</div>
		</c:when>
		<c:otherwise>
			<lams:Alert type="danger" close="false">
				<fmt:message key="signup.email.verify.fail" />
			</lams:Alert>
		</c:otherwise>
	</c:choose>
</div>



