-- /packages/news/sql/news-sc-drop.sql
--
-- @author Robert Locke (rlocke@infiniteinfo.com)
-- @created 2001-10-23
-- @cvs-id $Id$
--
-- Removes search support from news module.
--
drop trigger forums_message_search__itrg on forums_messages;
drop trigger forums_message_search__dtrg on forums_messages;
drop trigger forums_message_search__utrg on forums_messages;

drop function forums_message_search__itrg ();
drop function forums_message_search__dtrg ();
drop function forums_message_search__utrg ();

drop trigger forums_forums_search__itrg on forums_forums;
drop trigger forums_forums_search__dtrg on forums_forums;
drop trigger forums_forums_search__utrg on forums_forums;

drop function forums_forums_search__itrg ();
drop function forums_forums_search__dtrg ();
drop function forums_forums_search__utrg ();

