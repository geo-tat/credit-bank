@echo off

:: Установка переменных окружения
set EMAIL_HOST=
set EMAIL_PORT=
set EMAIL_PASSWORD=
set EMAIL_USERNAME=
set POSTGRES_DB_NAME=
set POSTGRES_USERNAME=
set POSTGRES_PASSWORD=

:: Запуск модулей в отдельных окнах командной строки
start "" /B mvn -pl api-gateway spring-boot:run
start "" /B mvn -pl calculator spring-boot:run
start "" /B mvn -pl deal spring-boot:run
start "" /B mvn -pl statement spring-boot:run
start "" /B mvn -pl dossier spring-boot:run

wait