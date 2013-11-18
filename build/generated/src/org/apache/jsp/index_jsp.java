package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.Vector _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n");
      out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
      out.write("    <head>\n");
      out.write("        <title>MyDropBox</title>\n");
      out.write("        <meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\" />\n");
      out.write("        <link rel=\"stylesheet\" type=\"text/css\" href=\"styles/admin.css\" />\n");
      out.write("    </head>\n");
      out.write("    ");
      project02.ServerTomCat server = null;
      synchronized (_jspx_page_context) {
        server = (project02.ServerTomCat) _jspx_page_context.getAttribute("server", PageContext.PAGE_SCOPE);
        if (server == null){
          server = new project02.ServerTomCat();
          _jspx_page_context.setAttribute("server", server, PageContext.PAGE_SCOPE);
        }
      }
      out.write("\n");
      out.write("    ");


        // -------------------------------- PARA O LOGIN -------------------------------- //

        HttpSession usersession = request.getSession(true);

        out.println(request.getParameter("username"));
        out.println(request.getParameter("password"));

        if(usersession.getAttribute("user") != null){
            
          out.println("<jsp:forward page='main.jsp'></jsp:forward>");

        }else{ 
            if (request.getParameter("username") != null && request.getParameter("password") != null && request.getParameter("do") == null) {
                String login = request.getParameter("username");
                String password = request.getParameter("password");

                out.println(login + " " + password);

                String rsp = server.login(login,password);
                if (rsp.contains("@")) {
                    usersession.setAttribute("user", login);
                    usersession.setAttribute("email", rsp); 
                    out.println("<jsp:forward page='main.jsp'></jsp:forward>");
                
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
 
    
      out.write("\n");
      out.write("    <body>\n");
      out.write("        <div class=\"blue\"></div>\n");
      out.write("        <div class=\"box\">\n");
      out.write("    ");
 if(request.getParameter("do") != null){ 
      out.write("\n");
      out.write("\n");
      out.write("        <form method=\"post\" name=\"form-login\">\n");
      out.write("                <div class=\"form\">\n");
      out.write("                    <div class=\"head\">\n");
      out.write("                        MyDropBox - Register\n");
      out.write("                    </div>\n");
      out.write("                    <div class=\"formItem\">\n");
      out.write("                        <input class=\"formText\" type=\"text\" name=\"username\" placeholder=\"Username\"/>\n");
      out.write("                    </div>\n");
      out.write("                    <div class=\"formItem\">\n");
      out.write("                        <input class=\"formText\" type=\"text\" name=\"email\" placeholder=\"email\"/>\n");
      out.write("                    </div>\n");
      out.write("                    <div class=\"formItem\">\n");
      out.write("                        <input class=\"formText\" type=\"password\" name=\"password\" placeholder=\"password\"/>\n");
      out.write("                    </div>\n");
      out.write("\n");
      out.write("                    <div class=\"formItem\">\n");
      out.write("                        <a href=\"index.jsp\" class=\"formSubmit\">Login</a>\n");
      out.write("                        <input class=\"formSubmit h-right\" type=\"submit\" name=\"submit-login\" value=\"Register\" />\n");
      out.write("                    </div>\n");
      out.write("                </div>\n");
      out.write("            </form>\n");
      out.write("\n");
      out.write("    ");
} else {
      out.write("\n");
      out.write("\n");
      out.write("        \n");
      out.write("            <form method=\"post\" name=\"form-login\">\n");
      out.write("                <div class=\"form\">\n");
      out.write("                    <div class=\"head\">\n");
      out.write("                        MyDropBox - Login\n");
      out.write("                    </div>\n");
      out.write("                    <div class=\"formItem\">\n");
      out.write("                        <input class=\"formText\" type=\"text\" name=\"username\" placeholder=\"Username\"/>\n");
      out.write("                    </div>\n");
      out.write("                    <div class=\"formItem\">\n");
      out.write("                        <input class=\"formText\" type=\"password\" name=\"password\" placeholder=\"Palavra-passe\"/>\n");
      out.write("                    </div>\n");
      out.write("                    <div class=\"formItem\">\n");
      out.write("                        <a href=\"index.jsp?do=register\"  type=\"submit\" class=\"formSubmit\">Register</a>\n");
      out.write("                        <input class=\"formSubmit h-right\" type=\"submit\" name=\"submit-login\" value=\"Login\" />\n");
      out.write("                    </div>\n");
      out.write("                </div>\n");
      out.write("            </form>\n");
      out.write("\n");
      out.write("        \n");
      out.write("   ");
 } 
      out.write("\n");
      out.write("        </div>\n");
      out.write("    </body>\n");
      out.write("</html>");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
