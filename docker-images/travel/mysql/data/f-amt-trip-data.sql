USE `travel_api`;

LOAD DATA INFILE '../../../docker-entrypoint-initdb.d/g-amt-trip-data.csv'
INTO TABLE 	Trip
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 ROWS
(User_email, Country_idCountry,Reason_idReason,visited, date);