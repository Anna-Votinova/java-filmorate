# java-filmorate
Template repository for Filmorate project.
![image](https://user-images.githubusercontent.com/97476027/191098529-43c4ac96-e263-4cc1-9e3a-71faa3e91e29.png)


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
