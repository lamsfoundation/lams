<%@ include file="/taglibs.jsp"%>

<!DOCTYPE html>
<lams:html>
<lams:head>
	<lams:css/>
	<lams:css suffix="learner"/>
	<link type="text/css" href="<lams:LAMSURL/>css/free.ui.jqgrid.min.css" rel="stylesheet">
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<style>
		th, td {
			text-align: center;
		}
	</style>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/free.jquery.jqgrid.min.js"></script>
	<script type="text/javascript">
		
		$(document).ready(function(){
  
			// for the ipad, we seem to need to force the grid to a sensible size to start
			$("#consents-grid").jqGrid({
				guiStyle: "bootstrap",
				iconSet: 'fontAwesome',
				autoencode:false,
				caption: "${organisationName}",
			    datatype: "json",
			    url: "<lams:LAMSURL />/admin/policyManagement/getConsentsGridData.do?policyUid=${policy.uid}",
				height: 'auto',
				width: $(window).width() - 100,
				shrinkToFit: false,
			    sortorder: "asc", 
			    sortname: "fullName", 
			    pager: 'consents-grid-pager',
			    rowList:[10,20,30,40,50,100],
			    rowNum:10,
			    colNames:[
			    	'',
			    	"<fmt:message key="label.user.full.name"/>", 
			    	"<fmt:message key="label.consented"/>", 
			    	"<fmt:message key="label.consented.on"/>"
			    ],
			    colModel:[
			      {name:'userId', index:'userId', sortable:false, editable:false, hidden:true, search:false, hidedlg:true},
			      {name:'fullName',index:'fullName', sortable:true, editable:false},
			      {name:'consented',index:'consented', sortable:true, editable:false, search:false, width:50, align:"center"},
			      {name:'consentedOn',index:'consentedOn', sortable:true, editable:false, search:false, align:"center"}
			    ],
 			    loadError: function(xhr,st,err) {
			    	$("#consents-grid").jqGrid('clearGridData');
			    	alert('<fmt:message key="admin.org.password.change.grid.error.load"/>');
			    },
				gridComplete: function(){
					//toolTip($(".jqgrow"));	// enable tooltips for grid
					fixPagerInCenter('consents-grid-pager', 0);
				}	
			}).navGrid("#consents-grid-pager", {edit:false,add:false,del:false,search:false});

			jQuery("#consents-grid").jqGrid('filterToolbar');	

	        //jqgrid autowidth (http://stackoverflow.com/a/1610197)
	        $(window).bind('resize', function() {
	            resizeJqgrid($(".ui-jqgrid-btable:visible"));
	        });
	        setTimeout(function(){ window.dispatchEvent(new Event('resize')); }, 300);
		});

		function fixPagerInCenter(pagername, numcolshift) {
			$('#'+pagername+'_right').css('display','inline');
			if ( numcolshift > 0 ) {
				var marginshift = - numcolshift * 12;
				$('#'+pagername+'_center table').css('margin-left', marginshift+'px');
			}
		}
	    function resizeJqgrid(jqgrids) {
	        jqgrids.each(function(index) {
	            var gridId = $(this).attr('id');
	            var parent = jQuery('#gbox_' + gridId).parent();
	            var gridParentWidth = parent.width();
	            if ( parent.hasClass('grid-holder') ) {
	                	gridParentWidth = gridParentWidth - 2;
	            }
	            jQuery('#' + gridId).setGridWidth(gridParentWidth, true);
	        });
	    };
	</script>
	
</lams:head>

<body>
<div class="row no-gutter no-margin">
<div class="col-12">
<div class="container" id="content">

	<h5>
		<c:out value="${policy.policyName}" />
	</h5>

	<fmt:message key="label.version" />: <c:out value="${policy.version}" />
	<br/><br/>
	
	 <div class="grid-holder">
 		<table id="consents-grid" class="scroll"></table>
		<div id="consents-grid-pager" class="scroll"></div>
 		<div class="tooltip-lg" id="tooltip"></div>
	</div>

</div>
</div>
</div>
</body>
</lams:html>