SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `qsky` ;
CREATE SCHEMA IF NOT EXISTS `qsky` DEFAULT CHARACTER SET utf8 ;
USE `qsky` ;

-- -----------------------------------------------------
-- Table `qsky`.`branch`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `qsky`.`branch` ;

CREATE  TABLE IF NOT EXISTS `qsky`.`branch` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(500) NOT NULL DEFAULT '' ,
  `active` TINYINT(1)  NOT NULL DEFAULT false ,
  `parent_id` BIGINT NULL ,
  `branch_id` BIGINT NOT NULL ,
  `time_zone` INT NULL DEFAULT 0 COMMENT 'учет время филиала относительно гринвича' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_branch_branch`
    FOREIGN KEY (`parent_id` )
    REFERENCES `qsky`.`branch` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'Филиалы. Заполняется администратором.' ;

CREATE INDEX `fk_branch_branch` ON `qsky`.`branch` (`parent_id` ASC) ;

CREATE UNIQUE INDEX `branch_id_UNIQUE` ON `qsky`.`branch` (`branch_id` ASC) ;


-- -----------------------------------------------------
-- Table `qsky`.`employee`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `qsky`.`employee` ;

CREATE  TABLE IF NOT EXISTS `qsky`.`employee` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `branch_id` BIGINT NOT NULL ,
  `employee_id` BIGINT NOT NULL ,
  `name` VARCHAR(500) NOT NULL DEFAULT '' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB, 
COMMENT = 'Пользователи. Приходит из систем.' ;

CREATE UNIQUE INDEX `id_UNIQUE` ON `qsky`.`employee` (`id` ASC) ;


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
ENGINE = InnoDB, 
COMMENT = 'Услуги и их названия' ;


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
  CONSTRAINT `fk_step_before_step_id`
    FOREIGN KEY (`before_step_id` )
    REFERENCES `qsky`.`step` (`id` )
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_step_after_step_id`
    FOREIGN KEY (`after_step_id` )
    REFERENCES `qsky`.`step` (`id` )
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB, 
COMMENT = 'Таблица отомарных обработак клиента' ;

CREATE INDEX `idx_step_before_step_id` ON `qsky`.`step` (`before_step_id` ASC) ;

CREATE INDEX `idx_step_after_step_id` ON `qsky`.`step` (`after_step_id` ASC) ;


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
  CONSTRAINT `fk_customer_before_customer_id`
    FOREIGN KEY (`before_customer_id` )
    REFERENCES `qsky`.`customer` (`id` )
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_customer_after_customer_id`
    FOREIGN KEY (`after_customer_id` )
    REFERENCES `qsky`.`customer` (`id` )
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_customer_first_step_id`
    FOREIGN KEY (`first_step_id` )
    REFERENCES `qsky`.`step` (`id` )
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB, 
COMMENT = 'Все клиенты' ;

CREATE INDEX `idx_customer_before_customer_id` ON `qsky`.`customer` (`before_customer_id` ASC) ;

CREATE INDEX `idx_customer_after_customer_id` ON `qsky`.`customer` (`after_customer_id` ASC) ;

CREATE INDEX `idx_customer_first_step_id` ON `qsky`.`customer` (`first_step_id` ASC) ;


-- -----------------------------------------------------
-- Table `qsky`.`pager_data`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `qsky`.`pager_data` ;

CREATE  TABLE IF NOT EXISTS `qsky`.`pager_data` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `data_type` INT NOT NULL DEFAULT 0 COMMENT 'Тип сообщения. 0-просто инфа, 1-выбор из списка, 2-ввод текста' ,
  `text_data` VARCHAR(1450) NOT NULL DEFAULT '' COMMENT 'html текст' ,
  `quiz_caption` VARCHAR(145) NULL COMMENT 'Это не null если есть опрос. Если не null и нет вариантов ответа, то ввод текста' ,
  `start_date` DATETIME NOT NULL COMMENT 'время начала публикации' ,
  `active` TINYINT(1)  NOT NULL DEFAULT true COMMENT 'показываем или в архиве' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB, 
COMMENT = 'Новости и опросы для пейджера' ;


-- -----------------------------------------------------
-- Table `qsky`.`pager_quiz_items`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `qsky`.`pager_quiz_items` ;

CREATE  TABLE IF NOT EXISTS `qsky`.`pager_quiz_items` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `pager_data_id` BIGINT NOT NULL ,
  `item_text` VARCHAR(145) NOT NULL DEFAULT '' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_quiz_pager_data`
    FOREIGN KEY (`pager_data_id` )
    REFERENCES `qsky`.`pager_data` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB, 
COMMENT = 'Тексты позиций опросов в пейджере' ;


-- -----------------------------------------------------
-- Table `qsky`.`pager_results`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `qsky`.`pager_results` ;

CREATE  TABLE IF NOT EXISTS `qsky`.`pager_results` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `ip` VARCHAR(45) NOT NULL COMMENT 'с какого адреса заходили' ,
  `event_time` DATETIME NOT NULL COMMENT 'время когда заходили' ,
  `pager_data_id` BIGINT NULL ,
  `quiz_id` BIGINT NULL ,
  `input_data` VARCHAR(545) NULL COMMENT 'Если опрос предполагал ввести текст самостоятельно' ,
  `qsys_version` VARCHAR(45) NULL COMMENT 'версия проги' ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_pager_results_pager_data`
    FOREIGN KEY (`pager_data_id` )
    REFERENCES `qsky`.`pager_data` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_pager_results_quiz`
    FOREIGN KEY (`quiz_id` )
    REFERENCES `qsky`.`pager_quiz_items` (`id` )
    ON DELETE SET NULL
    ON UPDATE SET NULL)
ENGINE = InnoDB, 
COMMENT = 'Данные по обращениям из пейджера и результаты опросов' ;

CREATE INDEX `idx_pager_results_pager_data` ON `qsky`.`pager_results` (`pager_data_id` ASC) ;

CREATE INDEX `idx_pager_results_quiz` ON `qsky`.`pager_results` (`quiz_id` ASC) ;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
