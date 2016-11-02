<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<script type="text/javascript" src="/lams/includes/javascript/jquery.js"></script>
<script type="text/javascript"
	src="/lams/includes/javascript/jquery-ui.js"></script>
<script type="text/javascript"
	src="/lams/includes/javascript/jquery.validate.js"></script>
<link rel="stylesheet" href="/lams/css/defaultHTML_learner.css"
	type="text/css" />
<script type="text/javascript"
	src="/lams/includes/javascript/bootstrap.min.js"></script>
<div class="container">
	<div class="panel" align="center">
		<div class="panel-heading">

			<img src="<lams:LAMSURL/>/images/svg/lams_logo_black.svg" alt=""
				class="img-center" width="200"></img>

		</div>
		<div class="panel-body">
			<c:if test="${not empty error}">
				<lams:Alert type="warning" id="error-messages" close="false">
					<fmt:message key="success.errors" />,
							<c:out value="${error}" />
				</lams:Alert>
			</c:if>
			<div class="panel">
				<fmt:message key="success.msg.1" />
				: <a href="<lams:LAMSURL />"><fmt:message key="success.login" /></a>.
			</div>
		</div>
	</div>
</div>



