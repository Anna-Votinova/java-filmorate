# java-filmorate
Template repository for Filmorate project.
![Untitled (2)](https://user-images.githubusercontent.com/97476027/191099068-299a6d83-c385-456e-bae6-0e7a6f4d6783.png)

Examples of queries

Get all users 

SELECT *
FROM users;

Get all films

SELECT *
FROM films;

Get the most popular films

SELECT f.id, f.name,
COUNT(l.user_id) AS rating
FROM films AS f
INNER JOIN likes AS l ON f.id = l.film_id
GROUP BY l.film_id
ORDER BY rating DESC
LIMIT 10;

