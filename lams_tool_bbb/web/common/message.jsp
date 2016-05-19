<%@ include file="/common/taglibs.jsp"%>

<div class="panel">
	<lams:Alert id="errorMessages" type="danger" close="false">
		<fmt:message key="${messageKey}" />
	</lams:Alert>

	<p>
		<a href="#" class="button" onclick="window.close();" style="float: right"> <fmt:message key="button.close" />
		</a>
	</p>
</div>

