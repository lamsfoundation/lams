<%@ include file="/common/taglibs.jsp"%>
	
<footer class="footer fixed-bottom">
	<div class="panel-heading ">
       	<div class="col-xs-12x col-md-6x form-groupx rowx form-inlinex btn-group-md voffset5">
       		<%-- Hide if we edit it on QB collections page or if we edit in a tool but the question is not in users' collections  --%>
       		<span <c:if test="${empty assessmentQuestionForm.sessionMapID or empty assessmentQuestionForm.userCollections}">style="visibility: hidden;"</c:if>>
	       		<fmt:message key="label.qb.collection" />&nbsp;
	        		
				<select class="btn btn-md btn-default" id="collection-uid-select">
					<c:forEach var="collection" items="${assessmentQuestionForm.userCollections}">
						<option value="${collection.uid}"
							<c:if test="${collection.uid == assessmentQuestionForm.oldCollectionUid}">selected="selected"</c:if>>
				        		<c:choose>
									<c:when test="${empty collection.userId and not collection.personal}">
										<fmt:message key="label.qb.collection.public.name" />
									</c:when>
									<c:otherwise>
										<c:out value="${collection.name}" />
									</c:otherwise>
								</c:choose>
								
								<c:if test="${collection.personal}">
									(<fmt:message key="label.qb.collection.private" />)
								</c:if>
						</option>
					</c:forEach>
				</select>
			</span>
				
        	<div class="pull-right col-xs-12x col-md-6x" style="margin-top: -5px;">
			    <a href="#nogo" onclick="javascript:self.parent.tb_remove();" class="btn btn-sm btn-default loffset5">
					<fmt:message key="label.cancel" />
				</a>
				
				<div class="btn-group btn-group-sm dropup">
					<a id="saveButton" type="button" class="btn btn-sm btn-default button-add-item" onClick="javascript:saveQuestion(false)">
						<fmt:message key="button.save" />
					</a>
					<button id="saveDropButton" type="button" class="btn btn-default dropdown-toggle"
							data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						<span class="caret"></span>
						<span class="sr-only">Toggle Dropdown</span>
					</button>
					<ul class="dropdown-menu">
						<li id="saveAsButton" onClick="javascript:saveQuestion(true)"><a href="#"><fmt:message key="button.save.new.version" /></a></li>
					</ul>
				</div>
			</div>
		</div>
   	</div>
</footer>