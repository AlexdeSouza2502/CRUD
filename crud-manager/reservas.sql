CREATE TABLE `reservas` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`nome` VARCHAR(100) NOT NULL COLLATE 'utf8mb4_general_ci',
	`data_inicio` DATE NOT NULL,
	`data_fim` DATE NOT NULL,
	`user_id` INT(11) NOT NULL,
	PRIMARY KEY (`id`) USING BTREE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=5;

ALTER TABLE `reservas`
ADD CONSTRAINT `fk_reservas_usuarios`
FOREIGN KEY (`user_id`) REFERENCES `usersreservas` (`id`)
ON DELETE CASCADE
ON UPDATE CASCADE;
