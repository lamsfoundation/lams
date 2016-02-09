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
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="tags-tiles" prefix="tiles" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<!DOCTYPE html>
<lams:html>
    <lams:head>
       <html:base/>
	   <lams:css/>
      <title><c:out value="${title}"/></title>

	<script language="JavaScript" type="text/javascript" src="<lams:LAMSURL/>includes/javascript/error.js"></script>

	<script type="text/javascript">
	<!-- 
		function init(){
			getBranchesAndNonmembers();
		}

		function adjustButtonStatus(){
			if(document.getElementById("branches").selectedIndex==-1){
				document.getElementById("nonmembersadd").disabled=true;
				document.getElementById("membersremove").disabled=true;
			}else{
				if(document.getElementById("nonmembers[]").selectedIndex==-1){
					document.getElementById("nonmembersadd").disabled=true;
				}else{
					document.getElementById("nonmembersadd").disabled=false;
				}
				if(document.getElementById("members[]").selectedIndex==-1 || ! <c:out value="${mayDelete}"/> ){
					document.getElementById("membersremove").disabled=true;
				}else{
					document.getElementById("membersremove").disabled=false;				}
			}
		}

		function getBranchesAndNonmembers(){
			getBranches();
			getNonmembers();
			document.getElementById("members[]").options.length = 0;
			adjustButtonStatus();
		}

		function getBranches(){
			displayLoadingMessage();
			url="<lams:LAMSURL/>monitoring/groupedBranching.do?method=getBranches&activityID=<c:out value="${activityID}"/>";
			if (window.XMLHttpRequest) { // Non-IE browsers
				branchRequest = new XMLHttpRequest();
				branchRequest.onreadystatechange = updateBranches;
				try {
						branchRequest.open("GET", url, true);
				} catch (e) {
						alert(e);
				}
				branchRequest.send(null);
			} else if (window.ActiveXObject) { // IE
				branchRequest = new ActiveXObject("Microsoft.XMLHTTP");
				if (branchRequest) {
						branchRequest.onreadystatechange = updateBranches;
						branchRequest.open("GET", url, true);
						branchRequest.send();
				}
			}
		}

		function updateBranches(){
			if (branchRequest.readyState == 4) { // Complete
				clearMessage();
				if (branchRequest.status == 200) { // OK response
					checkForErrorScreen("<fmt:message key="error.grouping.data"/>", branchRequest.responseText);
					var branchSelectObj = document.getElementById("branches");
					branchSelectObj.options.length = 0;
					var res = branchRequest.responseText.replace(/^\s*|\s*$/g,"");
					if(res.length>0){
						var branches = res.split(";");
						for (i=0; i<branches.length; i++){
							var branch = branches[i].split(",");
							branchSelectObj.options[branchSelectObj.length] = new Option(branch[1]+" ("+branch[2]+")",branch[0]);
						}
					}
				}else{
					alert("<fmt:message key="error.grouping.data"/>"+" "+branchRequest.status+".");
				}
				adjustButtonStatus();
			}
		}

		function getNonmembers(){
			displayLoadingMessage();
			url="<lams:LAMSURL/>monitoring/groupedBranching.do?method=getGroupsNotAssignedToBranch&activityID=<c:out value="${activityID}"/>";
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
					checkForErrorScreen("<fmt:message key="error.grouping.data"/>", nonmembersRequest.responseText);
					var nonmembersSelectObj = document.getElementById("nonmembers[]");
					nonmembersSelectObj.options.length = 0;
					var res = nonmembersRequest.responseText.replace(/^\s*|\s*$/g,"");
					if(res.length>0){
						var nonmembers = res.split(";");
						for (i=0; i<nonmembers.length; i++){
							var nonmember = nonmembers[i].split(",");
							nonmembersSelectObj.options[nonmembersSelectObj.length] = new Option(nonmember[1],nonmember[0]);
						}
					}
				}else{
					alert("<fmt:message key="error.grouping.data"/>"+" "+nonmembersRequest.status+".");
				}
				adjustButtonStatus();
			}
		}

		function getMembers(branch){
			displayLoadingMessage();
			url="<lams:LAMSURL/>monitoring/groupedBranching.do?method=getBranchGroups&branchID="+branch.value;
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
					checkForErrorScreen("<fmt:message key="error.grouping.data"/>", memberRequest.responseText);
					var membersSelectObj = document.getElementById("members[]");
					membersSelectObj.options.length = 0;
					var res = memberRequest.responseText.replace(/^\s*|\s*$/g,"");
					if(res.length>0){
						var members = res.split(";");
						for (i=0; i<members.length; i++){
							var member = members[i].split(",");
							membersSelectObj.options[membersSelectObj.length] = new Option(member[1],member[0]);
						}
					}
				}else{
					alert("<fmt:message key="error.grouping.data"/>"+" "+memberRequest.status);
				}
				adjustButtonStatus();
			}
		}

		
		var count = 0;

		function addMembersToBranch(){
			var nonmembersSelectObj = document.getElementById("nonmembers[]");
			var members = "";
			for(i=0; i<nonmembersSelectObj.length; i++){
				if(nonmembersSelectObj.options[i].selected){
					count++;
					members = members + "," + nonmembersSelectObj.options[i].value;
				}	
			}
			url="<lams:LAMSURL/>monitoring/groupedBranching.do?method=addGroups&branchID="+document.getElementById("branches").value+"&groups="+members.substr(1);
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
							checkForErrorScreen("<fmt:message key="error.grouping.data"/>", addmbrsRequest.responseText);
							getNonmembers(document.getElementById("branches"));
							getMembers(document.getElementById("branches"));
							var branchSelectObj = document.getElementById("branches");
							var branchName = branchSelectObj.options[branchSelectObj.selectedIndex].text;
							var index1 = branchName.lastIndexOf("(");
							var index2 = branchName.lastIndexOf(")");
							var num = branchName.substring(index1+1,index2);
							num = parseInt(num) + count;
							branchSelectObj.options[branchSelectObj.selectedIndex].text = branchName.substring(0,index1)+"("+num+")";
							count = 0;
						}else{
							alert("<fmt:message key="error.grouping.data"/>"+" "+addmbrsRequest.status);
						}
					adjustButtonStatus();
				}
		}

		
		function removeMembersFromBranch(){
			var membersSelectObj = document.getElementById("members[]");
			var nonmembers = "";
			for(i=0; i<membersSelectObj.length; i++){
				if(membersSelectObj.options[i].selected){
					count++;
					nonmembers = nonmembers + "," + membersSelectObj.options[i].value;
				}	
			}
			url="<lams:LAMSURL/>monitoring/groupedBranching.do?method=removeGroups&branchID="+document.getElementById("branches").value+"&groups="+nonmembers.substr(1);
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
						checkForErrorScreen("<fmt:message key="error.grouping.data"/>", rmmbrsRequest.responseText);
						getMembers(document.getElementById("branches"));
						getNonmembers(document.getElementById("branches"));
						var branchSelectObj = document.getElementById("branches");
						var branchName = branchSelectObj.options[branchSelectObj.selectedIndex].text;
						var index1 = branchName.lastIndexOf("(");
						var index2 = branchName.lastIndexOf(")");
						var num = branchName.substring(index1+1,index2);
						num = parseInt(num) - count;
						branchSelectObj.options[branchSelectObj.selectedIndex].text = branchName.substring(0,index1)+"("+num+")";
						count = 0;
					}else{
						alert("<fmt:message key="error.grouping.data"/>"+" "+rmmbrsRequest.status);
					}
			adjustButtonStatus();
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
	
</lams:head>

<body class="stripes" onLoad="init()">

	<div id="content">

	<h1>
		<c:out value="${title}"/>
	</h1>

	<form>

	<c:if test="${not empty description}">
		<p><c:out value="${description}"/></p>
	</c:if>
	
	<p>&nbsp;<span id="message" align="right"></span></p>

	<P><STRONG><fmt:message key="label.grouping.general.instructions.heading"/></STRONG> <fmt:message key="label.branching.general.group.instructions"/></P>

	<table class="chosenbranching">
		<tr>
			<th><fmt:message key="label.branching.branch.heading"/></th>
			<th><fmt:message key="label.branching.non.allocated.groups.heading"/></th>
			<th><fmt:message key="label.branching.allocated.groups.heading"/></th>
   		 </tr>
		<tr>
			<td width="34%">
				<select id="branches" name="branches" size="15" onChange="getMembers(this)">
				</select>
			</td >
			<td width="33%">
				<select  id="nonmembers[]" name="nonmembers[]" size="15" multiple="multiple" onChange="adjustButtonStatus()">
				</select>
			</td>
			<td width="33%">
				<select  id="members[]" name="members[]" size="15" multiple="multiple" onChange="adjustButtonStatus()">
				</select>
			</td>
		</tr>
		<tr>
			<td width="34%">
				&nbsp;
			</td >
			<td width="33%">
				<input type="button" class="button"  id="nonmembersadd" name="nonmembersadd" value="<fmt:message key="button.branching.add.user.to.branch"/>" onclick="addMembersToBranch()" disabled="true"/>
			</td>
			<td width="33%">
				   <input type="button" class="button" id="membersremove" name="membersremove" value="<fmt:message key="button.branching.remove.user.from.branch"/>" onclick="removeMembersFromBranch()" disabled="true"/>
			</td>
		</tr>
	</table>
	<%@ include file="../template/finishbutton.jsp" %>
	</form>

	</div>

	<div id="footer">
	</div><!--closes footer-->

</div>

</body>
</lams:html>
