
--
-- The Forums (LAMS integration)
--
-- @author ErnieG@melcoe.mq.edu.au
-- @creation-date 2008-07-23
--
--

alter table forums_forums add column is_lams boolean;
alter table forums_forums alter column is_lams set default false;
update forums_forums set is_lams = false;

drop view forums_forums_enabled;

create view forums_forums_enabled
as
    select *
    from forums_forums
    where enabled_p = 't' and
    is_lams = 'f';

