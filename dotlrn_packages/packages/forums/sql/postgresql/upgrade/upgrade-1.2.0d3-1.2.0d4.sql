-- 
-- packages/forums/sql/postgresql/upgrade/upgrade-1.2.0d3-1.2.0d4.sql
-- 
-- @author Deds Castillo (deds@i-manila.com.ph)
-- @creation-date 2006-05-10
-- @arch-tag: 3b12c94c-beed-4b92-a2bb-f07a2856154e
-- @cvs-id $Id$
--

-- increase charter to 4000 chars

alter table forums_forums add charter_temp varchar(4000);
update forums_forums set charter_temp = charter;
drop view forums_forums_enabled;
alter table forums_forums drop charter;
alter table forums_forums rename charter_temp to charter;

create view forums_forums_enabled
as
    select *
    from forums_forums
    where enabled_p = 't';
