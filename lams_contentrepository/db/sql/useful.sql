-- List all nodes, their versions and properties
select * from lams_cr_node node
       left join lams_cr_node_version nv on node.node_id = nv.node_id
       left join lams_cr_node_version_property nvp on nvp.nv_id = nv.nv_id
order by node.workspace_id, node.node_id
