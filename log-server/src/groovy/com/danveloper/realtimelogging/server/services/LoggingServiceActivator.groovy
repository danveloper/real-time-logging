package com.danveloper.realtimelogging.server.services

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.integration.annotation.Headers
import org.springframework.integration.annotation.Payload
import org.apache.log4j.spi.LoggingEvent

/**
 * User: ddcdwoods
 * Date: 3/19/13
 */
@Service
class LoggingServiceActivator {
    @Autowired
    private LoggingService loggingService


    public void log(@Headers Map<String, Object> headers, @Payload LoggingEvent loggingEvent) {
        LogMessageCommand command = new LogMessageCommand(
                level: loggingEvent.level.toString(),
                application: headers."application.name",
                hostname: headers.hostname,
                ipAddress: headers."ip-address",
                threadName: loggingEvent.threadName,
                message: loggingEvent.renderedMessage
        )
        loggingService.log(command)
    }
}
