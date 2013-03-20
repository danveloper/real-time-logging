var Logging = {
    $channels: $("#logging-channels"),
    router: function(response) {
        if (response.status == 200) {
            var data = response.responseBody;
            try {
                var dat = JSON.parse(data);
                if (dat.class && dat.class == "com.danveloper.realtimelogging.server.services.LogMessageCommand") {
                    Logging.singleAppCallback(response);
                } else {
                    Logging.allAppsCallback(response);
                }
            } catch (e) {}
        }
    },
    getHashCodeFromMessage: function(message) {
        // NOTE! The .hashCode() function comes from StringExt.js
        var appName = message.application;
        var hostname = message.hostname;
        var ipAddress = message.ipAddress;
        var constructed = appName + "/" + ipAddress + "/"+hostname;
        return constructed.hashCode();
    },
    buildMessageRow: function(message) {
        return $("#message-row").tmpl(message);
    },
    doContainerScroll: function($containerDiv) {
        var $widgetContainer = $containerDiv.find(".messages");
        $widgetContainer.scrollTop($widgetContainer[0].scrollHeight);
    },
    singleAppCallback: function(response) {
        if (response.status == 200) {
            var data = response.responseBody;
            if (data.length > 0) {
                var dat = [].concat(JSON.parse(data));
                var magicNumber = Logging.getHashCodeFromMessage(dat[0]);
                var $containerDiv = $("#app-"+magicNumber);
                if ($containerDiv.length > 0) {
                    var $messages = $containerDiv.find(".messages");

                    $.each(dat, function(i, message) {
                        var messageRow = Logging.buildMessageRow(message);
                        $messages.append(messageRow);
                        Logging.doContainerScroll($containerDiv);
                    });
                }
            }
        }
    },
    allAppsCallback: function(response) {
        if (response.status == 200) {
            var data = response.responseBody;
            if (data.length > 0) {
                var dat = [].concat(JSON.parse(data));

                $.each(dat, function(i, obj) {
                    $.each(obj, function(appName, val) {
                        $.each(val, function(k, app)  {
                            if ($("#no-channels").is(":visible")) {
                                $("#no-channels").hide();
                            }
                            app.application = appName;
                            var magicNumber = Logging.getHashCodeFromMessage(app);
                            $.get("/logging/renderChannelWidget", { id: appName, ip: app.ipAddress, hostname: app.hostname, fullscreen: false, magicNumber: magicNumber}, function(data) {
                                if ($("#app-"+magicNumber).length == 0) {
                                    try {
                                        Logging.$channels.append(data);
                                    } catch (e) {
                                        console.log("Error appending log", e);
                                    }
                                }
                            });
                        });
                    });
                });
            }
        }
    },
    isEmpty: function(map) {
        for(var key in map) {
            if (map.hasOwnProperty(key)) {
                return false;
            }
        }
        return true;
    }
};