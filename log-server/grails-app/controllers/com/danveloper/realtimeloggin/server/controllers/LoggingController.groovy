package com.danveloper.realtimeloggin.server.controllers

import grails.converters.JSON


class LoggingController {
    def loggingService

    def index() {
        [appLogs: (loggingService.applications as JSON)]
    }

    def renderChannelWidget() {
        def logHistory = loggingService.applications[params.id].find { it.ipAddress == params.ip && it.hostname == params.hostname } as JSON
        render (
            template: 'widget',
            model:
                [
                        id: params.id,
                        ip: params.ip,
                        hostname: params.hostname,
                        magicNumber: params.magicNumber,
                        fullscreen: params.fullscreen,
                        logHistory: logHistory ? logHistory.toString() : '{}'
                ]
        )
    }
}
