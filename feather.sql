-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le :  jeu. 25 mars 2021 à 13:03
-- Version du serveur :  5.7.24
-- Version de PHP :  7.4.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `feather`
--

-- --------------------------------------------------------

--
-- Structure de la table `evenement`
--

DROP TABLE IF EXISTS `evenement`;
CREATE TABLE IF NOT EXISTS `evenement` (
  `idEvent` int(20) NOT NULL AUTO_INCREMENT,
  `titre` varchar(255) NOT NULL,
  `dateDebut` timestamp NOT NULL,
  `dateFin` timestamp NOT NULL,
  `description` text,
  `lieu` varchar(20) DEFAULT NULL,
  `affiche` longblob,
  `dateRappel` timestamp NULL DEFAULT NULL,
  `image_name` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `image_size` int(11) DEFAULT NULL,
  PRIMARY KEY (`idEvent`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `evenement`
--

INSERT INTO `evenement` (`idEvent`, `titre`, `dateDebut`, `dateFin`, `description`, `lieu`, `affiche`, `dateRappel`, `image_name`, `updated_at`, `image_size`) VALUES
(1, 'event1', '2020-03-25 00:15:00', '2020-03-26 00:15:00', 'description', 'tunis', NULL, '2020-03-25 00:00:00', '5e7ac66a6e7c4554675018.jpg', '2020-03-25 02:48:10', 11852),
(2, 'titre', '2020-04-26 03:07:00', '2020-04-30 01:07:00', '', 'tunis', NULL, NULL, NULL, NULL, NULL),
(3, 'JPO', '2020-06-02 00:45:00', '2020-06-06 00:45:00', 'petite description', 'sousse', NULL, '2020-06-02 00:30:00', '5ed049970c45a292778004.jpg', '2020-05-28 23:30:31', 37638),
(4, 'Balle', '2020-06-01 12:10:00', '2020-06-01 21:50:00', 'petite+description', NULL, NULL, NULL, '5ea8ddbe7c803818045259.jpeg', '2020-04-29 01:51:58', 74439),
(17, 'test', '2020-05-05 19:05:00', '2020-05-05 20:05:00', '', NULL, NULL, NULL, 'test1.jpeg', '2020-05-04 20:58:54', 7710),
(18, 'event237', '2020-05-29 11:30:00', '2020-05-29 19:00:00', NULL, NULL, NULL, '2020-05-29 11:15:00', '5ed0f1a83f029608026323.png', '2020-05-29 11:27:36', 136049);

-- --------------------------------------------------------

--
-- Structure de la table `expose_projet`
--

DROP TABLE IF EXISTS `expose_projet`;
CREATE TABLE IF NOT EXISTS `expose_projet` (
  `idEvent` int(20) NOT NULL,
  `idProjet` int(20) NOT NULL,
  PRIMARY KEY (`idEvent`,`idProjet`),
  KEY `IDX_854F205033043433` (`idProjet`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `expose_projet`
--

INSERT INTO `expose_projet` (`idEvent`, `idProjet`) VALUES
(1, 1),
(3, 1),
(18, 1),
(3, 2),
(18, 2),
(18, 3),
(18, 4);

-- --------------------------------------------------------

--
-- Structure de la table `fonctionnalite`
--

DROP TABLE IF EXISTS `fonctionnalite`;
CREATE TABLE IF NOT EXISTS `fonctionnalite` (
  `idStory` int(20) NOT NULL AUTO_INCREMENT,
  `idSprint` int(11) DEFAULT NULL,
  `nomFn` varchar(20) NOT NULL,
  `priorite` int(20) NOT NULL,
  PRIMARY KEY (`idStory`),
  KEY `sprint` (`idSprint`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `groupe_travail`
--

DROP TABLE IF EXISTS `groupe_travail`;
CREATE TABLE IF NOT EXISTS `groupe_travail` (
  `idGroupe` int(20) NOT NULL AUTO_INCREMENT,
  `nom` varchar(20) DEFAULT NULL,
  `id_projet_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`idGroupe`),
  KEY `IDX_56EE9DF580F43E55` (`id_projet_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `groupe_travail`
--

INSERT INTO `groupe_travail` (`idGroupe`, `nom`, `id_projet_id`) VALUES
(1, 'dev', NULL),
(2, 'architecte', NULL),
(3, 'graphiste', 2);

-- --------------------------------------------------------

--
-- Structure de la table `notification`
--

DROP TABLE IF EXISTS `notification`;
CREATE TABLE IF NOT EXISTS `notification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `description` longtext COLLATE utf8_unicode_ci NOT NULL,
  `icon` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `route` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `route_parameters` longtext COLLATE utf8_unicode_ci COMMENT '(DC2Type:array)',
  `notification_date` datetime NOT NULL,
  `seen` tinyint(1) NOT NULL,
  `user_enable` text COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=131 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Déchargement des données de la table `notification`
--

INSERT INTO `notification` (`id`, `title`, `description`, `icon`, `route`, `route_parameters`, `notification_date`, `seen`, `user_enable`) VALUES
(56, 'Réunion Sprint StandUp', 'La réunion a été mise à jour', 'bg-warning', 'display_meeting', 'N;', '2020-04-05 23:45:35', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(58, 'Evenement zqsedfr', 'L\'evenement a été mise à jour', 'bg-warning', 'display_meeting', 'N;', '2020-04-10 23:14:12', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(61, 'Evenement zqsedfr', 'L\'evenement a été supprimé', 'bg-danger', 'display_meeting', 'N;', '2020-04-11 08:28:12', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(63, 'Evenement JPO', 'L\'evenement a été Ajouté', 'bg-success', 'display_meeting', 'N;', '2020-04-29 01:45:21', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(64, 'Evenement Balle', 'L\'evenement a été Ajouté', 'bg-success', 'display_meeting', 'N;', '2020-04-29 01:51:58', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(65, 'Evenement JPO', 'L\'evenement a été mise à jour', 'bg-warning', 'display_meeting', 'N;', '2020-04-29 02:08:26', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(66, 'Evenement Balle', 'L\'evenement a été mise à jour', 'bg-warning', 'display_meeting', 'N;', '2020-04-30 12:22:07', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(67, 'Evenement xwcw', 'L\'evenement a été Ajouté', 'bg-success', 'display_meeting', 'N;', '2020-05-04 17:27:29', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(68, 'Evenement xwcw', 'L\'evenement a été Ajouté', 'bg-success', 'display_meeting', 'N;', '2020-05-04 17:29:28', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(69, 'Evenement cvfsgf', 'L\'evenement a été Ajouté', 'bg-success', 'display_meeting', 'N;', '2020-05-04 18:45:05', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(70, 'Evenement xwcw', 'L\'evenement a été supprimé', 'bg-danger', 'display_meeting', 'N;', '2020-05-04 18:50:07', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(71, 'Evenement test', 'L\'evenement a été Ajouté', 'bg-success', 'display_meeting', 'N;', '2020-05-04 19:04:10', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(72, 'Evenement test', 'L\'evenement a été Ajouté', 'bg-success', 'display_meeting', 'N;', '2020-05-04 19:04:48', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(73, 'Evenement cvfsgf', 'L\'evenement a été supprimé', 'bg-danger', 'display_meeting', 'N;', '2020-05-04 19:05:47', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(74, 'Evenement test', 'L\'evenement a été supprimé', 'bg-danger', 'display_meeting', 'N;', '2020-05-04 19:24:59', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(75, 'Evenement test', 'L\'evenement a été Ajouté', 'bg-success', 'display_meeting', 'N;', '2020-05-04 20:23:37', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(76, 'Evenement test', 'L\'evenement a été supprimé', 'bg-danger', 'display_meeting', 'N;', '2020-05-04 20:31:53', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(77, 'Evenement test', 'L\'evenement a été Ajouté', 'bg-success', 'display_meeting', 'N;', '2020-05-04 20:32:15', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(78, 'Evenement test', 'L\'evenement a été supprimé', 'bg-danger', 'display_meeting', 'N;', '2020-05-04 20:39:35', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(79, 'Evenement test', 'L\'evenement a été Ajouté', 'bg-success', 'display_meeting', 'N;', '2020-05-04 20:40:07', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(80, 'Evenement test', 'L\'evenement a été supprimé', 'bg-danger', 'display_meeting', 'N;', '2020-05-04 20:40:54', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(81, 'Evenement test1', 'L\'evenement a été Ajouté', 'bg-success', 'display_meeting', 'N;', '2020-05-04 20:42:53', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(82, 'Evenement test', 'L\'evenement a été supprimé', 'bg-danger', 'display_meeting', 'N;', '2020-05-04 20:48:24', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(83, 'Evenement test1', 'L\'evenement a été Ajouté', 'bg-success', 'display_meeting', 'N;', '2020-05-04 20:48:59', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(84, 'Evenement test1', 'L\'evenement a été supprimé', 'bg-danger', 'display_meeting', 'N;', '2020-05-04 20:51:27', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(85, 'Evenement test1', 'L\'evenement a été Ajouté', 'bg-success', 'display_meeting', 'N;', '2020-05-04 20:51:55', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(86, 'Evenement test1', 'L\'evenement a été supprimé', 'bg-danger', 'display_meeting', 'N;', '2020-05-04 20:52:27', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(87, 'Evenement test1', 'L\'evenement a été Ajouté', 'bg-success', 'display_meeting', 'N;', '2020-05-04 20:53:06', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(88, 'Evenement test1', 'L\'evenement a été supprimé', 'bg-danger', 'display_meeting', 'N;', '2020-05-04 20:58:19', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(89, 'Evenement test1', 'L\'evenement a été Ajouté', 'bg-success', 'display_meeting', 'N;', '2020-05-04 20:58:55', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(90, 'Evenement test1', 'L\'evenement a été supprimé', 'bg-danger', 'display_meeting', 'N;', '2020-05-04 20:59:12', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(91, 'Evenement test1d', 'L\'evenement a été Ajouté', 'bg-success', 'display_meeting', 'N;', '2020-05-04 21:01:44', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(92, 'Evenement test1d', 'L\'evenement a été supprimé', 'bg-danger', 'display_meeting', 'N;', '2020-05-04 21:01:53', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(93, 'Evenement test1', 'L\'evenement a été mise à jour', 'bg-warning', 'display_meeting', 'N;', '2020-05-04 21:27:26', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(94, 'Evenement test', 'L\'evenement a été mise à jour', 'bg-warning', 'display_meeting', 'N;', '2020-05-04 21:29:59', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(95, 'Evenement test', 'L\'evenement a été mise à jour', 'bg-warning', 'display_meeting', 'N;', '2020-05-04 21:37:53', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(96, 'Evenement test', 'L\'evenement a été mise à jour', 'bg-warning', 'display_meeting', 'N;', '2020-05-04 21:47:24', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(97, 'Evenement test', 'L\'evenement a été mise à jour', 'bg-warning', 'display_meeting', 'N;', '2020-05-04 21:49:01', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(104, 'Evenement JPO', 'L\'evenement a été mise à jour', 'bg-warning', 'display_meeting', 'N;', '2020-05-09 05:19:11', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(105, 'Réunion Sprint Planning', 'La réunion a été mise à jour', 'bg-warning', 'display_meeting', 'N;', '2020-05-09 05:29:58', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(106, 'Réunion Sprint Planning', 'La réunion a été mise à jour', 'bg-warning', 'display_meeting', 'N;', '2020-05-09 05:30:40', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(107, 'Réunion Sprint Planning', 'La réunion a été mise à jour', 'bg-warning', 'display_meeting', 'N;', '2020-05-09 05:37:07', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(108, 'Réunion Sprint Planning', 'Nouvelle réunion programmée', 'bg-success', 'display_meeting', 'N;', '2020-05-09 06:09:10', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(109, 'Réunion Sprint Planning', 'La réunion a été mise à jour', 'bg-warning', 'display_meeting', 'N;', '2020-05-09 06:12:43', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(110, 'Réunion Sprint Planning', 'La réunion a été mise à jour', 'bg-warning', 'display_meeting', 'N;', '2020-05-09 06:22:09', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(111, 'Evenement xwcw', 'L\'evenement a été supprimé', 'bg-danger', 'display_meeting', 'N;', '2020-05-09 06:22:30', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(112, 'Réunion Sprint Planning', 'La réunion a été supprimée', 'bg-danger', 'display_meeting', 'N;', '2020-05-09 06:28:24', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(113, 'Réunion Sprint Planning', 'Nouvelle réunion programmée', 'bg-success', 'display_meeting', 'N;', '2020-05-09 06:28:24', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(115, 'Réunion Sprint Review', 'Nouvelle réunion programmée', 'bg-success', 'display_meeting', 'N;', '2020-05-09 06:31:25', 0, 'a:1:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(116, 'Réunion Sprint Planning', 'La réunion a été mise à jour', 'bg-warning', 'display_meeting', 'N;', '2020-05-10 00:23:12', 0, 'a:1:{i:9;a:2:{s:4:\"seen\";b:1;s:5:\"admin\";b:1;}}'),
(117, 'Réunion Sprint Planning', 'La réunion a été mise à jour', 'bg-warning', 'display_meeting', 'N;', '2020-05-11 05:13:31', 0, 'a:1:{i:9;a:2:{s:4:\"seen\";b:1;s:5:\"admin\";b:1;}}'),
(118, 'Réunion Sprint Planning', 'La réunion a été mise à jour', 'bg-warning', 'display_meeting', 'N;', '2020-05-12 03:27:22', 0, 'a:2:{i:9;a:2:{s:4:\"seen\";b:1;s:5:\"admin\";b:1;}i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(119, 'Evenement JPO', 'L\'evenement a été mise à jour', 'bg-warning', 'display_meeting', 'N;', '2020-05-28 23:30:31', 0, 'a:2:{i:9;a:2:{s:4:\"seen\";b:1;s:5:\"admin\";b:1;}i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(120, 'Réunion Sprint Planning', 'La réunion a été mise à jour', 'bg-warning', 'display_meeting', 'N;', '2020-05-28 23:43:27', 0, 'a:1:{i:9;a:2:{s:4:\"seen\";b:1;s:5:\"admin\";b:1;}}'),
(121, 'Réunion Sprint Retrospective', 'Nouvelle réunion programmée', 'bg-success', 'display_meeting', 'N;', '2020-05-29 11:24:32', 0, 'a:1:{i:9;a:2:{s:4:\"seen\";b:1;s:5:\"admin\";b:1;}}'),
(122, 'Réunion Sprint Retrospective', 'La réunion a été mise à jour', 'bg-warning', 'display_meeting', 'N;', '2020-05-29 11:25:19', 0, 'a:1:{i:9;a:2:{s:4:\"seen\";b:1;s:5:\"admin\";b:1;}}'),
(123, 'Evenement event237', 'L\'evenement a été Ajouté', 'bg-success', 'display_meeting', 'N;', '2020-05-29 11:27:36', 0, 'a:2:{i:9;a:2:{s:4:\"seen\";b:1;s:5:\"admin\";b:1;}i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(124, 'Réunion Sprint Retrospective', 'Nouvelle réunion programmée', 'bg-success', 'display_meeting', 'N;', '2020-05-29 15:36:19', 0, 'a:2:{i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}i:9;a:2:{s:4:\"seen\";b:1;s:5:\"admin\";b:1;}}'),
(125, 'Evenement zsderftgb', 'L\'evenement a été Ajouté', 'bg-success', 'display_meeting', 'N;', '2020-05-29 15:40:03', 0, 'a:2:{i:9;a:2:{s:4:\"seen\";b:1;s:5:\"admin\";b:1;}i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(126, 'Evenement zsderftgb', 'L\'evenement a été mise à jour', 'bg-warning', 'display_meeting', 'N;', '2020-05-29 15:40:43', 0, 'a:2:{i:9;a:2:{s:4:\"seen\";b:1;s:5:\"admin\";b:1;}i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(127, 'Evenement zsderftgb', 'L\'evenement a été supprimé', 'bg-danger', 'display_meeting', 'N;', '2020-05-29 15:41:03', 0, 'a:2:{i:9;a:2:{s:4:\"seen\";b:1;s:5:\"admin\";b:1;}i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(128, 'Evenement Balle', 'L\'evenement a été mise à jour', 'bg-warning', 'display_meeting', 'N;', '2020-06-01 13:01:12', 0, 'a:3:{i:9;a:2:{s:4:\"seen\";b:1;s:5:\"admin\";b:1;}i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}i:11;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(129, 'Réunion Sprint StandUp', 'La réunion a été mise à jour', 'bg-warning', 'display_meeting', 'N;', '2020-06-01 13:49:53', 0, 'a:2:{i:9;a:2:{s:4:\"seen\";b:1;s:5:\"admin\";b:1;}i:11;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}'),
(130, 'Réunion Sprint Review', 'La réunion a été mise à jour', 'bg-warning', 'display_meeting', 'N;', '2020-06-01 13:57:25', 0, 'a:2:{i:9;a:2:{s:4:\"seen\";b:1;s:5:\"admin\";b:1;}i:10;a:2:{s:4:\"seen\";b:0;s:5:\"admin\";b:0;}}');

-- --------------------------------------------------------

--
-- Structure de la table `objectif`
--

DROP TABLE IF EXISTS `objectif`;
CREATE TABLE IF NOT EXISTS `objectif` (
  `idObjectif` int(20) NOT NULL AUTO_INCREMENT,
  `idReunion` int(11) DEFAULT NULL,
  `objectif` varchar(20) NOT NULL,
  PRIMARY KEY (`idObjectif`),
  KEY `reunion1` (`idReunion`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `objectif`
--

INSERT INTO `objectif` (`idObjectif`, `idReunion`, `objectif`) VALUES
(14, 1, 'mon objectif');

-- --------------------------------------------------------

--
-- Structure de la table `participe_reunion`
--

DROP TABLE IF EXISTS `participe_reunion`;
CREATE TABLE IF NOT EXISTS `participe_reunion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `presence` int(11) NOT NULL,
  `idUser` int(11) DEFAULT NULL,
  `idReunion` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_308F69ADFE6E88D7` (`idUser`),
  KEY `IDX_308F69AD4BB9CF70` (`idReunion`)
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Déchargement des données de la table `participe_reunion`
--

INSERT INTO `participe_reunion` (`id`, `presence`, `idUser`, `idReunion`) VALUES
(47, 1, 11, 3),
(73, 1, 9, 7),
(74, 1, 10, 7),
(79, 1, 9, 1),
(80, 1, 10, 1),
(81, 1, 11, 1),
(82, 1, 11, 4),
(84, 0, 9, 8),
(85, 1, 10, 9),
(86, 1, 11, 9),
(87, 0, 9, 9),
(90, 1, 9, 2),
(91, 1, 10, 2);

-- --------------------------------------------------------

--
-- Structure de la table `projet`
--

DROP TABLE IF EXISTS `projet`;
CREATE TABLE IF NOT EXISTS `projet` (
  `idProjet` int(20) NOT NULL AUTO_INCREMENT,
  `nom` varchar(20) NOT NULL,
  `dateDebut` date NOT NULL,
  `dateFin` date NOT NULL,
  `description` text NOT NULL,
  `status` varchar(20) NOT NULL,
  PRIMARY KEY (`idProjet`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `projet`
--

INSERT INTO `projet` (`idProjet`, `nom`, `dateDebut`, `dateFin`, `description`, `status`) VALUES
(1, 'projet1', '2020-03-15', '2020-03-18', 'description', 'status'),
(2, 'projet2', '2020-03-20', '2020-03-31', 'descriptio', 'status'),
(3, 'projet3', '2020-07-15', '2020-09-18', 'description', 'status'),
(4, 'projet4', '2020-07-20', '2020-08-31', 'descriptio', 'status');

-- --------------------------------------------------------

--
-- Structure de la table `rate`
--

DROP TABLE IF EXISTS `rate`;
CREATE TABLE IF NOT EXISTS `rate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idrec` int(11) DEFAULT NULL,
  `val` int(11) NOT NULL,
  `idUser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_DFEC3F397D00914B` (`idrec`),
  KEY `IDX_DFEC3F39FE6E88D7` (`idUser`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Déchargement des données de la table `rate`
--

INSERT INTO `rate` (`id`, `idrec`, `val`, `idUser`) VALUES
(2, 1, 0, 9);

-- --------------------------------------------------------

--
-- Structure de la table `reclamation`
--

DROP TABLE IF EXISTS `reclamation`;
CREATE TABLE IF NOT EXISTS `reclamation` (
  `idRec` int(20) NOT NULL AUTO_INCREMENT,
  `idUser` int(11) DEFAULT NULL,
  `description` varchar(20) NOT NULL,
  `etat` int(20) NOT NULL,
  `dateRec` date NOT NULL,
  `reponse` text,
  `typeRec` varchar(255) NOT NULL,
  PRIMARY KEY (`idRec`),
  KEY `user` (`idUser`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `reclamation`
--

INSERT INTO `reclamation` (`idRec`, `idUser`, `description`, `etat`, `dateRec`, `reponse`, `typeRec`) VALUES
(1, 11, 'description', 0, '2020-05-09', NULL, 'event'),
(2, 9, 'zsedfg', 0, '2020-02-19', '', 'meeting');

-- --------------------------------------------------------

--
-- Structure de la table `reunion`
--

DROP TABLE IF EXISTS `reunion`;
CREATE TABLE IF NOT EXISTS `reunion` (
  `idReunion` int(20) NOT NULL AUTO_INCREMENT,
  `titre` varchar(60) NOT NULL,
  `Coordonateur` int(20) DEFAULT NULL,
  `idSprint` int(20) DEFAULT NULL,
  `dateDebut` timestamp NOT NULL,
  `dateFin` timestamp NOT NULL,
  `description` text,
  `themeDuJour` varchar(255) DEFAULT NULL,
  `importance` int(20) NOT NULL,
  `dateRappel` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`idReunion`),
  KEY `user3` (`Coordonateur`),
  KEY `sprint1` (`idSprint`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `reunion`
--

INSERT INTO `reunion` (`idReunion`, `titre`, `Coordonateur`, `idSprint`, `dateDebut`, `dateFin`, `description`, `themeDuJour`, `importance`, `dateRappel`) VALUES
(1, 'Sprint Planning', 9, 1, '2020-07-16 11:45:00', '2020-07-17 14:30:00', 'petite description ...', 'mon theme', 0, NULL),
(2, 'Sprint Review', 10, 4, '2020-06-01 13:55:00', '2020-06-01 14:55:00', NULL, NULL, 5, NULL),
(3, 'Sprint Retrospective', NULL, 1, '2020-04-26 00:55:00', '2020-04-26 03:55:00', '', 'theme', 5, NULL),
(4, 'Sprint Planning', 11, 1, '2020-05-11 05:05:00', '2020-05-11 21:05:00', NULL, 'sqdsf', 5, '2020-05-11 04:50:00'),
(7, 'Sprint Review', 11, 2, '2020-05-09 07:30:00', '2020-05-09 21:30:00', NULL, NULL, 3, NULL),
(8, 'Sprint Retrospective', 9, 4, '2020-05-29 11:30:00', '2020-05-29 12:30:00', NULL, NULL, 0, '2020-05-29 11:15:00'),
(9, 'Sprint Retrospective', 9, 4, '2020-05-29 15:45:00', '2020-05-29 16:30:00', NULL, NULL, 0, '2020-05-29 15:30:00');

-- --------------------------------------------------------

--
-- Structure de la table `sprint`
--

DROP TABLE IF EXISTS `sprint`;
CREATE TABLE IF NOT EXISTS `sprint` (
  `idSprint` int(20) NOT NULL AUTO_INCREMENT,
  `idProjet` int(11) DEFAULT NULL,
  `nomSprint` varchar(255) NOT NULL,
  `dateDebut` date NOT NULL,
  `dateFin` date NOT NULL,
  PRIMARY KEY (`idSprint`),
  KEY `projet2` (`idProjet`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `sprint`
--

INSERT INTO `sprint` (`idSprint`, `idProjet`, `nomSprint`, `dateDebut`, `dateFin`) VALUES
(1, 1, 'sprint1', '2020-02-10', '2020-02-19'),
(2, 1, 'sprint2', '2020-03-17', '2020-03-31'),
(3, 3, 'sprint4', '2015-01-01', '2015-07-01'),
(4, 2, 'sprint5', '2015-01-01', '2015-06-01'),
(5, 2, 'sprint6', '2015-01-01', '2015-03-01');

-- --------------------------------------------------------

--
-- Structure de la table `tache`
--

DROP TABLE IF EXISTS `tache`;
CREATE TABLE IF NOT EXISTS `tache` (
  `idTache` int(20) NOT NULL AUTO_INCREMENT,
  `idStory` int(11) DEFAULT NULL,
  `idUser` int(11) DEFAULT NULL,
  `dateDebut` date NOT NULL,
  `dateFin` date NOT NULL,
  `priorite` int(20) NOT NULL,
  `nom` varchar(20) NOT NULL,
  `etat` varchar(20) NOT NULL,
  `description` varchar(20000) NOT NULL,
  `userstory_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`idTache`),
  KEY `fonctionnalite` (`idStory`),
  KEY `user1` (`idUser`),
  KEY `IDX_938720758618BF41` (`userstory_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `idUser` int(20) NOT NULL AUTO_INCREMENT,
  `nom` varchar(20) DEFAULT NULL,
  `prenom` varchar(20) DEFAULT NULL,
  `email` varchar(180) NOT NULL,
  `typeuser` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(180) NOT NULL,
  `username_canonical` varchar(180) NOT NULL,
  `email_canonical` varchar(180) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `last_login` datetime DEFAULT NULL,
  `confirmation_token` varchar(180) DEFAULT NULL,
  `password_requested_at` datetime DEFAULT NULL,
  `roles` longtext NOT NULL COMMENT '(DC2Type:array)',
  PRIMARY KEY (`idUser`),
  UNIQUE KEY `UNIQ_8D93D64992FC23A8` (`username_canonical`),
  UNIQUE KEY `UNIQ_8D93D649A0D96FBF` (`email_canonical`),
  UNIQUE KEY `UNIQ_8D93D649C05FB297` (`confirmation_token`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`idUser`, `nom`, `prenom`, `email`, `typeuser`, `password`, `username`, `username_canonical`, `email_canonical`, `enabled`, `salt`, `last_login`, `confirmation_token`, `password_requested_at`, `roles`) VALUES
(9, 'taboua', 'freddy', 'tabouaf@gmail.com', 'scrum master', '$2y$13$T28oUzlBmIaO2rhjO1sAAOhtHABx4fi7i65dz.CASV7bLzAb95V.W', 'freddy', 'freddy', 'tabouaf@gmail.com', 1, NULL, '2020-06-18 20:35:17', NULL, NULL, 'a:3:{i:0;s:12:\"SCRUM_MASTER\";i:1;s:13:\"PRODUCT_OWNER\";i:2;s:10:\"ROLE_ADMIN\";}'),
(10, 'taboua1', 'freddy1', 'tabouaf1@gmail.com', ' ', '$2y$13$D6XxOK0v748CblSLuadpBOuQ28YTMzllJj0ZkM6P4j.XXtQtJvUfC', 'freddy1', 'freddy1', 'tabouaf1@gmail.com', 1, NULL, '2020-04-05 23:22:38', NULL, NULL, 'a:0:{}'),
(11, 'user1', 'user1', 'user1@gmail.com', ' ', '$2y$13$D6XxOK0v748CblSLuadpBOuQ28YTMzllJj0ZkM6P4j.XXtQtJvUfC', 'user1', 'user1', 'user1@gmail.com', 1, NULL, '2020-05-29 15:42:59', NULL, NULL, 'a:0:{}');

-- --------------------------------------------------------

--
-- Structure de la table `user_story`
--

DROP TABLE IF EXISTS `user_story`;
CREATE TABLE IF NOT EXISTS `user_story` (
  `idStory` int(20) NOT NULL AUTO_INCREMENT,
  `idProjet` int(11) DEFAULT NULL,
  `nomStory` varchar(20) NOT NULL,
  `bv` int(20) NOT NULL,
  `c` float NOT NULL,
  `priorite` int(20) NOT NULL,
  `complexite` float NOT NULL,
  `description` varchar(50) NOT NULL,
  `ROI` float NOT NULL,
  `sprint_id` int(11) DEFAULT NULL,
  `status` varchar(50) NOT NULL,
  PRIMARY KEY (`idStory`),
  KEY `IDX_994FF608C24077B` (`sprint_id`),
  KEY `IDX_994FF6033043433` (`idProjet`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `user_usergroup`
--

DROP TABLE IF EXISTS `user_usergroup`;
CREATE TABLE IF NOT EXISTS `user_usergroup` (
  `user_id` int(11) NOT NULL,
  `usergroup_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`usergroup_id`),
  KEY `IDX_4A84F5F3A76ED395` (`user_id`),
  KEY `IDX_4A84F5F3D2112630` (`usergroup_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Déchargement des données de la table `user_usergroup`
--

INSERT INTO `user_usergroup` (`user_id`, `usergroup_id`) VALUES
(9, 2),
(9, 3),
(10, 1);

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `expose_projet`
--
ALTER TABLE `expose_projet`
  ADD CONSTRAINT `FK_854F20502C6A49BA` FOREIGN KEY (`idEvent`) REFERENCES `evenement` (`idEvent`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_854F205033043433` FOREIGN KEY (`idProjet`) REFERENCES `projet` (`idProjet`) ON DELETE CASCADE;

--
-- Contraintes pour la table `fonctionnalite`
--
ALTER TABLE `fonctionnalite`
  ADD CONSTRAINT `FK_8F83CB488C91FD2D` FOREIGN KEY (`idSprint`) REFERENCES `sprint` (`idSprint`);

--
-- Contraintes pour la table `groupe_travail`
--
ALTER TABLE `groupe_travail`
  ADD CONSTRAINT `FK_56EE9DF580F43E55` FOREIGN KEY (`id_projet_id`) REFERENCES `projet` (`idProjet`);

--
-- Contraintes pour la table `objectif`
--
ALTER TABLE `objectif`
  ADD CONSTRAINT `FK_E2F868514BB9CF70` FOREIGN KEY (`idReunion`) REFERENCES `reunion` (`idReunion`) ON DELETE CASCADE;

--
-- Contraintes pour la table `participe_reunion`
--
ALTER TABLE `participe_reunion`
  ADD CONSTRAINT `FK_308F69AD4BB9CF70` FOREIGN KEY (`idReunion`) REFERENCES `reunion` (`idReunion`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_308F69ADFE6E88D7` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`) ON DELETE CASCADE;

--
-- Contraintes pour la table `rate`
--
ALTER TABLE `rate`
  ADD CONSTRAINT `FK_DFEC3F397D00914B` FOREIGN KEY (`idrec`) REFERENCES `reclamation` (`idRec`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_DFEC3F39FE6E88D7` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`);

--
-- Contraintes pour la table `reclamation`
--
ALTER TABLE `reclamation`
  ADD CONSTRAINT `FK_CE606404FE6E88D7` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`);

--
-- Contraintes pour la table `reunion`
--
ALTER TABLE `reunion`
  ADD CONSTRAINT `FK_5B00A4828C91FD2D` FOREIGN KEY (`idSprint`) REFERENCES `sprint` (`idSprint`) ON DELETE SET NULL,
  ADD CONSTRAINT `FK_5B00A482F84C7406` FOREIGN KEY (`Coordonateur`) REFERENCES `user` (`idUser`) ON DELETE SET NULL;

--
-- Contraintes pour la table `sprint`
--
ALTER TABLE `sprint`
  ADD CONSTRAINT `FK_EF8055B733043433` FOREIGN KEY (`idProjet`) REFERENCES `projet` (`idProjet`);

--
-- Contraintes pour la table `tache`
--
ALTER TABLE `tache`
  ADD CONSTRAINT `FK_938720758618BF41` FOREIGN KEY (`userstory_id`) REFERENCES `user_story` (`idStory`),
  ADD CONSTRAINT `FK_93872075FC924725` FOREIGN KEY (`idStory`) REFERENCES `fonctionnalite` (`idStory`),
  ADD CONSTRAINT `user1` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`);

--
-- Contraintes pour la table `user_story`
--
ALTER TABLE `user_story`
  ADD CONSTRAINT `FK_994FF6033043433` FOREIGN KEY (`idProjet`) REFERENCES `projet` (`idProjet`),
  ADD CONSTRAINT `FK_994FF608C24077B` FOREIGN KEY (`sprint_id`) REFERENCES `sprint` (`idSprint`) ON DELETE SET NULL;

--
-- Contraintes pour la table `user_usergroup`
--
ALTER TABLE `user_usergroup`
  ADD CONSTRAINT `FK_4A84F5F3A76ED395` FOREIGN KEY (`user_id`) REFERENCES `user` (`idUser`),
  ADD CONSTRAINT `FK_4A84F5F3D2112630` FOREIGN KEY (`usergroup_id`) REFERENCES `groupe_travail` (`idGroupe`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
