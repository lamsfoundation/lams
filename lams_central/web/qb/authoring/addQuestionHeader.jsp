<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>

 <!-- ********************  CSS ********************** -->
<lams:css />
<link href="<lams:LAMSURL/>/tool/laasse10/includes/css/assessment.css" rel="stylesheet" type="text/css">
<link href="<lams:LAMSURL/>css/qb-question.css" rel="stylesheet" type="text/css">

<!-- ********************  javascript ********************** -->
<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/bootstrap.tabcontroller.js"></script>
<script type="text/javascript">
	const QUESTION_TYPE = ${questionType};
	const ADD_OPTION_URL = "/lams/qb/edit/addOption.do";
	const CONFIRM_DELETE_ANSWER_LABEL = "<fmt:message key="label.do.you.want.to.delete.answer"></fmt:message>";
	const SLIDER_NONE_LABEL = "<fmt:message key="label.authoring.basic.none" />";
	const VALIDATION_ERROR_LABEL = "<fmt:message key='error.form.validation.error'/>";
	const VALIDATION_ERRORS_LABEL = "<fmt:message key='error.form.validation.errors'><fmt:param >{errors_counter}</fmt:param></fmt:message>";
</script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/qb-question.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/qb-option.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.slider.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.touch-punch.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/Sortable.js"></script>