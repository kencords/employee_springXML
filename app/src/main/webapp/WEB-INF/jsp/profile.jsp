<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
	
	<head>
        <meta charset="utf-8">
        <title>Employee Records System: Employee Profile</title>
    </head>

    <body>
    	<h1>EMPLOYEE PROFILE</h1>
    	<div align="left">
    		<button style="display:inline-block;" onclick="location.href='/employee/editPage?empId=${employee.empId}'">Edit Employee</button>
  			<button style="display:inline-block;" onClick="location.href='/home'">Back</button>
  			<div align="left">
  				<br><b>NAME:</b>${employee.title} ${employee.firstName} ${employee.middleName}, ${employee.lastName} ${employee.suffix}
  				<br><b>ADDRESS:</b> ${employee.address}
  				<br><b>BIRTHDATE:</b> ${bDate}
  				<br><b>GWA:</b> ${employee.gwa}
  				<br><b>CURRENTLY HIRED:</b> ${curHired}
  				<br><b>DATE HIRED:</b> ${hDate}
				<br><b>CONTACTS:</b><br>
				<c:forEach var="contact" items="${employee.contacts}">
					${contact.contactType}: ${contact.contactValue} <br>
				</c:forEach>  				
				<br><b>ROLES:</b><br> ${employee.roles}
  			</div>
    	</div>
    </body>
</html>