<%--
Needed parameters:
- it (of type HeartBeat)

Optional parameters :
- hideButtons
- hideTitle
- hideButtons

--%>
<div class="heartBeat main ${it.orderKey}">
    <g:if test="${!hideTitle}">
        <div class="title">
            ${it.title}
        </div>
    </g:if>

    <div class="box"
         data-heart-beat-url="<g:createLink controller="heartBeat" action="fetch" id="${it.id}"/>"
         data-heart-beat-refresh-rate="${it.refreshRate}"
         data-heart-beat-display="${it.display}"
         data-heart-beat-title="${it.title}">

        <g:if test="${!hideData}">
            <div class="data">
                <asset:image src="spinner.gif" alt="Chargement..."/>
            </div>
        </g:if>

    </div>

    <g:if test="${!hideErrors}">
        <div class="error"></div>
    </g:if>

    <g:if test="${!hideButtons}">
        <div class="action_buttons">
            <g:link controller="heartBeat" action="edit" title="edit" id="${it.id}"><asset:image
                    src="skin/database_edit.png" alt="edit"/></g:link>
            <g:link controller="heartBeat" action="delete" title="delete" id="${it.id}">
                <asset:image src="skin/database_delete.png" alt="delete">
                </asset:image>
            </g:link>
        </div>
    </g:if>

</div>