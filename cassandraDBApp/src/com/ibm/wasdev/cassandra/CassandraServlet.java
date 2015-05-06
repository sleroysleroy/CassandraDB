/*
 * COPYRIGHT LICENSE: This information contains sample code provided in source code form.
 * You may copy, modify, and distribute these sample programs in any form without payment
 * to IBM for the purposes of developing, using, marketing or distributing application
 * programs conforming to the application programming interface for the operating platform
 * for which the sample code is written.
 * 
 * Notwithstanding anything to the contrary, IBM PROVIDES THE SAMPLE SOURCE CODE ON 
 * AN "AS IS" BASIS AND IBM DISCLAIMS ALL WARRANTIES, EXPRESS OR IMPLIED, INCLUDING,
 * BUT NOT LIMITED TO, ANY IMPLIED WARRANTIES OR CONDITIONS OF MERCHANTABILITY,
 * SATISFACTORY QUALITY, FITNESS FOR A PARTICULAR PURPOSE, TITLE, AND ANY WARRANTY OR
 * CONDITION OF NON-INFRINGEMENT. IBM SHALL NOT BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE USE OR OPERATION OF
 * THE SAMPLE SOURCE CODE. IBM HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT,
 * UPDATES, ENHANCEMENTS OR MODIFICATIONS TO THE SAMPLE SOURCE CODE.
 * 
 * (C) Copyright IBM Corp. 2014.
 * All Rights Reserved. Licensed Materials - Property of IBM.
 */
package com.ibm.wasdev.cassandra;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// @WebServlet("/")
public class CassandraServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    ConnectionManager connMgr;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        String host = request.getParameter("host");
        String action = request.getParameter("action");
        PrintWriter writer = response.getWriter();

        // To create and initialize the connection

        HashMap<String, String> items = null;
        try {
            connMgr = ConnectionManager.getInstance(host);

            if ("Add".equalsIgnoreCase(action)) {
                //Adding items via ConnectionManager
                items = connMgr.addItem(request.getParameter("item"), request.getParameter("price"), request.getParameter("trend"));
                System.out.println("Item '" + request.getParameter("item") + "' is added successfully");
            } else {
                items = connMgr.removeItem(request.getParameter("item"));
                response.getWriter().println("The item " + request.getParameter("item") + " removed successfully");
            }

            request.setAttribute("items", items);
            RequestDispatcher rd = request.getRequestDispatcher("/ItemManagement.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            response.getWriter().println(e.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.GenericServlet#destroy()
     */
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        super.destroy();
        if (connMgr != null)
            connMgr.shutdown();
    }
}
