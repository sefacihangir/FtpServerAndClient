package com.serkanerip.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class Download extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("index3.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {

        String filePath = req.getParameter("downloadFilePath");
        String fileName = req.getParameter("downloadFileName");
        fileName = fileName == null ? "" : fileName;

        resp.setContentType("APPLICATION/OCTET-STREAM");
        resp.setHeader("Content-Disposition",
                "attachment;filename="+fileName);

        OutputStream outputStream = null;
        try {

            outputStream = resp.getOutputStream();

            File file = new File(filePath);
            System.out.println("Name: " + file.getPath());
            if(!file.exists() || file.isDirectory()) {
                req.getServletContext().setAttribute("info", "File not exists!");
                resp.sendRedirect("index.jsp");
                return;
            }

            FileInputStream fileInputStream = new FileInputStream(file);

            byte []bytes = new byte[(int)file.length()];

            while( fileInputStream.read(bytes, 0, (int)file.length()) != -1 )
            {
                outputStream.write(bytes, 0, (int)file.length());
            }

            fileInputStream.close();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
