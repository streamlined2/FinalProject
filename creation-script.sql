CREATE DATABASE `autoleasing` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;

CREATE TABLE `admin` (
  `id` int NOT NULL AUTO_INCREMENT,
  `login` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `passwordDigest` binary(32) NOT NULL,
  `firstName` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `lastName` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `blocked` tinyint DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_UNIQUE` (`login`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `car` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `model` varchar(30) NOT NULL,
  `manufacturer` int NOT NULL,
  `qualityGrade` int NOT NULL DEFAULT '0',
  `color` int NOT NULL DEFAULT '0',
  `style` int NOT NULL DEFAULT '0',
  `cost` decimal(10,0) NOT NULL,
  `productionDate` date NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `natural_key` (`model`,`manufacturer`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

CREATE TABLE `carreview` (
  `id` int NOT NULL AUTO_INCREMENT,
  `leaseOrder` int NOT NULL,
  `manager` int NOT NULL,
  `reviewTime` datetime NOT NULL,
  `carCondition` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `leaseorder_UNIQUE` (`leaseOrder`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

CREATE TABLE `client` (
  `id` int NOT NULL AUTO_INCREMENT,
  `login` varchar(20) COLLATE utf8_bin NOT NULL,
  `passwordDigest` binary(32) NOT NULL,
  `firstName` varchar(30) COLLATE utf8_bin NOT NULL,
  `lastName` varchar(30) COLLATE utf8_bin NOT NULL,
  `blocked` tinyint DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_UNIQUE` (`login`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `leaseinvoice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `leaseOrder` int NOT NULL,
  `manager` int NOT NULL,
  `signTime` datetime NOT NULL,
  `account` varchar(34) NOT NULL,
  `sum` decimal(18,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `leaseOrder` (`leaseOrder`,`manager`,`signTime`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

CREATE TABLE `leaseorder` (
  `id` int NOT NULL AUTO_INCREMENT,
  `client` int NOT NULL,
  `car` int NOT NULL,
  `passport` varchar(50) NOT NULL,
  `driverPresent` tinyint NOT NULL,
  `startTime` datetime NOT NULL,
  `endTime` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='	';

CREATE TABLE `maintenanceinvoice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `leaseOrder` int NOT NULL,
  `manager` int NOT NULL,
  `signTime` datetime NOT NULL,
  `account` varchar(34) NOT NULL,
  `sum` decimal(18,2) NOT NULL,
  `repairs` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `leaseOrder` (`leaseOrder`,`manager`,`signTime`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

CREATE TABLE `manager` (
  `id` int NOT NULL AUTO_INCREMENT,
  `login` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `passwordDigest` binary(32) NOT NULL,
  `firstName` varchar(30) COLLATE utf8_bin NOT NULL,
  `lastName` varchar(30) COLLATE utf8_bin NOT NULL,
  `blocked` tinyint DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_UNIQUE` (`login`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `orderreview` (
  `id` int NOT NULL AUTO_INCREMENT,
  `leaseOrder` int NOT NULL,
  `manager` int NOT NULL,
  `reviewTime` datetime NOT NULL,
  `orderStatus` int NOT NULL,
  `reasonNote` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `leaseorder_UNIQUE` (`leaseOrder`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
