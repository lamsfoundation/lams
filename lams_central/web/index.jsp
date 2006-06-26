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
				"content",
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
						node.childNodes.item(0).src = "http://www.dddekerf.dds.nl/DHTML_Treeview/minus.gif";
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
						node.childNodes.item(0).src = "http://www.dddekerf.dds.nl/DHTML_Treeview/plus.gif";
					}
				}
		
				nextDiv.style.display = 'none';
			}
		
		}
	</script>
</head>
<body onload="getContent()" bgcolor="#9DC5EC" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
	<table width="95%" height="95%" border="0" cellspacing="0" cellpadding="0" align="center">
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td width="100%" align="center">
			<table bgcolor="#ffffff" width="100%" height="100%" cellspacing="5" cellpadding="5" align="center">
				<tr>
					<td align="left" style="font-size:20pt">
						<fmt:message key="index.welcome"/>, <lams:user property="firstName"/> <img src="images/lamb_big.png"/>
					</td>
					<td align="right">
						<a title="<fmt:message key="index.refresh.hint"/>" href="javascript:getContent()"><fmt:message key="index.refresh"/></a>
						<a href="home.do?method=logout"><fmt:message key="index.logout"/></a>
					</td>
				</tr>
				<tr>
					<td colspan=2 id="content" width="100%" align="center">
						<img src="images/loading.gif" /> <font color="gray" size="4"><fmt:message key="msg.loading"/></font>
					</td>
				</tr>
				<tr valign="bottom">
					<td colspan="2" align="center">
						<img src="images/launch_page_graphic.jpg"/>
					</td>
				</tr>
				<tr valign="bottom">
					<td align="left">
						<A HREF="copyright.jsp" onClick="window.open('copyright.jsp','copyright','resizable,width=570,height=250,scrollbars');return false;">
							&copy; <fmt:message key="msg.LAMS.copyright.short"/>
						</a>
					</td>
					<td align="right"><fmt:message key="msg.LAMS.version"/></td>
				</tr>
			</table>
		</td>
	</tr>
	</table>
</body>	
</html>

