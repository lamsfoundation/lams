<!DOCTYPE html>

<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<c:set var="lams" ><lams:LAMSURL/></c:set>
<c:set var="title" scope="request">
	<fmt:message key="authoring.learning.design.templates"/>
</c:set>

<lams:html>
<lams:head>
	<title>${title}</title>
	<lams:css />
	<lams:css suffix="authoring"/>
</lams:head>

<body class="stripes">

<lams:Page type="admin" title="${title}">
<p><fmt:message key="authoring.template.list.introduction"/></p>
<table class="table table-striped table-bordered">
<tr><td><a id="tblLink" href="${lams}authoring/template/tbl/init.do"><fmt:message key="authoring.tbl.template.title"/></a> - <fmt:message key="authoring.tbl.template.description"/></td></tr>
</table>
<div class="button-group voffset10">
<a href="#" id="cancelButton" class="btn btn-default btn-sm pull-left" onclick="javascript:closeWindow()"><span class="cancelIcon"><fmt:message key="button.cancel"/></span></a>
</div>
</lams:Page>

</body>
</lams:html>
