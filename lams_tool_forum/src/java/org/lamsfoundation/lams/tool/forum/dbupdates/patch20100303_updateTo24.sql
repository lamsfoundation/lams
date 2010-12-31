
-- LDEV-1743 Be able to remove/update and change order of threads
alter table tl_lafrum11_message add column sequence_id integer;
update tl_lafrum11_message set sequence_id=uid where sequence_id IS NULL;