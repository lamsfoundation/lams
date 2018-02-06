<%@ include file="/taglibs.jsp"%>

<c:set var="lams"><lams:LAMSURL/></c:set>

<link rel="stylesheet" href="${lams}css/jquery-ui.timepicker.css" />
<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap.css">
<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager.css" >

<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script> 
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script> 
<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script> 
<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/portrait.js" ></script>


<script type="text/javascript">

	<fmt:message key="label.lesson.with.name" var="LESSON_LABEL_VAR"><fmt:param value="{0}"/></fmt:message>
	const LESSON_LABEL = '<c:out value="${LESSON_LABEL_VAR}" />';

	var areaMenu, 
		typeMenu,
		areaTypeMenus = {},
		areaOptions = [],
		typeDescriptions = {};
	
	$(document).ready(function(){
		debugger;
		<c:forEach var="eventType" items="${eventLogTypes}">
			addEventType("${eventType.id}","${eventType.description}","${eventType.areaCode}","${eventType.areaDescription}");
		</c:forEach>		
		
		areaMenu = document.getElementById("areaMenu");
		areaMenu.options[0] = new Option('<fmt:message key="label.select.topic"/>','');
		for ( i=1; i <= areaOptions.length; i++) {
			areaMenu.options[i] = areaOptions[i-1];
		}
		typeMenu = document.getElementById("typeMenu");
		
		$("#endDatePicker").datetimepicker();
		var now = new Date();
		$("#endDatePicker").datepicker( "setDate", now );
		$("#startDatePicker").datetimepicker();
		var startDateParts = "${startDate}".split("-"); 	// YYYY-MM-DD
		if ( startDateParts.length == 3 ) {
			var startDate = new Date(startDateParts[0], startDateParts[1]-1, startDateParts[2]);
			$("#startDatePicker").datepicker( "setDate", startDate );
		} else {
			$("#startDatePicker").datepicker( "setDate",  now);
		}
		
		$(".tablesorter").tablesorter({
			theme: 'bootstrap',
			headerTemplate : '{content} {icon}',
		    sortInitialOrder: 'desc',
		    sortList: [[0]],
		    widgets: [ "uitheme", "resizable", "filter" ],
		    headers: { 0: { filter: false}, 1: { filter: false, sorter: false}, 2: { filter: false, sorter: false}, 3: { filter: false, sort: false} }, 
		    sortList : [[0,1]],
		    widgetOptions: {
		    	resizable: true,
		    	// include column filters 
		        filter_columnFilters: true, 
		        filter_placeholder: { search : '<fmt:message key="label.search"/>' }, 
		        filter_searchDelay: 700 
		    }
		});

		$(".tablesorter").each(function() {
			$(this).tablesorterPager({
				processAjaxOnInit: false, 
				initialRows: {
			        total: 10,
			        filtered: 10
			      },
				savePages: false,
		        container: $(this).find(".ts-pager"),
		        output: '{startRow} to {endRow} ({totalRows})',
		        cssPageDisplay: '.pagedisplay',
		        cssPageSize: '.pagesize',
		        cssDisabled: 'disabled',
				ajaxUrl : "<c:url value='logevent.do'/>?method=getEventLog&sessionMapID=${sessionMapID}&page={page}&size={size}&{sortList:column}&{filterList:fcol}&s",
				ajaxProcessing: function (data, table) {
			    	if (data && data.hasOwnProperty('rows')) {
			    		var rows = [],
			            json = {};
			    		
						for (i = 0; i < data.rows.length; i++){
							var logData = data.rows[i];
							
							rows += '<tr>';

							rows += '<td style="width:20%">';
							rows += 	logData['dateOccurred'];
							rows += '</td>';

							rows += '<td>';
							var typeId = logData['typeId'];
							var typeDescription;
							if ( typeId )
								typeDescription = typeDescriptions[typeId];
							if ( ! typeDescription ) {
								// this should never occur but just in case
								typeDescription = '[<fmt:message key="label.unknown"/>'+typeId+']';
							}
							rows += typeDescription;
							rows += '</td>';

							rows += '<td>';
							if ( logData['userId'] ) {
								rows += 	definePortraitPopover(logData['userPortraitId'], logData['userId'], logData['userName'], logData['userName']);
							} else {
								rows += '-';
							}
							rows += '</td>';

							rows += '<td>';
							if ( logData['lesson'] && logData['description'] ) {
								debugger;
								rows += LESSON_LABEL.replace('{0}',logData['lesson'])+'<BR/>'+logData['description'];
							} else if ( logData['lesson'] ) {
								rows += 	logData['lesson'];
							} else if ( logData['description'] ) {
								rows += 	logData['description'];
							}
							rows += '</td>';

							rows += '</tr>';
						}
			            
						json.total = data.total_rows;
						json.rows = $(rows);
						return json;
			            
			    		}
				},
				customAjaxUrl: function(table, url) {
					debugger;
					var startDate = $("#startDatePicker").datepicker("getDate");
					if ( startDate )
						url += "&startDate="+startDate.getTime();
					var endDate = $("#endDatePicker").datepicker("getDate");
					if ( endDate )
						url += "&endDate="+endDate.getTime();
					var area = $("#areaMenu").val();
					if ( area )
						url += "&area="+area;
					var typeId = $("#typeMenu").val();
					if ( typeId )
				    	  	url += "&typeId="+typeId;
			        return url;
			   	},
			  })
			  .bind('pagerInitialized pagerComplete', function(event, options){
					initializePortraitPopover('${lams}');
				})
			 
			});

	}); // end document.ready
	
	function addEventType(typeId, typeDescription, areaCode, areaDescription) {
		var areaTypes = areaTypeMenus[areaCode];
		if ( ! areaTypes ) {
			areaTypes = [];
			areaTypes.push(new Option('<fmt:message key="label.select.type"/>',''));
			areaTypeMenus[areaCode] = areaTypes;
			areaOptions.push(new Option(areaDescription, areaCode));
		}
		areaTypes.push(new Option(typeDescription, typeId));
		typeDescriptions[typeId] = typeDescription;
	}
	
	function configureTypeDropdown( areaCode ) {
        typeMenu.length = 0;
        var newTypeOptions = areaTypeMenus[areaCode];
        if ( newTypeOptions ) {
	    		for ( i=0; i < newTypeOptions.length; i++) {
	    			typeMenu.options[i] = newTypeOptions[i];
	    		}
        }
        typeMenu[0].selected = true;
	}
	
	function getEvents() {
		debugger;
		$(".tablesorter").trigger('pagerUpdate', 1);
	}
/* 	function loadGroupStats(orgId) {
		if (orgId) {
			jQuery.ajax({		
				type: "GET",
				url: "<lams:WebAppURL/>/statistics.do",
				data: {method : "groupStats", orgId : orgId},
				cache: false,
				success: function (html) {
					jQuery("#groupDiv").html(html);
				}
			});
		} else {
			jQuery("#groupDiv").html(null);
		}
	}
 */
 </script>

<p><a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>

	<div class="form form-inline">
		<span><select id="areaMenu" class="form-control" onchange="javascript:configureTypeDropdown(this.value)"></select>&nbsp;
		<select id="typeMenu" class="form-control"></select></span>
		<span class="pull-right"><fmt:message key="label.between.dates"/>&nbsp;<input type="text" class="form-control" name="startDatePicker" id="startDatePicker" value=""/>
		&nbsp;<input type="text" class="form-control" name="endDatePicker" id="endDatePicker" value=""/>
		<a href="#" class="btn btn-default" onclick="javascript:getEvents()"><fmt:message key="admin.search"/></a>
		</span>
	</div>

	<div class="voffset10">	
	<lams:TSTable numColumns="4" dataId="data-session-id='1'">
			<th style="width:20%"><fmt:message key="label.date"/></th>
			<th><fmt:message key="label.event.type"/></th>
			<th><fmt:message key="admin.user.login"/></th>
			<th></th>
	</lams:TSTable>
	</div>
</div>