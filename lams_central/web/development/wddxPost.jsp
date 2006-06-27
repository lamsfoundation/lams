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

<body>

	<div id="page">	

		<h1 class="no-tabs-below">
			Test WDDX Packet
		</h1>
	
		<div id="header-no-tabs"></div>
		<div id="content">

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
	
		</div>
		
		<div id="footer"></div>
		
	</div>
</body>
</html>
