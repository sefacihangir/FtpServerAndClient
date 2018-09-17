<%@ page import="java.util.ArrayList" %>
<%@ page import="java.io.File" %>
<%@ page import="java.util.Deque" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="style.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ftp Server And Client</title>
</head>
<body>
<div class="container">
    <%
        ArrayList<File> files = (ArrayList<File>) application.getAttribute("files");
        Deque<File> backFolder = (Deque<File>) application.getAttribute("backFolder");
        System.out.println(application.getAttribute("lastModified"));
        if (application.getAttribute("lastModified") == null) {
            application.setAttribute("lastModified", new File("/home").lastModified());
        } else {
            if ((long)application.getAttribute("lastModified") != new File("/home").lastModified()) {
                out.print("----------CHANGED-------------");
            }
        }
    %>
    <div class="fileList">
            <div class="file">
            <form action="ChangeFolder.do" method="post">
                <input type="hidden" name="path" value="<%=backFolder.peek().getPath()%>">
                <input type="submit" name="file" value="geri">
            </form>
            </div>
            <% for(File file: files){ %>
                <div class="file <%=file.isDirectory() ? "folder" : ""%>" >
                    <% if(file.isDirectory()){%>
                    <form action="ChangeFolder.do" method="post">
                        <input type="hidden" name="path" value="<%=file.getPath()%>">
                        <input type="submit" name="file" value="<%=file.getName()%>">
                    </form>
                    <%}else {%>
                        <p class="pFile" id="<%=file.getPath()%>"><%=file.getName()%></p>
                        <form class="downloadForm" action="Download.do" method="post">
                            <input type="hidden" name="downloadFilePath" value="<%=file.getPath()%>" >
                            <input type="hidden" name="downloadFileName" value="<%=file.getName()%>" >
                            <input type="submit" value="download">
                        </form>
                    <%}%>
                </div>
            <%}%>
    </div>

    <form id="fileUpload" action="Upload.do" method="post" enctype="multipart/form-data">
        <div>
            <p>File: </p>
            <input type="file" name="file">
            <input type="submit" value="Upload">
        </div>
    </form>
    <c:if test="${applicationScope.info != null}">
        <c:if test="${!applicationScope.info.isEmpty()}">
            <div class="info">
                <p>${applicationScope.info}</p>
            </div>
            <c:set var="info" scope="application" value=""/>
        </c:if>
    </c:if>
</div>
</body>
</html>