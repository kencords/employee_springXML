<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
	
	<head>
        <meta charset="utf-8">
        <title>Employee Records System: Roles Page</title>
    </head>

    <body>
    	<h1>MANAGE ROLES</h1>

    	<button style="display:inline-block;" onClick="location.href='/home'">Manage Employees</button>

    	<c:forEach items="${logMsgs}" var="msg">
  			<p style="color:${msg.color};">${msg.logMsg}</p>
  		</c:forEach>

  		 <div align="left">
	  		<form method="POST" commandName="role">
	  				<button style="display:inline-block;" type ="submit" value="" name="addRoleBtn">Add Role</button>
	  				<table border="1">
	  					<tr>
	  						<th align="center">ID</th>
	  						<th align="center">Role Name</th>
	  						<th align="center">Action</th>
	  					</tr>
	  					<c:forEach var="role" items="${roleList}">
	  						<tr>
	  							<td align="center">${role.roleId}</td>
	  							<td align="center">${role.roleName}</td>
	  							<td align="center">
	  								<button style="display:inline-block;" type ="submit" value="${role.roleId}" name="editRoleBtn">Edit</button>
	  								<button style="display:inline-block;" type ="submit" value="${role.roleId}" name="delRoleBtn">Delete</button>
	  								<button style="display:inline-block;" type ="submit" value="${role.roleId}" name="showOwnerBtn">Show Owner</button>
	  							</td>
	  						</tr>
	  					</c:forEach>
	  				</table>
	  				<br>
	  				<c:if test="${not empty addRoleField}">
	  					<input type=text name="role_name" value ="" placeholder="Role Name">
	  					<button style="display:inline-block;" type ="submit" value="" name="addNowBtn">Add</button>
	  					<button style="display:inline-block;" type ="submit" value="Add Role Cancelled" name="cancel">Cancel</button>
	  				</c:if>
	  				<c:if test="${not empty role}">
	  					Edit Role ${role.roleName} <br>
	  					<input type=text name="role_name_ed" value ="" placeholder="New Role Name">
	  					<button style="display:inline-block;" type ="submit" value=${role.roleId} name="editNowBtn">Edit</button>
	  					<button style="display:inline-block;" type ="submit" value="Edit Role Cancelled" name="cancel">Cancel</button>
	  				</c:if>
	  				<c:if test="${not empty role_OV}">
	  					<c:choose>
			  				<c:when test="${not empty role_OV.employees}">
			  					<p>Role ${role_OV.roleName} is used by:</p>
			  					<table border="1">
				  					<tr>
				  						<th align="center">ID</th>
				  						<th align="center">Employee Name</th>
				  					</tr>
				  					<c:forEach var="owner" items="${role_OV.employees}">
				  						<tr>
				  							<td align="center">${owner.empId}</td>
				  							<td align="center">${owner.lastName}, ${owner.firstName} ${owner.middleName} ${owner.suffix}</td>
				  						</tr>
				  					</c:forEach>
				  				</table>
			  				</c:when>
			  				<c:otherwise>
			  					<p>Role ${role_OV.roleName} has no owner</p>
			  				</c:otherwise>
		  				</c:choose>
	  				</c:if>
	  		</form>
  		</div>
    </body>

</html>