# java-filmorate

Filmorate project by Anna Votinova 

![DBdiagram](https://dbdiagram.io/d/63275cf80911f91ba5d9226a)


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

