<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/includes/jsp/mapFunctions.jsp"%>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<html>
	<lams:head>
		<title><c:out value="${gmapDTO.title}" escapeXml="false" />
		</title>
		<lams:css localLinkPath="../" />
		
		<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAvPAE96y1iioFQOnrP1RCBxS3S_A0Q4kgEfsHF6TMv6-oezFszBTPVN72_75MGlxr3nP_6ixxWd30jw" type="text/javascript"></script>	

		<script type="text/javascript">
		<!--
		var webAppUrl = "${tool}";
		var map;
		var markers;
		var users = new Array();
		var geocoder = null;
		
		function initExportGmap()
		{
			if (GBrowserIsCompatible()) 
			{
				//map = new GMap2(document.getElementById("map_canvas"), { size: new GSize(640,320) } );
				map = new GMap2(document.getElementById("map_canvas"), { size: new GSize(500,320) } );
				markers = new Array();
		    	geocoder = new GClientGeocoder();
		    	
				map.setCenter(new GLatLng('${gmapDTO.mapCenterLatitude}', '${gmapDTO.mapCenterLongitude}' ));
				map.setZoom(${gmapDTO.mapZoom});
				
				// Set up map controls
				map.addControl(new GMapTypeControl());
				map.addControl(new GLargeMapControl());
				map.addMapType(G_PHYSICAL_MAP);
				
				// Set map type
				map.setMapType(${gmapDTO.mapType});
				
				// Add the author user to the list
				// addUserToList('0','<fmt:message key="label.authoring.basic.authored"></fmt:message>' );
		    	
		    	
		    	<c:forEach var="marker" items="${gmapDTO.gmapMarkers}">
					addMarker(new GLatLng('${marker.latitude}', '${marker.longitude}' ), '${marker.infoWindowMessage}', '${marker.title}', '${marker.uid}', true, false, '${marker.createdBy.firstName} ${marker.createdBy.lastName}', '${marker.createdBy.uid}');		
				</c:forEach>
		    	
				
		    	refreshSideBarMonitor();
		    	//fitMapMarkers();
		    }
		}
		
		function addUserToList(id, name)
		{
			var user = new Object();
			user.name = name;
			user.id = id;
			users[users.length] = user;
		}
		
		function refreshSideBarMonitor()
		{
			var sideBarText = "";
			
			var j=0;
			var i;
			
			//sideBarText += "<a href='javascript:refreshSideBar()'>View All</a><br>";
			for (;j<users.length; j++)
			{
				sideBarText += "<nobr><a href='javascript:makeUsersSideBarVisible(" + users[j].id + ");'>" + users[j].name + "</a></nobr><br>";
				sideBarText += "<div style='display:none;' id='userdiv" + users[j].id + "'>";
				for (i=0;i<markers.length; i++)
				{
					if (markers[i].createdById == users[j].id)
					{
						sideBarText += "<span class='sidebar'><nobr>";
						sideBarText += "&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:GEvent.trigger(markers[" + markers[i].sideBarIndex + "],\"click\")' ";
						sideBarText += "title='" + markers[i].createdBy + "' >" + markers[i].title + "</a>"
						sideBarText += "</span></nobr><br />";
					}
					
				}
				sideBarText += "</div>";
			}
			document.getElementById("usersidebar").innerHTML = sideBarText;
		}

		function makeUsersSideBarVisible(id)
		{
			
			var div = document.getElementById("userdiv" + id)
			
			if (div.style.display == "block")
			{
				document.getElementById("userdiv" + id).style.display = "none";
			}
			else if (div.style.display == "none")
			{
				document.getElementById("userdiv" + id).style.display = "block";
			}
			
		}
		
		//-->
		</script>
	
	</lams:head>

	<body class="stripes" onload="initExportGmap()">

			<div id="content">

			<h1>
				<c:out value="${gmapDTO.title}" escapeXml="false" />
			</h1>

				<p>
					<c:out value="${gmapDTO.instructions}" escapeXml="false" />
				</p>

				<c:if test='${mode == "teacher"}'>
					<div id="sessionContents">
						<ul>
							<c:forEach var="session" items="${gmapDTO.sessionDTOs}">
								<li>
									<a href="#sid-${session.sessionID}">${session.sessionName}</a>
								</li>
							</c:forEach>
						</ul>
					</div>
				</c:if>
				
				<c:forEach var="session" items="${gmapDTO.sessionDTOs}">
					<div id="sid-${session.sessionID}">
						<h2>
							${session.sessionName}
						</h2>
						<p>
							&nbsp;
						</p>
						
						<table style="cellpadding:0; cellspacing:0; border:0; width:500px;">
							<tr>
								<td width="80%">
								<div id="map_canvas" style="width:400px;height:300px;" ></div></td>
								<td width="20%">
								<div id="usersidebar" style="width:100px; overflow:auto;height:320px; background:WhiteSmoke; "></div>
								</td>
							</tr>
							<tr>
								<td>
								<br />
									<a href="javascript:fitMapMarkers()" class="button"/><fmt:message key="button.fitMarkers"/></a>
								</td>
							</tr>
						</table>
						<br />
						<table>
						<tr><td>
							<input type="text" size="60" name="address" id="address" value="<fmt:message key="label.authoring.basic.sampleAddress"></fmt:message>" />
					 		<a href="javascript:showAddress()" class="button"/><fmt:message key="button.go"/></a>
						</td></tr>
						</table>
					</div>
					
					<script type="text/javascript">
					<!--
						<c:forEach var="user" items="${session.userDTOs}">
							addUserToList('${user.uid}','${user.firstName} ${user.lastName}' );
						</c:forEach>
					//-->
					</script>
				</c:forEach>
				
				
			</div>
			<!--closes content-->

			<div id="footer">
			</div>
			<!--closes footer-->
	</body>
</html>

