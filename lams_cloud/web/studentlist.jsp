<%@ include file="taglibs.jsp"%>

<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />

<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<div id="studentList" >
	<div class="main_font_size" style="margin-top: 65px; margin-bottom: 0px;  ">
		<fmt:message key="cloud.addstudents.create.students.now" />
	</div>

	<table style="padding: 5px 15px; width: 700px; text-align: center;" id="studentTable" >
	   	<thead align="center"  >
		    <tr class="  ui-widget-header ">
				<td width="30%"> <fmt:message key="cloud.addstudents.first.name" /> </td>
			    <td width="30%"> <fmt:message key="cloud.addstudents.last.name" /> </td> 
			    <td width="30%" > <fmt:message key="cloud.addstudents.email" /> </td>
			    <td width="10%" style="vertical-align:middle;"> <fmt:message key="cloud.addstudents.action" /> </td>
		    </tr> 
	   	</thead>

		<c:forEach var="image" items="${param.studentList}" varStatus="status">
			<tr>
				<td>

				</td>
				
				<td>
				</td>

				<td>
				</td>
                
				<td >
					<img src="${tool}includes/images/cross.gif"
						title="<fmt:message key="label.authoring.basic.resource.delete" />"
						onclick="deleteItem(${status.index},'${sessionMapID}')" />
				</td>
			</tr>
		</c:forEach>
			<tr>
				<html:form action="/addStudents?dispatch=addStudent" method="post"
						styleId="studentForm" enctype="multipart/form-data">
					<html:hidden property="teacherUserId" />			
					<td>
						<html:text property="firstName" size="25" tabindex="1" />
					</td>
					
					<td>
						<html:text property="lastName" size="25" tabindex="2" />
					</td>
	
					<td>
						<html:text property="email" size="25" tabindex="3" />
					</td>
	                
					<td >
						<img src="${tool}includes/images/cross.gif"
							title="<fmt:message key="label.authoring.basic.resource.delete" />"
							onclick="deleteItem(${status.index},'${sessionMapID}')" />
						<a href="#" onclick="document.studentForm.submit();" class="button-add-item">
							<fmt:message key="cloud.addstudents.add" />
						</a>
					</td>
				</html:form>				
			</tr>		
	</table>	

			
			
			
			
			
		
		
		
	
</div>

<%-- This script will work when a new student submitted in order to refresh "Student List" panel. --%>
<script lang="javascript">
	if(window.top != null){
		//alert(window.top != null);
		var obj = window.top.document.getElementById('studentListArea');
		//alert(obj.innerHTML);
		obj.innerHTML= document.getElementById("studentList").innerHTML;
		//alert(obj.innerHTML);

		//var reourceInputArea = window.top.document.getElementById("reourceInputArea");
		//document.getElementById('check').;
		//alert(obj.innerHTML);
		//alert(reourceInputArea);
		//if (reourceInputArea.style.display=="block") {
			//initLytebox();
		//}

	} else {
		alert("ss");
	}
</script>
