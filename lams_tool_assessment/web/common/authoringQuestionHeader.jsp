<%@ include file="/common/header.jsp"%>
<link href="<lams:WebAppURL/>includes/css/addQuestion.css" rel="stylesheet" type="text/css">

<script type="text/javascript">
	const QUESTION_TYPE = ${questionType};
	const ADD_OPTION_URL = "<c:url value='/authoring/addOption.do'/>";
	const CONFIRM_DELETE_ANSWER_LABEL = "<fmt:message key="label.do.you.want.to.delete.answer"></fmt:message>";
	const SLIDER_NONE_LABEL = "<fmt:message key="label.authoring.basic.none" />";
	const VALIDATION_ERROR_LABEL = "<fmt:message key='error.form.validation.error'/>";
	const VALIDATION_ERRORS_LABEL = "<fmt:message key='error.form.validation.errors'><fmt:param >{errors_counter}</fmt:param></fmt:message>";
</script>
<script type="text/javascript" src="<lams:WebAppURL/>includes/javascript/authoring-question.js"></script>
<script type="text/javascript" src="<lams:WebAppURL/>includes/javascript/authoring-options.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.slider.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.touch-punch.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/Sortable.js"></script>