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
Deployment to GAE <br>
This is a simple Java Server Page to add 2 numbers <br>

<form action="calc.jsp" method=get>
  Enter the first number <input type=text name=parm1> <br>
  Enter the second number <input type=text name=parm2> <br>
  <input type=submit value="Submit">
  </form>
</body>
</html>