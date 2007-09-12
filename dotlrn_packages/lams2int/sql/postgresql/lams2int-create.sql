-- 
-- packages/lams2int/sql/postgresql/lams2int-create.sql
-- 
-- @author Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)
-- @creation-date 2007-04-16
-- @cvs-id $Id$
--
--  Copyright (C) 2007 LAMS Foundation
--
--  This package is free software; you can redistribute it and/or modify it under the
--  terms of the GNU General Public License as published by the Free Software
--  Foundation; version 2 of the License, 
--
--  It is distributed in the hope that it will be useful, but WITHOUT ANY
--  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
--  FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
--  details.
--


-- Configuration items table for LAMS/.LRN integration

CREATE TABLE lams_lessons (
  lesson_id     int
                constraint lams_lessons_lesson_id_pk
                references acs_objects (object_id)
                primary key,
  user_id       int
                constraint lams_lessons_user_id_fk
                references users (user_id),
  package_id    int
                constraint lams_lessons_pack_id_fk
                references apm_packages (package_id),   
  community_id  int
                constraint lams_lessons_comm_id_fk
                references dotlrn_communities_all (community_id),
  learning_session_id int,
  display_title varchar(250),
  introduction  text, 
  hide          boolean default 'f' not null, 
  start_time    timestamptz not null default current_timestamp,
  end_time      timestamptz
);


begin;
select  acs_object_type__create_type (
          'lams_lesson',          -- object_type
          'LAMS Lesson',              -- pretty_name
          'LAMS Lessons',             -- pretty_plural
          'acs_object',            -- supertype
          'lams_lessons',       -- table_name
          'seq_id',                -- id_column
          null,      -- name_method
          'f',
          null,
          null
        );
end;

\i lams2int-package-create.sql