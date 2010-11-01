<%@ include file="/taglibs.jsp"%>

<script>
	<!--
	jQuery(document).ready(function() {

		//big workaround in order to bind Lams event
		$.tablesorter.addWidget({
			id: "checkallCheckbox",
			// format is called when the on init and when a sorting has finished
			format: function(table) {
				var hasCheckedItems = false;
				jQuery("input[id=learners]").each(function(index) {
					hasCheckedItems = hasCheckedItems || $(this).attr('checked');
					//alert(index + " " + $(this).attr('checked')+ " " + hasCheckedItems);
				 });
				jQuery("#checkall").attr('checked', hasCheckedItems);
			}
		});
		
		jQuery("#learnersTable").tablesorter({
			widthFixed:true,
			sortList:[[1,0]],
			headers: { 0: { sorter: false}},
			textExtraction:'complex',
			widgets: ['checkallCheckbox']
		}).tablesorterPager({
			positionFixed: false,
			container: jQuery("#pager1")
		});
	});
	
	function checkAll () {
		var isChecked = jQuery("#checkall").attr('checked');
		jQuery("input[name=learners]").attr('checked', isChecked);
	}	
	//-->
</script>

<c:if test="${empty learners}">
	<p><fmt:message key="message.no.learners" /></p>
</c:if>

<c:if test="${not empty learners}">
	<p><fmt:message key="message.check.to.add.learner" /></p>

	<table id="learnersTable" class="alternative-color">
		<thead>
			<tr>
				<th><input type='checkbox' id='checkall' onclick='checkAll();' ></th>
				<th><fmt:message key="admin.user.login" /></th>
				<th><fmt:message key="admin.user.name" /></th>
				<th><fmt:message key="admin.user.email" /></th>
			</tr>
		</thead>
		
		<tbody>
			<c:forEach items="${learners}" var="user">
				<tr>
					<td>
						<input id="learners" name="learners" type="checkbox" value="<c:out value="${user.userId}" />" checked="checked" />
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
	
	<div id="pager1">
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