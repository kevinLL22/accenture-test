CREATE TABLE `products` (
	`id` BIGINT(19) NOT NULL AUTO_INCREMENT,
	`branch_id` BIGINT(19) NOT NULL DEFAULT '0',
	`name` VARCHAR(50) NOT NULL DEFAULT '0' COLLATE 'utf8mb4_general_ci',
	`stock` INT(10) NOT NULL DEFAULT '0',
	PRIMARY KEY (`id`) USING BTREE,
	INDEX `FK__branches` (`branch_id`) USING BTREE,
	CONSTRAINT `FK__branches` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;