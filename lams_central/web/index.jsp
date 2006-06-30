<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ page import="org.lamsfoundation.lams.security.JspRedirectStrategy" %>
<%@ page import="org.lamsfoundation.lams.web.util.HttpSessionManager" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<%JspRedirectStrategy.welcomePageStatusUpdate(request,response);%>
<%HttpSessionManager.getInstance().updateHttpSessionByLogin(request.getSession(),request.getRemoteUser()); %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>LAMS::<fmt:message key="index.welcome"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<lams:css/>
	<link rel="icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
		<script language="JavaScript" type="text/javascript" src="includes/javascript/getSysInfo.js"></script>
	<script language="JavaScript" type="text/javascript" src="includes/javascript/openUrls.js"></script>
	<script language="JavaScript" type="text/javascript" src="includes/javascript/prototype.js"></script>
	<script type="text/javascript" language="javascript">
		function getContent(){
			var url = "index.do";
			var params = "";
			var myAjax = new Ajax.Updater(
				"courselist",
				url,
				{
					method: 'get',
					parameters: params
				});
		}

		function findNextDiv(node)
		 {
		 	 var nd = node.nextSibling;
		 	  while(nd.nodeName.toUpperCase()!="DIV"){
		 	   	nd=nd.nextSibling;
		 	  }  
		 	   return nd
		 }
		 
		function toggle(node)
		{
			// Unfold the branch if it isn't visible
			var nextDiv = findNextDiv(node);
			if (nextDiv.style.display == 'none')
			{
				// Change the image (if there is an image)
				if (node.childNodes.length > 0)
				{
					if (node.childNodes.item(0).nodeName == "IMG")
					{
						node.childNodes.item(0).src = "images/tree_open.gif";
					}
				}
		
				nextDiv.style.display = 'block';
			}
			// Collapse the branch if it IS visible
			else
			{
				// Change the image (if there is an image)
				if (node.childNodes.length > 0)
				{
					if (node.childNodes.item(0).nodeName == "IMG")
					{
						node.childNodes.item(0).src = "images/tree_closed.gif";
					}
				}
		
				nextDiv.style.display = 'none';
			}
		
		}
	</script>
</head>
<body onload="getContent()" bgcolor="#9DC5EC" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
	<div id="page">	
		<H1 class="no-tabs-below"><fmt:message key="index.welcome"/>, <lams:user property="firstName"/> <img src="images/lamb_big.png"/></H1>
		<div id="header-no-tabs"></div>
		<div id="content">
			<p align="right"><fmt:message key="msg.LAMS.version"/></p>
			<p align="right"><a title="<fmt:message key="index.refresh.hint"/>" href="javascript:getContent()"><fmt:message key="index.refresh"/></a>
					<a href="home.do?method=logout"><fmt:message key="index.logout"/></a>
				</p>
			<div id="courselist" align="center">
					<img src="images/loading.gif" /> <font color="gray" size="4"><fmt:message key="msg.loading"/></font>
			</div>
			<p><img src="images/launch_page_graphic.jpg"/></p>
			<p><A HREF="copyright.jsp" target='copyright' onClick="window.open('copyright.jsp','copyright','resizable,left='+(self.screen.width/2-750/2)+',top='+(self.screen.height/2-33/2)+',width=750,height=388,scrollbars');return false;">
						&copy; <fmt:message key="msg.LAMS.copyright.short"/></a></p>
		</div>
	</div>
</body>	
</html>

