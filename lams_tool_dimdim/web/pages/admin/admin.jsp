<%@ include file="/common/taglibs.jsp"%>

<div id="content">

	<h1>
		<fmt:message key="admin.title" />
	</h1>

	<html:form action="/admin/save">

		<div>
			<fmt:message key="admin.dimdimServerURL" />
			:
			<html:text property="dimdimServerURL" />
		</div>
		<div class="align-right">
			<html:submit styleClass="button">
				<fmt:message key="label.save" />
			</html:submit>
		</div>
	</html:form>
</div>

