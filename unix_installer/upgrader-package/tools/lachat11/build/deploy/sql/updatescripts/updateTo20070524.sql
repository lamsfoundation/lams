-- Update the session table to include room_created column
-- This is for the LAMS 2.0.3 release.

ALTER TABLE tl_lachat11_session ADD room_created BIT AFTER jabber_room;