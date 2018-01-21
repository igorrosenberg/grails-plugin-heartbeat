<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'heartBeat.label', default: 'HeartBeat')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#edit-heartBeat" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
                <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <div id="edit-heartBeat" class="content scaffold-edit" role="main">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${this.heartBeat}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.heartBeat}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form resource="${this.heartBeat}" method="PUT">
                <g:hiddenField name="version" value="${this.heartBeat?.version}" />
                <div >
                    <div style="float:left; width:70%">
                    <fieldset class="form">
                    <f:all bean="heartBeat"/>
                    </fieldset>
                </div>
                    <div style="float:left; width:30%">
                        <div class='box'
                             data-heart-beat-url='<g:createLink action="fetch"/>'
                        ><div class="data">
                            <asset:image src="spinner.gif" alt="Chargement..."/>
                        </div>
                        </div>
                        <div class="error"></div>
                </div>
                <fieldset class="buttons" style=" clear: both;">
                    <input class="save" type="submit" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                    <a class="test button" href="#" style=" float: right" >
                        ${message(code: 'default.button.test.label', default: 'Test')}
                    </a>
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
