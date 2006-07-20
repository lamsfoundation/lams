drop table if exists lams_journal_entry;
create table lams_journal_entry (uid bigint not null auto_increment, lesson_id bigint, tool_session_id bigint, toolSignature varchar(255), user_id bigint, title varchar(255), entry varchar(255), primary key (uid));
