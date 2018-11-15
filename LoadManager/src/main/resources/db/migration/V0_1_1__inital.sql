--
-- Table structure for table `chargebox`
--

CREATE TABLE `chargebox` (
  `charge_box_pk` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `charge_box_id` varchar(255) NOT NULL,
   UNIQUE KEY(`charge_box_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `dbVersion`
--

CREATE TABLE `dbVersion` (
  `version` varchar(10) NOT NULL,
  `upateTimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dbVersion`
--

INSERT INTO `dbVersion` (`version`) VALUES ('0.1.0');

