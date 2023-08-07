<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" import="org.lamsfoundation.lams.util.ConfigurationKeys" %>

<lams:html>
<lams:head>
	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap-treeview.css" type="text/css" media="screen" />
	<lams:css suffix="authoring"/>
	<link rel="stylesheet" href="<lams:LAMSURL/>css/authoring-svg.css" type="text/css" media="screen" />
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.dialogextend.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.simple-color.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap-treeview.js" ></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/learning-design-treeview.js" ></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/snap.svg.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	<lams:JSImport src="includes/javascript/dialog.js" />	
	<script type="text/javascript">
		var LAMS_URL = '<lams:LAMSURL/>',
			LD_THUMBNAIL_URL_BASE = LAMS_URL + 'home/getLearningDesignThumbnail.do?ldId=',

			LABELS = {
				// ActivityLib
				<fmt:message key="authoring.fla.default.group.title" var="DEFAULT_GROUPING_TITLE_VAR"/>
				DEFAULT_GROUPING_TITLE : '<spring:escapeBody javaScriptEscape="true">${DEFAULT_GROUPING_TITLE_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.default.group.prefix" var="DEFAULT_GROUP_PREFIX_VAR"/>
				DEFAULT_GROUP_PREFIX : '<spring:escapeBody javaScriptEscape="true">${DEFAULT_GROUP_PREFIX_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.default.branching.title" var="DEFAULT_BRANCHING_TITLE_VAR"/>
				DEFAULT_BRANCHING_TITLE : '<spring:escapeBody javaScriptEscape="true">${DEFAULT_BRANCHING_TITLE_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.default.branch.prefix" var="DEFAULT_BRANCH_PREFIX_VAR"/>
				DEFAULT_BRANCH_PREFIX : '<spring:escapeBody javaScriptEscape="true">${DEFAULT_BRANCH_PREFIX_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.default.optional.activity.title" var="DEFAULT_OPTIONAL_ACTIVITY_TITLE_VAR"/>
				DEFAULT_OPTIONAL_ACTIVITY_TITLE : '<spring:escapeBody javaScriptEscape="true">${DEFAULT_OPTIONAL_ACTIVITY_TITLE_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.support.activity.title" var="SUPPORT_ACTIVITY_TITLE_VAR"/>
				SUPPORT_ACTIVITY_TITLE : '<spring:escapeBody javaScriptEscape="true">${SUPPORT_ACTIVITY_TITLE_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.gate.activity.label" var="GATE_ACTIVITY_LABEL_VAR"/>
				GATE_ACTIVITY_LABEL : '<spring:escapeBody javaScriptEscape="true">${GATE_ACTIVITY_LABEL_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.branching.start.suffix" var="BRANCHING_START_SUFFIX_VAR"/>
				BRANCHING_START_SUFFIX : '<spring:escapeBody javaScriptEscape="true">${BRANCHING_START_SUFFIX_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.branching.end.suffix" var="BRANCHING_END_SUFFIX_VAR"/>
				BRANCHING_END_SUFFIX : '<spring:escapeBody javaScriptEscape="true">${BRANCHING_END_SUFFIX_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.activity.dialog.title.suffix" var="ACTIVITY_DIALOG_TITLE_SUFFIX_VAR"/>
				ACTIVITY_DIALOG_TITLE_SUFFIX : '<spring:escapeBody javaScriptEscape="true">${ACTIVITY_DIALOG_TITLE_SUFFIX_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.remove.activity.confirm" var="REMOVE_ACTIVITY_CONFIRM_VAR"/>
				REMOVE_ACTIVITY_CONFIRM : '<spring:escapeBody javaScriptEscape="true">${REMOVE_ACTIVITY_CONFIRM_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.branching.create.confirm" var="BRANCHING_CREATE_CONFIRM_VAR"/>
				BRANCHING_CREATE_CONFIRM : '<spring:escapeBody javaScriptEscape="true">${BRANCHING_CREATE_CONFIRM_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.transition.to.exists.error" var="TRANSITION_TO_EXISTS_ERROR_VAR"/>
				TRANSITION_TO_EXISTS_ERROR : '<spring:escapeBody javaScriptEscape="true">${TRANSITION_TO_EXISTS_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.circular.sequence.error" var="CIRCULAR_SEQUENCE_ERROR_VAR"/>
				CIRCULAR_SEQUENCE_ERROR : '<spring:escapeBody javaScriptEscape="true">${CIRCULAR_SEQUENCE_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.activity.in.container.error" var="ACTIVITY_IN_CONTAINER_ERROR_VAR"/>
				ACTIVITY_IN_CONTAINER_ERROR : '<spring:escapeBody javaScriptEscape="true">${ACTIVITY_IN_CONTAINER_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.liveedit.readonly.activity.error" var="LIVEEDIT_READONLY_ACTIVITY_ERROR_VAR"/>
				LIVEEDIT_READONLY_ACTIVITY_ERROR : '<spring:escapeBody javaScriptEscape="true">${LIVEEDIT_READONLY_ACTIVITY_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.liveedit.readonly.move.parent.error" var="LIVEEDIT_READONLY_MOVE_PARENT_ERROR_VAR"/>
				LIVEEDIT_READONLY_MOVE_PARENT_ERROR : '<spring:escapeBody javaScriptEscape="true">${LIVEEDIT_READONLY_MOVE_PARENT_ERROR_VAR}</spring:escapeBody>',
				
				
				// DecorationLib
				<fmt:message key="authoring.fla.default.annotation.label.title" var="DEFAULT_ANNOTATION_LABEL_TITLE_VAR"/>
				DEFAULT_ANNOTATION_LABEL_TITLE : '<spring:escapeBody javaScriptEscape="true">${DEFAULT_ANNOTATION_LABEL_TITLE_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.region.fit.button.tooltip" var="REGION_FIT_BUTTON_TOOLTIP_VAR"/>
				REGION_FIT_BUTTON_TOOLTIP : '<spring:escapeBody javaScriptEscape="true">${REGION_FIT_BUTTON_TOOLTIP_VAR}</spring:escapeBody>',

				// General
				<fmt:message key="authoring.fla.folder" var="FOLDER_VAR"/>
				FOLDER : '<spring:escapeBody javaScriptEscape="true">${FOLDER_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.sequence" var="SEQUENCE_VAR"/>
				SEQUENCE : '<spring:escapeBody javaScriptEscape="true">${SEQUENCE_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.weights.title" var="WEIGHTS_TITLE_VAR"/>
				WEIGHTS_TITLE : '<spring:escapeBody javaScriptEscape="true">${WEIGHTS_TITLE_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.sequence.not.valid" var="SEQUENCE_NOT_VALID_VAR"/>
				SEQUENCE_NOT_VALID : '<spring:escapeBody javaScriptEscape="true">${SEQUENCE_NOT_VALID_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.sequence.validation.issues" var="SEQUENCE_VALIDATION_ISSUES_VAR"/>
				SEQUENCE_VALIDATION_ISSUES : '<spring:escapeBody javaScriptEscape="true">${SEQUENCE_VALIDATION_ISSUES_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.save.successful" var="SAVE_SUCCESSFUL_VAR"/>
				SAVE_SUCCESSFUL : '<spring:escapeBody javaScriptEscape="true">${SAVE_SUCCESSFUL_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.save.successful.check.grouping" var="SAVE_SUCCESSFUL_CHECK_GROUPING_VAR"/>
				SAVE_SUCCESSFUL_CHECK_GROUPING : '<spring:escapeBody javaScriptEscape="true">${SAVE_SUCCESSFUL_CHECK_GROUPING_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.liveedit.save.successful" var="LIVEEDIT_SAVE_SUCCESSFUL_VAR"/>
				LIVEEDIT_SAVE_SUCCESSFUL : '<spring:escapeBody javaScriptEscape="true">${LIVEEDIT_SAVE_SUCCESSFUL_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.delete.node.confirm" var="DELETE_NODE_CONFIRM_VAR"/>
				DELETE_NODE_CONFIRM : '<spring:escapeBody javaScriptEscape="true">${DELETE_NODE_CONFIRM_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.sequence.overwrite.confirm" var="SEQUENCE_OVERWRITE_CONFIRM_VAR"/>
				SEQUENCE_OVERWRITE_CONFIRM : '<spring:escapeBody javaScriptEscape="true">${SEQUENCE_OVERWRITE_CONFIRM_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.new.folder.title.prompt" var="NEW_FOLDER_TITLE_PROMPT_VAR"/>
				NEW_FOLDER_TITLE_PROMPT : '<spring:escapeBody javaScriptEscape="true">${NEW_FOLDER_TITLE_PROMPT_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.rename.title.prompt" var="RENAME_TITLE_PROMPT_VAR"/>
				RENAME_TITLE_PROMPT : '<spring:escapeBody javaScriptEscape="true">${RENAME_TITLE_PROMPT_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.save.sequence.title.prompt" var="SAVE_SEQUENCE_TITLE_PROMPT_VAR"/>
				SAVE_SEQUENCE_TITLE_PROMPT : '<spring:escapeBody javaScriptEscape="true">${SAVE_SEQUENCE_TITLE_PROMPT_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.import.part.choose.prompt" var="IMPORT_PART_CHOOSE_PROMPT_VAR"/>
				IMPORT_PART_CHOOSE_PROMPT : '<spring:escapeBody javaScriptEscape="true">${IMPORT_PART_CHOOSE_PROMPT_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.liveedit.cancel.confirm" var="LIVEEDIT_CANCEL_CONFIRM_VAR"/>
				LIVEEDIT_CANCEL_CONFIRM : '<spring:escapeBody javaScriptEscape="true">${LIVEEDIT_CANCEL_CONFIRM_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.replace.question.prompt" var="REPLACE_QUESTION_PROMPT_VAR"/>
				REPLACE_QUESTION_PROMPT : '<spring:escapeBody javaScriptEscape="true">${REPLACE_QUESTION_PROMPT_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.folder.not.selected.error" var="FOLDER_NOT_SELECTED_ERROR_VAR"/>
				FOLDER_NOT_SELECTED_ERROR : '<spring:escapeBody javaScriptEscape="true">${FOLDER_NOT_SELECTED_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.folder.can.not.save.error" var="FOLDER_CAN_NOT_SAVE_ERROR_VAR"/>
				FOLDER_CAN_NOT_SAVE_ERROR : '<spring:escapeBody javaScriptEscape="true">${FOLDER_CAN_NOT_SAVE_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.title.validation.error" var="TITLE_VALIDATION_ERROR_VAR"/>
				TITLE_VALIDATION_ERROR : '<spring:escapeBody javaScriptEscape="true">${TITLE_VALIDATION_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.folder.exists.error" var="FOLDER_EXISTS_ERROR_VAR"/>
				FOLDER_EXISTS_ERROR : '<spring:escapeBody javaScriptEscape="true">${FOLDER_EXISTS_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.sequence.exists.error" var="SEQUENCE_EXISTS_ERROR_VAR"/>
				SEQUENCE_EXISTS_ERROR : '<spring:escapeBody javaScriptEscape="true">${SEQUENCE_EXISTS_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.sequence.save.error" var="SEQUENCE_SAVE_ERROR_VAR"/>
				SEQUENCE_SAVE_ERROR : '<spring:escapeBody javaScriptEscape="true">${SEQUENCE_SAVE_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.readonly.forbidden" var="READONLY_FORBIDDEN_ERROR_VAR"/>
				READONLY_FORBIDDEN_ERROR : '<spring:escapeBody javaScriptEscape="true">${READONLY_FORBIDDEN_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.svg.save.error" var="SVG_SAVE_ERROR_VAR"/>
				SVG_SAVE_ERROR : '<spring:escapeBody javaScriptEscape="true">${SVG_SAVE_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.sequence.not.selected.error" var="SEQUENCE_NOT_SELECTED_ERROR_VAR"/>
				SEQUENCE_NOT_SELECTED_ERROR : '<spring:escapeBody javaScriptEscape="true">${SEQUENCE_NOT_SELECTED_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.sequence.load.error" var="SEQUENCE_LOAD_ERROR_VAR"/>
				SEQUENCE_LOAD_ERROR : '<spring:escapeBody javaScriptEscape="true">${SEQUENCE_LOAD_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.modify.error" var="RESOURCE_MODIFY_ERROR_VAR"/>
				RESOURCE_MODIFY_ERROR : '<spring:escapeBody javaScriptEscape="true">${RESOURCE_MODIFY_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.folder.move.to.child.error" var="FOLDER_MOVE_TO_CHILD_ERROR_VAR"/>
				FOLDER_MOVE_TO_CHILD_ERROR : '<spring:escapeBody javaScriptEscape="true">${FOLDER_MOVE_TO_CHILD_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.weights.sum.error" var="WEIGHTS_SUM_ERROR_VAR"/>
				WEIGHTS_SUM_ERROR : '<spring:escapeBody javaScriptEscape="true">${WEIGHTS_SUM_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.weights.none" var="WEIGHTS_NONE_FOUND_ERROR_VAR"/>
				WEIGHTS_NONE_FOUND_ERROR : '<spring:escapeBody javaScriptEscape="true">${WEIGHTS_NONE_FOUND_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.grouping.detached.error" var="GROUPING_DETACHED_ERROR_VAR"/>
				GROUPING_DETACHED_ERROR : '<spring:escapeBody javaScriptEscape="true">${GROUPING_DETACHED_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.input.detached.error" var="INPUT_DETACHED_ERROR_VAR"/>
				INPUT_DETACHED_ERROR : '<spring:escapeBody javaScriptEscape="true">${INPUT_DETACHED_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.learning.design.templates" var="TEMPLATES_VAR"/>
				TEMPLATES : '<spring:escapeBody javaScriptEscape="true">${TEMPLATES_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.learning.design.ai.wizard" var="AI_WIZARD_VAR"/>
				AI_WIZARD : '<spring:escapeBody javaScriptEscape="true">${AI_WIZARD_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.page.menu.apply.changes" var="LIVE_EDIT_SAVE_VAR"/>
				LIVE_EDIT_SAVE : '<spring:escapeBody javaScriptEscape="true">${LIVE_EDIT_SAVE_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.navigate.away.confirm" var="NAVIGATE_AWAY_CONFIRM_VAR"/>
				NAVIGATE_AWAY_CONFIRM : '<spring:escapeBody javaScriptEscape="true">${NAVIGATE_AWAY_CONFIRM_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.remove.button.confirm" var="REMOVE_BUTTON_CONFIRM_VAR"/>
				REMOVE_BUTTON_CONFIRM : '<spring:escapeBody javaScriptEscape="true">${REMOVE_BUTTON_CONFIRM_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.remove.button.tip" var="REMOVE_BUTTON_TOOLTIP_VAR"/>
				REMOVE_BUTTON_TOOLTIP : '<spring:escapeBody javaScriptEscape="true">${REMOVE_BUTTON_TOOLTIP_VAR}</spring:escapeBody>',

				// HandlerLib
				<fmt:message key="authoring.fla.transition.from.exists.error" var="TRANSITION_FROM_EXISTS_ERROR_VAR"/>
				TRANSITION_FROM_EXISTS_ERROR : '<spring:escapeBody javaScriptEscape="true">${TRANSITION_FROM_EXISTS_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.liveedit.readonly.remove.activity.error" var="LIVEEDIT_REMOVE_ACTIVITY_ERROR_VAR"/>
				LIVEEDIT_REMOVE_ACTIVITY_ERROR : '<spring:escapeBody javaScriptEscape="true">${LIVEEDIT_REMOVE_ACTIVITY_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.liveedit.readonly.remove.parent.error" var="LIVEEDIT_REMOVE_PARENT_ERROR_VAR"/>
				LIVEEDIT_REMOVE_PARENT_ERROR : '<spring:escapeBody javaScriptEscape="true">${LIVEEDIT_REMOVE_PARENT_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.liveedit.readonly.remove.child.error" var="LIVEEDIT_REMOVE_CHILD_ERROR_VAR"/>
				LIVEEDIT_REMOVE_CHILD_ERROR : '<spring:escapeBody javaScriptEscape="true">${LIVEEDIT_REMOVE_CHILD_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.liveedit.readonly.remove.transition.error" var="LIVEEDIT_REMOVE_TRANSITION_ERROR_VAR"/>
				LIVEEDIT_REMOVE_TRANSITION_ERROR : '<spring:escapeBody javaScriptEscape="true">${LIVEEDIT_REMOVE_TRANSITION_ERROR_VAR}</spring:escapeBody>',

				// MenuLib
				<fmt:message key="authoring.fla.export.sequence.dialog.title" var="EXPORT_SEQUENCE_DIALOG_TITLE_VAR"/>
				EXPORT_SEQUENCE_DIALOG_TITLE : '<spring:escapeBody javaScriptEscape="true">${EXPORT_SEQUENCE_DIALOG_TITLE_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.activity.copy.title.prefix" var="ACTIVITY_COPY_TITLE_PREFIX_VAR"/>
				ACTIVITY_COPY_TITLE_PREFIX : '<spring:escapeBody javaScriptEscape="true">${ACTIVITY_COPY_TITLE_PREFIX_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.preview.lesson.default.title" var="PREVIEW_LESSON_DEFAULT_TITLE_VAR"/>
				PREVIEW_LESSON_DEFAULT_TITLE : '<spring:escapeBody javaScriptEscape="true">${PREVIEW_LESSON_DEFAULT_TITLE_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.save.dialog.title" var="SAVE_DIALOG_TITLE_VAR"/>
				SAVE_DIALOG_TITLE : '<spring:escapeBody javaScriptEscape="true">${SAVE_DIALOG_TITLE_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.open.dialog.title" var="OPEN_DIALOG_TITLE_VAR"/>
				OPEN_DIALOG_TITLE : '<spring:escapeBody javaScriptEscape="true">${OPEN_DIALOG_TITLE_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.import.dialog.title" var="IMPORT_DIALOG_TITLE_VAR"/>
				IMPORT_DIALOG_TITLE : '<spring:escapeBody javaScriptEscape="true">${IMPORT_DIALOG_TITLE_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.import.part.dialog.title" var="IMPORT_PART_DIALOG_TITLE_VAR"/>
				IMPORT_PART_DIALOG_TITLE : '<spring:escapeBody javaScriptEscape="true">${IMPORT_PART_DIALOG_TITLE_VAR}</spring:escapeBody>',
				<fmt:message key="label.tab.lesson.sequence.folder" var="RUN_SEQUENCES_FOLDER_VAR"/>
				RUN_SEQUENCES_FOLDER : '<spring:escapeBody javaScriptEscape="true">${RUN_SEQUENCES_FOLDER_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.arrange.confirm" var="ARRANGE_CONFIRM_VAR"/>
				ARRANGE_CONFIRM : '<spring:escapeBody javaScriptEscape="true">${ARRANGE_CONFIRM_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.clear.canvas.confirm" var="CLEAR_CANVAS_CONFIRM_VAR"/>
				CLEAR_CANVAS_CONFIRM : '<spring:escapeBody javaScriptEscape="true">${CLEAR_CANVAS_CONFIRM_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.annotation.region.place.prompt" var="ANNOTATION_REGION_PLACE_PROMPT_VAR"/>
				ANNOTATION_REGION_PLACE_PROMPT : '<spring:escapeBody javaScriptEscape="true">${ANNOTATION_REGION_PLACE_PROMPT_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.annotation.label.place.prompt" var="ANNOTATION_LABEL_PLACE_PROMPT_VAR"/>
				ANNOTATION_LABEL_PLACE_PROMPT : '<spring:escapeBody javaScriptEscape="true">${ANNOTATION_LABEL_PLACE_PROMPT_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.transition.place.prompt" var="TRANSITION_PLACE_PROMPT_VAR"/>
				TRANSITION_PLACE_PROMPT : '<spring:escapeBody javaScriptEscape="true">${TRANSITION_PLACE_PROMPT_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.paste.error" var="PASTE_ERROR_VAR"/>
				PASTE_ERROR : '<spring:escapeBody javaScriptEscape="true">${PASTE_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.preview.error" var="PREVIEW_ERROR_VAR"/>
				PREVIEW_ERROR : '<spring:escapeBody javaScriptEscape="true">${PREVIEW_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.cross.branching.error" var="CROSS_BRANCHING_ERROR_VAR"/>
				CROSS_BRANCHING_ERROR : '<spring:escapeBody javaScriptEscape="true">${CROSS_BRANCHING_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.support.transition.error" var="SUPPORT_TRANSITION_ERROR_VAR"/>
				SUPPORT_TRANSITION_ERROR : '<spring:escapeBody javaScriptEscape="true">${SUPPORT_TRANSITION_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.end.match.error" var="END_MATCH_ERROR_VAR"/>
				END_MATCH_ERROR : '<spring:escapeBody javaScriptEscape="true">${END_MATCH_ERROR_VAR}</spring:escapeBody>',

				// PropertyLib
				<fmt:message key="authoring.fla.properties.dialog.title" var="PROPERTIES_DIALOG_TITLE_VAR"/>
				PROPERTIES_DIALOG_TITLE : '<spring:escapeBody javaScriptEscape="true">${PROPERTIES_DIALOG_TITLE_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.group.naming.dialog.title" var="GROUP_NAMING_DIALOG_TITLE_VAR"/>
				GROUP_NAMING_DIALOG_TITLE : '<spring:escapeBody javaScriptEscape="true">${GROUP_NAMING_DIALOG_TITLE_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.groups.to.branches.match.dialog_title" var="GROUPS_TO_BRANCHES_MATCH_DIALOG_TITLE_VAR"/>
				GROUPS_TO_BRANCHES_MATCH_DIALOG_TITLE : '<spring:escapeBody javaScriptEscape="true">${GROUPS_TO_BRANCHES_MATCH_DIALOG_TITLE_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.branch.mapping.groups.header" var="BRANCH_MAPPING_GROUPS_HEADER_VAR"/>
				BRANCH_MAPPING_GROUPS_HEADER : '<spring:escapeBody javaScriptEscape="true">${BRANCH_MAPPING_GROUPS_HEADER_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.branch.mapping.group.header" var="BRANCH_MAPPING_GROUP_HEADER_VAR"/>
				BRANCH_MAPPING_GROUP_HEADER : '<spring:escapeBody javaScriptEscape="true">${BRANCH_MAPPING_GROUP_HEADER_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.conditions.dialog.title" var="CONDITIONS_DIALOG_TITLE_VAR"/>
				CONDITIONS_DIALOG_TITLE : '<spring:escapeBody javaScriptEscape="true">${CONDITIONS_DIALOG_TITLE_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.branch.mapping.conditions.header" var="BRANCH_MAPPING_CONDITIONS_HEADER_VAR"/>
				BRANCH_MAPPING_CONDITIONS_HEADER : '<spring:escapeBody javaScriptEscape="true">${BRANCH_MAPPING_CONDITIONS_HEADER_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.branch.mapping.condition.header" var="BRANCH_MAPPING_CONDITION_HEADER_VAR"/>
				BRANCH_MAPPING_CONDITION_HEADER : '<spring:escapeBody javaScriptEscape="true">${BRANCH_MAPPING_CONDITION_HEADER_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.branch.mapping.gate.header" var="BRANCH_MAPPING_GATE_HEADER_VAR"/>
				BRANCH_MAPPING_GATE_HEADER : '<spring:escapeBody javaScriptEscape="true">${BRANCH_MAPPING_GATE_HEADER_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.branch.mapping.branches.header" var="BRANCH_MAPPING_BRANCHES_HEADER_VAR"/>
				BRANCH_MAPPING_BRANCHES_HEADER : '<spring:escapeBody javaScriptEscape="true">${BRANCH_MAPPING_BRANCHES_HEADER_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.branch.mapping.branch.header" var="BRANCH_MAPPING_BRANCH_HEADER_VAR"/>
				BRANCH_MAPPING_BRANCH_HEADER : '<spring:escapeBody javaScriptEscape="true">${BRANCH_MAPPING_BRANCH_HEADER_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.gate.state.mapping.dialog.title" var="GATE_STATE_MAPPING_DIALOG_TITLE_VAR"/>
				GATE_STATE_MAPPING_DIALOG_TITLE : '<spring:escapeBody javaScriptEscape="true">${GATE_STATE_MAPPING_DIALOG_TITLE_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.branch.mapping.dialog.title" var="BRANCH_MAPPING_DIALOG_TITLE_VAR"/>
				BRANCH_MAPPING_DIALOG_TITLE : '<spring:escapeBody javaScriptEscape="true">${BRANCH_MAPPING_DIALOG_TITLE_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.gate.state.open" var="GATE_STATE_OPEN_VAR"/>
				GATE_STATE_OPEN : '<spring:escapeBody javaScriptEscape="true">${GATE_STATE_OPEN_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.gate.state.closed" var="GATE_STATE_CLOSED_VAR"/>
				GATE_STATE_CLOSED : '<spring:escapeBody javaScriptEscape="true">${GATE_STATE_CLOSED_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.branch.mapping.default.branch.suffix" var="BRANCH_MAPPING_DEFAULT_BRANCH_SUFFIX_VAR"/>
				BRANCH_MAPPING_DEFAULT_BRANCH_SUFFIX : '<spring:escapeBody javaScriptEscape="true">${BRANCH_MAPPING_DEFAULT_BRANCH_SUFFIX_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.complex.output.suffix" var="COMPLEX_OUTPUT_SUFFIX_VAR"/>
				COMPLEX_OUTPUT_SUFFIX : '<spring:escapeBody javaScriptEscape="true">${COMPLEX_OUTPUT_SUFFIX_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.range.output.suffix" var="RANGE_OUTPUT_SUFFIX_VAR"/>
				RANGE_OUTPUT_SUFFIX : '<spring:escapeBody javaScriptEscape="true">${RANGE_OUTPUT_SUFFIX_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.boolean.output.suffix" var="BOOLEAN_OUTPUT_SUFFIX_VAR"/>
				BOOLEAN_OUTPUT_SUFFIX : '<spring:escapeBody javaScriptEscape="true">${BOOLEAN_OUTPUT_SUFFIX_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.range.condition.description" var="RANGE_CONDITION_DESCRIPTION_VAR"/>
				RANGE_CONDITION_DESCRIPTION : '<spring:escapeBody javaScriptEscape="true">${RANGE_CONDITION_DESCRIPTION_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.exact.condition.description" var="EXACT_CONDITION_DESCRIPTION_VAR"/>
				EXACT_CONDITION_DESCRIPTION : '<spring:escapeBody javaScriptEscape="true">${EXACT_CONDITION_DESCRIPTION_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.less.conditiondescription" var="LESS_CONDITION_DESCRIPTION_VAR"/>
				LESS_CONDITION_DESCRIPTION : '<spring:escapeBody javaScriptEscape="true">${LESS_CONDITION_DESCRIPTION_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.greater.condition.description" var="GREATER_CONDITION_DESCRIPTION_VAR"/>
				GREATER_CONDITION_DESCRIPTION : '<spring:escapeBody javaScriptEscape="true">${GREATER_CONDITION_DESCRIPTION_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.activity.unnamed.description" var="ACTIVITY_UNNAMED_DESCRIPTION_VAR"/>
				ACTIVITY_UNNAMED_DESCRIPTION : '<spring:escapeBody javaScriptEscape="true">${ACTIVITY_UNNAMED_DESCRIPTION_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.activity.gate.description" var="ACTIVITY_GATE_DESCRIPTION_VAR"/>
				ACTIVITY_GATE_DESCRIPTION : '<spring:escapeBody javaScriptEscape="true">${ACTIVITY_GATE_DESCRIPTION_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.activity.branching.description" var="ACTIVITY_BRANCHING_DESCRIPTION_VAR"/>
				ACTIVITY_BRANCHING_DESCRIPTION : '<spring:escapeBody javaScriptEscape="true">${ACTIVITY_BRANCHING_DESCRIPTION_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.default.range.condition.title.prefix" var="DEFAULT_RANGE_CONDITION_TITLE_PREFIX_VAR"/>
				DEFAULT_RANGE_CONDITION_TITLE_PREFIX : '<spring:escapeBody javaScriptEscape="true">${DEFAULT_RANGE_CONDITION_TITLE_PREFIX_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.page.prop.gradebook.none" var="GRADEBOOK_OUTPUT_NONE_VAR"/>
				GRADEBOOK_OUTPUT_NONE : '<spring:escapeBody javaScriptEscape="true">${GRADEBOOK_OUTPUT_NONE_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.clear.all.confirm" var="CLEAR_ALL_CONFIRM_VAR"/>
				CLEAR_ALL_CONFIRM : '<spring:escapeBody javaScriptEscape="true">${CLEAR_ALL_CONFIRM_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.remove.condition.confirm" var="REMOVE_CONDITION_CONFIRM_VAR"/>
				REMOVE_CONDITION_CONFIRM : '<spring:escapeBody javaScriptEscape="true">${REMOVE_CONDITION_CONFIRM_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.conditions.to.default.gate.state.confirm" var="CONDITIONS_TO_DEFAULT_GATE_STATE_CONFIRM_VAR"/>
				CONDITIONS_TO_DEFAULT_GATE_STATE_CONFIRM : '<spring:escapeBody javaScriptEscape="true">${CONDITIONS_TO_DEFAULT_GATE_STATE_CONFIRM_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.conditions.to.default.branch.confirm" var="CONDITIONS_TO_DEFAULT_BRANCH_CONFIRM_VAR"/>
				CONDITIONS_TO_DEFAULT_BRANCH_CONFIRM : '<spring:escapeBody javaScriptEscape="true">${CONDITIONS_TO_DEFAULT_BRANCH_CONFIRM_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.groups.to.default.branch.confirm" var="GROUPS_TO_DEFAULT_BRANCH_CONFIRM_VAR"/>
				GROUPS_TO_DEFAULT_BRANCH_CONFIRM : '<spring:escapeBody javaScriptEscape="true">${GROUPS_TO_DEFAULT_BRANCH_CONFIRM_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.range.condition.add.start.greater.than.end.error" var="RANGE_CONDITION_ADD_START_GREATER_THAN_END_ERROR_VAR"/>
				RANGE_CONDITION_ADD_START_GREATER_THAN_END_ERROR : '<spring:escapeBody javaScriptEscape="true">${RANGE_CONDITION_ADD_START_GREATER_THAN_END_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.range.condition.add.start.error" var="RANGE_CONDITION_ADD_START_ERROR_VAR"/>
				RANGE_CONDITION_ADD_START_ERROR : '<spring:escapeBody javaScriptEscape="true">${RANGE_CONDITION_ADD_START_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.range.condition.add.end.error" var="RANGE_CONDITION_ADD_END_ERROR_VAR"/>
				RANGE_CONDITION_ADD_END_ERROR : '<spring:escapeBody javaScriptEscape="true">${RANGE_CONDITION_ADD_END_ERROR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.group.title.validation.erorr" var="GROUP_TITLE_VALIDATION_ERORR_VAR"/>
				GROUP_TITLE_VALIDATION_ERORR : '<spring:escapeBody javaScriptEscape="true">${GROUP_TITLE_VALIDATION_ERORR_VAR}</spring:escapeBody>',
				<fmt:message key="authoring.fla.conditions.mapping.broken" var="CONDITIONS_MAPPING_BROKEN_ERROR_VAR"/>
				CONDITIONS_MAPPING_BROKEN_ERROR : '<spring:escapeBody javaScriptEscape="true">${CONDITIONS_MAPPING_BROKEN_ERROR_VAR}</spring:escapeBody>'
			},

			canSetReadOnly = ${canSetReadOnly},
			isReadOnlyMode = false,
			activitiesOnlySelectable = false,
			isLtiContentSelection = ${param.ltiContentSelection eq 'true'},
			initContentFolderID = '${contentFolderID}',
			initLearningDesignID = '<c:out value="${param.learningDesignID}" />',
			initRelaunchMonitorLessonID = '<c:out value="${param.relaunchMonitorLessonID}" />',
			initAccess = ${access},
			csrfTokenName = '<csrf:tokenname/>',
			csrfTokenValue = '<csrf:tokenvalue/>';
	</script>
	<lams:JSImport src="includes/javascript/authoring/authoringGeneral.js" />
	<lams:JSImport src="includes/javascript/authoring/authoringActivity.js" />
	<lams:JSImport src="includes/javascript/authoring/authoringDecoration.js" />
	<lams:JSImport src="includes/javascript/authoring/authoringProperty.js" />
	<lams:JSImport src="includes/javascript/authoring/authoringHandler.js" />
	<lams:JSImport src="includes/javascript/authoring/authoringMenu.js" />
</lams:head>
<body>
	<%-- "loading..." screen, gets removed on page full load --%>
	<div id="loadingOverlay">
		<i class="fa fa-refresh fa-spin fa-3x fa-fw"></i>
	</div>
	
	<div id="toolbar" class="buttons btn-group-sm">
		<i id="activitiesToolbarButton" onClick="javascript:MenuLib.toggleTemplateContainer()" class="fa fa-bars"
		   title="<fmt:message key='authoring.fla.tool.bar.expand' />"></i>
		
		<div class="btn-group btn-group-sm">
			<button id="newButton" class="btn btn-default desktopButton" onClick="javascript:GeneralLib.newLearningDesign(false)"
			    	title="<fmt:message key="authoring.fla.page.menu.new" />">
				<i class="fa fa-plus"></i> 
				<span class="hidden-sm hidden-xs"><fmt:message key="authoring.fla.page.menu.new" /></span>
			</button>
			<button id="newDropButton" type="button" class="btn btn-default desktopButton dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				<span class="caret"></span>
				<span class="sr-only">Toggle Dropdown</span>
			</button>
			<ul class="dropdown-menu dropdown-menu-right desktopButton">
				<li id="useAi" onClick="javascript:MenuLib.useAiToCreateLearningDesign()"><a href="#"><i class="fa fa-microchip text-primary"></i> <fmt:message key="authoring.fla.page.menu.new.ai" /></a></li>
				<li id="useTemplate" onClick="javascript:MenuLib.useTemplateToCreateLearningDesign()"><a href="#"><fmt:message key="authoring.fla.page.menu.new.template" /></a></li>
			</ul>
		</div>
		
		<div class="btn-group btn-group-sm">
			<button id="openButton" type="button" class="btn btn-default" onClick="javascript:MenuLib.openLearningDesign()"
			    	title="<fmt:message key="authoring.fla.page.menu.open" />">
				<i class="fa fa-folder-open-o"></i>
				<span class="hidden-sm hidden-xs"><fmt:message key="authoring.fla.page.menu.open" /></span>
			</button>
			<button id="openDropButton" type="button" class="btn btn-default desktopButton dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				<span class="caret"></span>
				<span class="sr-only">Toggle Dropdown</span>
			</button>
			<ul class="dropdown-menu dropdown-menu-right desktopButton">
				<li id="importSequenceButton" onClick="javascript:MenuLib.importLearningDesign()"><a href="#"><fmt:message key="authoring.fla.page.menu.import" /></a></li>
				<li id="importPartSequenceButton" onClick="javascript:MenuLib.importPartLearningDesign()"><a href="#"><fmt:message key="authoring.fla.page.menu.import.part" /></a></li>
			</ul>
		</div>

		<div class="btn-group btn-group-sm">
			<button id="saveButton" type="button" class="btn btn-default" onClick="javascript:MenuLib.saveLearningDesign()"
					data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i><span> <fmt:message key='authoring.fla.page.menu.save' /></span>"
			    	title="<fmt:message key="authoring.fla.page.menu.save" />">
				<i class="fa fa-save"></i>
				<span class="hidden-sm hidden-xs"><fmt:message key="authoring.fla.page.menu.save" /></span>
			</button>
			<button id="saveDropButton" type="button" class="btn btn-default desktopButton dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				<span class="caret"></span>
				<span class="sr-only">Toggle Dropdown</span>
			</button>
			<ul class="dropdown-menu dropdown-menu-right desktopButton">
				<li id="saveAsButton" onClick="javascript:MenuLib.saveLearningDesign(true)"><a href="#"><fmt:message key="authoring.fla.page.menu.saveas" /></a></li>
				<li id="exportLamsButton" onClick="javascript:MenuLib.exportLearningDesign(1)"><a href="#"><fmt:message key="authoring.fla.page.menu.export.lams" /></a></li>
			</ul>
		</div>
		
		<button id="cancelLiveEditButton" class="btn btn-default" onClick="javascript:GeneralLib.cancelLiveEdit()"
				data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i><span> <fmt:message key='authoring.fla.cancel.button' /></span>"
			    title="<fmt:message key="authoring.fla.cancel.button" />">
			<i class="fa fa-ban"></i> 
			<span class="hidden-sm hidden-xs"><fmt:message key="authoring.fla.cancel.button" /></span>
		</button>
		<div class="btn-group btn-group-sm desktopButton" role="group">
		<button id="copyButton" class="btn btn-default" onClick="javascript:MenuLib.copyActivity()"
			    title="<fmt:message key="authoring.fla.page.menu.copy" />">
			<i class="fa fa-copy"></i> 
			<span class="hidden-sm hidden-xs"><fmt:message key="authoring.fla.page.menu.copy" /></span>
		</button>
		<button id="pasteButton" class="btn btn-default" onClick="javascript:MenuLib.pasteActivity()"
			    title="<fmt:message key="authoring.fla.page.menu.paste" />">
			<i class="fa fa-paste"></i> 
			<span class="hidden-sm hidden-xs"><fmt:message key="authoring.fla.page.menu.paste" /></span>
		</button>
		</div>	
		
		<button id="transitionButton" class="btn btn-default" onClick="javascript:MenuLib.addTransition()"
			    title="<fmt:message key="authoring.fla.page.menu.transition" />">
			<i class="fa fa-long-arrow-right"></i> 
			<span class="hidden-sm hidden-xs"><fmt:message key="authoring.fla.page.menu.transition" /></span>
		</button>
		 
		<div class="btn-group btn-group-sm desktopButton" role="group">
		  <button class="btn btn-default dropdown-toggle" type="button" id="annotateButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true"
		  		  title="<fmt:message key="authoring.fla.page.menu.annotate" />">
		  	<i class="fa fa-font"></i>
		    <span class="hidden-sm hidden-xs"><fmt:message key="authoring.fla.page.menu.annotate" /></span>
		    <span class="caret"></span>
		  </button>
		  <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="annotateButton">
		    <li id="annotateLabelButton" onClick="javascript:MenuLib.addAnnotationLabel()"><a href="#"><fmt:message key="authoring.fla.page.menu.annotate.label" /></a></li>
		    <li id="annotateRegionButton" onClick="javascript:MenuLib.addAnnotationRegion()"><a href="#"><fmt:message key="authoring.fla.page.menu.annotate.region" /></a></li>
		  </ul>
		</div>
			
		<button id="arrangeButton" class="btn btn-default desktopButton" onClick="javascript:GeneralLib.arrangeActivities()"
			    title="<fmt:message key="authoring.fla.page.menu.arrange" />">
			<i class="fa fa-th"></i> 
			<span class="hidden-sm hidden-xs"><fmt:message key="authoring.fla.page.menu.arrange" /></span>
		</button>
		
		<button id="weightButton" class="btn btn-default desktopButton" onClick="javascript:MenuLib.openWeights()"
			    title="<fmt:message key="authoring.fla.weights.menu" />">
			<i class="fa fa-balance-scale"></i> 
			<span class="hidden-sm hidden-xs"><fmt:message key="authoring.fla.weights.menu" /></span>
		</button>	
		
		<button id="questionBankButton" class="btn btn-default desktopButton" onClick="javascript:MenuLib.openQuestionBank()"
			    title="<fmt:message key="label.question.bank" />">
			<i class="fa fa-bank"></i> 
			<span class="hidden-sm hidden-xs"><fmt:message key="label.question.bank" /></span>
		</button>
		
		<button id="previewButton" class="btn btn-default" onClick="javascript:MenuLib.openPreview()"
				data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i><span> <fmt:message key="authoring.fla.page.menu.preview" /></span>"
			    title="<fmt:message key="authoring.fla.page.menu.preview" />">
			<i class="fa fa-search-plus"></i> 
			<span class="hidden-sm hidden-xs"><fmt:message key="authoring.fla.page.menu.preview" /></span>
		</button>

		<a id="helpButton" class="btn btn-xs btn-default" target="_blank" href="<%=Configuration.get(ConfigurationKeys.HELP_URL)%>/authoring"><i class="fa fa-question-circle text-primary"></i></a>
	</div>
	
	<table id="authoringTable">
		<tr>
			<td id="templateContainerCell">
				<div id="template-container-collapse">
					<i id="template-categories-collapse-button" class="fa fa-chevron-circle-down"
					   title="<fmt:message key='authoring.fla.tool.category.expand' />"	
					   onClick="javascript:MenuLib.toggleExpandTemplateCategories()"></i>
				</div>
				<div id="template-container-panel-group" class="templateContainer scrollable panel-group" role="tablist" aria-multiselectable="true">
				
					<c:forTokens items="1,3,2,4,6,5" delims="," var="toolCategoryId">
						<div class="panel panel-default">
					 	  <div class="panel-heading" role="tab" id="collapse-heading-tool-category-${toolCategoryId}"
					 	  		data-toggle="collapse" href="#collapse-tool-category-${toolCategoryId}" 
								aria-expanded="false" aria-controls="collapse-tool-category-${toolCategoryId}">
					 	  		<span class="panel-title">
									<fmt:message key="authoring.fla.tool.category.${toolCategoryId}" />
					 	  		</span>
					 	  		<div class="collapse-heading-bottom-border"></div>
					 	  </div>
						 
						  <div id="collapse-tool-category-${toolCategoryId}" class="panel-collapse collapse" 
					       	   role="tabpanel" aria-labelledby="collapse-heading-tool-category-${toolCategoryId}">
					      </div>
					   </div>
					</c:forTokens>
					
					<%-- Shared with svgGenerator.jsp --%>
					<%@ include file="authoringTemplatePart.jsp"%> 
					
				</div>
			</td>
			<td id="canvasContainerCell">
				<div id="ldDescriptionDiv">
					<div id="ldDescriptionTitleContainer">
						<span id="ldDescriptionFieldTitle"><fmt:message key="authoring.fla.page.ld.title" /></span>
						<span id="ldDescriptionFieldModified"></span>
						<i id="ldDescriptionHideTip" class="fa fa-chevron-circle-down" title='<fmt:message key="authoring.fla.page.ld.title.desc" /> '
						   onClick="javascript:MenuLib.toggleDescriptionDiv()"></i>
					</div>
					<div id="ldDescriptionDetails">
						<div class="ldDescriptionLabel"><label for="ldDescriptionFieldDescription"><fmt:message key="authoring.fla.page.ld.description" /></label><p class="text-muted"><i class="text-primary fa fa-info-circle" aria-hidden="true"></i> <fmt:message key="authoring.fla.page.ld.description.placeholder" /></p></div>
						<div id="ldDescriptionEditorContainer">
							<lams:CKEditor id="ldDescriptionFieldDescription" contentFolderID="${contentFolderID}" value=""></lams:CKEditor>
						</div>

						<div id="ldLicenseDetails">						
						<img id="ldDescriptionLicenseImage" class="pull-right"/>
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
						<button id="ldDescriptionLicenseButton" class="btn btn-default btn-xs"><span><fmt:message key="label.view"/></span></button>
						<div id="ldDescriptionLicenseTextContainer">
							<div class="ldDescriptionLabel"><fmt:message key="authoring.fla.page.ld.license.info" /></div>
							<textarea id="ldDescriptionLicenseText" rows="5"></textarea>
						</div>
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
					<div id="ldStoreDialogAccessDiv">
						<div id="ldStoreDialogAccessTitle"><fmt:message key="authoring.fla.page.dialog.access" /></div>
					</div>
				</td>
			</tr>

			<tr>
				<td id="ldStoreDialogButtonCell" colspan="2">
					<div class="container-fluid">
						<div id="ldStoreDialogLeftButtonContainer" class="buttonsbar btn-group-sm">
							<button id="ldStoreDialogNewFolderButton" class="btn btn-default">
								<i class="fa fa-folder"></i> 
								<span><fmt:message key="authoring.fla.new.folder.button" /></span>
							</button>
							
							<button id="ldStoreDialogCopyButton" class="btn btn-default">
								<i class="fa fa-copy"></i> 
								<span><fmt:message key="authoring.fla.copy.button" /></span>
							</button>
							
							<button id="ldStoreDialogCutButton" class="btn btn-default">
								<i class="fa fa-cut"></i> 
								<span><fmt:message key="authoring.fla.cut.button" /></span>
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
							<span id="ldStoreDialogModifiedDateSpan" class="loffset20"></span>
							
							<label for="ldStoreDialogReadOnlyCheckbox" id="ldStoreDialogReadOnlyLabel" class="loffset10">
								<input type="checkbox" id="ldStoreDialogReadOnlyCheckbox" />
								<span id="ldStoreDialogReadOnlySpan"><fmt:message key="authoring.fla.readonly.checkbox" /></span>
							</label>
						</div>
						
						<div id="ldStoreDialogNameContainer">
							<span><fmt:message key="authoring.fla.page.dialog.ld.title" /></span>
							<input type="text"/>
						</div>
						
						<div id="ldStoreDialogRightButtonContainer" class="action-buttons pull-right">

							<button id="ldStoreDialogCancelButton" class="btn btn-sm btn-default">
								<i class="fa fa-ban"></i> 
								<span><fmt:message key="authoring.fla.cancel.button" /></span>
							</button>

							<button id="ldStoreDialogSaveButton" class="btn btn-sm btn-primary"
									data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i><span> <fmt:message key='authoring.fla.save.button' /></span>">
								<i class="fa fa-save"></i> 
								<span><fmt:message key="authoring.fla.save.button" /></span>
							</button>
							
							<button id="ldStoreDialogOpenButton" class="btn btn-sm btn-primary"
									data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i><span> <fmt:message key='authoring.fla.open.button' /></span>">
								<i class="fa fa-folder-open-o"></i> 
								<span><fmt:message key="authoring.fla.open.button" /></span>
							</button>
							
							<button id="ldStoreDialogImportPartButton" class="btn btn-sm btn-primary">
								<i class="fa fa-arrow-circle-o-down"></i> 
								<span><fmt:message key="authoring.fla.import.button" /></span>
							</button>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	
	<!-- GROUP OR CONDITION TO BRANCH MATCHING DIALOG -->
	
	<div id="branchMappingDialogContents" class="dialogContents branchMappingDialog" style="display:none">
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
			<tr id="branchMappingOrderedRow">
				<td colspan="5">
					<input id="branchMappingOrderedAscCheckbox" type="checkbox" checked="checked"></input>
					<fmt:message key="authoring.fla.branch.mapping.ordered.asc" />
				</td>
			</tr>
			<tr>
				<td colspan="5">
					<div class="container-fluid">
						<div class="pull-right btn-group btn-group-sm" role="group">
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
					 <input class="propertiesContentFieldTitle" type="text" maxlength="50">
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.default" />
				</td>
				<td>
					<input class="propertiesContentFieldDefault" type="checkbox">
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
					 <input class="propertiesContentFieldTitle" type="text" maxlength="50">
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
					 <input class="propertiesContentFieldTitle" type="text" maxlength="50">
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
					 		name="propertiesContentFieldGroupDivide">
					 <input class="propertiesContentFieldGroupCount spinner" type="number">
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.groups.learners" />
				</td>
				<td>
					<input class="propertiesContentFieldLearnerCountEnable" type="radio"
					 		name="propertiesContentFieldGroupDivide">
					 <input class="propertiesContentFieldLearnerCount spinner" type="number">
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.groups.equal" />
				</td>
				<td>
					 <input class="propertiesContentFieldEqualSizes" type="checkbox">
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.groups.view.learners" />
				</td>
				<td>
					 <input class="propertiesContentFieldViewLearners" type="checkbox">
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
				<!-- Empty row with 4 cells so table structure is the same for all gate types -->
				<td></td><td></td><td></td><td></td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.title" />
				</td>
				<td colspan="3">
					 <input class="propertiesContentFieldTitle" type="text" maxlength="50">
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
					 	<option value="password"><fmt:message key="authoring.fla.page.prop.gate.type.password" /></option>
					 </select>
				</td>
			</tr>
			<tr class="propertiesContentRowGateTypeBased">
				<td>
					<fmt:message key="authoring.fla.page.prop.input" />
				</td>
				<td>
					  <select class="propertiesContentFieldInput"></select>
				</td>
			</tr>
			<tr class="propertiesContentRowGateTypeBased propertiesContentRowGateSchedule">
				<td>
					<fmt:message key="authoring.fla.page.prop.gate.delay" />
				</td>
				<td>
					<input class="propertiesContentFieldOffsetDay spinner" type="number"> 
					<fmt:message key="authoring.fla.page.prop.days" />
				</td>
				<td>
					<input class="propertiesContentFieldOffsetHour spinner" type="number"> 
					<fmt:message key="authoring.fla.page.prop.hours" />
				</td>
				<td>
					<input class="propertiesContentFieldOffsetMinute spinner" type="number"> 
					<fmt:message key="authoring.fla.page.prop.minutes" />
				</td>
			</tr>
			<tr class="propertiesContentRowGateTypeBased propertiesContentRowGateSchedule">
				<td colspan="3">
					<fmt:message key="authoring.fla.page.prop.gate.activity.finish.based" />
				</td>
				<td>
					 <input class="propertiesContentFieldActivityCompletionBased" type="checkbox">
				</td>
			</tr>
			<tr class="propertiesContentRowGateTypeBased propertiesContentRowGateSchedule propertiesContentRowConditions propertiesContentRowPermission">
				<td colspan="3">
					<fmt:message key="authoring.fla.page.prop.gate.stop.at.preceding" />
				</td>
				<td>
					 <input class="propertiesContentFieldStopAtPrecedingActivity" type="checkbox">
				</td>
			</tr>
			<tr class="propertiesContentRowGateTypeBased propertiesContentRowGatePassword">
				<td>
					<fmt:message key="authoring.fla.page.prop.password" />
				</td>
				<td colspan="3">
					 <input class="propertiesContentFieldPassword" type="text" maxlength="32">
				</td>
			</tr>
			<tr class="propertiesContentRowGateTypeBased propertiesContentRowConditions">
				<td colspan="4">
					<div class="btn btn-default propertiesContentFieldCreateConditions"><fmt:message key="authoring.fla.page.prop.conditions.create" /></div>
				</td>
			</tr>
			<tr class="propertiesContentRowGateTypeBased propertiesContentRowConditions">
				<td colspan="4">
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
					 <input class="propertiesContentFieldTitle" type="text" maxlength="50">
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
					<input class="propertiesContentFieldOptionalSequenceMin spinner" type="number">
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.branching.sequences.max" />
				</td>
				<td>
					<input class="propertiesContentFieldOptionalSequenceMax spinner" type="number">
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
					 <input class="propertiesContentFieldTitle" type="text" maxlength="50">
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
					 <input class="propertiesContentFieldTitle" type="text" maxlength="50">
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.optional.activities.min" />
				</td>
				<td>
					<input class="propertiesContentFieldOptionalActivityMin spinner" type="number">
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="authoring.fla.page.prop.optional.activities.max" />
				</td>
				<td>
					<input class="propertiesContentFieldOptionalActivityMax spinner" type="number">
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
					 <input class="propertiesContentFieldTitle" type="text">
				</td>
			</tr>
			<tr height="50px" valign="top">
				<td>
					<fmt:message key="authoring.fla.page.prop.color" />
				</td>
				<td>
					 <input class="propertiesContentFieldColor" type="text">
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
					 <input class="propertiesContentFieldTitle" type="text">
				</td>
			</tr>
			<tr height="50px" valign="top">
				<td>
					<fmt:message key="authoring.fla.page.prop.color" />
				</td>
				<td>
					 <input class="propertiesContentFieldColor" type="text">
				</td>
			</tr>
			<tr >
				<td>
					<fmt:message key="authoring.fla.page.prop.size" />
				</td>
				<td>
					<i class="fa fa-plus labelPlusSize"></i><i class="fa fa-minus labelMinusSize"></i>
				</td>
			</tr>
			
		</table>
	</div>
	
	<div id="propertiesContentGroupNaming" class="dialogContents">
		<div id="groupNamingGroups"></div>
		<div class="container-fluid">
			<div class="btn-group btn-group-sm pull-right" role="group">
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
			 <input id="singleRangeSpinner" class="outputSelectDependent spinner" type="number" />
			 <div id="multiRangeDiv" class="outputSelectDependent">
			 	<fmt:message key="authoring.fla.page.dialog.cond.range.from" /> <input id="multiRangeFromSpinner" class="spinner" type="number" />
			 	<fmt:message key="authoring.fla.page.dialog.cond.range.to" /> <input id="multiRangeToSpinner" class="spinner" type="number" />
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
		 	<div class="btn-group btn-group-sm pull-right" role="group">
				<button id="outputConditionsOKButton" class="btn btn-default">
					<i class="fa fa-check"></i> 
					<span><fmt:message key="authoring.fla.ok.button" /></span>
				</button>
				
				<button id="outputConditionsCancelButton" class="btn btn-default">
					<i class="fa fa-ban"></i> 
					<span><fmt:message key="authoring.fla.cancel.button" /></span>
				</button>

			</div>
			
			<div class="btn-group btn-group-sm pull-right" role="group">
				<button id="outputConditionsClearAllButton" class="btn btn-default outputSelectDependent rangeOutputButton">
					<i class="fa fa-remove"></i> 
					<span><fmt:message key="authoring.fla.clear.all.button" /></span>
				</button>
				
				<button id="outputConditionsRemoveButton" class="btn btn-default outputSelectDependent rangeOutputButton">
					<i class="fa fa-remove"></i> 
					<span><fmt:message key="authoring.fla.remove.condition.button" /></span>
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
	
		
	<!-- OUTPUT WEIGHTS DIALOG -->
	<div id="weightsDialogContents" class="dialogContents">
		<table>
			<thead>
				<tr>
					<td><fmt:message key="authoring.fla.weights.activity" /></td>
					<td><fmt:message key="authoring.fla.weights.output" /></td>
					<td><fmt:message key="authoring.fla.weights.weight" /></td>
				</tr>
			</thead>
			<tbody>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="2"><fmt:message key="authoring.fla.weights.sum" /></td>
					<td id="sumWeightCell"></td>
				</tr>
			</tfoot>
		</table>
	</div>
	
	<!-- INFO DIALOG -->
	<div id="infoDialogContents" class="dialogContents">
		<div id="infoDialogBody"></div>
		<div id="infoDialogButtons">
			<button id="infoDialogOKButton" class="btn btn-default pull-right">
				<span><fmt:message key="authoring.fla.ok.button" /></span>
			</button>
		</div>
	</div>
</body>
</lams:html>