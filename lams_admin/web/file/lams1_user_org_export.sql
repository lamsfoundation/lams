# LAMS 1 integrated server prefixes and orgs
select prefix, orgid from ext_server_org_map;
# LAMS 1 user dump
select tu.login,tu.password,tu.fname,tu.lname,tud.email
from tbl_user tu, tbl_user_detail tud 
where tu.uid=tud.uid;
# LAMS 1 organisations dump
select sid,name,description,account_organisation
from u_organisation
where visible=1;
# LAMS 1 user roles dump
select tu.login, tug.gid
from tbl_user tu, tbl_user_group tug
where tu.uid=tug.uid
order by tu.login;
# LAMS 1 user rights dump
select tu.login, uo.sid, uuo.user_right
from tbl_user tu, u_organisation uo, u_user_organisation uuo
where tu.uid=uuo.user_id
and uuo.organisation_id=uo.sid
order by tu.login;