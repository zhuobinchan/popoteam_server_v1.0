CREATE TABLE `popoteam`.`pu_advertisement` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '广告id',
  `title` VARCHAR(128) NULL DEFAULT NULL COMMENT '广告标题',
  `content` VARCHAR(256) NULL DEFAULT NULL COMMENT '广告文本内容',
  `imageUrl` VARCHAR(256) NULL DEFAULT NULL COMMENT '广告图片路径',
  `webUrl` VARCHAR(1024) NULL DEFAULT NULL COMMENT '网页链接',
  `time_interval` INT(11) NOT NULL DEFAULT '3' COMMENT '广告轮播时长,单位秒',
  `type` INT(11) NOT NULL DEFAULT '0' COMMENT '广告类型，用于控制广告显示位置',
  `status` INT(11) NOT NULL DEFAULT '0' COMMENT '广告状态。0：无效。1：有效',
  `create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '广告创建时间',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '广告表';