#mysql
FROM mysql:5.7.24

ENV MYSQL_ROOT_PASSWORD root
ENV MYSQL_ROOT_HOST %

ADD scripts/create.sql  /docker-entrypoint-initdb.d/