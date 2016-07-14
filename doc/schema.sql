
CREATE TABLE `USER` (
  `id` int(11) NOT NULL,
  `username` varchar(64) NOT NULL,
  `password` varchar(256) NOT NULL,
  `salt` varchar(256) NOT NULL,
  `login_attempt` int(11) NOT NULL DEFAULT 0,
  `locked` boolean DEFAULT false,
  `created` bigint NULL DEFAULT NULL,
  `lastmod` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `USER_SESSION` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `session_id` varchar(256) NOT NULL,
  `created` bigint NOT NULL,
  `timeout` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`),
  CONSTRAINT `USER_SESSION_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `USER` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `TASK` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `title` text,
  `description` mediumtext,
  `state` int(2) DEFAULT NULL,
  `created` bigint DEFAULT NULL,
  `lastmod` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_task` (`user_id`,`id`),
  CONSTRAINT `TASK_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `USER` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;