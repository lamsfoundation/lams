<%@ include file="/common/taglibs.jsp"%>

<lams:Switch name="assessment.useSelectLeaderToolOuput" id="useSelectLeaderToolOuput"
	useSpringForm="true"
	labelKey="label.use.select.leader.tool.output"
	tooltipKey="label.use.select.leader.tool.output.tooltip"
	tooltipDescriptionKey="label.use.select.leader.tool.output.tooltip.description" />

<c:if test="${sessionMap.isQuestionEtherpadEnabled}">
	<lams:Switch name="assessment.questionEtherpadEnabled" id="questionEtherpadEnabled"
		useSpringForm="true"
		labelKey="label.authoring.advance.question.etherpad"
		tooltipKey="label.authoring.advance.question.etherpad.tooltip"
		tooltipDescriptionKey="label.authoring.advance.question.etherpad.tooltip.description" />
</c:if>
