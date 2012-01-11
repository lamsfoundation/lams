 /*
  * Tree component
  *
  * Date: 2012-01-07
  * LAMS Foundation (http://lamsfoundation.org)
  */
if (sequence_id > 0) {
  var node = tree.getNodeByProperty('id', sequence_id);
  var sequenceName = node.label;
  var updateDiv = document.createElement('div');
  updateDiv.setAttribute('class','note');
  updateDiv.setAttribute('id','currentsequence');
  updateDiv.innerHTML = '<p>' + updatewarning + '</p><strong>' + currentsequence + sequenceName +'</strong>';
  document.getElementById('updatesequence').appendChild(updateDiv);
}
tree.render();
tree.subscribe('clickEvent',function(oArgs) {
    selectSequence(oArgs.node.data.id, oArgs.node.label);
  });
