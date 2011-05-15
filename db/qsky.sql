SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `qsky` ;
CREATE SCHEMA IF NOT EXISTS `qsky` DEFAULT CHARACTER SET utf8 ;
USE `qsky` ;

-- -----------------------------------------------------
-- Table `qsky`.`company`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `qsky`.`company` ;

CREATE  TABLE IF NOT EXISTS `qsky`.`company` (
  `id` BIGINT NOT NULL ,
  `name` VARCHAR(500) NOT NULL DEFAULT '' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
COMMENT = 'Список компаний. Ведется администратором';


-- -----------------------------------------------------
-- Table `qsky`.`branch`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `qsky`.`branch` ;

CREATE  TABLE IF NOT EXISTS `qsky`.`branch` (
  `id` BIGINT NOT NULL ,
  `company_id` BIGINT NOT NULL ,
  `name` VARCHAR(500) NOT NULL DEFAULT '' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_branch_company` (`company_id` ASC) ,
  CONSTRAINT `fk_branch_company`
    FOREIGN KEY (`company_id` )
    REFERENCES `qsky`.`company` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
COMMENT = 'Филиалы. Заполняется администратором.';


-- -----------------------------------------------------
-- Table `qsky`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `qsky`.`user` ;

CREATE  TABLE IF NOT EXISTS `qsky`.`user` (
  `id` BIGINT NOT NULL ,
  `branch_id` BIGINT NOT NULL ,
  `user_id` BIGINT NOT NULL ,
  `name` VARCHAR(500) NOT NULL DEFAULT '' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_user_branch` (`branch_id` ASC) ,
  CONSTRAINT `fk_user_branch`
    FOREIGN KEY (`branch_id` )
    REFERENCES `qsky`.`branch` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
COMMENT = 'Пользователи. Приходит из систем.';


-- -----------------------------------------------------
-- Table `qsky`.`service`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `qsky`.`service` ;

CREATE  TABLE IF NOT EXISTS `qsky`.`service` (
  `id` BIGINT NOT NULL ,
  `branch_id` BIGINT NOT NULL ,
  `service_id` BIGINT NOT NULL ,
  `name` VARCHAR(500) NOT NULL DEFAULT '' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_service_branch` (`branch_id` ASC) ,
  CONSTRAINT `fk_service_branch`
    FOREIGN KEY (`branch_id` )
    REFERENCES `qsky`.`branch` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `qsky`.`customer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `qsky`.`customer` ;

CREATE  TABLE IF NOT EXISTS `qsky`.`customer` (
  `id` BIGINT NOT NULL ,
  `user_id` BIGINT NOT NULL ,
  `service_id` BIGINT NOT NULL ,
  `number` INT NOT NULL ,
  `service_prefix` VARCHAR(45) NOT NULL ,
  `stand` DATE NULL ,
  `start` DATE NULL ,
  `finish` DATE NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_customer_user` (`user_id` ASC) ,
  INDEX `fk_customer_service` (`service_id` ASC) ,
  CONSTRAINT `fk_customer_user`
    FOREIGN KEY (`user_id` )
    REFERENCES `qsky`.`user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_customer_service`
    FOREIGN KEY (`service_id` )
    REFERENCES `qsky`.`service` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
