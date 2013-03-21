Grails Real Time Logging Application
=================
***
This project is a Grails application that receives Log4j messages from a JMS channel and streams them to a web page view using Atmosphere and websockets. The full write up is available at [http://danveloper.github.com/grails-real-time-logging.html](http://danveloper.github.com/grails-real-time-logging.html).

Usage
===
***
The Grails app is contained inside of [log-server](https://github.com/danveloper/real-time-logging/log-server) directory. From there you can simply do **grails run-app** and it will fire up appropriately. Once the application is up and running, you can use the runnable [TestJMSRunner](https://github.com/danveloper/real-time-logging/blob/master/enhanced-log4j-jms-appender/src/test/java/com/danveloper/log4j/jms/TestJMSLogger.java) to test streaming log messages.

Remember, you need to be using a browser that supports websockets. I **only** tested this in Chrome.

License
===
***
As always, free for all. Do enjoy.

Contact
===
***
t([@danveloper](http://twitter.com/danveloper)) *** g([daniel.p.woods@gmail.com](mailto:daniel.p.woods@gmail.com))
