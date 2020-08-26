# Disclaimer of libility
The material and information contained on this repository is for educational purposes only. You should not rely upon the material or information on the repository as a basis for making any business, legal or any other decisions. Any reliance you place on this material is thereforce strictly at your risk. 

# Carpe-Diam AutoBot

An autobot that does the algorithimic trading of NIFTY/BANK NIFTY Buy CE/PE Options with Risk and Reward Profile. 

### Features
- Support Quartz Scheduler
- Support Zerodha Stock Broker
- Full-featured: Real-time Buy Call and Put options based on the current market nearest NIFTY & BANK NIFTY future price, Console display of Signal Wait, Processed Orders.

### Tech Stack
- Spring Boot, Web
- Mongo DB
- Zerodha Stock Broker
- jQuery / Bulma CSS

### Jobs & Schedule

1. Stock Broker Token Job  - At every day 9 AM - Acquire the access token from the stock broker for the day session.
2. Future Price Query Job  - At every minute past 9:10 AM - Get the real time future price to calculate the Call and Put Options price.
3. Buy Call/Put Signal Job - At every minute past 9:15 AM until 3:30 - Query for entry based on the candle stick algorithm.

### Configuration

> Update your zerodha stock broker credentials accordingly. 

- spring.boot.config.stock.broker.client= Your client id
- spring.boot.config.stock.broker.password= Your Web Client Password
- spring.boot.config.stock.broker.pin= Your Web Pin
- spring.boot.config.stock.broker.url= Your Kite Connect App Url. Please check this link for more info https://kite.trade/docs/connect/v3/apps/
- spring.boot.config.stock.broker.key=
- spring.boot.config.stock.broker.secret=
- spring.boot.config.stock.broker.logs=false
- spring.boot.config.stock.broker.expiry=weekly
- spring.boot.config.stock.broker.order=regular

> Update your mongo db accordingly. 

- spring.data.mongodb.database=carpe-diem
- spring.data.mongodb.host=localhost
- spring.data.mongodb.password=
- spring.data.mongodb.port=27017
- spring.data.mongodb.repositories.enabled=true
- spring.data.mongodb.uri=mongodb://localhost/carpe-diem
- spring.data.mongodb.username=autobot

### Execution Terminal

> Waiting for signal

![](https://github.com/ramkumar-yoganathan/carpe-diam-autobot-docs/blob/master/waiting-signal.PNG)

   
