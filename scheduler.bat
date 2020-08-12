@ECHO OFF
:: Use local variables
SETLOCAL ENABLEEXTENSIONS
:: Set title of the window
TITLE Carpe-Diem AutoBot
:: Set Maven Home
SET MAVEN_HOME=C:\apache-maven-3.6.1\bin
:: Clean and Create WorkSpace
RMDIR C:\Runner /F /Q
MKDIR C:\Runner
:: Git Clone Repository
git clone https://00ff8871658427b9ecb10d1385a81e4bffe9569c@github.com/ramkumar-yoganathan/carpe-diem-autobot.git
CD C:\Runner\carpe-diem-autobot
COPY C:\development\carpe-diem-autobot\src\main\resources\application.properties C:\Runner\carpe-diem-autobot\src\main\resources\application.properties
:: Run App
%MAVEN_HOME%\mvn clean spring-boot:run
ENDLOCAL