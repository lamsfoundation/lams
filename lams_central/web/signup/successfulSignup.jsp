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
	<div class="row vertical-center-row">
		<div
			class="col-xs-12 col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3">
			<div class="panel">
				<div class="panel-heading">
					<h1 align="center">
						<img src="<lams:LAMSURL/>/images/css/lams_login.gif" alt=""
							width="186" height="90"></img>
					</h1>
				</div>
				<div class="panel-body">
					<c:if test="${not empty error}">
						<div Class="warning">
							<fmt:message key="success.errors" />
							,
							<c:out value="${error}" />
						</div>
					</c:if>
					<div class="form-group">
						<fmt:message key="success.msg.1" />
						: <a href="<lams:LAMSURL />"><fmt:message key="success.login" /></a>.
					</div>
				</div>
			</div>
		</div>
	</div>
</div>



