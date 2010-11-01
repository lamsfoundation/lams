<%@ include file="/taglibs.jsp"%>

<script>
	<!--
	jQuery(document).ready(function() {
		//big workaround in order to bind Lams event
		$.tablesorter.addWidget({
			id: "checkallCheckbox2",
			// format is called when the on init and when a sorting has finished
			format: function(table) {
				var hasCheckedItems = false;
				jQuery("input[id=staff]").each(function(index) {
					hasCheckedItems = hasCheckedItems || $(this).attr('checked');
				 });
				jQuery("#checkAllStaff").attr('checked', hasCheckedItems);
			}
		});
		
		jQuery("#staffTable").tablesorter({
			widthFixed:true,
			sortList:[[1,0]],
			headers: { 0: { sorter: false}},
			textExtraction:'complex',
			widgets: ['checkallCheckbox2']			
		}).tablesorterPager({
			positionFixed: false,
			container: jQuery("#pager")
		});
	});

	function checkAllStaff () {
		var isChecked = jQuery("#checkAllStaff").attr('checked');
		jQuery("input[name=staff]").attr('checked', isChecked);
	}
	//-->
</script>

<c:if test="${empty monitors}">
	<p><fmt:message key="message.no.monitors" /></p>
</c:if>

<c:if test="${not empty monitors}">
	<p><fmt:message key="message.check.to.add.monitor" /></p>

	<table id="staffTable" class="alternative-color tablesorter-admin">
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
						<input id="staff" name="staff" type="checkbox" value="<c:out value="${user.userId}" />" checked="checked" />
					</td>
					<td>
						<c:out value="${user.login}" />
					</td>
					<td>
						<c:out value="${user.title}" /> <c:out value="${user.firstName}" /> <c:out value="${user.lastName}" />
					</td>
					<td>
						<c:out value="${user.email}" />
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<div id="pager">
		<form onsubmit="return false;">
			<img src="<lams:LAMSURL/>/images/first.png" class="first"/>
			<img src="<lams:LAMSURL/>/images/prev.png" class="prev">
			<input type="text" class="pagedisplay"/>
			<img src="<lams:LAMSURL/>/images/next.png" class="next">
			<img src="<lams:LAMSURL/>/images/last.png" class="last">
			<select class="pagesize">
				<option selected="selected" value="10">10&nbsp;&nbsp;</option>
				<option value="20">20</option>
				<option value="30">30</option>
				<option value="40">40</option>
				<option value="50">50</option>
				<option value="100">100</option>
			</select>
		</form>
	</div>
</c:if>