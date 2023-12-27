<%@ include file="/common/taglibs.jsp"%>

<!-- ========== Advanced Tab ========== -->

<lams:SimplePanel titleKey="label.image.options">
	<div class="checkbox">
		<label for="allowViewOthersImages">
			<form:checkbox path="allowViewOthersImages" value="1" id="allowViewOthersImages"/>
			<fmt:message key="advanced.allowViewOthersImages" />
		</label>
	</div>
</lams:SimplePanel>

<lams:OutcomeAuthor toolContentId="${sessionMap.toolContentID}" />

