-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- Convert to mindmup. Color now #xxxxxx not just xxxxxx
ALTER TABLE tl_lamind10_node MODIFY node_color VARCHAR(7);
UPDATE tl_lamind10_node SET node_color = CONCAT('#',node_color) WHERE NOT node_color LIKE ('#%');

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;