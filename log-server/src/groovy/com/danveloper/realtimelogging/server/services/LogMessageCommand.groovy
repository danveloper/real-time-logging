package com.danveloper.realtimelogging.server.services

import org.joda.time.LocalDateTime

/**
 * User: ddcdwoods
 * Date: 3/19/13
 */
class LogMessageCommand {
    String level
    String threadName
    String application
    String message
    String hostname
    String ipAddress
    LocalDateTime messageTime = new LocalDateTime()
}
