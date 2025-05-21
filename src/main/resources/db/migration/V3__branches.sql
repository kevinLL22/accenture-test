CREATE TABLE `branches` (
	`id` BIGINT(19) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(50) NOT NULL DEFAULT '0' COLLATE 'utf8mb4_general_ci',
	`franchise_id` BIGINT(19) NOT NULL DEFAULT '0',
	PRIMARY KEY (`id`) USING BTREE,
	INDEX `FK__franchise` (`franchise_id`) USING BTREE,
	CONSTRAINT `FK__franchise` FOREIGN KEY (`franchise_id`) REFERENCES `franchise` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;