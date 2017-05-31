<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
	
	<head>
        <meta charset="utf-8">
        <title>Employee Records System: Home Page</title>
    </head>

  	<body>
  		<h1>EMPLOYEE RECORDS SYSTEM</h1>

  		<button style="display:inline-block;" onClick="location.href='/addEmployee'">Add Employee</button>
  		<button style="display:inline-block;" onClick="location.href='/uploadFile'">Upload File</button>
  		<button style="display:inline-block;" onClick="location.href='/manageRoles'">Manage Roles</button>
  		
  		<c:forEach items="${logMsgs}" var="msg">
  			<p style="color:${msg.color};">${msg.logMsg}</p>
  		</c:forEach>

  		<div align="right">
	  		<form method="POST">
	  				<select name="sort">
	  					<option value="GWA">GWA</option>
	  					<option value="LastName">LastName</option>
	  					<option value="Hire Date">HireDate</option>
	  				</select>
	  				<select name="asc_desc">
	  					<option value="asc">Ascending</option>
	  					<option value="desc">Descending</option>
	  				</select>
	  				<button style="display:inline-block;" type ="submit" value="" name="sortBtn">Sort</button>
	  				<input type="text" name="search" value ="${param.search}" placeholder="Search by Name">
	  				<button style="display:inline-block;" type ="submit" value="" name="searchBtn">Search</button>
	  				<c:choose>
		  				<c:when test="${empty empList}">
		  					<div align="center"><h2>No Employees Found</h2></div>
		  				</c:when>
		  				<c:otherwise>
		  					<table style="width:100%" border="1">
			  					<tr>
			  						<th align="center">ID</th>
			  						<th align="center">Name</th>
			  						<th align="center">GWA</th>
			  						<th align="center">Hire Date</th>
			  						<th align="center">Actions</th>
			  					</tr>
				  				<c:forEach var="employee" items="${empList}">
				  					<tr>
				  						<td align="center">${employee.empId}</td>
				  						<td align="center">${employee.lastName}, ${employee.firstName} ${employee.middleName} ${employee.suffix}</td>
				  						<td align="center">${employee.gwa}</td>
				  						<td align="center">${employee.hireDate}</td>
				  						<td align="center">
				  							<button style="display:inline-block;" type ="submit" value="${employee.empId}" name="viewEmpBtn">View</button>
				  							<button style="display:inline-block;" type ="submit" value="${employee.empId}" name="delEmpBtn">Delete</button>
				  						</td>
				  					</tr>
				  				</c:forEach>
				  			</table>
			  			</c:otherwise>
	  				</c:choose>
	  		</form>
  		</div>
  	</body>
</html>