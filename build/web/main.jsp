<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>MyDropBox</title>
        <meta http-equiv="content-type" content="text/html;charset=utf-8" />
        <link rel="stylesheet" type="text/css" href="stylesheets/admin.css" />
        <link rel="stylesheet" type="text/css" href="stylesheets/jsGrowl.css" />
        <link rel="stylesheet" type="text/css" href="stylesheets/jsGrowl_black.css" />
        <script type="text/javascript" src="javascripts/comet.js"> </script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>
        <script src="javascripts/jsGrowl.js" type="text/javascript"></script> 
        <script src="javascripts/jsGrowl_jquery.js" type="text/javascript"></script>   
    </head>

    <script type="text/javascript">
    	
        // Initiate Comet object
        var comet = Comet("http://localhost:8080/Proj2/");  
        var readChars = 0;
        var js_growl = new jsGrowl('jsGrowl'); 
        // Register with Server for COMET callbacks.
        comet.get("taskServlet?type=register", function(response) {
            // updates the message board with the new response.
            var newContent = response.substring(readChars, response.length);
            readChars = response.length;
            
            var response_split = newContent.split("||");

            document.getElementById("tasks").innerHTML = response_split[0];
            document.getElementById("notificacoes").innerHTML = response_split[1];
            js_growl.addMessage({msg:response_split[1],title:'Última operação'}); 
        });

        function addTask() {
            var msg = document.getElementById('shout').value;    		
            //console.log(msg);
            comet.post("taskServlet?type=addTask", msg, function(response) {
                //nothing
            })
            // Clears the value of the message element
            document.getElementById('shout').value = '';
        }   
        
        function deleteTask(task){
            
            comet.post("taskServlet?type=deleteTask",task,function(response){
                                document.getElementById("tasks").innerHTML = '';

            })
        }
        
        function editTask(task){
            var inputid = "tarefa"+task;
            task = task+"|"+document.getElementById(inputid).value;
            comet.post("taskServlet?type=editTask",task,function(response){
                                document.getElementById("tasks").innerHTML = '';

            })
        }
        //This makes the browser call the quitChat function before unloading(or closing) the page
        //window.onunload = quitChat;
    </script>
    <jsp:useBean id="server" class="project02.ServerTomCat" />
    <%

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

    %>

    <body>        
        <div class="blue"></div>

        <div class="wrap-header">
            <div class="header">
                <div class="menu">
                    MyDropbox - Welcome <%= usersession.getAttribute("user")%>
                </div>
                <div class="menu" style="float:right">
                    <a href="main.jsp?do=logout">logout</a>
                </div>
            </div>
        </div>
        <div id="jsGrowl"></div>         
                
        <div class="content">
            <div class="box" style="width: 688px; overflow: auto; float:left">                   
                <div class="head">Tarefas</div>
                <div class="body">
                    <div id="tasks">

                        <!-- ajax goes here -->          
                    </div>
                </div>
            </div>
        <div class="box" style="width: 200px; overflow: auto; float:right">   

            <iframe name="online" src="online_users.jsp" frameBorder="0" width="200" height="242" scrolling="auto"/></iframe>
            <div class="head">Actividade</div>
                <div class="body" style="height: 208px; overflow: hidden;">
                    <div class="list list-32" style="border: 0;">
                        <div class="list-info">
                            <div id="notificacoes">
                                <!-- ajax goes here -->
                            </div>
                        </div>
                    </div>
                </div>
        </div>
        <div class="shout" style="float:left">
                <form onSubmit="addTask()">
                    <input id="shout" type="text" class="txt-shout" style="width: 500px; margin-top:10px;" name="message" value="" autocomplete="off"/>
                    <input  class="formSubmit form-green" type="submit" name="submit-message" value="Enviar"/>
                </form>
            </div>   
        </div>    
    </body>                       
</html>