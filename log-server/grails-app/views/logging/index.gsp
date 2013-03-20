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
            var url = '${createLink(uri:ApplicationHolder.application.config.grails.atmosphere.mappingUri)}/all';
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
</body>
</html>