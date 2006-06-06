	<%-- 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as 
  published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="tags-tiles" prefix="tiles" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html:html locale = "true">
    <head>
       <meta http-equiv="content-type" content="text/html; charset=UTF-8">
       <html:base/>
	   <lams:css/>
      <title><c:out value="${title}"/></title>
      <meta http-equiv="pragma" content="no-cache">
      <meta http-equiv="cache-control" content="no-cache">

	<script type="text/javascript">
	<!-- 
		function init(){
			getGroupsAndNonmembers();
		}


		function ajustButtonStatus(){
			// if the maximum number of groups reached, disable the add group fields
			if(document.getElementById("groups").length >= <c:out value="${maxNumberOfGroups}"/> ){
				document.getElementById("newgroupname").disabled=true;
				document.getElementById("groupadd").disabled=true;
			} else {
				document.getElementById("newgroupname").disabled=false;
				document.getElementById("groupadd").disabled=false;
			}

			if(document.getElementById("groups").selectedIndex==-1){
				document.getElementById("groupremove").disabled=true;
				document.getElementById("nonmembersadd").disabled=true;
				document.getElementById("membersremove").disabled=true;
			}else{
				if ( <c:out value="${mayDelete}"/> ) {
					document.getElementById("groupremove").disabled=false;
				}
				if(document.getElementById("nonmembers[]").selectedIndex==-1){
					document.getElementById("nonmembersadd").disabled=true;
				}else{
					document.getElementById("nonmembersadd").disabled=false;
				}
				if(document.getElementById("members[]").selectedIndex==-1 || ! <c:out value="${mayDelete}"/> ){
					document.getElementById("membersremove").disabled=true;
				}else{
					document.getElementById("membersremove").disabled=false;
				}
			}
		}

		function getGroupsAndNonmembers(){
			getGroups();
			getNonmembers();
			document.getElementById("members[]").options.length = 0;
			ajustButtonStatus();
		}

		function getGroups(){
			displayLoadingMessage();
			url="<lams:WebAppURL/>/grouping.do?method=getGroups&activityID=<c:out value="${activityID}"/>";
			if (window.XMLHttpRequest) { // Non-IE browsers
				groupRequest = new XMLHttpRequest();
				groupRequest.onreadystatechange = updateGroups;
				try {
						groupRequest.open("GET", url, true);
				} catch (e) {
						alert(e);
				}
				groupRequest.send(null);
			} else if (window.ActiveXObject) { // IE
				groupRequest = new ActiveXObject("Microsoft.XMLHTTP");
				if (groupRequest) {
						groupRequest.onreadystatechange = updateGroups;
						groupRequest.open("GET", url, true);
						groupRequest.send();
				}
			}
		}

		function updateGroups(){
			if (groupRequest.readyState == 4) { // Complete
				clearMessage();
				if (groupRequest.status == 200) { // OK response
					var grpSelectObj = document.getElementById("groups");
					grpSelectObj.options.length = 0;
					var res = groupRequest.responseText.replace(/^\s*|\s*$/g,"");
					if(res.length>0){
						var groups = res.split(";");
						for (i=0; i<groups.length; i++){
							var group = groups[i].split(",");
							grpSelectObj.options[grpSelectObj.length] = new Option(group[1]+" ("+group[2]+")",group[0]);
						}
					}
				}else{
					alert("<fmt:message key="error.grouping.data"/>"+" "+groupRequest.status+".");
				}
				ajustButtonStatus();
			}
		}

		function getNonmembers(){
			displayLoadingMessage();
			url="<lams:WebAppURL/>/grouping.do?method=getClassMembersNotGrouped&lessonID=<c:out value="${lessonID}"/>&activityID=<c:out value="${activityID}"/>";
			if (window.XMLHttpRequest) { // Non-IE browsers
				nonmembersRequest = new XMLHttpRequest();
				nonmembersRequest.onreadystatechange = updateNonmembers;
				try {
					nonmembersRequest.open("GET", url, true);
				} catch (e) {
					alert(e);
				}
				nonmembersRequest.send(null);
			} else if (window.ActiveXObject) { // IE
				nonmembersRequest = new ActiveXObject("Microsoft.XMLHTTP");
				if (nonmembersRequest) {
					nonmembersRequest.onreadystatechange = updateNonmembers;
					nonmembersRequest.open("GET", url, true);
					nonmembersRequest.send();
				}
			}
		}

		function updateNonmembers(){
			if (nonmembersRequest.readyState == 4) { // Complete
				clearMessage();
				if (nonmembersRequest.status == 200) { // OK response
					var nonmembersSelectObj = document.getElementById("nonmembers[]");
					nonmembersSelectObj.options.length = 0;
					var res = nonmembersRequest.responseText.replace(/^\s*|\s*$/g,"");
					if(res.length>0){
						var nonmembers = res.split(";");
						for (i=0; i<nonmembers.length; i++){
							var nonmember = nonmembers[i].split(",");
							nonmembersSelectObj.options[nonmembersSelectObj.length] = new Option(nonmember[2]+" "+nonmember[1],nonmember[0]);
						}
					}
				}else{
					alert("<fmt:message key="error.grouping.data"/>"+" "+groupRequest.status+".");
				}
				ajustButtonStatus();
			}
		}

		function getMembers(group){
			displayLoadingMessage();
			url="<lams:WebAppURL/>/grouping.do?method=getGroupMembers&activityID=<c:out value="${activityID}"/>&groupID="+group.value;
			if (window.XMLHttpRequest) { // Non-IE browsers
				memberRequest = new XMLHttpRequest();
				memberRequest.onreadystatechange = updateMembers;
				try {
					memberRequest.open("GET", url, true);
				} catch (e) {
					alert(e);
				}
				memberRequest.send(null);
			} else if (window.ActiveXObject) { // IE
				memberRequest = new ActiveXObject("Microsoft.XMLHTTP");
				if (memberRequest) {
					memberRequest.onreadystatechange = updateMembers;
					memberRequest.open("GET", url, true);
					memberRequest.send();
				}
			}
		}

		function updateMembers(){
			if (memberRequest.readyState == 4) { // Complete
				clearMessage();
				if (memberRequest.status == 200) { // OK response
					var membersSelectObj = document.getElementById("members[]");
					membersSelectObj.options.length = 0;
					var res = memberRequest.responseText.replace(/^\s*|\s*$/g,"");
					if(res.length>0){
						var members = res.split(";");
						for (i=0; i<members.length; i++){
							var member = members[i].split(",");
							membersSelectObj.options[membersSelectObj.length] = new Option(member[2]+" "+member[1],member[0]);
						}
					}
				}else{
					alert("<fmt:message key="error.grouping.data"/>"+" "+memberRequest.status);
				}
				ajustButtonStatus();
			}
		}

		
		function addGroup(){
			if(document.getElementById("newgroupname").value.replace(/^\s*|\s*$/g,"").length==0){
				alert("<fmt:message key="${error.grouping.add.group}"/>");
				document.getElementById("newgroupname").focus();
			}else{
				url="<lams:WebAppURL/>/grouping.do?method=addGroup&activityID=<c:out value="${activityID}"/>&name="+document.getElementById("newgroupname").value;
				if (window.XMLHttpRequest) { // Non-IE browsers
						addgrpRequest = new XMLHttpRequest();
						addgrpRequest.onreadystatechange = grpAdded;
						try {
								addgrpRequest.open("GET", url, true);
						} catch (e) {
								alert(e);
						}
						addgrpRequest.send(null);
				} else if (window.ActiveXObject) { // IE
						addgrpRequest = new ActiveXObject("Microsoft.XMLHTTP");
						if (addgrpRequest) {
								addgrpRequest.onreadystatechange = grpAdded;
								addgrpRequest.open("GET", url, true);
								addgrpRequest.send();
						}
				}
			}
		}

		function grpAdded(){
			if (addgrpRequest.readyState == 4) { // Complete
				if (addgrpRequest.status == 200) { // OK response
					getGroups();
					document.getElementById("newgroupname").value = "";
					document.getElementById("members[]").options.length=0;
				}else{
					alert("<fmt:message key="error.grouping.data"/>"+" "+addgrpRequest.status);
				}
				ajustButtonStatus();
			}
		}

		function removeGroup(){
			if(document.getElementById("groups").selectedIndex == -1){
				alert("<fmt:message key="${error.grouping.remove.group}"/>");
			}else{
				url="<lams:WebAppURL/>/grouping.do?method=removeGroup&activityID=<c:out value="${activityID}"/>&groupID="+document.getElementById("groups").value;
				if (window.XMLHttpRequest) { // Non-IE browsers
						rmgrpRequest = new XMLHttpRequest();
						rmgrpRequest.onreadystatechange = grpRemoved;
						try {
								rmgrpRequest.open("GET", url, true);
						} catch (e) {
								alert(e);
						}
						rmgrpRequest.send(null);
				} else if (window.ActiveXObject) { // IE
						rmgrpRequest = new ActiveXObject("Microsoft.XMLHTTP");
						if (rmgrpRequest) {
								rmgrpRequest.onreadystatechange = grpRemoved;
								rmgrpRequest.open("GET", url, true);
								rmgrpRequest.send();
						}
				}
			}
		}

		function grpRemoved(){
			if (rmgrpRequest.readyState == 4) { // Complete
				if (rmgrpRequest.status == 200) { // OK response
					var grpSelectObj = document.getElementById("groups");
					grpSelectObj.remove(grpSelectObj.selectedIndex);
					document.getElementById("members[]").options.length = 0;
					getNonmembers(document.getElementById("groupings"));
				}else{
					alert("<fmt:message key="error.grouping.data"/>"+" "+rmgrpRequest.status);
				}
				ajustButtonStatus();
			}
		}

		var count = 0;

		function addMembersToGroup(){
			var nonmembersSelectObj = document.getElementById("nonmembers[]");
			var members = "";
			for(i=0; i<nonmembersSelectObj.length; i++){
				if(nonmembersSelectObj.options[i].selected){
					count++;
					members = members + "," + nonmembersSelectObj.options[i].value;
				}	
			}
			url="<lams:WebAppURL/>/grouping.do?method=addMembers&activityID=<c:out value="${activityID}"/>&groupID="+document.getElementById("groups").value+"&members="+members.substr(1);
			if (window.XMLHttpRequest) { // Non-IE browsers
					addmbrsRequest = new XMLHttpRequest();
					addmbrsRequest.onreadystatechange = membersAdded;
					try {
							addmbrsRequest.open("GET", url, true);
					} catch (e) {
							alert(e);
					}
					addmbrsRequest.send(null);
			} else if (window.ActiveXObject) { // IE
					addmbrsRequest = new ActiveXObject("Microsoft.XMLHTTP");
					if (addmbrsRequest) {
							addmbrsRequest.onreadystatechange = membersAdded;
							addmbrsRequest.open("GET", url, true);
							addmbrsRequest.send();
					}
			}
		}

		function membersAdded(){
				if (addmbrsRequest.readyState == 4) { // Complete
						if (addmbrsRequest.status == 200) { // OK response
							getNonmembers(document.getElementById("groupings"));
							getMembers(document.getElementById("groups"));
							var groupSelectObj = document.getElementById("groups");
							var groupName = groupSelectObj.options[groupSelectObj.selectedIndex].text;
							var index1 = groupName.lastIndexOf("(");
							var index2 = groupName.lastIndexOf(")");
							var num = groupName.substring(index1+1,index2);
							num = parseInt(num) + count;
							groupSelectObj.options[groupSelectObj.selectedIndex].text = groupName.substring(0,index1)+"("+num+")";
							count = 0;
						}else{
							alert("<fmt:message key="error.grouping.data"/>"+" "+addmbrsRequest.status);
						}
					ajustButtonStatus();
				}
		}

		
		function removeMembersFromGroup(){
			var membersSelectObj = document.getElementById("members[]");
			var nonmembers = "";
			for(i=0; i<membersSelectObj.length; i++){
				if(membersSelectObj.options[i].selected){
					count++;
					nonmembers = nonmembers + "," + membersSelectObj.options[i].value;
				}	
			}
			url="<lams:WebAppURL/>/grouping.do?method=removeMembers&activityID=<c:out value="${activityID}"/>&groupID="+document.getElementById("groups").value+"&members="+nonmembers.substr(1);
			if (window.XMLHttpRequest) { // Non-IE browsers
					rmmbrsRequest = new XMLHttpRequest();
					rmmbrsRequest.onreadystatechange = membersRemoved;
					try {
							rmmbrsRequest.open("GET", url, true);
					} catch (e) {
							alert(e);
					}
					rmmbrsRequest.send(null);
			} else if (window.ActiveXObject) { // IE
					rmmbrsRequest = new ActiveXObject("Microsoft.XMLHTTP");
					if (rmmbrsRequest) {
							rmmbrsRequest.onreadystatechange = membersRemoved;
							rmmbrsRequest.open("GET", url, true);
							rmmbrsRequest.send();
					}
			}
		}

		function membersRemoved(){
			if (rmmbrsRequest.readyState == 4) { // Complete
					if (rmmbrsRequest.status == 200) { // OK response
						getMembers(document.getElementById("groups"));
						getNonmembers(document.getElementById("groupings"));
						var groupSelectObj = document.getElementById("groups");
						var groupName = groupSelectObj.options[groupSelectObj.selectedIndex].text;
						var index1 = groupName.lastIndexOf("(");
						var index2 = groupName.lastIndexOf(")");
						var num = groupName.substring(index1+1,index2);
						num = parseInt(num) - count;
						groupSelectObj.options[groupSelectObj.selectedIndex].text = groupName.substring(0,index1)+"("+num+")";
						count = 0;
					}else{
						alert("<fmt:message key="error.grouping.data"/>"+" "+rmmbrsRequest.status);
					}
			ajustButtonStatus();
			}
		}	

		function displayLoadingMessage() {
			document.getElementById("message").innerHTML = "<fmt:message key="label.grouping.loading"/>";
		}

		function clearMessage() {
			document.getElementById("message").innerHTML = "";
		}
		
	//-->
	</script>

	  <NOSCRIPT><!--This browser doesn't supports scripting--></NOSCRIPT>
	
</head>

<body onLoad="init()">

	<form>

	<h1><c:out value="${title}"/></h1>
	
	<p><c:out value="${description}"/></p>
	
	<p><STRONG><fmt:message key="label.grouping.max.num.in.group.heading"/></STRONG> <c:out value="${maxNumberOfGroups}"/> 
	<span id="message" align="right"></span></p>

	<P><STRONG><fmt:message key="label.grouping.general.instructions.heading"/></STRONG> <fmt:message key="label.grouping.general.instructions.line1"/></P>

	<p><fmt:message key="label.grouping.general.instructions.line2"/></p>

	<table cellspacing="5" cellpadding="4" class="flatborder">
		<tr>
			<th width="20%" class="shaded"><fmt:message key="label.grouping.group.heading"/></th>
			<th width="20%" class="shaded"><fmt:message key="label.grouping.non.grouped.users.heading"/></th>
			<th width="20%" class="shaded"><fmt:message key="label.grouping.grouped.users.heading"/></th>
   		 </tr>
		<tr align="center" valign="top">
			<td>
					<select class="nav" id="groups" name="groups" size="15" onChange="getMembers(this)">
					</select>
				<p>
					<input type="button" id="groupremove" name="groupremove" value="<fmt:message key="button.grouping.remove.selected.group"/>" onclick="removeGroup()" disabled=true class="nav" />
				</p>
				<p>
		   			<input id="newgroupname" name="newgroupname" type="text" size="50" class="nav" />
					<input type="button" id="groupadd" name="groupadd" value="<fmt:message key="button.grouping.add.group"/>" onclick="addGroup()" class="nav" />
				</p>
			</td>
			<td>
					<select class="nav" id="nonmembers[]" name="nonmembers[]" size="15" multiple="multiple" onChange="ajustButtonStatus()">
					</select>
				<p>
					<input type="button" id="nonmembersadd" name="nonmembersadd" value="<fmt:message key="button.grouping.add.user.to.group"/>" onclick="addMembersToGroup()" disabled=true class="nav" />
				</p>
			</td>
			<td>
					<select class="nav" id="members[]" name="members[]" size="15" multiple="multiple" onChange="ajustButtonStatus()">
					</select>
        			<p>
			        <input type="button" id="membersremove" name="membersremove" value="<fmt:message key="button.grouping.remove.user.from.group"/>" onclick="removeMembersFromGroup()" disabled=true class="nav" />
				</p>
			</td>
		</tr>
	</table>
		<a href="javascript:window.close();" class="button"><fmt:message key="button.finished"/></a>
	</form>
</body>
</html:html>
