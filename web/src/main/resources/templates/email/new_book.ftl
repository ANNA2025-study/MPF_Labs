<h3>Нова книга додана до каталогу</h3>

<p>Назва: <b>${title}</b></p>
<p>Автор: <b>${author}</b></p>

<#if comments?? && comments?size > 0>
    <p>Коментарі:</p>
    <ul>
        <#list comments as c>
            <li>${c}</li>
        </#list>
    </ul>
</#if>

<p>Дата додавання: ${createdAt?string("yyyy-MM-dd HH:mm")}</p>
