<%@ taglib uri="/WEB-INF/struts/tlds/struts-html.tld" prefix="html" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>Send WDDX Packet To Server</title>
</head>

<body>

<html:form action="/WDDXPost.do" target="_self" method="POST" enctype="multipart/form-data">

  <p>Test posting file to form - this should be WDDX XML to a URL expecting 
  the XML in the post body.</p>
  <p>Form action: 
  
  	<html:text property="urlAction" maxlength="300"  size="50"/>
</p>
  <p>
    File to post: 
	<html:file property="wddxFile" size="40" styleClass="button" />
</p>
  <p>
	<html:submit>Send</html:submit>
  </p>
</html:form>
</body>
</html>
