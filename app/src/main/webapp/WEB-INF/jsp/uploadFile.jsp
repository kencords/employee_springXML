<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <title>Employee Records System: Upload File</title>
    </head>

    <body>
    	<h1>UPLOAD EMPLOYEE FILE</h1>
    	<div style="border:1px solid black; width:400px; margin: 0 auto; text-align:center;">
	    	<c:forEach items="${logMsgs}" var="msg">
	  			<p style="color:${msg.color};">${msg.logMsg}</p>
	  		</c:forEach>
	    	<form method="POST" enctype="multipart/form-data">
	    		<table align=center>
	                <tr>
	                    <td align="center"><p><input type="file" name="file"/><br/>
	                </tr>
	                <tr>
	                    <td align="center"><p><button style="display:inline-block;" type="submit" name="uploadBtn">Upload</button></td>
	                </tr>
	            </table>
	    	</form>
	    	<div align="center"><button onClick="location.href='/home'">Back</button></div>
	    	<br>
	    </div>
    </body>

</html>