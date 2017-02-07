CREATE TABLE `system_mail_templates` (
  id int NOT NULL AUTO_INCREMENT,
  created_date datetime,
  last_modified_date datetime ,
  html_content longtext NOT NULL,
  portraits varchar(255) not null,
  name varchar(255) NOT NULL,
  use_count bigint(20) NOT NULL,
  version bigint(20) NOT NULL,
  PRIMARY KEY (id)
)
