<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<title><fmt:message key="authoring.fla.page.title" /></title>
	
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-redmond-theme.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/yui/treeview.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/yui/folders.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/authoring.css" type="text/css" media="screen">

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.dialogextend.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.simple-color.js"></script>
	
	<!-- Fix for iPad
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.ui.touch-punch.js"></script>
	-->
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/dialog.js"></script>	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/yui/yahoo-dom-event.js" ></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/yui/animation-min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/yui/json-min.js" ></script> 
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/yui/treeview-min.js" ></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/snap.svg.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringGeneral.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringActivity.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringDecoration.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringProperty.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringHandler.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringMenu.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/canvg/rgbcolor.js"></script> 
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/canvg/StackBlur.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/canvg/canvg.js"></script> 
	<script type="text/javascript">
		var LAMS_URL = '<lams:LAMSURL/>',
			LD_THUMBNAIL_URL_BASE = LAMS_URL + 'home.do?method=createLearningDesignThumbnail&ldId=',

			decoderDiv = $('<div />'),
			LABELS = {
				// ActivityLib
				<fmt:message key="authoring.fla.default.group.title" var="DEFAULT_GROUPING_TITLE_VAR"/>
				DEFAULT_GROUPING_TITLE : '<c:out value="${DEFAULT_GROUPING_TITLE_VAR}" />',
				<fmt:message key="authoring.fla.default.group.prefix" var="DEFAULT_GROUP_PREFIX_VAR"/>
				DEFAULT_GROUP_PREFIX : '<c:out value="${DEFAULT_GROUP_PREFIX_VAR}" />',
				<fmt:message key="authoring.fla.default.branching.title" var="DEFAULT_BRANCHING_TITLE_VAR"/>
				DEFAULT_BRANCHING_TITLE : '<c:out value="${DEFAULT_BRANCHING_TITLE_VAR}" />',
				<fmt:message key="authoring.fla.default.branch.prefix" var="DEFAULT_BRANCH_PREFIX_VAR"/>
				DEFAULT_BRANCH_PREFIX : '<c:out value="${DEFAULT_BRANCH_PREFIX_VAR}" />',
				<fmt:message key="authoring.fla.default.optional.activity.title" var="DEFAULT_OPTIONAL_ACTIVITY_TITLE_VAR"/>
				DEFAULT_OPTIONAL_ACTIVITY_TITLE : '<c:out value="${DEFAULT_OPTIONAL_ACTIVITY_TITLE_VAR}" />',
				<fmt:message key="authoring.fla.support.activity.title" var="SUPPORT_ACTIVITY_TITLE_VAR"/>
				SUPPORT_ACTIVITY_TITLE : '<c:out value="${SUPPORT_ACTIVITY_TITLE_VAR}" />',
				<fmt:message key="authoring.fla.gate.activity.label" var="GATE_ACTIVITY_LABEL_VAR"/>
				GATE_ACTIVITY_LABEL : '<c:out value="${GATE_ACTIVITY_LABEL_VAR}" />',
				<fmt:message key="authoring.fla.branching.start.suffix" var="BRANCHING_START_SUFFIX_VAR"/>
				BRANCHING_START_SUFFIX : '<c:out value="${BRANCHING_START_SUFFIX_VAR}" />',
				<fmt:message key="authoring.fla.branching.end.suffix" var="BRANCHING_END_SUFFIX_VAR"/>
				BRANCHING_END_SUFFIX : '<c:out value="${BRANCHING_END_SUFFIX_VAR}" />',
				<fmt:message key="authoring.fla.activity.dialog.title.suffix" var="ACTIVITY_DIALOG_TITLE_SUFFIX_VAR"/>
				ACTIVITY_DIALOG_TITLE_SUFFIX : '<c:out value="${ACTIVITY_DIALOG_TITLE_SUFFIX_VAR}" />',
				<fmt:message key="authoring.fla.remove.activity.confirm" var="REMOVE_ACTIVITY_CONFIRM_VAR"/>
				REMOVE_ACTIVITY_CONFIRM : decoderDiv.html('<c:out value="${REMOVE_ACTIVITY_CONFIRM_VAR}" />').text(),
				<fmt:message key="authoring.fla.branching.create.confirm" var="BRANCHING_CREATE_CONFIRM_VAR"/>
				BRANCHING_CREATE_CONFIRM : decoderDiv.html('<c:out value="${BRANCHING_CREATE_CONFIRM_VAR}" />').text(),
				<fmt:message key="authoring.fla.transition.to.exists.error" var="TRANSITION_TO_EXISTS_ERROR_VAR"/>
				TRANSITION_TO_EXISTS_ERROR : decoderDiv.html('<c:out value="${TRANSITION_TO_EXISTS_ERROR_VAR}" />').text(),
				<fmt:message key="authoring.fla.circular.sequence.error" var="CIRCULAR_SEQUENCE_ERROR_VAR"/>
				CIRCULAR_SEQUENCE_ERROR : decoderDiv.html('<c:out value="${CIRCULAR_SEQUENCE_ERROR_VAR}" />').text(),
				<fmt:message key="authoring.fla.activity.in.container.error" var="ACTIVITY_IN_CONTAINER_ERROR_VAR"/>
				ACTIVITY_IN_CONTAINER_ERROR : decoderDiv.html('<c:out value="${ACTIVITY_IN_CONTAINER_ERROR_VAR}" />').text(),
				
				// DecorationLib
				<fmt:message key="authoring.fla.default.annotation.label.title" var="DEFAULT_ANNOTATION_LABEL_TITLE_VAR"/>
				DEFAULT_ANNOTATION_LABEL_TITLE : '<c:out value="${DEFAULT_ANNOTATION_LABEL_TITLE_VAR}" />',
				<fmt:message key="authoring.fla.region.fit.button.tooltip" var="REGION_FIT_BUTTON_TOOLTIP_VAR"/>
				REGION_FIT_BUTTON_TOOLTIP : '<c:out value="${REGION_FIT_BUTTON_TOOLTIP_VAR}" />',
				
				// General
				<fmt:message key="authoring.fla.new.folder.button" var="NEW_FOLDER_BUTTON_VAR"/>
				NEW_FOLDER_BUTTON : '<c:out value="${NEW_FOLDER_BUTTON_VAR}" />',
				<fmt:message key="authoring.fla.copy.button" var="COPY_BUTTON_VAR"/>
				COPY_BUTTON : '<c:out value="${COPY_BUTTON_VAR}" />',
				<fmt:message key="authoring.fla.paste.button" var="PASTE_BUTTON_VAR"/>
				PASTE_BUTTON : '<c:out value="${PASTE_BUTTON_VAR}" />',
				<fmt:message key="authoring.fla.delete.button" var="DELETE_BUTTON_VAR"/>
				DELETE_BUTTON : '<c:out value="${DELETE_BUTTON_VAR}" />',
				<fmt:message key="authoring.fla.rename.button" var="RENAME_BUTTON_VAR"/>
				RENAME_BUTTON : '<c:out value="${RENAME_BUTTON_VAR}" />',
				<fmt:message key="authoring.fla.open.button" var="OPEN_BUTTON_VAR"/>
				OPEN_BUTTON : '<c:out value="${OPEN_BUTTON_VAR}" />',
				<fmt:message key="authoring.fla.save.button" var="SAVE_BUTTON_VAR"/>
				SAVE_BUTTON : '<c:out value="${SAVE_BUTTON_VAR}" />',
				<fmt:message key="authoring.fla.import.button" var="IMPORT_BUTTON_VAR"/>
				IMPORT_BUTTON : '<c:out value="${IMPORT_BUTTON_VAR}" />',
				<fmt:message key="authoring.fla.folder" var="FOLDER_VAR"/>
				FOLDER : '<c:out value="${FOLDER_VAR}" />',
				<fmt:message key="authoring.fla.sequence" var="SEQUENCE_VAR"/>
				SEQUENCE : '<c:out value="${SEQUENCE_VAR}" />',
				<fmt:message key="authoring.fla.sequence.not.valid" var="SEQUENCE_NOT_VALID_VAR"/>
				SEQUENCE_NOT_VALID : decoderDiv.html('<c:out value="${SEQUENCE_NOT_VALID_VAR}" />').text(),
				<fmt:message key="authoring.fla.sequence.validation.issues" var="SEQUENCE_VALIDATION_ISSUES_VAR"/>
				SEQUENCE_VALIDATION_ISSUES : '<c:out value="${SEQUENCE_VALIDATION_ISSUES_VAR}" />',
				<fmt:message key="authoring.fla.save.successful" var="SAVE_SUCCESSFUL_VAR"/>
				SAVE_SUCCESSFUL : decoderDiv.html('<c:out value="${SAVE_SUCCESSFUL_VAR}" />').text(),
				<fmt:message key="authoring.fla.delete.node.confirm" var="DELETE_NODE_CONFIRM_VAR"/>
				DELETE_NODE_CONFIRM : decoderDiv.html('<c:out value="${DELETE_NODE_CONFIRM_VAR}" />').text(),
				<fmt:message key="authoring.fla.sequence.overwrite.confirm" var="SEQUENCE_OVERWRITE_CONFIRM_VAR"/>
				SEQUENCE_OVERWRITE_CONFIRM : decoderDiv.html('<c:out value="${SEQUENCE_OVERWRITE_CONFIRM_VAR}" />').text(),
				<fmt:message key="authoring.fla.new.folder.title.prompt" var="NEW_FOLDER_TITLE_PROMPT_VAR"/>
				NEW_FOLDER_TITLE_PROMPT : decoderDiv.html('<c:out value="${NEW_FOLDER_TITLE_PROMPT_VAR}" />').text(),
				<fmt:message key="authoring.fla.rename.title.prompt" var="RENAME_TITLE_PROMPT_VAR"/>
				RENAME_TITLE_PROMPT : decoderDiv.html('<c:out value="${RENAME_TITLE_PROMPT_VAR}" />').text(),
				<fmt:message key="authoring.fla.save.sequence.title.prompt" var="SAVE_SEQUENCE_TITLE_PROMPT_VAR"/>
				SAVE_SEQUENCE_TITLE_PROMPT : decoderDiv.html('<c:out value="${SAVE_SEQUENCE_TITLE_PROMPT_VAR}" />').text(),
				<fmt:message key="authoring.fla.import.part.choose.prompt" var="IMPORT_PART_CHOOSE_PROMPT_VAR"/>
				IMPORT_PART_CHOOSE_PROMPT : decoderDiv.html('<c:out value="${IMPORT_PART_CHOOSE_PROMPT_VAR}" />').text(),
				<fmt:message key="authoring.fla.folder.not.selected.error" var="FOLDER_NOT_SELECTED_ERROR_VAR"/>
				FOLDER_NOT_SELECTED_ERROR : decoderDiv.html('<c:out value="${FOLDER_NOT_SELECTED_ERROR_VAR}" />').text(),
				<fmt:message key="authoring.fla.title.validation.error" var="TITLE_VALIDATION_ERROR_VAR"/>
				TITLE_VALIDATION_ERROR : decoderDiv.html('<c:out value="${TITLE_VALIDATION_ERROR_VAR}" />').text(),
				<fmt:message key="authoring.fla.folder.exists.error" var="FOLDER_EXISTS_ERROR_VAR"/>
				FOLDER_EXISTS_ERROR : decoderDiv.html('<c:out value="${FOLDER_EXISTS_ERROR_VAR}" />').text(),
				<fmt:message key="authoring.fla.sequence.exists.error" var="SEQUENCE_EXISTS_ERROR_VAR"/>
				SEQUENCE_EXISTS_ERROR : decoderDiv.html('<c:out value="${SEQUENCE_EXISTS_ERROR_VAR}" />').text(),
				<fmt:message key="authoring.fla.sequence.save.error" var="SEQUENCE_SAVE_ERROR_VAR"/>
				SEQUENCE_SAVE_ERROR : decoderDiv.html('<c:out value="${SEQUENCE_SAVE_ERROR_VAR}" />').text(),
				<fmt:message key="authoring.fla.sequence.not.selected.error" var="SEQUENCE_NOT_SELECTED_ERROR_VAR"/>
				SEQUENCE_NOT_SELECTED_ERROR : decoderDiv.html('<c:out value="${SEQUENCE_NOT_SELECTED_ERROR_VAR}" />').text(),
				<fmt:message key="authoring.fla.sequence.load.error" var="SEQUENCE_LOAD_ERROR_VAR"/>
				SEQUENCE_LOAD_ERROR : decoderDiv.html('<c:out value="${SEQUENCE_LOAD_ERROR_VAR}" />').text(),
				
				// HandlerLib
				<fmt:message key="authoring.fla.transition.from.exists.error" var="TRANSITION_FROM_EXISTS_ERROR_VAR"/>
				TRANSITION_FROM_EXISTS_ERROR : decoderDiv.html('<c:out value="${TRANSITION_FROM_EXISTS_ERROR_VAR}" />').text(),
				
				// MenuLib
				<fmt:message key="authoring.fla.export.image.dialog.title" var="EXPORT_IMAGE_DIALOG_TITLE_VAR"/>
				EXPORT_IMAGE_DIALOG_TITLE : '<c:out value="${EXPORT_IMAGE_DIALOG_TITLE_VAR}" />',
				<fmt:message key="authoring.fla.export.sequence.dialog.title" var="EXPORT_SEQUENCE_DIALOG_TITLE_VAR"/>
				EXPORT_SEQUENCE_DIALOG_TITLE : '<c:out value="${EXPORT_SEQUENCE_DIALOG_TITLE_VAR}" />',
				<fmt:message key="authoring.fla.activity.copy.title.prefix" var="ACTIVITY_COPY_TITLE_PREFIX_VAR"/>
				ACTIVITY_COPY_TITLE_PREFIX : '<c:out value="${ACTIVITY_COPY_TITLE_PREFIX_VAR}" />',
				<fmt:message key="authoring.fla.preview.lesson.default.title" var="PREVIEW_LESSON_DEFAULT_TITLE_VAR"/>
				PREVIEW_LESSON_DEFAULT_TITLE : '<c:out value="${PREVIEW_LESSON_DEFAULT_TITLE_VAR}" />',
				<fmt:message key="authoring.fla.save.dialog.title" var="SAVE_DIALOG_TITLE_VAR"/>
				SAVE_DIALOG_TITLE : '<c:out value="${SAVE_DIALOG_TITLE_VAR}" />',
				<fmt:message key="authoring.fla.open.dialog.title" var="OPEN_DIALOG_TITLE_VAR"/>
				OPEN_DIALOG_TITLE : '<c:out value="${OPEN_DIALOG_TITLE_VAR}" />',
				<fmt:message key="authoring.fla.import.dialog.title" var="IMPORT_DIALOG_TITLE_VAR"/>
				IMPORT_DIALOG_TITLE : '<c:out value="${IMPORT_DIALOG_TITLE_VAR}" />',
				<fmt:message key="authoring.fla.import.part.dialog.title" var="IMPORT_PART_DIALOG_TITLE_VAR"/>
				IMPORT_PART_DIALOG_TITLE : '<c:out value="${IMPORT_PART_DIALOG_TITLE_VAR}" />',
				<fmt:message key="label.tab.lesson.sequence.folder" var="RUN_SEQUENCES_FOLDER_VAR"/>
				RUN_SEQUENCES_FOLDER : '<c:out value="${RUN_SEQUENCES_FOLDER_VAR}" />',
				<fmt:message key="authoring.fla.arrange.confirm" var="ARRANGE_CONFIRM_VAR"/>
				ARRANGE_CONFIRM : decoderDiv.html('<c:out value="${ARRANGE_CONFIRM_VAR}" />').text(),
				<fmt:message key="authoring.fla.clear.canvas.confirm" var="CLEAR_CANVAS_CONFIRM_VAR"/>
				CLEAR_CANVAS_CONFIRM : decoderDiv.html('<c:out value="${CLEAR_CANVAS_CONFIRM_VAR}" />').text(),
				<fmt:message key="authoring.fla.branching.start.place.prompt" var="BRANCHING_START_PLACE_PROMPT_VAR"/>
				BRANCHING_START_PLACE_PROMPT : decoderDiv.html('<c:out value="${BRANCHING_START_PLACE_PROMPT_VAR}" />').text(),
				<fmt:message key="authoring.fla.branching.end.place.prompt" var="BRANCHING_END_PLACE_PROMPT_VAR"/>
				BRANCHING_END_PLACE_PROMPT : decoderDiv.html('<c:out value="${BRANCHING_END_PLACE_PROMPT_VAR}" />').text(),
				<fmt:message key="authoring.fla.annotation.region.place.prompt" var="ANNOTATION_REGION_PLACE_PROMPT_VAR"/>
				ANNOTATION_REGION_PLACE_PROMPT : decoderDiv.html('<c:out value="${ANNOTATION_REGION_PLACE_PROMPT_VAR}" />').text(),
				<fmt:message key="authoring.fla.annotation.label.place.prompt" var="ANNOTATION_LABEL_PLACE_PROMPT_VAR"/>
				ANNOTATION_LABEL_PLACE_PROMPT : decoderDiv.html('<c:out value="${ANNOTATION_LABEL_PLACE_PROMPT_VAR}" />').text(),
				<fmt:message key="authoring.fla.optional.activity.place.prompt" var="OPTIONAL_ACTIVITY_PLACE_PROMPT_VAR"/>
				OPTIONAL_ACTIVITY_PLACE_PROMPT : decoderDiv.html('<c:out value="${OPTIONAL_ACTIVITY_PLACE_PROMPT_VAR}" />').text(),
				<fmt:message key="authoring.fla.support.activity.place.prompt" var="SUPPORT_ACTIVITY_PLACE_PROMPT_VAR"/>
				SUPPORT_ACTIVITY_PLACE_PROMPT : decoderDiv.html('<c:out value="${SUPPORT_ACTIVITY_PLACE_PROMPT_VAR}" />').text(),
				<fmt:message key="authoring.fla.transition.place.prompt" var="TRANSITION_PLACE_PROMPT_VAR"/>
				TRANSITION_PLACE_PROMPT : decoderDiv.html('<c:out value="${TRANSITION_PLACE_PROMPT_VAR}" />').text(),
				<fmt:message key="authoring.fla.paste.error" var="PASTE_ERROR_VAR"/>
				PASTE_ERROR : decoderDiv.html('<c:out value="${PASTE_ERROR_VAR}" />').text(),
				<fmt:message key="authoring.fla.preview.error" var="PREVIEW_ERROR_VAR"/>
				PREVIEW_ERROR : decoderDiv.html('<c:out value="${PREVIEW_ERROR_VAR}" />').text(),
				<fmt:message key="authoring.fla.cross.branching.error" var="CROSS_BRANCHING_ERROR_VAR"/>
				CROSS_BRANCHING_ERROR : decoderDiv.html('<c:out value="${CROSS_BRANCHING_ERROR_VAR}" />').text(),
				<fmt:message key="authoring.fla.support.transition.error" var="SUPPORT_TRANSITION_ERROR_VAR"/>
				SUPPORT_TRANSITION_ERROR : decoderDiv.html('<c:out value="${SUPPORT_TRANSITION_ERROR_VAR}" />').text(),
				<fmt:message key="authoring.fla.end.match.error" var="END_MATCH_ERROR_VAR"/>
				END_MATCH_ERROR : decoderDiv.html('<c:out value="${END_MATCH_ERROR_VAR}" />').text(),
				
				
				// PropertyLib
				<fmt:message key="authoring.fla.ok.button" var="OK_BUTTON_VAR"/>
				OK_BUTTON : '<c:out value="${OK_BUTTON_VAR}" />',
				<fmt:message key="authoring.fla.cancel.button" var="CANCEL_BUTTON_VAR"/>
				CANCEL_BUTTON : '<c:out value="${CANCEL_BUTTON_VAR}" />',
				<fmt:message key="authoring.fla.clear.all.button" var="CLEAR_ALL_BUTTON_VAR"/>
				CLEAR_ALL_BUTTON : '<c:out value="${CLEAR_ALL_BUTTON_VAR}" />',
				<fmt:message key="authoring.fla.refresh.button" var="REFRESH_BUTTON_VAR"/>
				REFRESH_BUTTON : '<c:out value="${REFRESH_BUTTON_VAR}" />',
				<fmt:message key="authoring.fla.remove.condition.button" var="REMOVE_CONDITION_BUTTON_VAR"/>
				REMOVE_CONDITION_BUTTON : '<c:out value="${REMOVE_CONDITION_BUTTON_VAR}" />',
				<fmt:message key="authoring.fla.properties.dialog.title" var="PROPERTIES_DIALOG_TITLE_VAR"/>
				PROPERTIES_DIALOG_TITLE : '<c:out value="${PROPERTIES_DIALOG_TITLE_VAR}" />',
				<fmt:message key="authoring.fla.group.naming.dialog.title" var="GROUP_NAMING_DIALOG_TITLE_VAR"/>
				GROUP_NAMING_DIALOG_TITLE : '<c:out value="${GROUP_NAMING_DIALOG_TITLE_VAR}" />',
				<fmt:message key="authoring.fla.groups.to.branches.match.dialog_title" var="GROUPS_TO_BRANCHES_MATCH_DIALOG_TITLE_VAR"/>
				GROUPS_TO_BRANCHES_MATCH_DIALOG_TITLE : '<c:out value="${GROUPS_TO_BRANCHES_MATCH_DIALOG_TITLE_VAR}" />',
				<fmt:message key="authoring.fla.branch.mapping.groups.header" var="BRANCH_MAPPING_GROUPS_HEADER_VAR"/>
				BRANCH_MAPPING_GROUPS_HEADER : '<c:out value="${BRANCH_MAPPING_GROUPS_HEADER_VAR}" />',
				<fmt:message key="authoring.fla.branch.mapping.group.header" var="BRANCH_MAPPING_GROUP_HEADER_VAR"/>
				BRANCH_MAPPING_GROUP_HEADER : '<c:out value="${BRANCH_MAPPING_GROUP_HEADER_VAR}" />',
				<fmt:message key="authoring.fla.conditions.dialog.title" var="CONDITIONS_DIALOG_TITLE_VAR"/>
				CONDITIONS_DIALOG_TITLE : '<c:out value="${CONDITIONS_DIALOG_TITLE_VAR}" />',
				<fmt:message key="authoring.fla.branch.mapping.conditions.header" var="BRANCH_MAPPING_CONDITIONS_HEADER_VAR"/>
				BRANCH_MAPPING_CONDITIONS_HEADER : '<c:out value="${BRANCH_MAPPING_CONDITIONS_HEADER_VAR}" />',
				<fmt:message key="authoring.fla.branch.mapping.condition.header" var="BRANCH_MAPPING_CONDITION_HEADER_VAR"/>
				BRANCH_MAPPING_CONDITION_HEADER : '<c:out value="${BRANCH_MAPPING_CONDITION_HEADER_VAR}" />',
				<fmt:message key="authoring.fla.branch.mapping.gate.header" var="BRANCH_MAPPING_GATE_HEADER_VAR"/>
				BRANCH_MAPPING_GATE_HEADER : '<c:out value="${BRANCH_MAPPING_GATE_HEADER_VAR}" />',
				<fmt:message key="authoring.fla.branch.mapping.branches.header" var="BRANCH_MAPPING_BRANCHES_HEADER_VAR"/>
				BRANCH_MAPPING_BRANCHES_HEADER : '<c:out value="${BRANCH_MAPPING_BRANCHES_HEADER_VAR}" />',
				<fmt:message key="authoring.fla.branch.mapping.branch.header" var="BRANCH_MAPPING_BRANCH_HEADER_VAR"/>
				BRANCH_MAPPING_BRANCH_HEADER : '<c:out value="${BRANCH_MAPPING_BRANCH_HEADER_VAR}" />',
				<fmt:message key="authoring.fla.gate.state.mapping.dialog.title" var="GATE_STATE_MAPPING_DIALOG_TITLE_VAR"/>
				GATE_STATE_MAPPING_DIALOG_TITLE : '<c:out value="${GATE_STATE_MAPPING_DIALOG_TITLE_VAR}" />',
				<fmt:message key="authoring.fla.branch.mapping.dialog.title" var="BRANCH_MAPPING_DIALOG_TITLE_VAR"/>
				BRANCH_MAPPING_DIALOG_TITLE : '<c:out value="${BRANCH_MAPPING_DIALOG_TITLE_VAR}" />',
				<fmt:message key="authoring.fla.gate.state.open" var="GATE_STATE_OPEN_VAR"/>
				GATE_STATE_OPEN : '<c:out value="${GATE_STATE_OPEN_VAR}" />',
				<fmt:message key="authoring.fla.gate.state.closed" var="GATE_STATE_CLOSED_VAR"/>
				GATE_STATE_CLOSED : '<c:out value="${GATE_STATE_CLOSED_VAR}" />',
				<fmt:message key="authoring.fla.branch.mapping.default.branch.suffix" var="BRANCH_MAPPING_DEFAULT_BRANCH_SUFFIX_VAR"/>
				BRANCH_MAPPING_DEFAULT_BRANCH_SUFFIX : '<c:out value="${BRANCH_MAPPING_DEFAULT_BRANCH_SUFFIX_VAR}" />',
				<fmt:message key="authoring.fla.complex.output.suffix" var="COMPLEX_OUTPUT_SUFFIX_VAR"/>
				COMPLEX_OUTPUT_SUFFIX : '<c:out value="${COMPLEX_OUTPUT_SUFFIX_VAR}" />',
				<fmt:message key="authoring.fla.range.output.suffix" var="RANGE_OUTPUT_SUFFIX_VAR"/>
				RANGE_OUTPUT_SUFFIX : '<c:out value="${RANGE_OUTPUT_SUFFIX_VAR}" />',
				<fmt:message key="authoring.fla.range.condition.description" var="RANGE_CONDITION_DESCRIPTION_VAR"/>
				RANGE_CONDITION_DESCRIPTION : '<c:out value="${RANGE_CONDITION_DESCRIPTION_VAR}" />',
				<fmt:message key="authoring.fla.exact.condition.description" var="EXACT_CONDITION_DESCRIPTION_VAR"/>
				EXACT_CONDITION_DESCRIPTION : '<c:out value="${EXACT_CONDITION_DESCRIPTION_VAR}" />',
				<fmt:message key="authoring.fla.less.condition.description" var="LESS_CONDITION_DESCRIPTION_VAR"/>
				LESS_CONDITION_DESCRIPTION : '<c:out value="${LESS_CONDITION_DESCRIPTION_VAR}" />',
				<fmt:message key="authoring.fla.greater.condition.description" var="GREATER_CONDITION_DESCRIPTION_VAR"/>
				GREATER_CONDITION_DESCRIPTION : '<c:out value="${GREATER_CONDITION_DESCRIPTION_VAR}" />',
				<fmt:message key="authoring.fla.default.range.condition.title.prefix" var="DEFAULT_RANGE_CONDITION_TITLE_PREFIX_VAR"/>
				DEFAULT_RANGE_CONDITION_TITLE_PREFIX : '<c:out value="${DEFAULT_RANGE_CONDITION_TITLE_PREFIX_VAR}" />',
				<fmt:message key="authoring.fla.clear.all.confirm" var="CLEAR_ALL_CONFIRM_VAR"/>
				CLEAR_ALL_CONFIRM : decoderDiv.html('<c:out value="${CLEAR_ALL_CONFIRM_VAR}" />').text(),
				<fmt:message key="authoring.fla.remove.condition.confirm" var="REMOVE_CONDITION_CONFIRM_VAR"/>
				REMOVE_CONDITION_CONFIRM : decoderDiv.html('<c:out value="${REMOVE_CONDITION_CONFIRM_VAR}" />').text(),
				<fmt:message key="authoring.fla.refresh.conditions.confirm" var="REFRESH_CONDITIONS_CONFIRM_VAR"/>
				REFRESH_CONDITIONS_CONFIRM : decoderDiv.html('<c:out value="${REFRESH_CONDITIONS_CONFIRM_VAR}" />').text(),
				<fmt:message key="authoring.fla.conditions.to.default.gate.state.confirm" var="CONDITIONS_TO_DEFAULT_GATE_STATE_CONFIRM_VAR"/>
				CONDITIONS_TO_DEFAULT_GATE_STATE_CONFIRM : decoderDiv.html('<c:out value="${CONDITIONS_TO_DEFAULT_GATE_STATE_CONFIRM_VAR}" />').text(),
				<fmt:message key="authoring.fla.conditions.to.default.branch.confirm" var="CONDITIONS_TO_DEFAULT_BRANCH_CONFIRM_VAR"/>
				CONDITIONS_TO_DEFAULT_BRANCH_CONFIRM : decoderDiv.html('<c:out value="${CONDITIONS_TO_DEFAULT_BRANCH_CONFIRM_VAR}" />').text(),
				<fmt:message key="authoring.fla.groups.to.default.branch.confirm" var="GROUPS_TO_DEFAULT_BRANCH_CONFIRM_VAR"/>
				GROUPS_TO_DEFAULT_BRANCH_CONFIRM : decoderDiv.html('<c:out value="${GROUPS_TO_DEFAULT_BRANCH_CONFIRM_VAR}" />').text(),
				<fmt:message key="authoring.fla.range.condition.add.start.error" var="RANGE_CONDITION_ADD_START_ERROR_VAR"/>
				RANGE_CONDITION_ADD_START_ERROR : decoderDiv.html('<c:out value="${RANGE_CONDITION_ADD_START_ERROR_VAR}" />').text(),
				<fmt:message key="authoring.fla.range.condition.add.end.error" var="RANGE_CONDITION_ADD_END_ERROR_VAR"/>
				RANGE_CONDITION_ADD_END_ERROR : decoderDiv.html('<c:out value="${RANGE_CONDITION_ADD_END_ERROR_VAR}" />').text(),
				<fmt:message key="authoring.fla.group.title.validation.erorr" var="GROUP_TITLE_VALIDATION_ERORR_VAR"/>
				GROUP_TITLE_VALIDATION_ERORR : decoderDiv.html('<c:out value="${GROUP_TITLE_VALIDATION_ERORR_VAR}" />').text()
			},
			
			isReadOnlyMode = false,
			activitiesOnlySelectable = false,
			initContentFolderID = '${contentFolderID}',
			initLearningDesignID = '${param.learningDesignID}',
			learningLibraryGroups = ${learningLibraryGroups},
			initAccess = ${access};
	</script>
</lams:head>
<body onresize="javascript:GeneralLib.resizePaper()">
	<div id="toolbar" class="ui-corner-all">
		<div id="newButton" class="ui-button" onClick="javascript:GeneralLib.newLearningDesign(false, false)">
			<fmt:message key="authoring.fla.page.menu.new" />
		</div>
		<div>
			<div class="split-ui-button">
				<div id="openButton" onClick="javascript:MenuLib.openLearningDesign()">
					<span><fmt:message key="authoring.fla.page.menu.open" /></span>
				</div>
				<div id="openDropButton">&nbsp;</div>
			</div>
			<ul>
				<li id="importSequenceButton" onClick="javascript:MenuLib.importLearningDesign()"><fmt:message key="authoring.fla.page.menu.import" /></li>
				<li id="importPartSequenceButton" onClick="javascript:MenuLib.importPartLearningDesign()"><fmt:message key="authoring.fla.page.menu.import.part" /></li>
			</ul>
		</div>
		<div>
			<div class="split-ui-button">
				<div id="saveButton" onClick="javascript:MenuLib.saveLearningDesign()">
					<span><fmt:message key="authoring.fla.page.menu.save" /></span>
				</div>
				<div id="saveDropButton">&nbsp;</div>
			</div>
			<ul>
				<li id="saveAsButton" onClick="javascript:MenuLib.saveLearningDesign(true)"><fmt:message key="authoring.fla.page.menu.saveas" /></li>
				<li id="exportButton"><fmt:message key="authoring.fla.page.menu.export" /><span class="ui-icon ui-menu-icon ui-icon-carat-1-e"></span>
					<ul>
						<li id="exportLamsButton" class="exportSequenceButton"
							title="Standard LAMS ZIP format"
							onClick="javascript:MenuLib.exportLearningDesign(1)"><fmt:message key="authoring.fla.page.menu.export.lams" /></li>
						<li id="exportImsButton" class="exportSequenceButton"
							title="<fmt:message key='authoring.fla.page.menu.export.ims.tooltip' />"
							onClick="javascript:MenuLib.exportLearningDesign(2)"><fmt:message key="authoring.fla.page.menu.export.ims" /></li>
						<li id="exportPngButton" class="exportImageButton" onClick="javascript:MenuLib.exportPNG(true)"><fmt:message key="authoring.fla.page.menu.export.png" /></li>
						<li id="exportSvgButton" class="exportImageButton" onClick="javascript:MenuLib.exportSVG(true)"><fmt:message key="authoring.fla.page.menu.export.svg" /></li>
					</ul>
				</li>
			</ul>
		</div>
		<div id="copyButton" class="ui-button" onClick="javascript:MenuLib.copyActivity()">
			<fmt:message key="authoring.fla.page.menu.copy" />
		</div>
		<div id="pasteButton" class="ui-button" onClick="javascript:MenuLib.pasteActivity()">
			<fmt:message key="authoring.fla.page.menu.paste" />
		</div>
		<div id="transitionButton" class="ui-button" onClick="javascript:MenuLib.addTransition()">
			<fmt:message key="authoring.fla.page.menu.transition" />
		</div>
		<div>
			<div id="optionalButton" class="split-ui-button">
				<div>
					<span><fmt:message key="authoring.fla.page.menu.optional" /></span>
				</div>
				<div id="optionalDropButton">&nbsp;</div>
			</div>
			<ul>
				<li id="optionalActivityButton" onClick="javascript:MenuLib.addOptionalActivity()"><fmt:message key="authoring.fla.page.menu.optional.activity" /></li>
				<li id="floatingActivityButton" onClick="javascript:MenuLib.addFloatingActivity()"><fmt:message key="authoring.fla.page.menu.optional.support" /></li>
			</ul>
		</div>
		<div>
			<div id="flowButton" class="split-ui-button">
				<div>
					<span><fmt:message key="authoring.fla.page.menu.flow" /></span>
				</div>
				<div id="flowDropButton">&nbsp;</div>
			</div>
			<ul>
				<li id="gateButton" onClick="javascript:MenuLib.addGate()"><fmt:message key="authoring.fla.page.menu.flow.gate" /></li>
				<li id="branchingButton" onClick="javascript:MenuLib.addBranching()"><fmt:message key="authoring.fla.page.menu.flow.branch" /></li>
			</ul>
		</div>
		<div id="groupButton" class="ui-button" onClick="javascript:MenuLib.addGrouping()">
			<fmt:message key="authoring.fla.page.menu.group" />
		</div>
		<div>
			<div id="annotateButton" class="split-ui-button">
				<div>
					<span><fmt:message key="authoring.fla.page.menu.annotate" /></span>
				</div>
				<div id="annotateDropButton">&nbsp;</div>
			</div>
			<ul>
				<li id="annotateLabelButton" onClick="javascript:MenuLib.addAnnotationLabel()"><fmt:message key="authoring.fla.page.menu.annotate.label" /></li>
				<li id="annotateRegionButton" onClick="javascript:MenuLib.addAnnotationRegion()"><fmt:message key="authoring.fla.page.menu.annotate.region" /></li>
			</ul>
		</div>
		<div id="arrangeButton" class="ui-button" onClick="javascript:GeneralLib.arrangeActivities()">
			<fmt:message key="authoring.fla.page.menu.arrange" />
		</div>
		<div id="previewButton" class="ui-button" onClick="javascript:MenuLib.openPreview()">
			<fmt:message key="authoring.fla.page.menu.preview" />
		</div>
		<!-- 
		<div id="zoomButton" class="ui-button" onClick="javascript:MenuLib.zoom()">
			Zoom out
		</div>
 		-->
	</div>
	
	<table id="authoringTable">
		<tr>
			<td id="templateContainerCell">
				<select>
					<option><fmt:message key="authoring.fla.tool.groups.all" /></option>
				</select>
				<div class="templateContainer">
					<c:forEach var="tool" items="${tools}">
						<div
							 Id="tool${tool.toolDisplayName}"
							 toolId="${tool.toolId}"
							 learningLibraryId="${tool.learningLibraryId}"
							 supportsOutputs="${tool.supportsOutputs}"
							 activityCategoryId="${tool.activityCategoryID}"
							 childToolIds="
							 <c:forEach var='childId' items='${tool.childToolIds}'>
							 	${childId},
							 </c:forEach>
							 "
							 class="template">
							<c:if test="${not empty tool.iconPath}">
								<img src="<lams:LAMSURL/>${tool.iconPath}" />
							</c:if>
							<div><c:out value="${tool.toolDisplayName}" /></div>
						</div>
					</c:forEach>
				</div>
			</td>
			<td id="canvasContainerCell">
				<div id="ldDescriptionDiv">
					<div id="ldDescriptionTitleContainer" title="Click to show the sequence description"
						 onClick="javascript:MenuLib.toggleDescriptionDiv()">
						<span id="ldDescriptionFieldTitle"><fmt:message key="authoring.fla.page.ld.title" /></span>
						<span id="ldDescriptionFieldModified"></span>
						<span id="ldDescriptionHideTip">▼</span>
					</div>
					<div id="ldDescriptionDetails">
						<div class="ldDescriptionLabel"><fmt:message key="authoring.fla.page.ld.description" /></div>
						<div id="ldDescriptionEditorContainer">
							<lams:CKEditor id="ldDescriptionFieldDescription" value=""
								   height="300px"
								   contentFolderID="${contentFolderID}"/>
						</div>
						
						<div class="ldDescriptionLabel"><fmt:message key="authoring.fla.page.ld.license" /></div>
						<select id="ldDescriptionLicenseSelect">
							<option value="0" selected="selected"><fmt:message key="authoring.fla.page.ld.license.none" /></option>
							<c:forEach var="license" items="${licenses}">
								<option value="<c:out value='${license.licenseID}' escapeXml='true' />"
										pictureURL="<c:out value='${license.pictureURL}' escapeXml='true' />"
										url="<c:out value='${license.url}' escapeXml='true' />"
								>
									<c:out value='${license.name}' escapeXml='true' />
								</option>
							</c:forEach>
						</select>
						<img id="ldDescriptionLicenseImage" />
						<input id="ldDescriptionLicenseButton" class="button"
							   type="button" value="View" />
						<div id="ldDescriptionLicenseTextContainer">
							<div class="ldDescriptionLabel"><fmt:message key="authoring.fla.page.ld.license.info" /></div>
							<textarea id="ldDescriptionLicenseText" rows="5"></textarea>
						</div>
					</div>
				</div>
				<div id="canvas"></div>
			</td>
		</tr>
	</table>
	
	<!-- DIALOGS CONTENTS -->
	
	<!-- SEQUENCE LOAD/SAVE DIALOG -->
	<div id="ldStoreDialog" class="dialogContainer">
		<table>
			<tr>
				<td id="ldStoreDialogTreeCell">
					<div id="ldStoreDialogTree"></div>
				</td>
				<td id="ldStoreDialogCanvasCell" rowspan="2">
					<div id="ldStoreDialogCanvasDiv">
			    		<img id="ldScreenshotAuthor" class="ldChoiceDependentCanvasElement" />
		    			<img id="ldScreenshotLoading" class="ldChoiceDependentCanvasElement"
		    			     src="<lams:LAMSURL/>images/ajax-loader-big.gif" />
		    			<iframe id="ldStoreDialogImportPartFrame"></iframe>
			    	</div>
				</td>
			</tr>
			<tr>
				<td id="ldStoreDialogAccessCell" >
					<div id="ldStoreDialogAccessTitle"><fmt:message key="authoring.fla.page.dialog.access" /></div>
				</td>
			</tr>
		</table>
		<%-- This will be moved to dialog's button pane using JS --%>
		<div id="ldStoreDialogNameContainer" class="ldStoreDialogSaveOnly">
			<span><fmt:message key="authoring.fla.page.dialog.ld.title" /></span><input id="ldStoreDialogNameField" type="text"/>
		</div>
	</div>
	
	
	<!-- GROUP OR CONDITION TO BRANCH MATCHING DIALOG -->
	
	<div id="branchMappingDialog" class="dialogContainer branchMappingDialog">
		<table>
			<tr>
				<td></td>
				<td></td>
				<td rowspan="2"></td>
				<td colspan="2" class="branchMappingLabelCell">
					<fmt:message key="authoring.fla.page.dialog.mappings" />
				</td>
			</tr>
			<tr>
				<td class="branchMappingLabelCell branchMappingFreeItemHeaderCell"></td>
				<td class="branchMappingLabelCell branchMappingFreeBranchHeaderCell"></td>
				<td class="branchMappingLabelCell branchMappingBoundItemHeaderCell"></td>
				<td class="branchMappingLabelCell branchMappingBoundBranchHeaderCell"></td>
			</tr>
			<tr>
				<td class="branchMappingFreeItemCell branchMappingListCell"></td>
				<td class="branchMappingFreeBranchCell branchMappingListCell"></td>
				<td>
					<div class="branchMappingAddButton branchMappingButton"></div>
					<div class="branchMappingRemoveButton branchMappingButton"></div>
				</td>
				<td class="branchMappingBoundItemCell branchMappingListCell"></td>
				<td class="branchMappingBoundBranchCell branchMappingListCell"></td>
			</tr>
		</table>
	</div>
	
	
	<!-- PROPERTY DIALOG CONTENTS FOR DIFFERENT OBJECT TYPES -->
	
	<div id="propertiesContentTransition" class="dialogContainer">
		<table>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.title" />
				</td>
				<td>
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.default" />
				</td>
				<td>
					<input class="propertiesContentFieldDefault" type="checkbox"></input>
				</td>
			</tr>
		</table>
	</div>
	
	<div id="propertiesContentTool" class="dialogContainer">
		<table>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.title" />
				</td>
				<td>
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.grouping" />
				</td>
				<td>
					  <select class="propertiesContentFieldGrouping"></select>
				</td>
			</tr>
		</table>
	</div>
	
	
	<div id="propertiesContentGrouping" class="dialogContainer">
		<table>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.title" />
				</td>
				<td>
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.grouping.type" />
				</td>
				<td>
					 <select class="propertiesContentFieldGroupingType">
					 	<option value="random"><fmt:message key="authoring.fla.page.prop.grouping.type.random" /></option>
					 	<option value="monitor"><fmt:message key="authoring.fla.page.prop.grouping.type.monitor" /></option>
					 	<option value="learner"><fmt:message key="authoring.fla.page.prop.grouping.type.learner" /></option>
					 </select>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.groups.number" />
				</td>
				<td>
					 <input class="propertiesContentFieldGroupCountEnable" type="radio"
					 		name="propertiesContentFieldGroupDivide"></input>
					 <input class="propertiesContentFieldGroupCount spinner" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.groups.learners" />
				</td>
				<td>
					<input class="propertiesContentFieldLearnerCountEnable" type="radio"
					 		name="propertiesContentFieldGroupDivide"></input>
					 <input class="propertiesContentFieldLearnerCount spinner" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.groups.equal" />
				</td>
				<td>
					 <input class="propertiesContentFieldEqualSizes" type="checkbox"></input>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.groups.view.learners" />
				</td>
				<td>
					 <input class="propertiesContentFieldViewLearners" type="checkbox"></input>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<div class="propertiesContentFieldNameGroups"><fmt:message key="authoring.fla.page.prop.groups.name" /></div>
				</td>
			</tr>
		</table>
	</div>


	<div id="propertiesContentGate" class="dialogContainer">
		<table>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.title" />
				</td>
				<td colspan="3">
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.description" />
				</td>
				<td colspan="3">
					 <textarea class="propertiesContentFieldDescription"></textarea>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.gate.type" />
				</td>
				<td colspan="3">
					 <select class="propertiesContentFieldGateType">
					 	<option value="condition"><fmt:message key="authoring.fla.page.prop.gate.type.condition" /></option>
					 	<option value="sync"><fmt:message key="authoring.fla.page.prop.gate.type.sync" /></option>
					 	<option value="schedule"><fmt:message key="authoring.fla.page.prop.gate.type.schedule" /></option>
					 	<option value="permission"><fmt:message key="authoring.fla.page.prop.gate.type.permission" /></option>
					 </select>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.input" />
				</td>
				<td>
					  <select class="propertiesContentFieldInput"></select>
				</td>
			</tr>
			<tr class="propertiesContentRowGateSchedule">
				<td>
					<fmt:message key="authoring.fla.page.prop.gate.delay" />
				</td>
				<td>
					<input class="propertiesContentFieldOffsetDay spinner" type="text"></input> <fmt:message key="authoring.fla.page.prop.days" />
				</td>
				<td>
					<input class="propertiesContentFieldOffsetHour spinner" type="text"></input> <fmt:message key="authoring.fla.page.prop.hours" />
				</td>
				<td>
					<input class="propertiesContentFieldOffsetMinute spinner" type="text"></input> <fmt:message key="authoring.fla.page.prop.minutes" />
				</td>
			</tr>
			<tr class="propertiesContentRowGateSchedule">
				<td colspan="3">
					<fmt:message key="authoring.fla.page.prop.gate.activity.finish.based" />
				</td>
				<td>
					 <input class="propertiesContentFieldActivityCompletionBased" type="checkbox"></input>
				</td>
			</tr>
			<tr class="propertiesContentRowConditions">
				<td colspan="3">
					<div class="propertiesContentFieldCreateConditions"><fmt:message key="authoring.fla.page.prop.conditions.create" /></div>
				</td>
			</tr>
			<tr class="propertiesContentRowConditions">
				<td colspan="3">
					<div class="propertiesContentFieldMatchConditions"><fmt:message key="authoring.fla.page.prop.gate.conditions.map" /></div>
				</td>
			</tr>
		</table>
	</div>
	
	
	<div id="propertiesContentBranching" class="dialogContainer">
		<table>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.title" />
				</td>
				<td>
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.branching.type" />
				</td>
				<td>
					 <select class="propertiesContentFieldBranchingType">
					 	<option value="chosen"><fmt:message key="authoring.fla.page.prop.branching.type.chose" /></option>
					 	<option value="group"><fmt:message key="authoring.fla.page.prop.branching.type.group" /></option>
					 	<option value="tool"><fmt:message key="authoring.fla.page.prop.branching.type.tool" /></option>
					 	<option value="optional"><fmt:message key="authoring.fla.page.prop.branching.type.optional" /></option>
					 </select>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.grouping" />
				</td>
				<td>
					<select class="propertiesContentFieldGrouping"></select>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.input" />
				</td>
				<td>
					<select class="propertiesContentFieldInput"></select>
				</td>
			</tr>
			<tr class="propertiesContentRowConditions">
				<td colspan="2">
					<div class="propertiesContentFieldCreateConditions"><fmt:message key="authoring.fla.page.prop.conditions.create" /></div>
				</td>
			</tr>
			<tr class="propertiesContentRowConditions">
				<td colspan="2">
					<div class="propertiesContentFieldMatchConditions"><fmt:message key="authoring.fla.page.prop.branching.conditions.match" /></div>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<div class="propertiesContentFieldMatchGroups"><fmt:message key="authoring.fla.page.prop.branching.groups.match" /></div>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.branching.sequences.min" />
				</td>
				<td>
					<input class="propertiesContentFieldOptionalSequenceMin spinner" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.branching.sequences.max" />
				</td>
				<td>
					<input class="propertiesContentFieldOptionalSequenceMax spinner" type="text"></input>
				</td>
			</tr>
		</table>
	</div>
	
		
	<div id="propertiesContentParallel" class="dialogContainer">
		<table>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.title" />
				</td>
				<td>
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.grouping" />
				</td>
				<td>
					  <select class="propertiesContentFieldGrouping"></select>
				</td>
			</tr>
		</table>
	</div>
	
	
	<div id="propertiesContentOptionalActivity" class="dialogContainer">
		<table>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.title" />
				</td>
				<td>
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.optional.activities.min" />
				</td>
				<td>
					<input class="propertiesContentFieldOptionalActivityMin spinner" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.optional.activities.max" />
				</td>
				<td>
					<input class="propertiesContentFieldOptionalActivityMax spinner" type="text"></input>
				</td>
			</tr>
		</table>
	</div>
	
	
	<div id="propertiesContentRegion" class="dialogContainer">
		<table>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.title" />
				</td>
				<td>
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.color" />
				</td>
				<td>
					 <input class="propertiesContentFieldColor" type="text"></input>
				</td>
			</tr>
		</table>
	</div>
	
	
	<div id="propertiesContentLabel" class="dialogContainer">
		<table>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.title" />
				</td>
				<td>
					 <input class="propertiesContentFieldTitle" type="text"></input>
				</td>
			</tr>
		</table>
	</div>
	
	
	<!-- TOOL OUTPUT CONDITIONS DIALOG -->
	<div id="outputConditionsDialog" class="dialogContainer">
		 <select id="outputSelect">
		 	<option value="none"><fmt:message key="authoring.fla.page.dialog.cond.output.choose" /></option>
		 </select>
		 <select id="rangeOptionSelect" class="outputSelectDependent">
		 	<option value="none" selected="selected"><fmt:message key="authoring.fla.page.dialog.cond.options.choose" /></option>
		 	<option value="greater"><fmt:message key="authoring.fla.page.dialog.cond.greater" /></option>
		 	<option value="less"><fmt:message key="authoring.fla.page.dialog.cond.less" /></option>
		 	<option value="range"><fmt:message key="authoring.fla.page.dialog.cond.range" /></option>
		 </select>
		 <div id="rangeAddDiv" class="outputSelectDependent">
			 <input id="singleRangeSpinner" class="outputSelectDependent spinner" />
			 <div id="multiRangeDiv" class="outputSelectDependent">
			 	<fmt:message key="authoring.fla.page.dialog.cond.range.from" /> <input id="multiRangeFromSpinner" class="spinner" />
			 	<fmt:message key="authoring.fla.page.dialog.cond.range.to" /> <input id="multiRangeToSpinner" class="spinner" />
			 </div>
			 <span id="rangeAddButton"><fmt:message key="authoring.fla.page.dialog.cond.add" /></span>
		 </div>
		 <div id="complexConditions" class="outputSelectDependent">
		 	<div><fmt:message key="authoring.fla.page.dialog.cond.name" /></div>
		 	<ul></ul>
		 </div>
		 <table id="rangeConditions" class="outputSelectDependent">
		 	<tr id="rangeConditionsHeaderRow">
		 		<th><fmt:message key="authoring.fla.page.dialog.cond.list.name" /></th>
		 		<th><fmt:message key="authoring.fla.page.dialog.cond.list.condition" /></th>
		 	</tr>
		 </table>
	</div>
	
	
	<!-- EXPORT CANVAS AS IMAGE DIALOG -->
	<div id="exportImageDialog" class="dialogContainer exportDialog">
		<a href="#"><fmt:message key="authoring.fla.page.download.image" /></a>
	</div>
	
	
	<!-- EXPORT LEARNING DESIGN DIALOG -->
	<div id="exportLDDialog" class="dialogContainer exportDialog">
		<span><fmt:message key="authoring.fla.page.download.wait" /><br /><fmt:message key="authoring.fla.page.download.close" /></span>
		<iframe></iframe>
	</div>
</body>
</lams:html>