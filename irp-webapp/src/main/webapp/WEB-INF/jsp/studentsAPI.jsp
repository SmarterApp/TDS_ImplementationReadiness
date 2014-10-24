<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Students API page</title>
</head>
<body>
<a href="/IReadinessPackage/index.jsp">Index (Home) Page</a>

<h2>Students</h2>

<b>GET HTTP Request</b><br>
IReadinessPackage/students<br>
<b>Path Parameter:</b> <br>
<b>Returns application/json structure</b> with list of student object<br>
<b>Example:</b>
<a href="/IReadinessPackage/students">/students/</a>
<br>
<br>
<b>GET HTTP Request</b><br>
IReadinessPackage/students/{studentIdentifier}<br>
<b>Path Parameter:</b> <a href="${pageContext.request.contextPath}/pages?fdAction=studentIdentifier">studentIdentifier</a> required.<br>
<b>Returns application/json structure</b> with student object<br>
<b>Example:</b>
<a href="/IReadinessPackage/students/524335">/students/<font color="blue"><b>524335</b></font></a>
<br>
<br>
<b>POST HTTP Request</b><br>
IReadinessPackage/students/addstudent<br>
<b>Path Parameter:</b>Student Object<br>
<br>
<br>
<b>PUT HTTP Request</b><br>
IReadinessPackage/students/student?studentIdentifier=524335<br>
<b>Path Parameter:</b>Student Object and studentIdentifier<br>
</body>
</html>