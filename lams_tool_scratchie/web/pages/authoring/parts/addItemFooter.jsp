<%@ include file="/common/taglibs.jsp"%>

<footer class="footer fixed-bottom">
	<div class="panel-heading ">
       	<div class="col-12x col-lg-6x form-groupx rowx form-inlinex btn-group-md voffset5">
       		<span>
	       		Collection
	        		
				<select class="btn btn-md btn-default" id="collection-uid-select">
					<c:forEach var="collection" items="${scratchieItemForm.userCollections}">
						<option value="${collection.uid}"
							<c:if test="${collection.uid == scratchieItemForm.oldCollectionUid}">selected="selected"</c:if>>
							<c:out value="${collection.name}" />
						</option>
					</c:forEach>
				</select>
			</span>
				
        	<div class="pull-right col-12x col-lg-6x" style="margin-top: -5px;">
			    <a href="#nogo" onclick="javascript:self.parent.tb_remove();" class="btn btn-sm btn-default loffset5">
					<fmt:message key="label.authoring.cancel.button" />
				</a>
				<a href="#nogo" onclick="javascript:$('#scratchieItemForm').submit();" class="btn btn-sm btn-default button-add-item">
					<fmt:message key="label.authoring.save.button" />
				</a>
			</div>
		</div>
   	</div>
</footer>