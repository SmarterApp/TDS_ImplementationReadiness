<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<a href="/IReadinessPackage/index.jsp">Index (Home) Page</a>

<h2>Scoring</h2>
<b>GET HTTP Request</b><br>
IReadinessPackage/scoring/test<br>
<b>Path Parameter:</b> <br>
<b>Returns application/json structure</b> with Test object<br>
<b>Example:</b>
<a href="/IReadinessPackage/scoring/test">/scoring/test</a>
<br>
<br>
<b>GET HTTP Request</b><br>
IReadinessPackage/scoring/examinee<br>
<b>Path Parameter:</b> <br>
<b>Returns application/json structure</b> with Examinee object<br>
<b>Example:</b>
<a href="/IReadinessPackage/scoring/examinee">/scoring/examinee</a>
<br>
<br>
<b>GET HTTP Request</b><br>
IReadinessPackage/scoring/opportunity<br>
<b>Path Parameter:</b> <br>
<b>Returns application/json structure</b> with Opportunity object<br>
<b>Example:</b>
<a href="/IReadinessPackage/scoring/opportunity">/scoring/opportunity</a>
<br>
<br>
<b>GET HTTP Request</b><br>
IReadinessPackage/scoring/opportunity/scores<br>
<b>Path Parameter:</b> <br>
<b>Returns application/json structure</b> with list of Score object for Opportunity object<br>
<b>Example:</b>
<a href="/IReadinessPackage/scoring/opportunity/scores">/scoring/opportunity/scores</a>
<br>
<br>
<b>GET HTTP Request</b><br>
IReadinessPackage/scoring/opportunity/items<br>
<b>Path Parameter:</b> <br>
<b>Returns application/json structure</b> with list of Item object for Opportunity object<br>
<b>Example:</b>
<a href="/IReadinessPackage/scoring/opportunity/items">/scoring/opportunity/items</a>
<br>
<br>
<b>GET HTTP Request</b><br>
IReadinessPackage/scoring/comments<br>
<b>Path Parameter:</b> <br>
<b>Returns application/json structure</b> with list of Comment object<br>
<b>Example:</b>
<a href="/IReadinessPackage/scoring/comments">/scoring/comments</a>
<br>
<br>
<b>GET HTTP Request</b><br>
IReadinessPackage/scoring/toolusage<br>
<b>Path Parameter:</b> <br>
<b>Returns application/json structure</b> with Toolusage object<br>
<b>Example:</b>
<a href="/IReadinessPackage/scoring/toolusage">/scoring/toolusage</a>
<br>
<br>

</body>
</html>