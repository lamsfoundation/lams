<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-bean" prefix="bean"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-logic" prefix="logic"%>
<%@ taglib uri="tags-core" prefix="c"%>

<script language="JavaScript" type="text/javascript"
	src="includes/javascript/prototype.js"></script>
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
	
<div style="clear:both;"></div>

<h2 class="small-space-top"><fmt:message key="index.myprofile" />
</h2>

<div class="shading-bg">

<table>

	<tr>

		<td class="align-right"><span class="field-name"><fmt:message
			key="label.name" />:</span></td>
		<td><bean:write name="fullName" /></td>

		<td rowspan="3" class="userpage-td" valign="top">
		<ul>
			<li class="no-list-type"><a
				href="index.do?state=active&tab=editprofile"><fmt:message
				key="title.profile.edit.screen" /> </a></li>

			<li class="no-list-type"><a
				href="index.do?state=active&tab=password"><fmt:message
				key="title.password.change.screen" /></a></li>

			<li class="no-list-type"><a
				href="index.do?state=active&tab=portrait"><fmt:message
				key="title.portrait.change.screen" /></a></li>
		</ul>

		</td>
	</tr>
	<tr>
		<td class="align-right"><span class="field-name"><fmt:message
			key="label.email" />:</span></td>
		<td><bean:write name="email" /></td>
	</tr>
	<tr>
		<td class="align-right" valign="top"><span class="field-name"><fmt:message
			key="label.portrait.current" />:</span></td>
		<td valign="top"><logic:notEqual name="portraitUuid" value="0">
			<img class="img-border"
				src="/lams/download/?uuid=<bean:write name="portraitUuid" />&preferDownload=false" />
		</logic:notEqual> <logic:equal name="portraitUuid" value="0">
			<c:set var="lams">
				<lams:LAMSURL />
			</c:set>
				<em><fmt:message key="msg.portrait.none" /></em>
			<div class="small-space-top">
			<img src="${lams}/images/default_user.gif" alt="User Portrait"
				class="img-border">
				</div>
		</logic:equal></td>
	<tr>
</table>

</div>


<h2 class="space-top"><fmt:message key="title.archived.groups" />
</h2>

<div id="courselist" class="shading-bg"><img
	src="images/loading.gif" /> <font color="gray" size="4"><fmt:message
	key="msg.loading" /> </font></div>

<script type="text/javascript" language="javascript">
	getContent();
</script>
