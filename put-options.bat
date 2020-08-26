@ECHO OFF
:: Use local variables
SETLOCAL ENABLEEXTENSIONS
:: Set title of the window
TITLE Carpe-Diem AutoBot
:: Set Maven Home
CLS
SET MAVEN_HOME=C:\apache-maven-3.6.1\bin
CD C:\development\carpe-diem-autobot
%MAVEN_HOME%\mvn clean spring-boot:run -Dspring-boot.run.arguments=pe
ENDLOCAL