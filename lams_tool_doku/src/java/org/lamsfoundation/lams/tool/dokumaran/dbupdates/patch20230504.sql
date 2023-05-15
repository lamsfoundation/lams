-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-5391 Add clusters to gallery walk
ALTER TABLE tl_ladoku11_dokumaran ADD COLUMN gallery_walk_cluster_size TINYINT UNSIGNED NOT NULL DEFAULT '0' AFTER gallery_walk_read_only;

CREATE TABLE tl_ladoku11_gallery_walk_cluster (
    source_session_uid BIGINT NOT NULL,
    target_session_uid BIGINT NOT NULL,
    PRIMARY KEY (source_session_uid, target_session_uid),
    CONSTRAINT FK_tl_ladoku11_gallery_walk_cluster_1 FOREIGN KEY (source_session_uid)
        REFERENCES tl_ladoku11_session(uid) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_tl_ladoku11_gallery_walk_cluster_2 FOREIGN KEY (target_session_uid)
        REFERENCES tl_ladoku11_session(uid) ON DELETE CASCADE ON UPDATE CASCADE
);


-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;