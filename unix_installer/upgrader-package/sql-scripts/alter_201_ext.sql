-- Script to be run for LAMS 2.0.1 release, on LAMS 2.0 tables.
-- Converts organisations and users for external servers from being ROOT/COURSE type
-- to being COURSE/CLASS type, as well as removing group manager from existing
-- external users.

-- change ext org's type from root to course type
UPDATE lams_organisation 
SET organisation_type_id=2 
WHERE organisation_type_id=1 
AND organisation_id!=1;

-- change ext org's children from course type to class type
UPDATE lams_organisation
SET organisation_type_id=3
WHERE organisation_id IN (
	SELECT classid 
	FROM lams_ext_course_class_map
	);

-- remove group manager from all ext users
DELETE uor
FROM lams_user u,
lams_organisation o,
lams_user_organisation uo,
lams_user_organisation_role uor
WHERE u.user_id=uo.user_id
AND o.organisation_id=uo.organisation_id
AND uor.user_organisation_id=uo.user_organisation_id
AND uor.role_id=2
AND u.user_id IN (
	SELECT user_id
	FROM lams_ext_user_userid_map
	);

-- add all child org members as members of parent
INSERT INTO lams_user_organisation (organisation_id, user_id)
SELECT o.orgid, u.user_id
FROM lams_ext_server_org_map o,
lams_ext_user_userid_map u
WHERE u.ext_server_org_map_id=o.sid;

-- give them learner role
INSERT INTO lams_user_organisation_role (user_organisation_id, role_id)
SELECT uo.user_organisation_id, r.role_id
FROM lams_user_organisation uo,
lams_role r,
lams_ext_server_org_map o,
lams_ext_user_userid_map u
WHERE r.name='LEARNER'
AND u.ext_server_org_map_id=o.sid
AND o.orgid=uo.organisation_id
AND u.user_id=uo.user_id
AND NOT EXISTS (SELECT * 
				FROM lams_user_organisation_role uor
				WHERE uor.user_organisation_id=uo.user_organisation_id
				AND uor.role_id=5);

COMMIT;