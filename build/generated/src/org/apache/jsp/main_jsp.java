package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class main_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      out.write("        <link rel=\"stylesheet\" type=\"text/css\" href=\"stylesheets/admin.css\" />\n");
      out.write("        <link rel=\"stylesheet\" type=\"text/css\" href=\"stylesheets/jsGrowl.css\" />\n");
      out.write("        <link rel=\"stylesheet\" type=\"text/css\" href=\"stylesheets/jsGrowl_black.css\" />\n");
      out.write("        <script type=\"text/javascript\" src=\"javascripts/comet.js\"> </script>\n");
      out.write("        <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js\" type=\"text/javascript\"></script>\n");
      out.write("        <script src=\"javascripts/jsGrowl.js\" type=\"text/javascript\"></script> \n");
      out.write("        <script src=\"javascripts/jsGrowl_jquery.js\" type=\"text/javascript\"></script>   \n");
      out.write("    </head>\n");
      out.write("\n");
      out.write("    <script type=\"text/javascript\">\n");
      out.write("    \t\n");
      out.write("        // Initiate Comet object\n");
      out.write("        var comet = Comet(\"http://localhost:8080/Proj2/\");  \n");
      out.write("        var readChars = 0;\n");
      out.write("        var js_growl = new jsGrowl('jsGrowl'); \n");
      out.write("        // Register with Server for COMET callbacks.\n");
      out.write("        comet.get(\"taskServlet?type=register\", function(response) {\n");
      out.write("            // updates the message board with the new response.\n");
      out.write("            var newContent = response.substring(readChars, response.length);\n");
      out.write("            readChars = response.length;\n");
      out.write("            \n");
      out.write("            var response_split = newContent.split(\"||\");\n");
      out.write("\n");
      out.write("            document.getElementById(\"tasks\").innerHTML = response_split[0];\n");
      out.write("            document.getElementById(\"notificacoes\").innerHTML = response_split[1];\n");
      out.write("            js_growl.addMessage({msg:response_split[1],title:'Última operação'}); \n");
      out.write("        });\n");
      out.write("\n");
      out.write("        function addTask() {\n");
      out.write("            var msg = document.getElementById('shout').value;    \t\t\n");
      out.write("            //console.log(msg);\n");
      out.write("            comet.post(\"taskServlet?type=addTask\", msg, function(response) {\n");
      out.write("                //nothing\n");
      out.write("            })\n");
      out.write("            // Clears the value of the message element\n");
      out.write("            document.getElementById('shout').value = '';\n");
      out.write("        }   \n");
      out.write("        \n");
      out.write("        function deleteTask(task){\n");
      out.write("            \n");
      out.write("            comet.post(\"taskServlet?type=deleteTask\",task,function(response){\n");
      out.write("                                document.getElementById(\"tasks\").innerHTML = '';\n");
      out.write("\n");
      out.write("            })\n");
      out.write("        }\n");
      out.write("        \n");
      out.write("        function editTask(task){\n");
      out.write("            var inputid = \"tarefa\"+task;\n");
      out.write("            task = task+\"|\"+document.getElementById(inputid).value;\n");
      out.write("            comet.post(\"taskServlet?type=editTask\",task,function(response){\n");
      out.write("                                document.getElementById(\"tasks\").innerHTML = '';\n");
      out.write("\n");
      out.write("            })\n");
      out.write("        }\n");
      out.write("        //This makes the browser call the quitChat function before unloading(or closing) the page\n");
      out.write("        //window.onunload = quitChat;\n");
      out.write("    </script>\n");
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


        // -------------------------------- CHECK -------------------------------- //

        HttpSession usersession = request.getSession(true);
        RequestDispatcher dispatcher;

        if (request.getParameter("do") != null) {
            String name = (String) request.getSession().getAttribute("user");
            server.logout(name);
            usersession.setAttribute("user", null);
            usersession.setAttribute("email", null);
        }

        if (usersession.getAttribute("user") == null) {
            dispatcher = request.getRequestDispatcher("index.jsp");
            dispatcher.forward(request, response);
        }        

    
      out.write("\n");
      out.write("\n");
      out.write("    <body>        \n");
      out.write("        <div class=\"blue\"></div>\n");
      out.write("\n");
      out.write("        <div class=\"wrap-header\">\n");
      out.write("            <div class=\"header\">\n");
      out.write("                <div class=\"menu\">\n");
      out.write("                    MyDropbox - Welcome ");
      out.print( usersession.getAttribute("user"));
      out.write("\n");
      out.write("                </div>\n");
      out.write("                <div class=\"menu\" style=\"float:right\">\n");
      out.write("                    <a href=\"main.jsp?do=logout\">logout</a>\n");
      out.write("                </div>\n");
      out.write("            </div>\n");
      out.write("        </div>\n");
      out.write("        <div id=\"jsGrowl\"></div>         \n");
      out.write("                \n");
      out.write("        <div class=\"content\">\n");
      out.write("            <div class=\"box\" style=\"width: 688px; overflow: auto; float:left\">                   \n");
      out.write("                <div class=\"head\">Tarefas</div>\n");
      out.write("                <div class=\"body\">\n");
      out.write("                    <div id=\"tasks\">\n");
      out.write("\n");
      out.write("                        <!-- ajax goes here -->          \n");
      out.write("                    </div>\n");
      out.write("                </div>\n");
      out.write("            </div>\n");
      out.write("        <div class=\"box\" style=\"width: 200px; overflow: auto; float:right\">   \n");
      out.write("\n");
      out.write("            <iframe name=\"online\" src=\"online_users.jsp\" frameBorder=\"0\" width=\"200\" height=\"242\" scrolling=\"auto\"/></iframe>\n");
      out.write("            <div class=\"head\">Actividade</div>\n");
      out.write("                <div class=\"body\" style=\"height: 208px; overflow: hidden;\">\n");
      out.write("                    <div class=\"list list-32\" style=\"border: 0;\">\n");
      out.write("                        <div class=\"list-info\">\n");
      out.write("                            <div id=\"notificacoes\">\n");
      out.write("                                <!-- ajax goes here -->\n");
      out.write("                            </div>\n");
      out.write("                        </div>\n");
      out.write("                    </div>\n");
      out.write("                </div>\n");
      out.write("        </div>\n");
      out.write("        <div class=\"shout\" style=\"float:left\">\n");
      out.write("                <form onSubmit=\"addTask()\">\n");
      out.write("                    <input id=\"shout\" type=\"text\" class=\"txt-shout\" style=\"width: 500px; margin-top:10px;\" name=\"message\" value=\"\" autocomplete=\"off\"/>\n");
      out.write("                    <input  class=\"formSubmit form-green\" type=\"submit\" name=\"submit-message\" value=\"Enviar\"/>\n");
      out.write("                </form>\n");
      out.write("            </div>   \n");
      out.write("        </div>    \n");
      out.write("    </body>                       \n");
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
