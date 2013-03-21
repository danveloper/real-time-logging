<%@ page import="org.codehaus.groovy.grails.commons.ApplicationHolder" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
    <jq:resources/>
    <jqui:resources/>
    <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Unica+One">
    <atmosphere:resources/>
    <r:require module="application"/>
    <r:layoutResources/>

    <r:script>
        $(document).ready(function() {
            var url = '${createLink(uri:ApplicationHolder.application.config.grails.atmosphere.mappingUri)}';
            $.atmosphere.subscribe(url, Logging.router, $.atmosphere.request = {transport:'websocket', fallbackTransport:'streaming'});

            var initialRequest = new Object();
            initialRequest.status = 200;
            initialRequest.responseBody = '${appLogs}';
            Logging.allAppsCallback(initialRequest);
        });
    </r:script>
</head>
<body>
<div id="header-bar" class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container-fluid">
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <a class="brand" href="#">Real Time Logger</a>
            <div class="nav-collapse collapse">
                <p class="navbar-text pull-right">
                    <a href="http://twitter.com/danveloper" target="_twblank">Written By <span class="right">@danveloper</span></a>
                </p>
            </div><!--/.nav-collapse -->
        </div>
    </div>
</div>

<div class="container-fluid">
    <div class="row-fluid">
        <ul id="sidebar" class="nav nav-pills nav-stacked">
            <li id="product-link">
                <a href="http://grails.org" target="_blank">
                    <img src="${resource(dir:'images', file:'apple-touch-icon.png')}">
                </a>
            </li>
            <li>
                <a href="${createLink(action:'index')}">
                    <i class="icon-dashboard"></i>
                    <span>Dashboard</span>
                </a>
            </li>
            <li>
                <a href="index.html">
                    <i class="icon-cog"></i>
                    <span>Configure</span>
                </a>
            </li>
        </ul>
        <div class="span11" id="content-container">
            <div id="logging-channels"></div>
            <div id="no-channels">
                <i class="icon-warning-sign"></i>
                <br/>
                No Channels
            </div>
        </div><!--/span-->
    </div><!--/row-->

    <r:layoutResources/>

</div><!--/.fluid-container-->


<script type="text/x-jquery-tmpl" id="message-row">
    <div class="log-message">
        <span class="message-time">[ $<%%>{messageTime} ] </span>
        <span class="host-info">[ $<%%>{ipAddress} / $<%%>{hostname} ] </span>
        <span class="log-level $<%%>{level}">[ $<%%>{level} ] </span>
        <span> - </span>
        <span class="message"> $<%%>{message} </span>
    </div>
</script>

<script id="atmosphere-script">
!function($) {
    var Atmosphere = function(url) {
        this.url = url;
    };
    Atmosphere.prototype = {
        constructor: Atmosphere,
        toString: function() {
            return "Atmosphere";
        },
        subscribe: function() {
            $.atmosphere.subscribe(this.url, this.messageEvent, $.atmosphere.request = {transport:'websocket', fallbackTransport:'streaming'});
        },
        messageEvent: function(response) {
            console.log("Message Event!", response);
        },
        // Fuck tha po-lice!
        source: function() {
            var src = "var " + this.toString() + " = " + this.constructor + ";\n";
            src += this.toString()+".prototype = {\n\tconstructor: "+this.toString()+",\n";
            for (var key in this.__proto__) {
                if (!(key in ['constructor', '__proto__'])) {
                    src += "\t"+key+": "+this[key]+",\n";
                }
            }
            src += "\ttoString: "+this["toString"]+"\n};";
            return src;
        }
    };

    $.atmosphereExt = function(options) {
        return new Atmosphere(options.url);
    }
}(window.jQuery);
</script>

</body>
</html>