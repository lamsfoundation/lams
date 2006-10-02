<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="tags-tiles" prefix="tiles"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
        
<html>
	<tiles:insert attribute="header" />
	<body>
		<tiles:useAttribute name="pageTitleKey" scope="request" />
		<bean:define name="pageTitleKey" id="pTitleKey" type="String" />
		<logic:notEmpty name="pTitleKey">
			<h1>
				<bean:message key="<%=pTitleKey %>" />
			</h1>
		</logic:notEmpty>

		<tiles:insert attribute="body" />
		<tiles:insert attribute="footer" />
	</body>
</html>
