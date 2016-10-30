<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<link rel="stylesheet" href="<lams:LAMSURL/>css/yui/treeview.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/yui/folders.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/defaultHTML_learner.css" type="text/css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/authoring.css" type="text/css" media="screen" />
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.dialogextend.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.simple-color.js"></script>
	
	<!-- Fix for iPad
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.ui.touch-punch.js"></script>
	-->

	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/yui/yahoo-dom-event.js" ></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/yui/animation-min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/yui/json-min.js" ></script> 
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/yui/treeview-min.js" ></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/snap.svg.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/dialog.js"></script>	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringGeneral.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringActivity.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringDecoration.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringProperty.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringHandler.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/authoring/authoringMenu.js"></script>
	<script type="text/javascript">
		var LAMS_URL = '<lams:LAMSURL/>',
			LD_THUMBNAIL_URL_BASE = LAMS_URL + 'home.do?method=getLearningDesignThumbnail&ldId=',

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
				<fmt:message key="authoring.fla.liveedit.readonly.activity.error" var="LIVEEDIT_READONLY_ACTIVITY_ERROR_VAR"/>
				LIVEEDIT_READONLY_ACTIVITY_ERROR : '<c:out value="${LIVEEDIT_READONLY_ACTIVITY_ERROR_VAR}" />',
				<fmt:message key="authoring.fla.liveedit.readonly.move.parent.error" var="LIVEEDIT_READONLY_MOVE_PARENT_ERROR_VAR"/>
				LIVEEDIT_READONLY_MOVE_PARENT_ERROR : '<c:out value="${LIVEEDIT_READONLY_MOVE_PARENT_ERROR_VAR}" />',
				
				
				// DecorationLib
				<fmt:message key="authoring.fla.default.annotation.label.title" var="DEFAULT_ANNOTATION_LABEL_TITLE_VAR"/>
				DEFAULT_ANNOTATION_LABEL_TITLE : '<c:out value="${DEFAULT_ANNOTATION_LABEL_TITLE_VAR}" />',
				<fmt:message key="authoring.fla.region.fit.button.tooltip" var="REGION_FIT_BUTTON_TOOLTIP_VAR"/>
				REGION_FIT_BUTTON_TOOLTIP : '<c:out value="${REGION_FIT_BUTTON_TOOLTIP_VAR}" />',
				
				// General
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
				<fmt:message key="authoring.fla.liveedit.save.successful" var="LIVEEDIT_SAVE_SUCCESSFUL_VAR"/>
				LIVEEDIT_SAVE_SUCCESSFUL : decoderDiv.html('<c:out value="${LIVEEDIT_SAVE_SUCCESSFUL_VAR}" />').text(),
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
				<fmt:message key="authoring.fla.liveedit.cancel.confirm" var="LIVEEDIT_CANCEL_CONFIRM_VAR"/>
				LIVEEDIT_CANCEL_CONFIRM : '<c:out value="${LIVEEDIT_CANCEL_CONFIRM_VAR}" />',
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
				<fmt:message key="authoring.fla.svg.save.error" var="SVG_SAVE_ERROR_VAR"/>
				SVG_SAVE_ERROR : decoderDiv.html('<c:out value="${SVG_SAVE_ERROR_VAR}" />').text(),
				<fmt:message key="authoring.fla.sequence.not.selected.error" var="SEQUENCE_NOT_SELECTED_ERROR_VAR"/>
				SEQUENCE_NOT_SELECTED_ERROR : decoderDiv.html('<c:out value="${SEQUENCE_NOT_SELECTED_ERROR_VAR}" />').text(),
				<fmt:message key="authoring.fla.sequence.load.error" var="SEQUENCE_LOAD_ERROR_VAR"/>
				SEQUENCE_LOAD_ERROR : decoderDiv.html('<c:out value="${SEQUENCE_LOAD_ERROR_VAR}" />').text(),
				
				// HandlerLib
				<fmt:message key="authoring.fla.transition.from.exists.error" var="TRANSITION_FROM_EXISTS_ERROR_VAR"/>
				TRANSITION_FROM_EXISTS_ERROR : decoderDiv.html('<c:out value="${TRANSITION_FROM_EXISTS_ERROR_VAR}" />').text(),
				<fmt:message key="authoring.fla.liveedit.readonly.remove.activity.error" var="LIVEEDIT_REMOVE_ACTIVITY_ERROR_VAR"/>
				LIVEEDIT_REMOVE_ACTIVITY_ERROR : '<c:out value="${LIVEEDIT_REMOVE_ACTIVITY_ERROR_VAR}" />',
				<fmt:message key="authoring.fla.liveedit.readonly.remove.parent.error" var="LIVEEDIT_REMOVE_PARENT_ERROR_VAR"/>
				LIVEEDIT_REMOVE_PARENT_ERROR : '<c:out value="${LIVEEDIT_REMOVE_PARENT_ERROR_VAR}" />',
				<fmt:message key="authoring.fla.liveedit.readonly.remove.child.error" var="LIVEEDIT_REMOVE_CHILD_ERROR_VAR"/>
				LIVEEDIT_REMOVE_CHILD_ERROR : '<c:out value="${LIVEEDIT_REMOVE_CHILD_ERROR_VAR}" />',
				<fmt:message key="authoring.fla.liveedit.readonly.remove.transition.error" var="LIVEEDIT_REMOVE_TRANSITION_ERROR_VAR"/>
				LIVEEDIT_REMOVE_TRANSITION_ERROR : '<c:out value="${LIVEEDIT_REMOVE_TRANSITION_ERROR_VAR}" />',
					
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
	<%-- "loading..." screen, gets removed on page full load --%>
	<div id="loadingOverlay">
		<i class="fa fa-refresh fa-spin fa-3x fa-fw"></i>
	</div>
	
	<div id="toolbar" class="btn-group" role="toolbar">
	
		<button id="newButton" class="btn btn-default" onClick="javascript:GeneralLib.newLearningDesign(false)">
			<i class="fa fa-plus"></i> 
			<span><fmt:message key="authoring.fla.page.menu.new" /></span>
		</button>
		
		<div class="btn-group" role="group">
		  <button id="openDropButton" class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		  	<i class="fa fa-folder-open-o"></i> 
		    <span><fmt:message key="authoring.fla.page.menu.open" /></span>
		    <span class="caret"></span>
		  </button>
		  <ul class="dropdown-menu" aria-labelledby="openDropButton">
			<li id="openButton" onClick="javascript:MenuLib.openLearningDesign()"><a href="#"><fmt:message key="authoring.fla.page.menu.open" /></a></li>
		    <li id="importSequenceButton" onClick="javascript:MenuLib.importLearningDesign()"><a href="#"><fmt:message key="authoring.fla.page.menu.import" /></a></li>
		    <li id="importPartSequenceButton" onClick="javascript:MenuLib.importPartLearningDesign()"><a href="#"><fmt:message key="authoring.fla.page.menu.import.part" /></a></li>
		  </ul>
		</div>
		
		<div class="btn-group" role="group">
		  <button class="btn btn-default dropdown-toggle" type="button" id="saveDropButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		  	<i class="fa fa-save"></i>
		    <span><fmt:message key="authoring.fla.page.menu.save" /></span>
		    <span class="caret"></span>
		  </button>
		  <ul class="dropdown-menu" aria-labelledby="saveDropButton">
		    <li id="saveButton" onClick="javascript:MenuLib.saveLearningDesign()"><a href="#"><fmt:message key="authoring.fla.page.menu.save" /></a></li>
		    <li id="saveAsButton" onClick="javascript:MenuLib.saveLearningDesign(true)"><a href="#"><fmt:message key="authoring.fla.page.menu.saveas" /></a></li>
		    <li role="separator" class="divider"></li>
		    <li class="dropdown-header"><fmt:message key="authoring.fla.page.menu.export" /></li>
		    <li id="exportLamsButton" onClick="javascript:MenuLib.exportLearningDesign(1)"><a href="#"><fmt:message key="authoring.fla.page.menu.export.lams" /></a></li>
		    <li id="exportSvgButton"  onClick="javascript:MenuLib.exportSVG(true))"><a href="#"><fmt:message key="authoring.fla.page.menu.export.svg" /></a></li>
		  </ul>
		</div>
		
		<button id="cancelLiveEditButton" class="btn btn-default" onClick="javascript:GeneralLib.cancelLiveEdit()">
			<i class="fa fa-ban"></i> 
			<span><fmt:message key="authoring.fla.cancel.button" /></span>
		</button>
		
		<button id="copyButton" class="btn btn-default" onClick="javascript:MenuLib.copyActivity()">
			<i class="fa fa-copy"></i> 
			<span><fmt:message key="authoring.fla.page.menu.copy" /></span>
		</button>
		
		<button id="pasteButton" class="btn btn-default" onClick="javascript:MenuLib.pasteActivity()">
			<i class="fa fa-paste"></i> 
			<span><fmt:message key="authoring.fla.page.menu.paste" /></span>
		</button>
		
		<button id="transitionButton" class="btn btn-default" onClick="javascript:MenuLib.addTransition()">
			<i class="fa fa-long-arrow-right"></i> 
			<span><fmt:message key="authoring.fla.page.menu.transition" /></span>
		</button>

		<div class="btn-group" role="group">
		  <button class="btn btn-default dropdown-toggle" type="button" id="optionalButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		  	<i class="fa fa-check-square-o"></i>
		    <span><fmt:message key="authoring.fla.page.menu.optional" /></span>
		    <span class="caret"></span>
		  </button>
		  <ul class="dropdown-menu" aria-labelledby="optionalButton">
		    <li id="optionalActivityButton" onClick="javascript:MenuLib.addOptionalActivity()"><a href="#"><fmt:message key="authoring.fla.page.menu.optional.activity" /></a></li>
		    <li id="floatingActivityButton" onClick="javascript:MenuLib.addFloatingActivity()"><a href="#"><fmt:message key="authoring.fla.page.menu.optional.support" /></a></li>
		  </ul>
		</div>

		<div class="btn-group" role="group">
		  <button class="btn btn-default dropdown-toggle" type="button" id="flowButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		  	<i class="fa fa-map-o"></i>
		    <span><fmt:message key="authoring.fla.page.menu.flow" /></span>
		    <span class="caret"></span>
		  </button>
		  <ul class="dropdown-menu" aria-labelledby="flowButton">
		    <li id="gateButton" onClick="javascript:MenuLib.addGate()"><a href="#"><fmt:message key="authoring.fla.page.menu.flow.gate" /></a></li>
		    <li id="branchingButton" onClick="javascript:MenuLib.addBranching()"><a href="#"><fmt:message key="authoring.fla.page.menu.flow.branch" /></a></li>
		  </ul>
		</div>
		
		<button id="groupButton" class="btn btn-default" onClick="javascript:MenuLib.addGrouping()">
			<i class="fa fa-group"></i> 
			<span><fmt:message key="authoring.fla.page.menu.group" /></span>
		</button>
		
		<div class="btn-group" role="group">
		  <button class="btn btn-default dropdown-toggle" type="button" id="annotateButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		  	<i class="fa fa-font"></i>
		    <span><fmt:message key="authoring.fla.page.menu.annotate" /></span>
		    <span class="caret"></span>
		  </button>
		  <ul class="dropdown-menu" aria-labelledby="annotateButton">
		    <li id="annotateLabelButton" onClick="javascript:MenuLib.addAnnotationLabel()"><a href="#"><fmt:message key="authoring.fla.page.menu.annotate.label" /></a></li>
		    <li id="annotateRegionButton" onClick="javascript:MenuLib.addAnnotationRegion()"><a href="#"><fmt:message key="authoring.fla.page.menu.annotate.region" /></a></li>
		  </ul>
		</div>
				
		<button id="arrangeButton" class="btn btn-default" onClick="javascript:GeneralLib.arrangeActivities()">
			<i class="fa fa-th"></i> 
			<span><fmt:message key="authoring.fla.page.menu.arrange" /></span>
		</button>
				
		<button id="previewButton" class="btn btn-default" onClick="javascript:MenuLib.openPreview()">
			<i class="fa fa-search-plus"></i> 
			<span><fmt:message key="authoring.fla.page.menu.preview" /></span>
		</button>
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
							 toolId="${tool.toolId}"
							 learningLibraryId="${tool.learningLibraryId}"
							 learningLibraryTitle="${tool.learningLibraryTitle}"
							 defaultToolContentId="${tool.defaultToolContentId}"
							 supportsOutputs="${tool.supportsOutputs}"
							 activityCategoryId="${tool.activityCategoryID}"
							 iconPath="${tool.iconPath}"
							 childToolIds="
							 <c:forEach var='childId' items='${tool.childToolIds}'>
							 	${childId},
							 </c:forEach>
							 "
							 class="template"
							 <%-- Hide invalid tools --%>
							 <c:if test="${not tool.valid}">
							 	style="display: none"
							 </c:if>
							 >
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
							 <lams:CKEditor id="ldDescriptionFieldDescription" value="" contentFolderID="${contentFolderID}"></lams:CKEditor>
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
						<button id="ldDescriptionLicenseButton" class="btn btn-default"><span>View</span></button>
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
	<div id="ldStoreDialogContents" class="dialogContents">
		<table>
			<tr>
				<td id="ldStoreDialogTreeCell">
					<div id="ldStoreDialogTree"></div>
				</td>
				<td id="ldStoreDialogCanvasCell" rowspan="2">
					<div id="ldStoreDialogCanvasDiv">
			    		<div id="ldScreenshotAuthor"></div>
			    		<i id="ldScreenshotLoading" class="fa fa-refresh fa-spin fa-3x fa-fw"></i>
		    			<iframe id="ldStoreDialogImportPartFrame"></iframe>
			    	</div>
				</td>
			</tr>
			<tr>
				<td id="ldStoreDialogAccessCell" >
					<div id="ldStoreDialogAccessTitle"><fmt:message key="authoring.fla.page.dialog.access" /></div>
				</td>
			</tr>

			<tr>
				<td id="ldStoreDialogButtonCell" colspan="2">
					<div class="container-fluid">
						<div id="ldStoreDialogLeftButtonContainer" class="btn-group" role="group">
							<button id="ldStoreDialogNewFolderButton" class="btn btn-default">
								<i class="fa fa-folder"></i> 
								<span><fmt:message key="authoring.fla.new.folder.button" /></span>
							</button>
							
							<button id="ldStoreDialogCopyButton" class="btn btn-default">
								<i class="fa fa-copy"></i> 
								<span><fmt:message key="authoring.fla.copy.button" /></span>
							</button>
							
							<button id="ldStoreDialogPasteButton" class="btn btn-default">
								<i class="fa fa-paste"></i> 
								<span><fmt:message key="authoring.fla.paste.button" /></span>
							</button>
							
							<button id="ldStoreDialogDeleteButton" class="btn btn-default">
								<i class="fa fa-trash-o"></i> 
								<span><fmt:message key="authoring.fla.delete.button" /></span>
							</button>
							
							<button id="ldStoreDialogRenameButton" class="btn btn-default">
								<i class="fa fa-font"></i> 
								<span><fmt:message key="authoring.fla.rename.button" /></span>
							</button>
						</div>
						
						<div id="ldStoreDialogNameContainer">
							<span><fmt:message key="authoring.fla.page.dialog.ld.title" /></span>
							<input type="text"/>
						</div>
						
						<div id="ldStoreDialogRightButtonContainer" class="btn-group pull-right" role="group">
							<button id="ldStoreDialogSaveButton" class="btn btn-default">
								<i class="fa fa-save"></i> 
								<span><fmt:message key="authoring.fla.save.button" /></span>
							</button>
							
							<button id="ldStoreDialogOpenButton" class="btn btn-default">
								<i class="fa fa-folder-open-o"></i> 
								<span><fmt:message key="authoring.fla.open.button" /></span>
							</button>
							
							<button id="ldStoreDialogImportPartButton" class="btn btn-default">
								<i class="fa fa-arrow-circle-o-down"></i> 
								<span><fmt:message key="authoring.fla.import.button" /></span>
							</button>
							
							<button id="ldStoreDialogCancelButton" class="btn btn-default">
								<i class="fa fa-ban"></i> 
								<span><fmt:message key="authoring.fla.cancel.button" /></span>
							</button>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	
	<!-- GROUP OR CONDITION TO BRANCH MATCHING DIALOG -->
	
	<div id="branchMappingDialogContents" class="dialogContents branchMappingDialog">
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
					<button class="btn btn-default branchMappingAddButton branchMappingButton"><i class="fa fa-chevron-right"></i></button>
					<button class="btn btn-default branchMappingRemoveButton branchMappingButton"><i class="fa fa-chevron-left"></i></button>
				</td>
				<td class="branchMappingBoundItemCell branchMappingListCell"></td>
				<td class="branchMappingBoundBranchCell branchMappingListCell"></td>
			</tr>
			<tr>
				<td colspan="5">
					<div class="container-fluid">
						<div class="pull-right btn-group" role="group">
							<button class="branchMappingOKButton btn btn-default">
								<i class="fa fa-check"></i> 
								<span><fmt:message key="authoring.fla.ok.button" /></span>
							</button>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	
	
	<!-- PROPERTY DIALOG CONTENTS FOR DIFFERENT OBJECT TYPES -->
	
	<div id="propertiesContentTransition" class="dialogContents">
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
	
	<div id="propertiesContentTool" class="dialogContents">
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
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.gradebook" />
				</td>
				<td>
					  <select class="propertiesContentFieldGradebook"></select>
				</td>
			</tr>
		</table>
	</div>
	
	
	<div id="propertiesContentGrouping" class="dialogContents">
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
					<button class="btn btn-default propertiesContentFieldNameGroups"><fmt:message key="authoring.fla.page.prop.groups.name" /></button>
				</td>
			</tr>
		</table>
	</div>


	<div id="propertiesContentGate" class="dialogContents">
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
					<button class="btn btn-default propertiesContentFieldMatchConditions"><fmt:message key="authoring.fla.page.prop.gate.conditions.map" /></button>
				</td>
			</tr>
		</table>
	</div>
	
	
	<div id="propertiesContentBranching" class="dialogContents">
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
					<button class="btn btn-default propertiesContentFieldCreateConditions"><fmt:message key="authoring.fla.page.prop.conditions.create" /></button>
				</td>
			</tr>
			<tr class="propertiesContentRowConditions">
				<td colspan="2">
					<button class="btn btn-default propertiesContentFieldMatchConditions"><fmt:message key="authoring.fla.page.prop.branching.conditions.match" /></button>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<button class="btn btn-default propertiesContentFieldMatchGroups"><fmt:message key="authoring.fla.page.prop.branching.groups.match" /></button>
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
	
		
	<div id="propertiesContentParallel" class="dialogContents">
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
	
	
	<div id="propertiesContentOptionalActivity" class="dialogContents">
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
	
	
	<div id="propertiesContentRegion" class="dialogContents">
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
	
	
	<div id="propertiesContentLabel" class="dialogContents">
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
	
	<div id="propertiesContentGroupNaming" class="dialogContents">
		<div id="groupNamingGroups"></div>
		<div class="container-fluid">
			<div class="btn-group pull-right" role="group">
				<button id="groupNamingOKButton" class="btn btn-default">
					<i class="fa fa-check"></i> 
					<span><fmt:message key="authoring.fla.ok.button" /></span>
				</button>
				
				<button id="groupNamingCancelButton" class="btn btn-default">
					<i class="fa fa-ban"></i> 
					<span><fmt:message key="authoring.fla.cancel.button" /></span>
				</button>
			</div>
		</div>
	</div>
	
	
	<!-- TOOL OUTPUT CONDITIONS DIALOG -->
	<div id="outputConditionsDialogContents" class="dialogContents">
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
			 <input id="singleRangeSpinner" class="outputSelectDependent spinner"/>
			 <div id="multiRangeDiv" class="outputSelectDependent">
			 	<fmt:message key="authoring.fla.page.dialog.cond.range.from" /> <input id="multiRangeFromSpinner" class="spinner" />
			 	<fmt:message key="authoring.fla.page.dialog.cond.range.to" /> <input id="multiRangeToSpinner" class="spinner" />
			 </div>
			 <button id="rangeAddButton" class="btn btn-default">
			 	<i class="fa fa-plus"></i>
			 	<span><fmt:message key="authoring.fla.page.dialog.cond.add" /></span>
			 </button>
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
		 <div class="container-fluid">
		 	<div class="btn-group pull-right" role="group">
				<button id="outputConditionsOKButton" class="btn btn-default">
					<i class="fa fa-check"></i> 
					<span><fmt:message key="authoring.fla.ok.button" /></span>
				</button>
				
				<button id="outputConditionsCancelButton" class="btn btn-default">
					<i class="fa fa-ban"></i> 
					<span><fmt:message key="authoring.fla.cancel.button" /></span>
				</button>

			</div>
			
			<div class="btn-group pull-right" role="group">
				<button id="outputConditionsClearAllButton" class="btn btn-default outputSelectDependent rangeOutputButton">
					<i class="fa fa-remove"></i> 
					<span><fmt:message key="authoring.fla.clear.all.button" /></span>
				</button>
				
				<button id="outputConditionsRemoveButton" class="btn btn-default outputSelectDependent rangeOutputButton">
					<i class="fa fa-remove"></i> 
					<span><fmt:message key="authoring.fla.remove.condition.button" /></span>
				</button>
				
				<button id="outputConditionsRefreshButton" class="btn btn-default outputSelectDependent complexOutputButton">
					<i class="fa fa-refresh"></i> 
					<span><fmt:message key="authoring.fla.refresh.button" /></span>
				</button>
			</div>
		 </div>
	</div>
	
	
	<!-- EXPORT CANVAS AS IMAGE DIALOG -->
	<div id="exportImageDialogContents" class="dialogContents exportDialog">
		<a href="#"><fmt:message key="authoring.fla.page.download.image" /></a>
	</div>
	
	
	<!-- EXPORT LEARNING DESIGN DIALOG -->
	<div id="exportLDDialogContents" class="dialogContents exportDialog">
		<span><fmt:message key="authoring.fla.page.download.wait" /><br /><fmt:message key="authoring.fla.page.download.close" /></span>
		<iframe></iframe>
	</div>
</body>
</lams:html>