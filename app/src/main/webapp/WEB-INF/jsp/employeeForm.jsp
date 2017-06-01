<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
	<head>
        <meta charset="utf-8">
        <title>Employee Records System: Add Employee</title>
    </head>

  	<body>
  		<h1>ADD EMPLOYEE</h1>
  		<div style="border:1px solid black; width:500px; margin: 0 auto; text-align:center;">
  			<c:forEach items="${logMsgs}" var="msg">
	  			<p style="color:${msg.color};">${msg.logMsg}</p>
	  		</c:forEach>
  			<form method="POST">
  				<h3>PERSONAL INFORMATION</h3>
  				<input type="text" name="title" value ="${param.title}" placeholder="Title"><br>
  				<input type="text" name="lastName" value ="${param.lastName}" placeholder="Last Name*"><br>
  				<input type="text" name="firstName" value ="${param.firstName}" placeholder="First Name*"><br>
  				<input type="text" name="middleName" value ="${param.middleName}" placeholder="Middle Name*"><br>
  				<input type="text" name="suffix" value ="${param.suffix}" placeholder="Suffix"><br>
  				<input type="text" name="bDate" value ="${param.bDate}" placeholder="Birthdate (yyyy-mm-dd)*"><br>
  				<input type=text name="gwa" value ="${param.gwa}" placeholder="Gwa"><br>
  				<h3>ADDRESS</h3>
  				<input type="text" name="strNo" value ="${param.strNo}" placeholder="Street Number*"><br>
  				<input type="text" name="street" value ="${param.street}" placeholder="Street*"><br>
  				<input type="text" name="brgy" value ="${param.brgy}" placeholder="Barangay*"><br>
  				<input type="text" name="city" value ="${param.city}" placeholder="City*"><br>
  				<input type="text" name="zipcode" value ="${param.zipcode}" placeholder="Zipcode*"><br>
  				
  				<h3>CONTACTS</h3>
  				<c:forEach var="contact" items="${contacts}" varStatus="loop">
  					<input type="text" name="${contact.contactType}${loop.index}" value="${contact.contactType}" readonly>
  					<c:choose>
  						<c:when test="${page=='add'}">
  							<input type="text" name="${contact.contactValue}${loop.index}" value="${contact.contactValue}" readonly>
  						</c:when>
  						<c:otherwise>
  							<input type="text" name="contact${loop.index}" value ="${contact.contactValue}" placeholder="Enter Contact">
  							<button style="display:inline-block;" type ="submit" value="${loop.index}" name="updateConBtn">UPDATE</button>
  						</c:otherwise>
  					</c:choose>
  					<button style="display:inline-block;" type ="submit" value="${loop.index}" name="delConBtn">DELETE</button><br>
  				</c:forEach>
  				<select name="conOpt">
	  				<option value="Landline (xxx-xxxx)">Landline (xxx-xxxx)</option>
	  				<option value="Mobile (xxxx-xxx-xxxx)">Mobile (xxxx-xxx-xxxx)</option>
	  				<option value="Email">Email</option>
	  			</select>
	  			<input type="text" name="contact" value ="" placeholder="Enter Contact">
	  			<button style="display:inline-block;" type="submit" value="" name="addContactBtn">ADD</button><br>

	  			<h3>CAREER INFORMATION</h3>
	  			<p style="display:inline-block;">Currently Hired? &nbsp;</p>
	  			<select name="currentlyHired">
	  				<option value="YES">YES</option>
	  				<option value="NO">NO</option>
	  			</select><br>
	  			<input type="text" name="hDate" value ="${param.hDate}" placeholder="Hire Date (yyyy-mm-dd)*"><br>

	  			<h3>ROLES</h3>
	  			<c:forEach var="role" items="${roles}" varStatus="loop">
	  				<input type="text" name="${role.roleName}${loop.index}" value="${role.roleName}" readonly>
	  				<button style="display:inline-block;" type ="submit" value="${loop.index}" name="delRoleBtn">DELETE</button><br>
	  			</c:forEach>
	  			<c:if test="${not empty availRoles}">
            <select name="roleOpt">
  		  			<c:forEach var="availRole" items="${availRoles}">
  		  				<option value="${availRole.roleId}">${availRole.roleName}</option>
  		  			</c:forEach>
  	  			</select>
	  			  <button style="display:inline-block;" type ="submit" value="" name="addRoleBtn">ADD</button>
          </c:if>
	  			<br><br><button style="display:inline-block;" type ="submit" value="" name="saveEmployeeBtn">SAVE EMPLOYEE</button>
  			</form>
        <button style="display:inline-block;" onClick="location.href='/home'">BACK</button><br><br>
  		</div>
  	</body>
 </html>