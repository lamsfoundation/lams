<%@ taglib uri="tags-html-el" prefix="html-el" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ page import="org.apache.struts.Globals" %>
<%
String cprotocol = request.getProtocol();
if(cprotocol.startsWith("HTTPS")){
	cprotocol = "https://";
}else{
	cprotocol = "http://";
}
String rootPath = cprotocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<logic:present name="<%=Globals.ERROR_KEY%>">
<tr>
	<td width="10%"  align="right" >
		<img src="<%=rootPath%>/images/error.jpg" alt="Error occured"/>
	</td>
	<td width="90%" valign="center" class="body" colspan="2">
		<html-el:errors/>
	</td>
</tr>
</logic:present>