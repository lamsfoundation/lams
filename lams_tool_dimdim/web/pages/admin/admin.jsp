<%@ include file="/common/taglibs.jsp"%>

<div id="content">

	<h1><fmt:message key="admin.title" /></h1>
	
	<html:form action="/admin/save">
		<fmt:message key="admin.dimdimServerURL"/> : <html:text property="dimdimServerURL" />
		<html:submit titleKey="label.save" styleClass="button"></html:submit>	
	</html:form>

</div>

