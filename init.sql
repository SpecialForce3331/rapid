CREATE DATABASE IF NOT EXISTS rapid DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;

GRANT ALL PRIVILEGES ON rapid.* TO 'rapid'@'localhost' IDENTIFIED BY '123456';

USE rapid;

CREATE TABLE IF NOT EXISTS files (
  id int(11) NOT NULL AUTO_INCREMENT,
  random int(11) NOT NULL,
  file text NOT NULL,
  who text NOT NULL,
  size int(11) NOT NULL,
  timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;