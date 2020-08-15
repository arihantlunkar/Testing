-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 19, 2020 at 10:20 AM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.4.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `rishtey`
--

-- --------------------------------------------------------

--
-- Table structure for table `biodata`
--

CREATE TABLE `biodata` (
  `id` char(14) NOT NULL,
  `name` varchar(32) NOT NULL,
  `mobileNumber` char(10) DEFAULT NULL,
  `place` enum('Andhra Pradesh','Arunachal Pradesh','Assam','Bihar','Chhattisgarh','Goa','Gujarat','Haryana','Himachal Pradesh','Jammu and Kashmir','Jharkhand','Karnataka','Kerala','Madhya Pradesh','Maharashtra','Manipur','Meghalaya','Mizoram','Nagaland','Odisha','Punjab','Rajasthan','Sikkim','Tamil Nadu','Telangana','Tripura','Uttar Pradesh','Uttarakhand','West Bengal','Andaman and Nicobar Islands','Chandigarh','Dadra and Nagar Haveli','Daman and Diu','Delhi','Lakshadweep','Pondicherry') NOT NULL,
  `age` tinyint(4) NOT NULL,
  `height` varchar(8) NOT NULL,
  `occupation` enum('In Service','In Business','Not Working','In Profession') NOT NULL,
  `sex` enum('Bride','Groom') NOT NULL,
  `dosha` enum('Manglik','Non Manglik','Unknown') NOT NULL,
  `maritialStatus` enum('Single','Widow','Divorcee') NOT NULL,
  `sampradaya` enum('Shwetambar','Digambar') NOT NULL,
  `biodataLink` tinytext NOT NULL,
  `image1Link` tinytext NOT NULL,
  `image2Link` tinytext DEFAULT NULL,
  `image3Link` tinytext DEFAULT NULL,
  `image4Link` tinytext DEFAULT NULL,
  `image5Link` tinytext DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `download`
--

CREATE TABLE `download` (
  `fromID` char(14) NOT NULL,
  `biodataID` char(14) NOT NULL,
  `downloadTime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `education`
--

CREATE TABLE `education` (
  `biodataID` char(14) NOT NULL,
  `education` enum('Under Graduate & Below','Graduate','Post Graduate','C.A','C.S','ICWA','Diploma','Doctor','Engineer','MS','PhD','Lawyer','M.B.A','M.C.A & Equivalent','Other') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `upload`
--

CREATE TABLE `upload` (
  `fromID` char(14) NOT NULL,
  `biodataID` char(14) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` char(14) NOT NULL,
  `socialMedia` enum('Facebook','Gmail') NOT NULL,
  `socialMediaID` tinytext NOT NULL,
  `isUserBlocked` tinyint(1) NOT NULL DEFAULT 0,
  `name` varchar(32) NOT NULL,
  `email` varchar(320) NOT NULL,
  `birthday` varchar(16) DEFAULT NULL,
  `gender` enum('Male','Female','NA') DEFAULT NULL,
  `pictureURL` tinytext DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `verification`
--

CREATE TABLE `verification` (
  `biodataID` char(14) NOT NULL,
  `isVerified` tinyint(1) NOT NULL DEFAULT 0,
  `isValid` tinyint(1) NOT NULL DEFAULT 0,
  `verifiedTime` datetime DEFAULT NULL,
  `rejectionReason` tinytext DEFAULT NULL,
  `markDeleted` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `biodata`
--
ALTER TABLE `biodata`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- Indexes for table `verification`
--
ALTER TABLE `verification`
  ADD PRIMARY KEY (`biodataID`) USING BTREE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
