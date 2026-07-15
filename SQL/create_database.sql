DROP TABLE IF EXISTS committee_member CASCADE;
DROP TABLE IF EXISTS article          CASCADE;
DROP TABLE IF EXISTS committee         CASCADE;
DROP TABLE IF EXISTS lecturer          CASCADE;
DROP TABLE IF EXISTS department        CASCADE;
DROP TABLE IF EXISTS college           CASCADE;

CREATE TABLE college (
    college_id  SERIAL       PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE department (
    department_id SERIAL       PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    college_id    INT          NOT NULL,
    CONSTRAINT fk_department_college
        FOREIGN KEY (college_id) REFERENCES college (college_id),
    CONSTRAINT uq_department_name UNIQUE (college_id, name)
);

CREATE TABLE lecturer (
    lecturer_id    VARCHAR(20)  PRIMARY KEY,
    name           VARCHAR(100) NOT NULL UNIQUE,
    name_of_degree VARCHAR(100),
    degree_level   VARCHAR(20)  NOT NULL,
    wage           INT          NOT NULL,
    institution    VARCHAR(100),
    department_id  INT,
    CONSTRAINT fk_lecturer_department
        FOREIGN KEY (department_id) REFERENCES department (department_id),
    CONSTRAINT chk_degree_level
        CHECK (degree_level IN ('1', '2', 'Doctor', 'Professor')),
    CONSTRAINT chk_wage_non_negative
        CHECK (wage >= 0)
);

CREATE TABLE committee (
    committee_id    SERIAL       PRIMARY KEY,
    name            VARCHAR(100) NOT NULL UNIQUE,
    level_of_degree VARCHAR(20)  NOT NULL,
    chairman_id     VARCHAR(20)  NOT NULL,
    CONSTRAINT fk_committee_chairman
        FOREIGN KEY (chairman_id) REFERENCES lecturer (lecturer_id),
    CONSTRAINT chk_committee_level
        CHECK (level_of_degree IN ('1', '2', 'Doctor', 'Professor'))
);

CREATE TABLE article (
    article_id  SERIAL       PRIMARY KEY,
    title       VARCHAR(200) NOT NULL,
    lecturer_id VARCHAR(20)  NOT NULL,
    CONSTRAINT fk_article_lecturer
        FOREIGN KEY (lecturer_id) REFERENCES lecturer (lecturer_id) ON DELETE CASCADE
);

CREATE TABLE committee_member (
    committee_id INT         NOT NULL,
    lecturer_id  VARCHAR(20) NOT NULL,
    CONSTRAINT pk_committee_member PRIMARY KEY (committee_id, lecturer_id),
    CONSTRAINT fk_member_committee
        FOREIGN KEY (committee_id) REFERENCES committee (committee_id) ON DELETE CASCADE,
    CONSTRAINT fk_member_lecturer
        FOREIGN KEY (lecturer_id)  REFERENCES lecturer  (lecturer_id)  ON DELETE CASCADE
);
