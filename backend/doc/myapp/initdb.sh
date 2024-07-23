#/bin/sh
service mariadb start;
sleep 3;
mysqladmin -u root password 'password';
mysql -u root -e 'create database qifu4;';
mysql -u root qifu4 < /root/qifu4.sql;
