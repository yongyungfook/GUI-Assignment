package Servlet;

import java.time.*;
import java.util.Date;
import java.io.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class editProfile extends HttpServlet {

    private Connection conn;
    private PreparedStatement pstmt;
    private String host = "jdbc:derby://localhost:1527/assignmentdb";
    private String user = "nbuser";
    private String password = "nbuser";
    private int errorCount = 0;

    // Initialize variables
    @Override
    public void init() throws ServletException {
        initializeJdbc();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Obtain parameters from the client
        HttpSession httpSession = request.getSession();
        String username = (String)httpSession.getAttribute("username");
        String pass = request.getParameter("password");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        
        
        String birthDay = request.getParameter("birthDate");
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate BirthDate = LocalDate.parse(birthDay, f);
        Date birth = Date.from(BirthDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        java.sql.Date birthDate = new java.sql.Date(birth.getTime());

        try {
            if (pass.length() == 0 || email.length() == 0 || phoneNumber.length() == 0) {
                out.println("Please fill out all the fields!");
                errorCount++;
            }

            if (errorCount == 0) {
                editAccount(pass, birthDate, email, phoneNumber, username);
                httpSession.setAttribute("birthDate", birthDay);
                httpSession.setAttribute("password", pass);
                httpSession.setAttribute("email", email);
                httpSession.setAttribute("phoneNumber", phoneNumber);
                response.sendRedirect("admin/adminT/forms/profile.jsp");
            }
        } catch (SQLException ex) {
            out.println("ERROR: " + ex.getMessage());
        }
    }

    private void initializeJdbc() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection(host, user, password);
            pstmt = conn.prepareStatement("update account set password = ?, birthDate = ?, email = ?, phoneNumber = ? where id = ?");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void editAccount(String pass, java.sql.Date birthDate, String email, String phoneNumber, String username) throws SQLException {
        pstmt.setString(1, pass);
        pstmt.setDate(2, birthDate);
        pstmt.setString(3, email);
        pstmt.setString(4, phoneNumber);
        pstmt.setString(5, username);
        pstmt.executeUpdate();
    }

    public boolean equals(Object obj) {
        return (this == obj);
    }
}