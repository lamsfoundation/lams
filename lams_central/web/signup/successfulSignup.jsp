<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>

<lams:css/>

<script type="text/javascript" src="/lams/includes/javascript/jquery.js"></script>
<script type="text/javascript" src="/lams/includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="/lams/includes/javascript/jquery.validate.js"></script>
<script type="text/javascript" src="/lams/includes/javascript/bootstrap.min.js"></script>

<div class="panel-heading text-center">
	<img src="<lams:LAMSURL/>/images/svg/lams_logo_black.svg" class="img-center" width="200px"></img>
</div>
<div class="panel-body text-center">
	<c:if test="${not empty error}">
		<lams:Alert type="danger" id="error-messages" close="false">
			<fmt:message key="success.errors" />,
					<c:out value="${error}" />
		</lams:Alert>
	</c:if>
	<div class="panel">
		<fmt:message key="success.msg.1" />
		<p class="voffset20">
			<a class="btn btn-sm btn-primary" href="<lams:LAMSURL />"><fmt:message key="success.login" /></a>
		</p>
	</div>
</div>



