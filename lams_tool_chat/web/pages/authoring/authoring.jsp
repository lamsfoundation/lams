<%@ include file="/common/taglibs.jsp"%>

<lams:Tabs>
	<lams:Tab id="1" key="button.basic" />
	<lams:Tab id="2" key="button.advanced" />
	<lams:Tab id="3" key="button.instructions" />
</lams:Tabs>

<div class="tabbody">
	<lams:TabBody id="1" titleKey="button.basic" page="basic.jsp" />
	<lams:TabBody id="2" titleKey="button.advanced" page="advanced.jsp" />
	<lams:TabBody id="3" titleKey="button.instructions" page="instructions.jsp" />
</div>
