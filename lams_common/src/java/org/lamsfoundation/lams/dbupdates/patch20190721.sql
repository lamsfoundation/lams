CREATE FUNCTION `strip_tags`($str MEDIUMTEXT, $leaveMeaningfulTags BOOLEAN) RETURNS MEDIUMTEXT
BEGIN
    DECLARE $start, $end INT DEFAULT 1;
    DECLARE $tag CHAR(3);
    IF ($str IS NULL) THEN RETURN NULL; END IF;
    main: LOOP
        SET $start = LOCATE("<", $str, $start);
        IF (NOT $start) THEN RETURN $str; END IF;
        SET $end = LOCATE(">", $str, $start);
        IF (NOT $end) THEN SET $end = $start; END IF;
        IF ($leaveMeaningfulTags = FALSE) THEN
        	SET $tag = SUBSTRING($str, $start + 1, 3);
        	IF $tag = 'img' OR $tag = 'IMG' THEN SET $start = $end; ITERATE main; END IF;
        END IF;
        SET $str = INSERT($str, $start, $end - $start + 1, "");
    END LOOP;
END;
