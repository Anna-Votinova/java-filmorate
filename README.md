# java-filmorate
Filmorate project by Anna Votinova.

![The BDdiagram](https://dbdiagram.io/d/63275cf80911f91ba5d9226a)


������� ��������

��������� �������� ���� �������:
SELECT name AS film_name
FROM films;

��������� ���� ���� �������������:
SELECT name AS user_name
FROM users;

��������� ���������� ������� �� ��������:
SELECT f.id, f.name AS film_name,
COUNT(l.user_id) AS rating
FROM films AS f
INNER JOIN likes AS l ON f.id = l.film_id
CROUP BY l.film_id
ORDER BY rating DESC
LIMIT 10; 
