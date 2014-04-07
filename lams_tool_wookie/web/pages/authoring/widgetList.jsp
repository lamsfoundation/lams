<%@ include file="/common/taglibs.jsp"%>
<table class="table-wookie" >	
	<tr align="center" >
		<c:forEach var="widget" begin="0" end="3" items="${widgetList}">	
			<td class="td-widget" align="center" valign="top">		
				
				
				<table border="0" class="wookie-widget" bordercolor="#ffffff" align="center">									 			    
					<tr align="center">
						<td height="120" align="center" valign="top">			
							<div align="center" class="wookie-icon-area">
								<img align="center" class="wookie-icon" src="${widget.icon}" width="75" height="75">
							</div>
						</td>
					</tr>
					<tr align="center">
						<td class="wookie-title" align="center" valign="top">
							<a href="javascript:selectWookieWidget('${widget.identifier}')"><c:out value="${widget.title}" escapeXml="true"/></a>
						</td>
					</tr>
					<tr align="center">
						<td class="wookie-description" align="center" valign="top">
							<c:out value="${widget.description}" escapeXml="false"/>
						</td>
					</tr>				    			    
				</table>			    
			</td> 				 
		</c:forEach>
	</tr>
	<tr align="center" >
		<c:forEach var="widget" begin="4" end="7" items="${widgetList}">
		
		
			<td class="td-widget" align="center" valign="top">		
				
				
				<table border="0" class="wookie-widget" bordercolor="#ffffff" align="center">									 			    
					<tr align="center">
						<td height="120" align="center" valign="top">			
							<div align="center" class="wookie-icon-area">
								<img align="center" class="wookie-icon" src="${widget.icon}" width="75" height="75">
							</div>
						</td>
					</tr>
					<tr align="center">
						<td class="wookie-title" align="center" valign="top">
							<a href="javascript:selectWookieWidget('${widget.identifier}')"><c:out value="${widget.title}"/></a>
						</td>
					</tr>
					
					<tr align="center">
						<td class="wookie-description" align="center" valign="top">
							<c:out value="${widget.description}" escapeXml="false"/>
						</td>
					</tr>				    			    
				</table>			    
			</td> 		 
		</c:forEach>
	</tr>
</table>

