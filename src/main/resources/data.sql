INSERT INTO camp (name, location)
SELECT 'Camp A', 'Tirur' WHERE NOT EXISTS (SELECT 1 FROM camp WHERE name = 'Camp A');

INSERT INTO camp (name, location)
SELECT 'Camp B', 'Valanchery' WHERE NOT EXISTS (SELECT 1 FROM camp WHERE name = 'Camp B');