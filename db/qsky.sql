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
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(500) NOT NULL DEFAULT '' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
COMMENT = 'Список компаний. Ведется администратором';


-- -----------------------------------------------------
-- Table `qsky`.`branch`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `qsky`.`branch` ;

CREATE  TABLE IF NOT EXISTS `qsky`.`branch` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `company_id` BIGINT NOT NULL ,
  `name` VARCHAR(500) NOT NULL DEFAULT '' ,
  `active` TINYINT(1)  NOT NULL DEFAULT false ,
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
-- Table `qsky`.`employee`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `qsky`.`employee` ;

CREATE  TABLE IF NOT EXISTS `qsky`.`employee` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `branch_id` BIGINT NOT NULL ,
  `employee_id` BIGINT NOT NULL ,
  `name` VARCHAR(500) NOT NULL DEFAULT '' ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) )
ENGINE = InnoDB
COMMENT = 'Пользователи. Приходит из систем.';


-- -----------------------------------------------------
-- Table `qsky`.`service`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `qsky`.`service` ;

CREATE  TABLE IF NOT EXISTS `qsky`.`service` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `branch_id` BIGINT NOT NULL ,
  `service_id` BIGINT NOT NULL ,
  `name` VARCHAR(500) NOT NULL DEFAULT '' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
COMMENT = 'Услуги и их названия';


-- -----------------------------------------------------
-- Table `qsky`.`step`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `qsky`.`step` ;

CREATE  TABLE IF NOT EXISTS `qsky`.`step` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `branch_id` BIGINT NOT NULL COMMENT 'Филиал' ,
  `customer_id` BIGINT NOT NULL COMMENT 'Клиент' ,
  `service_id` BIGINT NULL COMMENT 'Оказываемая услуга клиенту' ,
  `employee_id` BIGINT NULL COMMENT 'Этот сотрудник работал на этом шаге с клиентом' ,
  `before_step_id` BIGINT NULL COMMENT 'Этот клиент впереди' ,
  `after_step_id` BIGINT NULL COMMENT 'Этот клиент за ним' ,
  `stand_time` DATETIME NULL COMMENT 'Клиент начал ожидать вызова' ,
  `start_time` DATETIME NULL COMMENT 'Начали работать с клиентом' ,
  `finish_time` DATETIME NULL COMMENT 'Закончили работать с клиентом' ,
  `waiting` BIGINT NOT NULL DEFAULT 0 COMMENT 'Время ожидания клиента на шаге в милисекундах' ,
  `working` BIGINT NOT NULL DEFAULT 0 COMMENT 'Время работы с клиентом в милисекундах' ,
  `start_state` INT NULL COMMENT 'Шаг начался этим состоянием' ,
  `finish_state` INT NULL COMMENT 'Шаг завершился этим состоянием' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_before` (`before_step_id` ASC) ,
  INDEX `fk_after` (`after_step_id` ASC) ,
  CONSTRAINT `fk_before`
    FOREIGN KEY (`before_step_id` )
    REFERENCES `qsky`.`step` (`id` )
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_after`
    FOREIGN KEY (`after_step_id` )
    REFERENCES `qsky`.`step` (`id` )
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB
COMMENT = 'Таблица отомарных обработак клиента';


-- -----------------------------------------------------
-- Table `qsky`.`customer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `qsky`.`customer` ;

CREATE  TABLE IF NOT EXISTS `qsky`.`customer` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `branch_id` BIGINT NOT NULL ,
  `service_id` BIGINT NULL ,
  `number` INT NULL COMMENT 'Номер клиента' ,
  `service_prefix` VARCHAR(45) NULL COMMENT 'Префикс услуги в номере клиента' ,
  `visit_time` DATETIME NOT NULL COMMENT 'Время прихода клиента' ,
  `customer_id` BIGINT NOT NULL ,
  `before_customer_id` BIGINT NULL ,
  `after_customer_id` BIGINT NULL ,
  `first_step_id` BIGINT NULL COMMENT 'Первый шаг в обработке' ,
  `waiting` BIGINT NOT NULL DEFAULT 0 COMMENT 'Среднее время ожидания на всех шагах в милисекундах' ,
  `working` BIGINT NOT NULL DEFAULT 0 COMMENT 'Среднее время работы за все шаги в милисекундах' ,
  `present_state` INT NULL COMMENT 'Текущее состояние: набор констант' ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_before` (`before_customer_id` ASC) ,
  INDEX `fk_after` (`after_customer_id` ASC) ,
  INDEX `fk_customer_step` (`first_step_id` ASC) ,
  CONSTRAINT `fk_before`
    FOREIGN KEY (`before_customer_id` )
    REFERENCES `qsky`.`customer` (`id` )
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_after`
    FOREIGN KEY (`after_customer_id` )
    REFERENCES `qsky`.`customer` (`id` )
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_customer_step`
    FOREIGN KEY (`first_step_id` )
    REFERENCES `qsky`.`step` (`id` )
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB
COMMENT = 'Все клиенты';



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
