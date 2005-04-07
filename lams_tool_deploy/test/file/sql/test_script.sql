# Connection: ROOT LOCAL
# Host: localhost
# Saved: 2005-04-06 15:44:33
# 
# Connection: ROOT LOCAL
# Host: localhost
# Saved: 2005-04-06 15:15:40
# 
-- SCRIPT RUNNER TESTING SCRIPT
-- IF YOU ALTER THIS YOU WILL BREAK THE TEST!
CREATE TABLE TEST_1 (
TEST_1_INT INT, 
TEST_1_STR VARCHAR(255), #asdadasdsd
TEST_1_DATE DATETIME
) TYPE=InnoDB;

-- more comments
INSERT INTO TEST_1 VALUES (1, 'foo', NOW());
INSERT INTO TEST_1 VALUES (2, 'foo-bar', NOW());
    
-- comments
-- comments
ALTER TABLE TEST_1 -- asdasdasd
ADD INDEX IDX_TEST_1 (TEST_1_INT);

-- -- double comments
UPDATE TEST_1 SET TEST_1_STR = 'bar' WHERE TEST_1_INT = 1;


-- more comments!
DROP TABLE TEST_1;