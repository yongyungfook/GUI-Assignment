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

public class signUpServlets extends HttpServlet {

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
        String id = request.getParameter("id");
        String pass = request.getParameter("password");
        String pass2 = request.getParameter("password2");
        String genderString = request.getParameter("gender");
        char gender = genderString.charAt(0);
        String birthDay = request.getParameter("birthDate");
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate BirthDate = LocalDate.parse(birthDay, f);
        char level = 'C';

        LocalDate RegDate = java.time.LocalDate.now();
        Date reg = Date.from(RegDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date birth = Date.from(BirthDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        java.sql.Date birthDate = new java.sql.Date(birth.getTime());
        java.sql.Date regDate = new java.sql.Date(reg.getTime());
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");

        try {
            if (id.length() == 0 || pass.length() == 0 || gender == ' ' || email.length() == 0 || phoneNumber.length() == 0) {
                out.println("Please fill out all the fields!");
                errorCount++;
            }

            if (errorCount == 0) {
                storeAccount(id, pass, gender, birthDate, regDate, email, phoneNumber, level);
                out.println("Congratulation " + id + ", your registration is successful! ");
                out.println("<a href='admin/adminT/forms/login.jsp'>Go back to login page -></a>");
            }
        } catch (SQLException ex) {
            out.println("ERROR: " + ex.getMessage());
        }
    }

    private void initializeJdbc() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection(host, user, password);
            pstmt = conn.prepareStatement("INSERT INTO Account VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void storeAccount(String id, String pass, char gender, java.sql.Date birthDate, java.sql.Date regDate, String email, String phoneNumber, char level) throws SQLException {
        pstmt.setString(1, id);
        pstmt.setString(2, pass);
        pstmt.setString(3, String.valueOf(gender));
        pstmt.setDate(4, birthDate);
        pstmt.setDate(5, regDate);
        pstmt.setString(6, email);
        pstmt.setString(7, phoneNumber);
        pstmt.setString(8, String.valueOf(level));
        pstmt.executeUpdate();
    }

    public boolean equals(Object obj) {
        return (this == obj);
    }
}