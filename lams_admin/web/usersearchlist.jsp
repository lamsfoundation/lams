<%@ include file="/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>

<c:set var="useInternalSMTPServer"><lams:Configuration key="InternalSMTPServer"/></c:set>
<c:set var="smtpServer"><lams:Configuration key="SMTPServer"/></c:set>

<link type="text/css" href="${lams}css/jquery.tablesorter.theme-blue.css" rel="stylesheet">
<link type="text/css" href="${lams}css/jquery.tablesorter.pager.css" rel="stylesheet">
<style>
	#tablesorter thead .disabled {
		display: none
	}
</style>

<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script>
<script type="text/javascript">
  	$(document).ready(function(){
	    
  		var $table = $("#tablesorter").tablesorter({
			theme: 'blue',
			widgets: ["zebra", "filter"],
		    widgetOptions : {
		    	filter_external : '.search',
		        // include column filters
		        filter_columnFilters: false,
		        filter_placeholder: { search : 'Search...' },
		        filter_saveFilters : true,
		        filter_reset: '.reset'
		      },
		    widthFixed: true,
			headers : { 5 : { sorter: false } },
		    sortInitialOrder: 'desc',
            sortList: [[1]] 
		});
		
		$("#tablesorter").each(function() {
			$(this).tablesorterPager({
				savePages: false,
				ajaxUrl : "<c:url value='/usersearch.do'/>?dispatch=getPagedUsers&page={page}&size={size}&{sortList:column}&{filterList:fcol}",
				ajaxProcessing: function (data, table) {
			    	if (data && data.hasOwnProperty('rows')) {
			    		var rows = [],
			            json = {};
			    		
						for (i = 0; i < data.rows.length; i++){
							var orgData = data.rows[i];
							
							rows += '<tr>';
							
							rows += '<td>';
							rows += 	orgData["userId"];
							rows += '</td>';

							rows += '<td>';
							rows += 	orgData["login"];
							rows += '</td>';

							rows += '<td>';
							rows += 	orgData["firstName"];
							rows += '</td>';

							rows += '<td>';
							rows += 	orgData["lastName"];
							rows += '</td>';

							rows += '<td>';
							rows += 	orgData["email"];
							rows += '</td>';
							
							rows += '<td>';
							rows += 	'[<a href="user.do?method=edit&userId=' + orgData["userId"] + '">';
							rows += 		'<fmt:message key="admin.edit"/>';
							rows += 	'</a>]';
							rows += 	'&nbsp;';
							rows += 	'[<a href="user.do?method=remove&userId=' + orgData["userId"] + '">';
							rows += 		'<fmt:message key="admin.user.delete"/>';
							rows += 	'</a>]';
							rows += 	'&nbsp;';
							rows += 	'[<a href="<lams:LAMSURL/>/loginas.do?login=' + orgData["login"] + '">';
							rows += 		'<fmt:message key="label.login.as"/>';
							rows += 	'</a>]';
							rows += 	'&nbsp;';

							if (${(useInternalSMTPServer || not empty smtpServer)} && orgData["email"] != null && orgData["email"] != "") {
							rows += 	'[<a href="<lams:LAMSURL/>emailUser.do?method=composeMail&returnUrl=admin/usersearch.do&userID=' + orgData["userId"] + '">';
							rows += 		'<fmt:message key="label.email"/>';
							rows += 	'</a>]';
							}
							
							rows += '</td>';
							
							rows += '</tr>';
						}
			            
						json.total = data.total_rows;
						json.rows = $(rows);
						return json;
			            
			    	}
				},
			    container: $(this).next("#pager"),
			    output: '{startRow} to {endRow} ({totalRows})',// possible variables: {page}, {totalPages}, {filteredPages}, {startRow}, {endRow}, {filteredRows} and {totalRows}
			    // css class names of pager arrows
			    cssNext: '.tablesorter-next', // next page arrow
				cssPrev: '.tablesorter-prev', // previous page arrow
				cssFirst: '.tablesorter-first', // go to first page arrow
				cssLast: '.tablesorter-last', // go to last page arrow
				cssGoto: '.gotoPage', // select dropdown to allow choosing a page
				cssPageDisplay: '.pagedisplay', // location of where the "output" is displayed
				cssPageSize: '.pagesize', // page size selector - select dropdown that sets the "size" option
				// class added to arrows when at the extremes (i.e. prev/first arrows are "disabled" when on the first page)
				cssDisabled: 'disabled' // Note there is no period "." in front of this class name
			})
		});
		
		// make demo search buttons work
		$('button[data-column]').on('click', function(){
			var $this = $(this),
		    	filter = [];

		    // text to add to filter
		    filter[ $this.data('1') ] = $this.text();
		    $table.trigger('search', [ filter ]);
		    return false;
		});
  	})
</script>

<h4>
	<a href="orgmanage.do?org=1">
		<fmt:message key="admin.course.manage" />
	</a>
</h4>
<h1>
	<fmt:message key="admin.user.find"/>
</h1>

<p align="right">
	<input class="button" type="button" value="<fmt:message key="admin.user.create"/>" onclick=javascript:document.location='user.do?method=edit' />
</p>

<p align="right">
	<fmt:message key="admin.search" />:&nbsp;<input class="search" type="search" data-column="1">
</p>

<table id="tablesorter">
	<thead>
		<tr>
			<th width="3%" clas="{sorter: 'digit'}">
				<fmt:message key="admin.user.userid"/>
			</th>
			<th width="15%" align="center">
				<fmt:message key="admin.user.login"/>
			</th>
			<th width="15%" align="center">
				<fmt:message key="admin.user.first_name"/>
			</th>
			<th width="15%" align="center">
				<fmt:message key="admin.user.last_name"/>
			</th>
			<th width="15%" align="center">
				<fmt:message key="admin.user.email"/>
			</th>
			<th width="20%" align="center">
				<fmt:message key="admin.user.actions"/>
			</th>
		</tr>
	</thead>
			
	<tbody>
	</tbody>
</table>
		
<!-- pager -->
<div id="pager">
	<form>
		<img class="tablesorter-first"/>
		<img class="tablesorter-prev"/>
		<span class="pagedisplay"></span>
		<img class="tablesorter-next"/>
		<img class="tablesorter-last"/>
		<select class="pagesize">
			<option selected="selected" value="10">10</option>
			<option value="20">20</option>
			<option value="30">30</option>
			<option value="40">40</option>
			<option value="50">50</option>
			<option value="100">100</option>
		</select>
	</form>
</div>
