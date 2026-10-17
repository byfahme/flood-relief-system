INSERT INTO camp (name, location, received)
SELECT 'Camp A', 'Tirur', FALSE
WHERE NOT EXISTS (SELECT 1 FROM camp WHERE name = 'Camp A');

INSERT INTO camp (name, location, received)
SELECT 'Camp B', 'Valanchery', FALSE
WHERE NOT EXISTS (SELECT 1 FROM camp WHERE name = 'Camp B');