<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page isELIgnored="false" %>

<!DOCTYPE HTML>
<%@ page errorPage="error.jsp"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<%@ taglib uri="/bbData" prefix="bbData"%>
<%@ taglib uri="/tags-core" prefix="c"%>
<bbData:context id="ctx">

<bbNG:learningSystemPage contextOverride="CTRL_PANEL">
	<bbNG:cssFile href="../includes/css/treeview.css" />
	<bbNG:cssFile href="../includes/css/folders.css" />
	<bbNG:cssBlock> 
	<style type="text/css"> 
		#monitor-button, #sync-button {
			visibility:hidden;
		}
		#buttons {
			float:right;
		}
	</style> 
	</bbNG:cssBlock>

	<bbNG:jsFile href="includes/javascript/yahoo-dom-event.js" />
	<bbNG:jsFile href="includes/javascript/treeview-min.js" />
	<bbNG:jsFile href="includes/javascript/jquery.js" />
	<bbNG:jsFile href="includes/javascript/openLamsPage.js" />
	
	<bbNG:breadcrumbBar environment="CTRL_PANEL" isContent="false">
		<bbNG:breadcrumb>LAMS Monitor</bbNG:breadcrumb>
	</bbNG:breadcrumbBar>
	<bbNG:pageHeader>
		<bbNG:pageTitleBar title="LAMS Monitor" />
	</bbNG:pageHeader>

	<%-- Monitor Button --%>
	<div id="buttons">
		<div id="course-gradebook-button" class="yui-button yui-link-button">
			<button id="course-gradebook-button-but" onclick="openCourseGradebook('${param.course_id}'); return false;">
				Open course gradebook
			</button>
		</div>
		
		<span id="monitor-button" class="yui-button yui-link-button">
			<button onclick="openMonitor('${param.course_id}', sequenceId); return false;">
				Monitor this lesson
			</button>
		</span>
		<span id="sync-button" class="yui-button yui-link-button">
			<button id="sync-button-but" onclick="syncMarks(); return false;">
				Sync marks with LAMS server
			</button>
		</span>
	</div>
	
	<div id="treeDiv"></div>

<bbNG:jsBlock>
	<script type="text/javascript">
    
	    var $j = jQuery.noConflict();
	    
	    // Open the LAMS Seuence Monitor Window
	    function syncMarks() {
	    	$j("#sync-button-but").hide();
	    	
	        $j.ajax({
	        	async: false,
	            url: '../GradebookSync',
	            data: 'lsId='  + sequenceId,
	            type: 'post',
	            success: function (response) {
	            	$j("#sync-button-but").show();
	            	alert(response);
	            },
	            error: function (request, status, error) {
	            	$j("#sync-button-but").show();
	                //alert(request.responseText);
	                //alert(request.status);
	                alert(error);
	            }
	       	});
	        
	        return false;
	    }
		
        var sequenceId = null;
        var monitorWin = null;
        var courseGradebookWin = null;
        
		function seqSelected(seqId, seqName) {
			sequenceId = seqId;
			
			var visibility = (seqId != 0) ? 'visible' : 'hidden';
		    document.getElementById('monitor-button').style.visibility=visibility;
		    document.getElementById('sync-button').style.visibility=visibility;
		}

		var tree = new YAHOO.widget.TreeView("treeDiv", ${treeView});
		tree.getNodeByIndex(1).expand(true);
		tree.render();
		tree.subscribe('clickEvent',function(oArgs) {
			seqSelected(oArgs.node.data.id, oArgs.node.label);
		});

	</script>
</bbNG:jsBlock>

</bbNG:learningSystemPage>
</bbData:context>
