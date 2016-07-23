<%@ page import="java.util.*, java.net.*,
                java.text.SimpleDateFormat,
                blackboard.data.*,
                blackboard.persist.*,
                blackboard.data.course.*,
                blackboard.data.user.*,
				blackboard.data.navigation.*,
                blackboard.persist.course.*,
				blackboard.persist.navigation.*,
                blackboard.data.content.*,
                blackboard.persist.content.*,
                blackboard.db.*,
                blackboard.base.*,
                blackboard.platform.*,
                blackboard.platform.plugin.*,
                org.lamsfoundation.ld.integration.*,
				org.lamsfoundation.ld.integration.blackboard.*" errorPage="error.jsp" %>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<%@ taglib uri="/bbData" prefix="bbData"%>
<bbData:context id="ctx">
<%!
public static String extractParameterValue(String url, String param) {
	if (url != null && param != null) {
	    int quotationMarkIndex = url.indexOf("?");
	    String queryPart = quotationMarkIndex > -1 ? url.substring(quotationMarkIndex + 1) : url;
	    String[] paramEntries = queryPart.split("&");
	    for (String paramEntry : paramEntries) {
			String[] paramEntrySplitted = paramEntry.split("=");
			if ((paramEntrySplitted.length > 1) && param.equalsIgnoreCase(paramEntrySplitted[0])) {
			    return paramEntrySplitted[1];
			}
	    }
	}
	return null;
}

public String getChild(Content f, ContentDbLoader cLoader) {
	StringBuilder sb = new StringBuilder();
	try {
	    
	    if (f.getIsFolder()) {
		
			BbList<Content> cList = cLoader.loadChildren(f.getId());
			Content[] cArray = cList.toArray(new Content[0]);
			//sort content by title
			Arrays.sort(cArray, new Comparator<Content>() {
			    @Override
			    public int compare(Content o1, Content o2) {
				if (o1 != null && o2 != null) {
				    return o1.getTitle().compareToIgnoreCase(o2.getTitle());
				} else if (o1 != null)
				    return 1;
				else
				    return -1;
			    }
			});
			
			String title = f.getTitle();
			if (title.indexOf("'") != -1) {
			    title = title.replace("'", "\\'");
			}
			sb.append("{type:'Text', label:'" + title + "', id:0");
			
			if (cArray.length == 0) {
			    sb.append(", expanded:0, children:[{type:'HTML', html:'<i>null</i>', id:0}]}");
			    return sb.toString();
			    
			} else {
			    sb.append(", children:[");
			    sb.append(getChild(cArray[0], cLoader));
			    for (int i = 1; i < cArray.length; i++) {
					sb.append(", ").append(getChild(cArray[i], cLoader));
			    }
			    sb.append("]}");
			}
			return sb.toString();
			
	    } else {
		
			if (f.getContentHandler().equals("resource/x-lams-lamscontent")) {
			    String strUrl = f.getUrl();
			    String strId = extractParameterValue(strUrl, "lsid");
			    String strTitle = f.getTitle().replace("'", "\\'");
			    sb.append("{type:'Text', label:'" + strTitle + "', id:'" + strId + "'}");
			    //			return sb.toString();
			
			} else if (f.getContentHandler().equals("resource/x-ntu-hdllams")) {
			    String strUrl = f.getUrl();
				String strId = "0";
			    if (strUrl.indexOf("&seq_id=") != -1) {
			       int pos1 = strUrl.indexOf("&seq_id=") + 8;
//				   int pos2 = strUrl.indexOf("&", pos1);
				   strId = strUrl.substring(pos1);
			    }
				String strTitle = f.getTitle().replace("'", "\\'");
	            sb.append("{type:'Text', label:'" + strTitle + "', id:'" + strId + "'}");
 
			} else {
			    //	        sb.append("{type:'HTML', html:'<i>null</i>', id:0}");
			}
			return sb.toString();
	    }
	    
	} catch (Exception e) {
	    return sb.toString();
	}
}
%>

<%
    //check permission
    if (!PlugInUtil.authorizeForCourseControlPanel(request, response)) {
        return;
    }

    BbPersistenceManager bbPm = BbServiceManager.getPersistenceService().getDbPersistenceManager();
    Container bbContainer = bbPm.getContainer();
    
    ContentDbLoader cLoader = (ContentDbLoader) bbPm.getLoader( ContentDbLoader.TYPE );

	CourseTocDbLoader ctLoader = (CourseTocDbLoader) bbPm.getLoader(CourseTocDbLoader.TYPE);
	Id courseId = new PkId(bbContainer, Course.DATA_TYPE, request.getParameter("course_id"));
	Course course = ctx.getCourse();
	BbList ctList = ctLoader.loadByCourseId(courseId);
	CourseToc[] courseTocs = (CourseToc[]) ctList.toArray(new CourseToc[0]);
	String strOut = "[[]]";
	int idx = 0;
	StringBuilder strB = new StringBuilder();
	strB.append("[{type:'Text', label:'" + course.getTitle().replace("'", "\\'") + "', id:0, children:[");
	for (int i = 0; i < courseTocs.length; i++) {
	   if (courseTocs[i].getTargetType().compareTo(CourseToc.Target.CONTENT) == 0) {
		  Content cont = cLoader.loadByTocId(courseTocs[i].getId());
	      strB.append(getChild(cont, cLoader));
		  idx = i;
		  break;
	   }
	}
	for (int i = idx + 1; i < courseTocs.length; i++) {
	   if (courseTocs[i].getTargetType().compareTo(CourseToc.Target.CONTENT) == 0) {
		  Content cont = cLoader.loadByTocId(courseTocs[i].getId());
	      strB.append(", ").append(getChild(cont, cLoader));
	   }
	}
	strB.append("]}]");
	strOut = strB.toString();
	
	String monitorURL = "monitoring.jsp?course_id=" + course.getId().toExternalString();
%>
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
	
	<bbNG:breadcrumbBar environment="CTRL_PANEL" isContent="false">
		<bbNG:breadcrumb>LAMS Monitor</bbNG:breadcrumb>
	</bbNG:breadcrumbBar>
	<bbNG:pageHeader>
		<bbNG:pageTitleBar title="LAMS Monitor" />
	</bbNG:pageHeader>

	<%-- Monitor Button --%>
	<div id="buttons">
		<span id="monitor-button" class="yui-button yui-link-button">
			<button onclick="openMonitor(); return false;">
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
	<script language="JavaScript" type="text/javascript">
    
	    var $j = jQuery.noConflict();
	    
	    // Open the LAMS Seuence Monitor Window
	    function syncMarks() {
	    	$j("#sync-button-but").hide();
	    	
	    	//var monitorWin2 = window.open('../GradebookSync?lsId='  + sequenceId,'mWindow','width=1280,height=720,resizable');
	       // monitorWin2.focus();
	       // return;
	    	
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
	               // alert(request.status);
	                alert(error);
	            }
	       	});
	        
	        return false;
	    }
		
        var sequenceId = null;
        var monitorWin = null;
        
		function seqSelected(seqId, seqName) {
			sequenceId = seqId;
			
			var visibility = (seqId != 0) ? 'visible' : 'hidden';
		    document.getElementById('monitor-button').style.visibility=visibility;
		    document.getElementById('sync-button').style.visibility=visibility;
		}
		
        // Open the LAMS Seuence Monitor Window
        function openMonitor() {
        	
            var monitorURL = "<%= monitorURL %>&lsid=" + sequenceId;
            
            if(monitorWin && !monitorWin.closed){
                try {
                    monitorWin.focus();
                }catch(e){
                    // popups blocked by a 3rd party
                    alert("Pop-up windows have been blocked by your browser.  Please allow pop-ups for this site and try again");
                }
                
            } else{
                try {
                    monitorWin = window.open(monitorURL,'mWindow','width=1280,height=720,resizable');
                    monitorWin.focus();
                }catch(e){
                    // popups blocked by a 3rd party
                    alert("Pop-up windows have been blocked by your browser.  Please allow pop-ups for this site and try again");
                }
            }
            
            return false;
        }

		var tree = new YAHOO.widget.TreeView("treeDiv", <%= strOut %>);
		tree.getNodeByIndex(1).expand(true);
		tree.render();
		tree.subscribe('clickEvent',function(oArgs) {
			seqSelected(oArgs.node.data.id, oArgs.node.label);
		});

	</script>
</bbNG:jsBlock>

</bbNG:learningSystemPage>
</bbData:context>
