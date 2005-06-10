<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head><title>Learning Design Details</title></head>
<body>
	<%
		String str = (String)request.getSession().getAttribute("details");
		out.println(str);
	%>
</body>
</html>