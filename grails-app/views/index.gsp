<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>ProHeartBeat</title>
    <style>

    /* User-specific page look-n-feel */
    .admin_link {
        text-align: right;
    }

    .my_data_line {
        clear: both;
    }

    /* heartBeat GSP specific */
    .heartBeat.main {
        width: 20%;
        float: left;
        border: 1px solid grey;
        margin: 20px;
        text-align: center;
    }

    .heartBeat.main .title {
    }

    .heartBeat.main .action_buttons {
        float: right;
    }
    .heartBeat.main .error {
        float: left;
    }

    .heartBeat.main.B5 {
        width: 50%;
    }

    </style>
</head>

<body>

<div class="spinnerTemplate hidden">
    <asset:image src="spinner.gif" alt="Chargement..."/>
</div>

<div>
    <div class="admin_link">
        <g:link controller="heartBeat">Admin</g:link>
    </div>

    <g:set var="list" value="${heartbeat.HeartBeat.list().sort { it.orderKey }}"/>
    <g:set var="hideButtons" value="${params.boolean('hideButtons')}"/>
    <g:set var="hideTitle" value="${params.boolean('hideTitle')}"/>
    <g:set var="hideButtons" value="${params.boolean('hideButtons')}"/>

    <div class="my_data_line">
        <g:render template="/heartBeat" collection="${list.findAll { it.orderKey.startsWith 'A' }}"/>
    </div>

    <div class="my_data_line">
        <g:render template="/heartBeat" collection="${list.findAll { it.orderKey.startsWith 'B' }}"/>
    </div>

</div>

</body>
</html>
