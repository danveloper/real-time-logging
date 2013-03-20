package com.danveloper.log4j.jms;

import org.apache.activemq.ActiveMQConnection;
import org.apache.log4j.net.JMSAppender;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * User: ddcdwoods
 * Date: 3/19/13
 */
public class EnhancedJMSAppender extends JMSAppender {
    private String appName;

    /**
     * Method enriches the headers so that JMS consumers know who you are
     */
    @Override
    public void append(LoggingEvent event) {
        if(!checkEntryConditions()) {
            return;
        }

        try {
            ActiveMQConnection activeMqConnection = (ActiveMQConnection)getTopicConnection();

            if (!activeMqConnection.isStarted()) {
                activeMqConnection.start();
            }

            ObjectMessage msg = getTopicSession().createObjectMessage();
            if (getLocationInfo()) {
                event.getLocationInformation();
            }
            msg.setStringProperty("application.name", appName);
            msg.setStringProperty("hostname", InetAddress.getLocalHost().getHostName());
            msg.setStringProperty("ip-address", InetAddress.getLocalHost().getHostAddress());
            msg.setObject(event);
            getTopicPublisher().publish(msg);
        } catch(JMSException e) {
            errorHandler.error("Could not publish message in JMSAppender ["+name+"].", e,
                    ErrorCode.GENERIC_FAILURE);
        } catch(RuntimeException e) {
            errorHandler.error("Could not publish message in JMSAppender ["+name+"].", e,
                    ErrorCode.GENERIC_FAILURE);
        } catch (UnknownHostException e) {
            errorHandler.error("Could not publish message in JMSAppender ["+name+"].", e,
                    ErrorCode.GENERIC_FAILURE);
        }
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

}
