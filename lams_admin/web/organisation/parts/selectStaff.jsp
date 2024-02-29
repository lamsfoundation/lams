<%@ include file="/taglibs.jsp"%>

<link type="text/css" href="${lams}css/jquery.tablesorter.theme.bootstrap.css" rel="stylesheet">
<link type="text/css" href="${lams}css/jquery.tablesorter.pager.css"  rel="stylesheet">

<script>
	$(document).ready(function() {
		$(".tablesorter").tablesorter({
			widthFixed:true,
			theme: 'bootstrap',
			headerTemplate : '{content} {icon}',
			sortList:[[1,0]],
			headers: { 0: { sorter: false}},
			textExtraction:'complex',
			widgets: ['uitheme','checkallCheckbox2']		
		});
		
		//sort table only in case there is a data inside (it's a tablesorter bug)
		if ($(".tablesorter tbody tr").length > 0) {
			$(".tablesorter").tablesorterPager({
				container: jQuery(".ts-pager"),
	            output: '{startRow} to {endRow} ({totalRows})'
			});
			$(".tablesorter").trigger("sorton", [[[0, 0]]]);
		}
		
		updateAllStaff();
	});
	
	function updateAllStaff() {
		var allCheckedItems = true;
		$("input[id=staff]").each(function(index) {
			allCheckedItems = allCheckedItems && $(this).prop('checked');
		 });
		$("#checkAllStaff").prop('checked', allCheckedItems);
	}
	
	function checkAllStaff () {
		var isChecked = $("#checkAllStaff").prop('checked');
		$("input[name=staff]").prop('checked', isChecked);
	}
	//-->
</script>

<c:if test="${empty monitors}">
	<p><fmt:message key="message.no.monitors" /></p>
</c:if>

<c:if test="${not empty monitors}">
	<p><fmt:message key="message.check.to.add.monitor" /></p>

<table id="staffTable" class="tablesorter">
	<thead>
		<tr>
			<th><input type='checkbox' id='checkAllStaff' onclick='checkAllStaff();' ></th>
			<th><fmt:message key="admin.user.login" /></th>
			<th><fmt:message key="admin.user.name" /></th>
			<th><fmt:message key="admin.user.email" /></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${monitors}" var="user">
			<tr>
				<td>
					<input id="staff" name="staff" type="checkbox" value="<c:out value="${user.userId}" />" checked="checked" onclick='updateAllStaff();'/>
				</td>
				<td>
					<c:out value="${user.login}" />
				</td>
				<td>
					<c:out value="${user.title}" />&nbsp;<c:out value="${user.getFullName()}" />
				</td>
				<td>
					<c:out value="${user.email}" />
				</td>
			</tr>
		</c:forEach>
	</tbody>
	<tfoot>
		<tr>
	    	<th colspan="4" class="ts-pager form-horizontal">
	    	<form onsubmit="return false;">	    	
		        <button type="button" class="btn btn-xs first"><i class="fa fa-step-backward"></i></button>
		        <button type="button" class="btn btn-xs prev"><i class="fa fa-backward"></i></button>
		        <span class="pagedisplay"></span> <!-- this can be any element, including an input -->
		        <button type="button" class="btn btn-xs next"><i class="fa fa-forward"></i></button>
		        <button type="button" class="btn btn-xs last"><i class="fa fa-step-forward"></i></button>
		        <select class="pagesize" title="Select page size">
		      		<option selected="selected" value="10">10</option>
		      		<option value="20">20</option>
		      		<option value="30">30</option>
		      		<option value="40">40</option>
		      		<option value="50">50</option>
		      		<option value="100">100</option>
		        </select>
		    </form>
	        </th>
	    </tr>
	</tfoot>
	<tbody>
	</tbody>
</table> 	
</c:if>