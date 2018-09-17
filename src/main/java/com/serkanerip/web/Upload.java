package com.serkanerip.web;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class Upload extends HttpServlet {

    private static final int MAX_MEMORY_SIZE = 52428800;
    private static final int MAX_REQUEST_SIZE = 52428800;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("index.jsp");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        String uploadFolder = getServletContext().getInitParameter("file-upload");

        if (!isMultipart) {
            return;
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(MAX_MEMORY_SIZE);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload sfu = new ServletFileUpload(factory);
        sfu.setSizeMax(MAX_REQUEST_SIZE);

        try{
            List items = sfu.parseRequest(req);
            Iterator it = items.iterator();

            while(it.hasNext())
            {
                FileItem item = (FileItem) it.next();

                if(!item.isFormField()){
                    String fileName = new File(item.getName()).getName();
                    if (fileName == null || fileName.isEmpty()){
                        getServletContext().setAttribute("info", "Should Select A File!");
                        resp.sendRedirect("index.jsp");
                        return;
                    }
                    String filePath = uploadFolder + File.separator + fileName;
                    File uploadedFile = new File(filePath);
                    item.write(uploadedFile);
                }
            }
            getServletContext().setAttribute("info", "File Uploaded!");
            resp.sendRedirect("index.jsp");
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
