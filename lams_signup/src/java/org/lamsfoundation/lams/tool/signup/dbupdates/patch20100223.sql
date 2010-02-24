ALTER TABLE lams_signup_organisation ADD COLUMN add_with_author tinyint(1) DEFAULT 0 AFTER add_as_staff;
ALTER TABLE lams_signup_organisation ADD COLUMN add_with_monitor tinyint(1) DEFAULT 0 AFTER add_with_author;
