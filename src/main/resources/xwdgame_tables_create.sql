-- MariaDB dump 10.19  Distrib 10.6.8-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: xwdgame
-- ------------------------------------------------------
-- Server version	10.6.8-MariaDB-1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Clue`
--

DROP TABLE IF EXISTS `Clue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Clue`
(
    `ID`          int(11) NOT NULL AUTO_INCREMENT,
    `CLUE`        varchar(255) NOT NULL,
    `CLUE_ID`     varchar(255) NOT NULL,
    `CLUE_NUMBER` int(11) NOT NULL,
    `DIRECTION`   varchar(255) NOT NULL,
    `LENGTH`      int(11) NOT NULL,
    `SETTER`      varchar(255) NOT NULL,
    `SOLUTION`    varchar(255) NOT NULL,
    `XWD_NAME`    varchar(255) NOT NULL,
    PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=191172 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SOLVED`
--

DROP TABLE IF EXISTS `SOLVED`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SOLVED`
(
    `ID`         int(11) NOT NULL AUTO_INCREMENT,
    `CLUE`       int(11) NOT NULL,
    `TWITCHUSER` int(11) NOT NULL,
    `SOLVEDWHEN` bigint(20) NOT NULL,
    `SCORE`      int(11) NOT NULL DEFAULT 0,
    PRIMARY KEY (`ID`),
    KEY          `FK_CLUE` (`CLUE`),
    KEY          `FK_TWITCHUSER` (`TWITCHUSER`),
    CONSTRAINT `FK_CLUE` FOREIGN KEY (`CLUE`) REFERENCES `Clue` (`ID`),
    CONSTRAINT `FK_TWITCHUSER` FOREIGN KEY (`TWITCHUSER`) REFERENCES `TWITCHUSER` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6351 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TWITCHUSER`
--

DROP TABLE IF EXISTS `TWITCHUSER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TWITCHUSER`
(
    `ID`   int(11) NOT NULL AUTO_INCREMENT,
    `NAME` varchar(255) NOT NULL,
    PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-09-13 21:49:21
