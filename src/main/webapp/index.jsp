<%
/*
The MIT License (MIT)

Copyright (c) 2015 IBM

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.ResourceBundle"%>
<%@ page import="java.net.URL" %>
<%@ page import="java.util.ResourceBundle.Control" %>
<%@ page import="com.ibm.g11n.pipeline.client.ServiceAccount" %>
<%@ page import="com.ibm.g11n.pipeline.client.rb.CloudResourceBundle" %>
<%@ page import="com.ibm.g11n.pipeline.client.rb.CloudResourceBundleControl" %>
<%@ page import="com.ibm.g11n.pipeline.client.rb.CloudResourceBundleControl.LookupMode" %>
<%@ page import="com.ibm.globalization.Globalization" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>



<%
//ResourceBundle res = ResourceBundle.getBundle("com.ibm.translation", request.getLocale());
ServiceAccount account = ServiceAccount.getInstance();
Control control = CloudResourceBundleControl.getInstance(account, CloudResourceBundleControl.LookupMode.LOCAL_THEN_REMOTE);
ResourceBundle res = ResourceBundle.getBundle("com.ibm.translation", request.getLocale(), control);
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><%=res.getString("product")%></title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">

<style type="text/css">
<%@ include file="css/grid.css" %>
</style>

<style type="text/css">
    body{
    	padding-top: 70px;
    }
</style>

</head>
<body>

<nav id="myNavbar" class="navbar navbar-default navbar-inverse navbar-fixed-top" role="navigation">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbarCollapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#"><%=res.getString("product")%></a>
            </div>
            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="navbarCollapse">
                <ul class="nav navbar-nav">
                    <li class="active"><a href="index.jsp"><%=res.getString("home")%></a></li>
                    <li><a href="form.jsp"><%=res.getString("start")%></a></li>
                </ul>
            </div>
        </div>
    </nav>
    <div class="container">
        <div class="jumbotron">
            <h1><%=res.getString("product")%></h1>
            <p><%=res.getString("information")%></p>
            <p><a href="form.jsp" class="btn btn-success btn-lg"><%=res.getString("get_started")%></a></p>
        </div>

        <hr>
        <footer>
        	<p><%=res.getString("footer")%></p>
      	</footer>
    </div>

 <script src="//code.jquery.com/jquery-1.11.2.min.js"></script>
 <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

</body>
</html>
