CREATE OR REPLACE FUNCTION check_chairman_degree()
RETURNS TRIGGER AS $$
DECLARE
    chairman_degree VARCHAR(20);
BEGIN
    SELECT degree_level INTO chairman_degree
    FROM lecturer
    WHERE lecturer_id = NEW.chairman_id;

    IF chairman_degree IN ('1', '2') THEN
        RAISE EXCEPTION 'Chairman must be a Doctor or Professor (got degree %)', chairman_degree;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_check_chairman_degree ON committee;
CREATE TRIGGER trg_check_chairman_degree
    BEFORE INSERT OR UPDATE ON committee
    FOR EACH ROW
    EXECUTE FUNCTION check_chairman_degree();

CREATE OR REPLACE FUNCTION check_member_degree()
RETURNS TRIGGER AS $$
DECLARE
    member_degree    VARCHAR(20);
    committee_level  VARCHAR(20);
BEGIN
    SELECT degree_level INTO member_degree
    FROM lecturer
    WHERE lecturer_id = NEW.lecturer_id;

    SELECT level_of_degree INTO committee_level
    FROM committee
    WHERE committee_id = NEW.committee_id;

    IF member_degree <> committee_level THEN
        RAISE EXCEPTION 'Degree mismatch: lecturer degree % does not match committee level %',
            member_degree, committee_level;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_check_member_degree ON committee_member;
CREATE TRIGGER trg_check_member_degree
    BEFORE INSERT ON committee_member
    FOR EACH ROW
    EXECUTE FUNCTION check_member_degree();
