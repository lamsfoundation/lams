<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-logic" prefix="logic" %>

	<script language="JavaScript" type="text/javascript" src="includes/javascript/prototype.js"></script>
	<script type="text/javascript" language="javascript">
		var i = Math.round(1000*Math.random());
		function getContent(){
			i++;
			var url = "index.do?unique="+i+"&state=archived";
			var params = "";
			var myAjax = new Ajax.Updater(
				"courselist",
				url,
				{
					method: 'get',
					parameters: params
				});
		}
	</script>

<table border="0">
	<tr>
		<td><h2><fmt:message key="index.myprofile" /></h2></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="label.name" />: 
		</td>
		<td><bean:write name="fullName" />
		</td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="label.email" />: 
		</td>
		<td><bean:write name="email" />
		</td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="label.portrait.current" />:
		</td>
		<td><logic:notEqual name="portraitUuid" value="0">
				<img src="/lams/download/?uuid=<bean:write name="portraitUuid" />&preferDownload=false" />
			</logic:notEqual>
			<logic:equal name="portraitUuid" value="0">
				<i><fmt:message key="msg.portrait.none" /></i>
			</logic:equal>
		</td>
	</tr>
</table>

<p><a href="index.do?state=active&tab=editprofile"><fmt:message key="title.profile.edit.screen" /></a><br />
<a href="index.do?state=active&tab=password"><fmt:message key="title.password.change.screen" /></a><br />
<a href="index.do?state=active&tab=portrait"><fmt:message key="title.portrait.change.screen" /></a><br />
</p>

<h2><fmt:message key="title.archived.groups" /></h2>

<div id="courselist" align="center">
	<img src="images/loading.gif" /> <font color="gray" size="4"><fmt:message key="msg.loading"/></font>
</div>

<script type="text/javascript" language="javascript">
	getContent();
</script>
