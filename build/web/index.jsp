<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>MyDropBox</title>
        <meta http-equiv="content-type" content="text/html;charset=utf-8" />
        <link rel="stylesheet" type="text/css" href="stylesheets/admin.css" />
    </head>
    <jsp:useBean id="server" class="project02.ServerTomCat" />
    <%

        // -------------------------------- PARA O LOGIN -------------------------------- //

        HttpSession usersession = request.getSession(true);
        RequestDispatcher dispatcher;

        out.println(request.getParameter("username"));
        out.println(request.getParameter("password"));

        if(usersession.getAttribute("user") != null){
                    dispatcher = request.getRequestDispatcher("main.jsp");
                    dispatcher.forward(request, response);
        }else{ 
            if (request.getParameter("username") != null && request.getParameter("password") != null && request.getParameter("do") == null) {
                String login = request.getParameter("username");
                String password = request.getParameter("password");

                out.println(login + " " + password);

                String rsp = server.login(login,password);
                if (rsp.contains("@")) {
                    usersession.setAttribute("user", login);
                    usersession.setAttribute("email", rsp); 
                    dispatcher = request.getRequestDispatcher("main.jsp");
                    dispatcher.forward(request, response);
                    
                } else {
                    out.println(rsp);   
                }  
            }
        }
        

        // -------------------------------- PARA O REGISTO -------------------------------- //

        if (request.getParameter("username") != null && request.getParameter("email") != null && request.getParameter("password") != null) {
            String login = request.getParameter("username");
            String password = request.getParameter("password");
            String email = request.getParameter("email");

            String rsp = server.registo(login, password, email);

            if(rsp.compareTo("accept")==0)
                        out.println(rsp);
            else
                        out.println(rsp);
        }
 
    %>
    <body>
        <div class="blue"></div>
        <div class="box">
    <% if(request.getParameter("do") != null){ %>

        <form method="post" name="form-login">
                <div class="form">
                    <div class="head">
                        MyDropBox - Register
                    </div>
                    <div class="formItem">
                        <input class="formText" type="text" name="username" placeholder="Username"/>
                    </div>
                    <div class="formItem">
                        <input class="formText" type="text" name="email" placeholder="email"/>
                    </div>
                    <div class="formItem">
                        <input class="formText" type="password" name="password" placeholder="password"/>
                    </div>

                    <div class="formItem">
                        <a href="index.jsp" class="formSubmit">Login</a>
                        <input class="formSubmit h-right" type="submit" name="submit-login" value="Register" />
                    </div>
                </div>
            </form>

    <%} else {%>

        
            <form method="post" name="form-login">
                <div class="form">
                    <div class="head">
                        MyDropBox - Login
                    </div>
                    <div class="formItem">
                        <input class="formText" type="text" name="username" placeholder="Username"/>
                    </div>
                    <div class="formItem">
                        <input class="formText" type="password" name="password" placeholder="Palavra-passe"/>
                    </div>
                    <div class="formItem">
                        <a href="index.jsp?do=register"  type="submit" class="formSubmit">Register</a>
                        <input class="formSubmit h-right" type="submit" name="submit-login" value="Login" />
                    </div>
                </div>
            </form>

        
   <% } %>
        </div>
    </body>
</html>