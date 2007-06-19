<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.web.session.SessionManager" %>
<%@ page import="org.lamsfoundation.lams.admin.service.IImportService" %>

<%
	Integer importTotal = (Integer)SessionManager.getSession().getAttribute(IImportService.STATUS_IMPORT_TOTAL);
	Integer imported = (Integer)SessionManager.getSession().getAttribute(IImportService.STATUS_IMPORTED);
	String progress = "";
	try {
		float percent = imported.floatValue()/importTotal.floatValue() * 100;
		progress = (new Float(percent)).toString();
		progress = (progress.length() >= 5 ? progress.substring(0,5) : progress);
	} catch (Exception e) {}
	if (progress.length()>0) out.println(progress+" % completed...");
%>