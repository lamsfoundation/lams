DROP TABLE webct_lams_lesson;
CREATE TABLE webct_lams_lesson
(
    id NUMBER NOT NULL,
    pt_id NUMBER NOT NULL,
    lesson_id NUMBER NOT NULL,
    learning_context_id NUMBER NOT NULL,
    sequence_id NUMBER,
    owner_id VARCHAR2 (255),
    owner_first_name VARCHAR2 (255),
    owner_last_name VARCHAR2 (255),
    title VARCHAR2 (255) NOT NULL,
    description VARCHAR2(4000),
    hidden NUMBER DEFAULT 0,
    schedule NUMBER DEFAULT 0,
    start_date_time TIMESTAMP ,
    end_date_time TIMESTAMP ,
  CONSTRAINT pk_webct_lams_lesson_id PRIMARY KEY (id),
  CONSTRAINT uk_webct_lams_lesson_lesson_id UNIQUE(lesson_id)
);

DROP INDEX webct_lams_lesson_lesson_id;
CREATE UNIQUE INDEX webct_lams_lesson_lesson_id ON webct_lams_lesson (lesson_id, pt_id);

DROP SEQUENCE webct_lams_lesson_sequence;
CREATE SEQUENCE webct_lams_lesson_sequence
START WITH 1
INCREMENT BY 1;

DROP TRIGGER webct_lams_lesson_trigger;
CREATE TRIGGER webct_lams_lesson_trigger
BEFORE INSERT
ON webct_lams_lesson
REFERENCING NEW AS NEW
FOR EACH ROW
BEGIN
SELECT webct_lams_lesson_sequence.nextval INTO :NEW.ID FROM dual;
END;
/
COMMIT;