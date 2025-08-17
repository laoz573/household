FROM ubuntu:latest

WORKDIR /work

RUN apt-get update && apt-get install -y \
    maven \
    openjdk-11-jre\
    mysql-server-8.0

ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8

# MySQLの初期設定（rootパスワード設定）
RUN service mysql start && \
    mysql -u root -e "ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root12345'; FLUSH PRIVILEGES;"

RUN apt update && apt install -y docker.io
RUN curl -fsSL https://get.docker.com | sh

RUN apt update && apt install -y make
RUN apt update && apt install -y curl
RUN apt update && apt install -y rsync
