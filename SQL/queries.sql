SELECT ROUND(AVG(wage), 2) AS average_salary
FROM lecturer;

SELECT d.name AS department, ROUND(AVG(l.wage), 2) AS average_salary
FROM department d
JOIN lecturer l ON l.department_id = d.department_id
GROUP BY d.name
ORDER BY average_salary DESC;

SELECT l.name, l.degree_level, l.wage
FROM lecturer l
JOIN department d ON l.department_id = d.department_id
WHERE d.name = 'Computer Science';

SELECT c.name AS committee, COUNT(cm.lecturer_id) AS num_members
FROM committee c
LEFT JOIN committee_member cm ON c.committee_id = cm.committee_id
GROUP BY c.name
ORDER BY num_members DESC;

SELECT c.name AS committee, COUNT(cm.lecturer_id) AS num_members
FROM committee c
LEFT JOIN committee_member cm ON c.committee_id = cm.committee_id
GROUP BY c.name
ORDER BY num_members DESC
LIMIT 1;

SELECT l.name, l.degree_level, COUNT(a.article_id) AS num_articles
FROM lecturer l
JOIN article a ON a.lecturer_id = l.lecturer_id
GROUP BY l.name, l.degree_level
ORDER BY num_articles DESC;

SELECT l.name, l.degree_level
FROM lecturer l
WHERE l.lecturer_id NOT IN (SELECT lecturer_id FROM committee_member);

SELECT name, degree_level
FROM lecturer
WHERE department_id IS NULL;

SELECT c.name AS committee, COUNT(a.article_id) AS total_articles_of_members
FROM committee c
JOIN committee_member cm ON c.committee_id = cm.committee_id
LEFT JOIN article a ON a.lecturer_id = cm.lecturer_id
GROUP BY c.name
ORDER BY total_articles_of_members DESC;

SELECT d.name AS department, l.name AS lecturer, l.wage
FROM lecturer l
JOIN department d ON l.department_id = d.department_id
WHERE l.wage = (
    SELECT MAX(l2.wage)
    FROM lecturer l2
    WHERE l2.department_id = l.department_id
)
ORDER BY d.name;

SELECT c.name AS committee, l.name AS chairman, l.degree_level
FROM committee c
JOIN lecturer l ON c.chairman_id = l.lecturer_id
ORDER BY c.name;

SELECT degree_level, COUNT(*) AS num_lecturers
FROM lecturer
GROUP BY degree_level
ORDER BY num_lecturers DESC;

SELECT name, institution
FROM lecturer
WHERE degree_level = 'Professor';
