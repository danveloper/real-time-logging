<%@ page import="org.codehaus.groovy.grails.commons.ApplicationHolder" %>
<div class="span4 channel-widget" id="app-${magicNumber}" data-fullscreen="${fullscreen}">
    <div class="well widget">
        <div class="widget-header">
            <h3 class="title"><i class="icon-table"></i> Streaming Logs - ${id} on ${ip}/${hostname}</h3>
        </div>
        <div class="widget-content foo">
            <div class="span12 messages">
            </div>
            <div style="clear:both"></div>
        </div>


        <script>
            (function() {
                console.log('${logHistory}');
                var history = JSON.parse('${logHistory}');
                console.log("constructing history", history);
                var initialRequest = new Object();
                initialRequest.status = 200;

                console.log("calling singleAppCallback");
                try {
                    initialRequest.responseBody = JSON.stringify(history.history);
                    Logging.singleAppCallback(initialRequest);
                    console.log("all done!");
                } catch (e) {
                    console.log("error calling singleAppCallback!!", e);
                }
            })();
        </script>
        <div style="clear:both"></div>
    </div>
    <div style="clear:both"></div>
</div>

