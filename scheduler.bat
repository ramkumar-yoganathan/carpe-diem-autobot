@ECHO OFF
:: Use local variables
SETLOCAL ENABLEEXTENSIONS
:: Set title of the window
TITLE Carpe-Diem AutoBot
SET MAVEN_HOME=C:\apache-maven-3.6.1\bin
CD C:\development\carpe-diem-autobot
%MAVEN_HOME%\mvn clean spring-boot:run
ENDLOCAL