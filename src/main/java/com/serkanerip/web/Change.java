package com.serkanerip.web;

import com.serkanerip.web.helper.GetFilesAndDirectories;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Map;

public class Change extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Deque<File> backFolder = ((Deque<File>)getServletContext().getAttribute("backFolder"));

        if (req.getParameter("file").equalsIgnoreCase("geri")) {
            ArrayList<File> files = GetFilesAndDirectories.checkFolderChanges((Map<File, String>) getServletContext().getAttribute("lastModifies"));
            System.out.println(backFolder.pop().getName());
            System.out.println(backFolder.peek());
            getServletContext().setAttribute("files", GetFilesAndDirectories.getFiles(backFolder.peek().getPath()));
        } else {
            getServletContext().setAttribute("currentFolder", new File(req.getParameter("path")));
            backFolder.push((File)getServletContext().getAttribute("currentFolder"));
            getServletContext().setAttribute("files", GetFilesAndDirectories.getFiles(req.getParameter("path")));

        }
        getServletContext().setAttribute("backFolder", backFolder);
        resp.sendRedirect("index.jsp");
    }
}
