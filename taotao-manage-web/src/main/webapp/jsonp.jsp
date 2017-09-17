<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	//接收回调函数名
	String callback = request.getParameter("callback");
	out.print(callback + "({\"test\":\"heima\"});");
%>