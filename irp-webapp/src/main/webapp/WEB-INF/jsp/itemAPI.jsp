<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Item API page</title>
</head>
<body>
<a href="/IReadinessPackage/index.jsp">Index (Home) Page</a>

<h2>Items</h2>
<b>GET HTTP Request</b><br>
IReadinessPackage/items/item/{id}<br>
<b>Path Parameter:</b> <a href="${pageContext.request.contextPath}/pages?fdAction=itemIDs">id</a> required.<br>
<b>Returns application/json structure</b> with list of attriblist, tutorial, statistic, resourceslist, MachineRubric, content, keywordList<br>
<b>Example:</b>
<a href="/IReadinessPackage/items/item/174">/items/item/<font color="blue"><b>174</b></font></a>
<br>
<br>
<b>GET HTTP Request</b><br>
IReadinessPackage/items/item/{id}/attribute<br>
<b>Path Parameter:</b> <a href="${pageContext.request.contextPath}/pages?fdAction=itemIDs">id</a> required.<br>
<b>Returns application/json structure</b> with attribute information for item tag<br>
<b>Example:</b>
<a href="/IReadinessPackage/items/item/174/attribute">/items/item/<font color="blue"><b>174</b></font>/attribute</a>
<br>
<br>
<b>GET HTTP Request</b><br>
IReadinessPackage/items/item/{id}/attriblist<br>
<b>Path Parameter:</b> <a href="${pageContext.request.contextPath}/pages?fdAction=itemIDs">id</a> required.<br>
<b>Returns application/json structure</b> with list of attrib<br>
<b>Example:</b>
<a href="/IReadinessPackage/items/item/174/attriblist">/items/item/<font color="blue"><b>174</b></font>/attriblist</a>
<br>
<br>
<b>GET HTTP Request</b><br>
IReadinessPackage/items/item/{id}/attriblist/{attid}<br>
<b>Path Parameters:</b> <a href="${pageContext.request.contextPath}/pages?fdAction=itemIDs">id</a> required, attid (index value of (0, 1, 2, . . .)) required<br>
<b>Returns application/json structure</b> with list of attrib<br>
<b>Example:</b>
<a href="/IReadinessPackage/items/item/174/attriblist/1">/items/item/<font color="blue"><b>174</b></font>/attriblist/<font color="blue">1</font></a>
<br>
<br>
<b>GET HTTP Request</b><br>
IReadinessPackage/items/item/{id}/attriblist<b>2</b>/{attid}<br>
<b>Path Parameters:</b> <a href="${pageContext.request.contextPath}/pages?fdAction=itemIDs">id</a> required, <a href="${pageContext.request.contextPath}/pages?fdAction=attidIDs">attid</a> required<br>
<b>Returns application/json structure</b> with list of attrib<br>
<b>Example:</b>
<a href="/IReadinessPackage/items/item/174/attriblist2/itm_item_subject">/items/item/<font color="blue"><b>174</b></font>/attriblist<b>2</b>/<font color="blue">itm_item_subject</font></a>
<br>
<br>
<b>GET HTTP Request</b><br>
IRP/api/items/item/{id}/tutorial<br>
<b>Path Parameter:</b> <a href="${pageContext.request.contextPath}/pages?fdAction=itemIDs">id</a> required.<br>
<b>Returns application/json structure</b> with tutorial information for item tag<br>
<b>Example:</b>
<a href="/IReadinessPackage/items/item/174/tutorial">/items/item/<font color="blue"><b>174</b></font>/tutorial</a>
<br>
<br>
<b>GET HTTP Request</b><br>
IRP/api/items/item/{id}/resourceslist<br>
<b>Path Parameter:</b> <a href="${pageContext.request.contextPath}/pages?fdAction=itemIDs">id</a> required.<br>
<b>Returns application/json structure</b> with list of resources for item tag<br>
<b>Example:</b>
<a href="/IReadinessPackage/items/item/174/resourceslist">/items/item/<font color="blue"><b>174</b></font>/resourceslist</a>
<br>
<br>
<b>GET HTTP Request</b><br>
IRP/api/items/item/{id}/statistic<br>
<b>Path Parameter:</b> <a href="${pageContext.request.contextPath}/pages?fdAction=itemIDs">id</a> required.<br>
<b>Returns text/plain</b> with statistic information for item tag<br>
<b>Example:</b>
<a href="/IReadinessPackage/items/item/174/statistic">/items/item/<font color="blue"><b>174</b></font>/statistic</a>
<br>
<br>
<b>GET HTTP Request</b><br>
IRP/api/items/item/{id}/MachineRubric<br>
<b>Path Parameter:</b> <a href="${pageContext.request.contextPath}/pages?fdAction=itemIDs">id</a> required.<br>
<b>Returns application/json structure</b> with MachineRubric information for item tag<br>
<b>Example:</b>
<a href="/IReadinessPackage/items/item/540/MachineRubric">/items/item/<font color="blue"><b>540</b></font>/MachineRubric</a>
<br>
<br>
<b>GET HTTP Request</b><br>
IRP/api/items/item/{id}/RendererSpec<br>
<b>Path Parameter:</b> <a href="${pageContext.request.contextPath}/pages?fdAction=itemIDs">id</a> required.<br>
<b>Returns application/json structure</b> with RendererSpec information for item tag<br>
<b>Example:</b>
<a href="/IReadinessPackage/items/item/631/MachineRubric">/items/item/<font color="blue"><b>631</b></font>/RendererSpec</a>
<br>
<br>
<b>GET HTTP Request</b><br>
IRP/api/items/item/{id}/gridanswerspace<br>
<b>Path Parameter:</b> <a href="${pageContext.request.contextPath}/pages?fdAction=itemIDs">id</a> required.<br>
<b>Returns text/plain</b> with gridanswerspace information for item tag<br>
<b>Example:</b>
<a href="/IReadinessPackage/items/item/766/gridanswerspace">/items/item/<font color="blue"><b>766</b></font>/gridanswerspace</a>
<br>
<br>
<b>GET HTTP Request</b><br>
IRP/api/items/item/{id}/content<br>
<b>Path Parameter:</b> <a href="${pageContext.request.contextPath}/pages?fdAction=itemIDs">id</a> required.<br>
<b>Returns application/json structure</b> with list of content for item<br>
<b>Example:</b>
<a href="/IReadinessPackage/items/item/174/content">/items/item/<font color="blue"><b>174</b></font>/content</a>
<br>
<br>
<b>GET HTTP Request</b><br>
IRP/api/items/item/{id}/content/{language}<br>
<b>Path Parameters:</b> <a href="${pageContext.request.contextPath}/pages?fdAction=itemIDs">id</a> required, language ("ENU", "ESN") required<br>
<b>Returns application/json structure</b> with content for a specific language<br>
<b>Example:</b>
<a href="/IReadinessPackage/items/item/174/content/ENU">/items/item/<font color="blue"><b>174</b></font>/content/<font color="blue">ENU</font></a>
<br>
<br>
<b>GET HTTP Request</b><br>
IRP/api/items/item/{id}/keywordList<br>
<b>Path Parameter:</b> <a href="${pageContext.request.contextPath}/pages?fdAction=itemIDs">id</a> required.<br>
<b>Returns application/json structure</b> with list of keyword for item<br>
<b>Example:</b>
<a href="/IReadinessPackage/items/item/787/keywordList">\/items/item/<font color="blue"><b>787</b></font>/keywordList</a>
<br>
<br>

</body>
</html>