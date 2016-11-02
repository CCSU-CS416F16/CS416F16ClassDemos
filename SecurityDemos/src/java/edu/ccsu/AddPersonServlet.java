/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ccsu;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author cw1491
 */
@WebServlet(name = "AddPersonServlet", urlPatterns = {"/AddPersonServlet"})
public class AddPersonServlet extends HttpServlet {

    @Resource(name = "jdbc/Lect8aDB")
    private DataSource dataSource;

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            // Connect to database
            Connection connection = dataSource.getConnection();

            // Find the max person id to determine what the next person id should be
            String sql = "select max(id) from person";
            PreparedStatement maxIdSelect = connection.prepareStatement(sql);
            ResultSet results = maxIdSelect.executeQuery();
            results.next();
            int nextId = results.getInt(1) + 1;
            results.close();
            maxIdSelect.close();


            // Add the new person to the DB
            Statement stmt = connection.createStatement();
            // suceptible to XSS
            sql = "insert into person (id,firstname,lastname,admin) values ("+nextId+",'"+firstName+"','"+lastName+"',0)";
            System.out.println("SQL to execute: "+sql);
            stmt.execute(sql);
            
            //PreparedStatement insertPerson = connection.prepareStatement(sql);

                request.getRequestDispatcher("DisplayNames").forward(request, response);
        } catch (Exception e) {
            out.println("Error Occurred "+e.getMessage());
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
