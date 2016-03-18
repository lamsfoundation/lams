<%@ include file="/common/taglibs.jsp"%>

<lams:Alert id="errorMessages" type="danger" close="false">
	${requestScope.message};
</lams:Alert>
