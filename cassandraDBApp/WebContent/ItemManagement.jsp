<!-- 
 COPYRIGHT LICENSE: This information contains sample code provided in source code form.
 You may copy, modify, and distribute these sample programs in any form without payment
 to IBM for the purposes of developing, using, marketing or distributing application
 programs conforming to the application programming interface for the operating platform
 for which the sample code is written.

 Notwithstanding anything to the contrary, IBM PROVIDES THE SAMPLE SOURCE CODE ON 
 AN "AS IS" BASIS AND IBM DISCLAIMS ALL WARRANTIES, EXPRESS OR IMPLIED, INCLUDING,
 BUT NOT LIMITED TO, ANY IMPLIED WARRANTIES OR CONDITIONS OF MERCHANTABILITY,
 SATISFACTORY QUALITY, FITNESS FOR A PARTICULAR PURPOSE, TITLE, AND ANY WARRANTY OR
 CONDITION OF NON-INFRINGEMENT. IBM SHALL NOT BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE USE OR OPERATION OF
 THE SAMPLE SOURCE CODE. IBM HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT,
 UPDATES, ENHANCEMENTS OR MODIFICATIONS TO THE SAMPLE SOURCE CODE.

 (C) Copyright IBM Corp. 2014.
 All Rights Reserved. Licensed Materials - Property of IBM.
-->
<%@ page import="java.util.HashMap"%>
<html>
<head>
<title>Cassandra Sample</title>
</head>
<body>
	<h2>Item Management</h2>
	<form name="ItemMgmt" method="get" action="cassandraDBApp">
		<label for="item"> Item : </label> <input type="text" name="item"
			size="25">
		<p></p>
		<label for="price">Price:</label> <input type="text" name="price"
			size="25">
		<p></p>
		
		<input type="submit" name="action" value= "Add"> 
		<input type="submit" name="action" value= "Remove"> 
		<input type="reset"	value="Reset">
	</form>

<%
	HashMap<String, String> items = (HashMap<String, String>) request.getAttribute("items");
	if (items != null && !items.isEmpty()) {
%>
	<table width="50%" border="1">
		<tr bgcolor="#AAAAAAA">
			<th>Item</th>
			<th>Price</th>
		</tr>
		<%
		    for(String key : items.keySet()) {
        		;
        		out.print("<tr><td>" + key + "</td>\n");
					out.println("<td> " + items.get(key)
							+ "</td></tr>\n");
        	}
		 %>
	</table>
	<% } %>
</body>
</html>
