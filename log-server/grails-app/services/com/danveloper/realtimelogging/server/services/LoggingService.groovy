package com.danveloper.realtimelogging.server.services

import grails.converters.JSON
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.joda.time.LocalDateTime

import java.util.concurrent.ConcurrentHashMap

/**
 * Atmosphere endpoint
 */
class LoggingService {
    def grailsApplication

    // This will be a registry of the applications that are logging to us
    public static def applications = new ConcurrentHashMap()

    /**
     *  These two fields represent the atmosphere "config".
     */
    static final MAPPING_URI = ApplicationHolder.application.config.grails.atmosphere.mappingUri
    static atmosphere = [mapping: MAPPING_URI]

    // This will be our lock on the message writer, a painfully obscure lesson.
    static final Object MUTEX = new Object()

    /**
     * The {@link LoggingServiceActivator} delegates its messages to this method.
     * This method, in turn, broadcasts them to the UI via the atmosphere broadcaster.
     *
     * @param command
     * @return
     */
    def log(LogMessageCommand command) {
        registerApplicationInstance(command.ipAddress, command.hostname, command.application)
        addMessageToLogHistory(command)
        command.message = command.message
        def message = command as JSON
        broadcaster[MAPPING_URI].broadcast(message.toString())
    }

    // Add this log message to this logging instance's history reference.
    // In the UI, we'll want to show the latest few log messages, similar to "tail"'s argument-free functionality.
    private void addMessageToLogHistory(LogMessageCommand command) {
        // Get this application's logging data out of the registry
        def myApplicationObj = findApplicationObject(command.application, command.ipAddress, command.hostname)

        // Construct the history if it doesn't exist...
        def history = myApplicationObj["history"]
        if (!history) {
            history = []
        }
        // If it does exist, make sure we're keeping a limited amount of history (this can get out of hand quickly)
        if (history.size() > 10) {
            history = history[-9..-1]
        }
        command.message = command.message.encodeAsHTML()
        history << command
        myApplicationObj["history"] = history
    }

    // Our method of "looking up" application instances from the registry.
    // Application Name, IP Address, and Hostname provide a strong composite key
    private def findApplicationObject(appName, ipAddress, hostname) {
        applications[appName].find { it.ipAddress == ipAddress && it.hostname == hostname }
    }

    // Put an application's logging instance in the registry. From there we'll store history and other things.
    // If a mapping already exists, update its "latest update time" property.
    private def registerApplicationInstance(String ipAddress, String hostname, String appName) {
        if (!applications[appName]) {
            applications[appName] = []
        }
        def existing = findApplicationObject(appName, ipAddress, hostname)
        if (!existing) {
            // Register the new application and create a unique mapping for it.
            applications[appName] << [ipAddress: ipAddress, hostname: hostname, lastUpdateTime: new LocalDateTime()]

            // Broadcast to the default channel, letting the UI know that we have new loggers
            broadcaster[MAPPING_URI].broadcast( (applications as JSON).toString(true) )
        } else {
            existing.lastUpdateTime = new LocalDateTime()
        }
    }

    /** ----------X----------- **/
    /** ----------X----------- **/
    /** ---------------------- **/
    /** ATMOSPHERE HANDLERS !! **/
    /** ----------X----------- **/
    /** ----------X----------- **/
    /** ----------X----------- **/

    def onRequest = { event ->
        def request = event.request
        event.suspend()
    }

    def onStateChange = { event ->
        if (event.cancelled) {
            log.info "onStateChange, cancelling $event"
        }
        else if (event.message) {
            log.info "onStateChange, message: ${event.message}"

            // Only one message at a time, please.
            // This can cause some really funky errors if it isn't here.
            synchronized( MUTEX ) {
                def writer = event.resource.response.writer

                writer.write event.message
                writer.flush()
            }
        }
    }

}
