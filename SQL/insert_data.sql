INSERT INTO college (name) VALUES ('Afeka College');

INSERT INTO department (name, college_id) VALUES
    ('Computer Science',     1),
    ('Software Engineering', 1),
    ('Mathematics',          1),
    ('Physics',              1);

INSERT INTO lecturer
    (lecturer_id, name, name_of_degree, degree_level, wage, institution, department_id)
VALUES
    ('111', 'David Cohen',   'PhD in Computer Science', 'Professor', 30000, 'MIT',      1),
    ('222', 'Sarah Levi',    'PhD in Software Eng.',    'Doctor',    22000, NULL,       2),
    ('333', 'Noa Mizrahi',   'PhD in Machine Learning', 'Doctor',    21000, NULL,       1),
    ('444', 'Yossi Bar',     'M.Sc in Mathematics',     '2',         15000, NULL,       3),
    ('555', 'Dana Katz',     'B.Sc in Mathematics',     '1',         12000, NULL,       3),
    ('666', 'Avi Friedman',  'PhD in Physics',          'Professor', 32000, 'Technion', 4),
    ('777', 'Maya Gold',     'M.Sc in Software Eng.',   '2',         16000, NULL,       2),
    ('888', 'Tom Shapiro',   'PhD in Physics',          'Doctor',    23000, NULL,       4),
    ('999', 'Ran Peled',     'B.Sc in Computer Sci.',   '1',         11000, NULL,    NULL);

INSERT INTO article (title, lecturer_id) VALUES
    ('Distributed Systems at Scale', '111'),
    ('Consensus Algorithms',         '111'),
    ('Fault Tolerance',              '111'),
    ('Clean Code Practices',         '222'),
    ('Refactoring Legacy Systems',   '222'),
    ('Graph Neural Networks',        '333'),
    ('Quantum Entanglement',         '666'),
    ('Black Hole Thermodynamics',    '666'),
    ('String Theory Basics',         '666'),
    ('Particle Physics',             '666'),
    ('Superconductors',              '888');

INSERT INTO committee (name, level_of_degree, chairman_id) VALUES
    ('Research Committee',  'Doctor',    '111'),
    ('Promotion Committee', 'Professor', '666'),
    ('Teaching Committee',  '2',         '222');

INSERT INTO committee_member (committee_id, lecturer_id) VALUES
    (1, '222'),
    (1, '333'),
    (1, '888'),
    (2, '111'),
    (3, '444'),
    (3, '777');
