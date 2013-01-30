<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.ravi.Calc" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>The great Calculator</title>
</head>
<body>
Hello CloudMunch <br>
This is a Java Server Page
The addition of two numbers <%=request.getParameter("parm1") %> and <%=request.getParameter("parm2") %> is <br>
<% 
double result= Calc.add(Double.parseDouble(request.getParameter("parm1")), Double.parseDouble(request.getParameter("parm2"))); %>
<%= result %>

