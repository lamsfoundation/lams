
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/jstl/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale = "true">
    <head>
       <html:base target="../"/>
      <title><tiles:getAsString name="title"/></title>
      <meta http-equiv="pragma" content="no-cache">
      <meta http-equiv="cache-control" content="no-cache">
	  <link href="../style.css" rel="stylesheet" type="text/css">
    </head>
    
    <body bgcolor="#9DC5EC" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
      <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
        <!-- header -->
        <c:set var="pageheader" scope="session"><tiles:getAsString name="pageHeader"/></c:set>
		<tiles:insert attribute="header" />
        <!-- end of header -->
        <!-- main content -->
        <tiles:insert attribute="content" />
        <!--end of main content-->
        <!--footer-->
        <tiles:insert attribute="footer" />
        <!-- end of footer -->
      </table>
    </body>
</html:html>
