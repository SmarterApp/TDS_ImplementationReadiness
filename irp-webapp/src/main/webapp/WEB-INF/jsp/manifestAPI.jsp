<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Manifest API page</title>
</head>
<body>
<a href="/IReadinessPackage/index.jsp">Index (Home) Page</a>

<h2>Manifest</h2>
<b>GET HTTP Request</b><br>
IReadinessPackage/manifest<br>
<b>Path Parameter:</b><br>
<b>Returns application/json structure</b> with list of metadata, organizations, resources<br>
<b>Example:</b>
<a href="/IReadinessPackage/manifest">/manifest/</a>
<br>
<br>
<b>GET HTTP Request</b><br>
IReadinessPackage/manifest/resources<br>
<b>Path Parameter:</b><br>
<b>Returns application/json structure</b> with list of resources<br>
<b>Example:</b>
<a href="/IReadinessPackage/manifest/resources">/manifest/resources</a>
<br>
<br>
<b>GET HTTP Request</b><br>
IReadinessPackage/manifest/resources/{identifier}<br>
<b>Path Parameters:</b> <a href="${pageContext.request.contextPath}/pages?fdAction=resourcesIdentifier">identifier</a> required <br>
<b>Returns application/json structure</b> with resource structure<br>
<b>Example:</b>
<a href="/IReadinessPackage/manifest/resources/item-187-174">/manifest/resources/<font color="blue"><b>item-187-174</b></font></a>
<br>
<br>
<b>GET HTTP Request</b><br>
IReadinessPackage/manifest/metadata<br>
<b>Path Parameter:</b><br>
<b>Returns application/json structure</b><br>
<b>Example:</b>
<a href="/IReadinessPackage/manifest/metadata">/manifest/metadata</a>
<br>
<br>
<b>GET HTTP Request</b><br>
IReadinessPackage/manifest/organizations<br>
<b>Path Parameter:</b> <br>
<b>Returns text/plain</b><br>
<b>Example:</b>
<a href="/IReadinessPackage/manifest/organizations">/manifest/organizations</a>
<br>
<br>

</body>
</html>