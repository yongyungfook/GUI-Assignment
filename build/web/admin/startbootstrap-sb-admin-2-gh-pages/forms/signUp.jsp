
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@page import="domain.Account" %>
<jsp:useBean id="account"
             class="domain.Account" scope="session"></jsp:useBean>
<jsp:setProperty name="accountId" property="*" />

<jsp:useBean id="AccountDAId" 
             class="da.AccountDA" scope="application">
</jsp:useBean>

<html>
    <body>
        <%
        
            if(!(accountDAId.addRecord(account))) {

            out.println("Congratulation " + account.getId() + ", your registration is successful! ");

            }
        %>

        <a href="signIn.html">Back to login</a>
    </body>
</html>