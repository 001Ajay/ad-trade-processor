drop table if exists `txn`;
CREATE TABLE `txn` (
	`txn_id` INT NOT NULL AUTO_INCREMENT,
	`stock` VARCHAR(20),
	`type` TEXT(3),
	`qty` INT(6),
	`price` DOUBLE(6),
	`amount` DOUBLE(12),
	`portfolio_id` INT,
	`currentPrice` DOUBLE(12),
	`delta` DOUBLE(12),
	`status` TEXT(12),
	`trade_date` TEXT(12),
	PRIMARY KEY (`txn_id`)
);


drop table if exists `menu`;
CREATE TABLE `menu` (
	`menu_id` INT NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(60),
	`url` VARCHAR(200),
	PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB;

drop table if exists `trade`;
CREATE TABLE `trade` (
	`trade_id` INT NOT NULL AUTO_INCREMENT,
	`stock` VARCHAR(20),
	`type` TEXT(4),
	`ticker` TEXT(10),
	`qty` INT(6),
	`price` DOUBLE(6),
	`amount` DOUBLE(12),
	`portfolio_id` INT,
	`current_price` DOUBLE(12),
	`delta` DOUBLE(12),
	`status` TEXT(12),
	`trade_date` TEXT(12),
	PRIMARY KEY (`trade_id`)
);