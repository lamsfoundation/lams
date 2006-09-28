<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-html" prefix="html" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<META http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Send WDDX Packet To Server</title>
<lams:css/>
</head>

<body class="stripes">

		<h1>
			Test WDDX Packet
		</h1>

		<div id="content">
			<html:form action="/servlet/WDDXPost.do" target="_self" method="POST" enctype="multipart/form-data">
			
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
				<html:submit styleClass="button">Send</html:submit>
			  </p>
			</html:form>
	
			<p>If this call doesn't work, check your struts-config.xml file. The struts action and form
			for this page is normally commented out and will need to be uncommented. Look for WDDXPost.</p>
		</div>
	   
		<div id="footer">
		</div><!--closes footer-->

</body>
</html>
