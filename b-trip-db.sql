-- MySQL Script generated by MySQL Workbench
-- Sun Nov 10 21:05:46 2019
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema trip_api
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `trip_api` ;

-- -----------------------------------------------------
-- Schema trip_api
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `trip_api` DEFAULT CHARACTER SET utf8 ;
USE `trip_api` ;

-- -----------------------------------------------------
-- Table `trip_api`.`User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trip_api`.`User` ;

CREATE TABLE IF NOT EXISTS `trip_api`.`User` (
  `idUser` INT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`idUser`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `trip_api`.`Country`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trip_api`.`Country` ;

CREATE TABLE IF NOT EXISTS `trip_api`.`Country` (
  `idCountry` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`idCountry`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `trip_api`.`Reason`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trip_api`.`Reason` ;

CREATE TABLE IF NOT EXISTS `trip_api`.`Reason` (
  `idReason` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idReason`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `trip_api`.`Trip`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trip_api`.`Trip` ;

CREATE TABLE IF NOT EXISTS `trip_api`.`Trip` (
  `idTrip` INT NOT NULL AUTO_INCREMENT,
  `User_idUser` INT NOT NULL,
  `Country_idCountry` INT NOT NULL,
  `visited` TINYINT NULL,
  `date` DATE NULL,
  `Reason_idReason` INT NOT NULL,
  PRIMARY KEY (`idTrip`, `User_idUser`, `Country_idCountry`, `Reason_idReason`),
  INDEX `fk_User_has_Country_Country1_idx` (`Country_idCountry` ASC) VISIBLE,
  INDEX `fk_User_has_Country_User_idx` (`User_idUser` ASC) VISIBLE,
  INDEX `fk_Trip_Reason1_idx` (`Reason_idReason` ASC) VISIBLE,
  CONSTRAINT `fk_User_has_Country_User`
    FOREIGN KEY (`User_idUser`)
    REFERENCES `trip_api`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_User_has_Country_Country1`
    FOREIGN KEY (`Country_idCountry`)
    REFERENCES `trip_api`.`Country` (`idCountry`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Trip_Reason1`
    FOREIGN KEY (`Reason_idReason`)
    REFERENCES `trip_api`.`Reason` (`idReason`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
