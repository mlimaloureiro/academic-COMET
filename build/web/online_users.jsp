<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<meta http-equiv="content-type" content="text/html;charset=utf-8" />
<link rel="stylesheet" type="text/css" href="stylesheets/admin.css" />
<jsp:useBean id="server" class="project02.ServerTomCat" />
<META HTTP-EQUIV=Refresh CONTENT="10; URL=http://localhost:8080/Proj2/online_users.jsp">

<div class="head">Online Users</div>
<div class="body" style="height: 208px; overflow: hidden;">


<!-- vai buscar com o jsp o tamanho do array de online users

<div id="empty" class="list list-32" style="border: 0; display: none;">
    <div class="list-info">
        <div class="list-head">No online users</div>
        <div class="list-body">The are no online users</div>
    </div>
</div>

-->
<% 
    String data = server.online(); 
    String dados[] = data.split("\\|");
    
    if(dados[1].equals("vazio")){

%>
    <div class="list list-32" style="border: 0;">
        <div class="list-info">
            <div class="list-head">No users online</div>
            <div class="list-body">There are no logged in users</div>
        </div>
    </div>

<% } else { 
        
    for(int k=1;k<dados.length;k++){
        
        out.println("<ul id='result' class='result'>"
                + "<li> "
                +   "<div class='list list-32' style='border: 0; min-heigth:5px;'>"
                +       "<div class='list-info'>"
                +           "<div class='list-body' style='padding:5px 15px;'>"+ dados[k] +"</div>" 
                +       "</div>"
                +   "</div>"
                + "</li>"
        );                                     
    }
 } %>

</div>

