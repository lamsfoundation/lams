<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<c:set var="allowCreatingQbCollections"><%=Configuration.get(ConfigurationKeys.QB_COLLECTIONS_CREATE_ALLOW)%></c:set>
<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="isAuthoringRestricted" value="${sessionMap.mode == 'teacher'}" />

<lams:html>
	<lams:head>
		<lams:css />
		<link href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" rel="stylesheet" type="text/css">
		<link href="<lams:LAMSURL/>css/qb-question.css" rel="stylesheet" type="text/css">
		<style>
			label.error {
				float: none;
				color: #d9534f;
				vertical-align: top;
				font-weight: bold;
				font-style: italic;
			}
		</style>

		<lams:JSImport src="includes/javascript/common.js" />
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.tabcontroller.js"></script>
		<script type="text/javascript">
			const VALIDATION_ERROR_LABEL = "<fmt:message key='error.form.validation.error'/>";
			const VALIDATION_ERRORS_LABEL = "<fmt:message key='error.form.validation.errors'><fmt:param >{errors_counter}</fmt:param></fmt:message>";
			const ADD_COLLECTION_LABEL = '<fmt:message key="label.questions.choice.collection.new.prompt" />';
			const LAMS_URL = '<lams:LAMSURL/>';

			let isNewQuestion = ${isNewQuestion},
					csrfTokenName = '<csrf:tokenname/>',
					csrfTokenValue = '<csrf:tokenvalue/>';
		</script>
		<lams:JSImport src="includes/javascript/qb-question.js" />
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.form.js"></script>
		<script type="text/javascript">
			function saveQuestion(isNewVersion){
				let form = $('#newQuestionForm');
				if (isNewVersion) {
					action = form.attr('action');
					form.attr('action', action + '&newVersion=true');
				}
				form.submit();
			}

			$(document).ready(function(){

				$("#newQuestionForm").validate({
					ignore : 'div.cke_editable',
					rules: {
						title: "required",
						minWordsLimit: {
							digits: true
						}
					},
					messages: {
						title: "<fmt:message key='label.title.required'/>"
					},
					invalidHandler: function(form, validator) {
						var errors = validator.numberOfInvalids();
						if (errors) {
							var message = errors == 1
									? "<fmt:message key='error.form.validation.error'/>"
									: "<fmt:message key='error.form.validation.errors'><fmt:param >" + errors + "</fmt:param></fmt:message>";

							$("div.error span").html(message);
							$("div.error").show();
						} else {
							$("div.error").hide();
						}
					},
					debug: false,
					submitHandler: function(form) {
						$("#description").val(CKEDITOR.instances.description.getData());
						$("#feedback").val(CKEDITOR.instances.feedback.getData());
						$("#new-collection-uid").val($("#collection-uid-select option:selected").val());

						var items = {
							target:  parent.jQuery('#itemArea'),
							success: function (responseText, statusText)  {
								self.parent.refreshThickbox()
								self.parent.tb_remove();
							}
						};

						$('#newQuestionForm').ajaxSubmit(items);
					},
					invalidHandler: formValidationInvalidHandler,
					errorElement: "em",
					errorPlacement: formValidationErrorPlacement,
					highlight: formValidationHighlight,
					unhighlight: formValidationUnhighlight
				});

				//spinner
				var minimumWordsSpinner = $( "#min-words-limit" ).spinner({
					min: 0,
					disabled: ($( "#min-words-limit" ).val() == 0)
				});

				//spinner
				$("#min-words-limit-checkbox").click(function() {
					if ( minimumWordsSpinner.spinner( "option", "disabled" ) ) {
						minimumWordsSpinner.spinner( "enable" );
					} else {
						minimumWordsSpinner.spinner( "disable" );
					}
				});
			});
		</script>
	</lams:head>
	<body>
	<div class="panel-default add-file">
		<div class="panel-heading">
			<div class="panel-title panel-learner-title">
				<fmt:message key="label.add.new.question" />
			</div>
		</div>

		<div class="panel-body panel-${type}-body">
			<c:set var="csrfToken"><csrf:token/></c:set>
			<form:form action="/lams/tool/laqa11/authoring/saveQuestion.do?${csrfToken}" method="post" modelAttribute="newQuestionForm" id="newQuestionForm">
				<form:hidden path="sessionMapID" />
				<form:hidden path="itemIndex" />
				<form:hidden path="contentFolderID"/>
				<form:hidden path="questionId" />
				<form:hidden path="oldCollectionUid" id="old-collection-uid"/>
				<form:hidden path="newCollectionUid" id="new-collection-uid"/>

				<button type="button" id="question-settings-link" class="btn btn-default btn-sm">
					<fmt:message key="label.settings" />
				</button>

				<div class="question-tab">
					<lams:errors/>

					<div id="title-container" class="form-group">
						<c:set var="TITLE_LABEL"><fmt:message key="label.title"/> </c:set>
						<form:input path="title" id="title" cssClass="form-control borderless-text-input" tabindex="1" maxlength="255"
									placeholder="${TITLE_LABEL}"/>
					</div>

					<div class="form-group form-group-cke">
						<c:set var="QUESTION_DESCRIPTION_LABEL"><fmt:message key="label.question"/></c:set>
						<lams:CKEditor id="description" value="${newQuestionForm.description}" contentFolderID="${newQuestionForm.contentFolderID}"
									   placeholder="${QUESTION_DESCRIPTION_LABEL}"	 />
					</div>
				</div>

				<div class="settings-tab">
					<div class="form-group row form-inline" style="display: flex; align-items: center;">
						<label for="min-words-limit-checkbox" class="col-sm-4">
							<input type="checkbox" id="min-words-limit-checkbox" name="noname"
								   <c:if test="${newQuestionForm.minWordsLimit != 0}">checked="checked"</c:if>/>
							<fmt:message key="label.minimum.number.words" >
								<fmt:param> </fmt:param>
							</fmt:message>
						</label>

						<div class="col-sm-8">
							<form:input path="minWordsLimit" id="min-words-limit"/>
							<label id="min-words-limit-error" class="alert alert-danger" for="min-words-limit" style="display: none;"></label>
						</div>
					</div>

					<div class="form-group">
						<c:set var="FEEDBACK_LABEL"><fmt:message key="label.feedback"/></c:set>
						<lams:CKEditor id="feedback" value="${newQuestionForm.feedback}"
									   placeholder="${FEEDBACK_LABEL}" contentFolderID="${newQuestionForm.contentFolderID}" />
					</div>

					<lams:OutcomeAuthor qbQuestionId="${newQuestionForm.questionId}"  />
				</div>
			</form:form>
		</div>
	</div>

	<footer class="footer fixed-bottom">
		<div class="panel-heading ">
			<div class="col-xs-12x col-md-6x form-groupx rowx form-inlinex btn-group-md voffset5">
					<%-- Hide if the question is not in users' collections  --%>
				<span <c:if test="${empty newQuestionForm.userCollections}">style="visibility: hidden;"</c:if>>
			       		Collection
			        		
						<select class="btn btn-md btn-default" id="collection-uid-select">
							<c:forEach var="collection" items="${newQuestionForm.userCollections}">
								<option value="${collection.uid}"
										<c:if test="${collection.uid == newQuestionForm.oldCollectionUid}">selected="selected"</c:if>>
									<c:out value="${collection.name}" />
								</option>
							</c:forEach>
							<c:if test="${allowCreatingQbCollections}">
								<fmt:message key="label.questions.choice.collection.new.option" var="newOptionLabel"/>
								<option value="-1"><c:out escapeXml="true" value="${newOptionLabel}" /></option>
							</c:if>
						</select>
					</span>

				<div class="pull-right col-xs-12x col-md-6x" style="margin-top: -5px;">
					<a href="#nogo" onclick="javascript:self.parent.tb_remove();" class="btn btn-sm btn-default loffset5">
						<fmt:message key="label.cancel" />
					</a>

					<div class="btn-group btn-group-sm dropup">
						<a id="saveButton" type="button" class="btn btn-sm btn-default button-add-item" onClick="javascript:saveQuestion(false)">
							<fmt:message key="label.save.question" />
						</a>
						<button id="saveDropButton" type="button" class="btn btn-default dropdown-toggle"
								data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							<span class="caret"></span>
							<span class="sr-only">Toggle Dropdown</span>
						</button>
						<ul class="dropdown-menu">
							<li id="saveAsButton" onClick="javascript:saveQuestion(true)"><a href="#">
								<fmt:message key="label.save.question.new.version" />
							</a></li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</footer>

	</body>
</lams:html>