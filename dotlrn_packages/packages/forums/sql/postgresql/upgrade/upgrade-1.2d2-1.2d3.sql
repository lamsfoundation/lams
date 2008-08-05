CREATE INDEX forums_forums_pkg_enable_idx
  ON forums_forums
  USING btree
  (package_id, enabled_p);
