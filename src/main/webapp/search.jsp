<%--
  Created by IntelliJ IDEA.
  User: Alpen Ditrix
  Date: 14.10.2014
  Time: 4:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head><title>Search</title></head>
<body>
<form method="post">
    Искомое слово:
    <input type="text" name="q" autofocus>
    <br>
    <input type="submit" value="Разыскать!">
</form>

<c:if test="${not empty q}">
    <div>
        Запрос: ${q} </br>
        <c:if test="${not empty chw}">
            Всего вхождений: ${chw.totalOccurrencesAmount}<p>
            <table border=1>
                <tr>
                    <td>#</td>
                    <td>Номер файла</td>
                    <td>Окрестность</td>
                </tr>
                <c:forEach var="rendezvois" items="${chw.rvs}" varStatus="idx">
                    <tr>
                        <td>${idx.index}</td>
                        <td><a href="/index/uliss18_${rendezvois.file}">${rendezvois.file}</a></td>
                        <td>
                            <c:forEach var="place" items="${rendezvois.places}">
                                #${place.location}: ${place.ambit}<br>
                            </c:forEach>
                        </td>
                    </tr>
                </c:forEach>
            </table
        </c:if>
        <c:if test="${empty chw}">
            Не найдено вхождений такого полного наборов слов ни в один файл
        </c:if>
    </div>
</c:if>
</body>
</html>