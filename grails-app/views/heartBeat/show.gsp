<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'heartBeat.label', default: 'HeartBeat')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#show-heartBeat" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
                <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <div id="show-heartBeat" class="content scaffold-show" role="main">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <div>
                <div style="float:left; width:70%">
                    <f:display bean="heartBeat" />
                </div>
                <div style="float:left; width:30%">
                    <div class='box'
                         data-heart-beat-url='<g:createLink action="fetch"/>'><div class="data">
                        <div class="spinner">
                            <g:message code="spinner.alt" default="Loading&hellip;"/>
                        </div>
                    </div>
                </div>
            </div>
            <g:form resource="${this.heartBeat}" method="DELETE">
                <fieldset class="buttons" style="clear: both;">
                    <g:link class="edit" action="edit" resource="${this.heartBeat}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                    <input class="delete" type="submit" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                    <a class="test button" href="#" style=" float: right">
                        ${message(code: 'default.button.test.label', default: 'Test')}
                    </a>
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
