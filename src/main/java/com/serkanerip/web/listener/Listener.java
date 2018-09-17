package com.serkanerip.web.listener;

import com.serkanerip.web.helper.GetFilesAndDirectories;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.annotation.WebListener;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionBindingEvent;
import java.io.File;
import java.util.*;

@WebListener()
public class Listener implements ServletContextListener,
        HttpSessionListener, HttpSessionAttributeListener, ServletRequestListener {

    // Public constructor is required by servlet spec
    public Listener() {
    }

    public void contextInitialized(ServletContextEvent sce) {
        try {
            String basePath = sce.getServletContext().getInitParameter("base-path");
            Map<File, ArrayList<File>> aq = GetFilesAndDirectories.getAllFiles(basePath);
            Map<File, String> lastModifies = new HashMap<>();

            aq.forEach((f,l) -> lastModifies.put(f, f.lastModified()+""));


            ArrayList<File> files = GetFilesAndDirectories.getFiles(basePath);
            sce.getServletContext().setAttribute("files", files);
            sce.getServletContext().setAttribute("currentFolder", new File(basePath));
            sce.getServletContext().setAttribute("lastModifies", lastModifies);
            Deque<File> backFolder = new ArrayDeque<>();
            backFolder.push((File)sce.getServletContext().getAttribute("currentFolder"));
            sce.getServletContext().setAttribute("backFolder", backFolder);



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
//        if (sre.getServletContext().getAttribute("lastModifies") != null) {
//            Map<File, String> lastModifies = (Map<File, String>) sre.getServletContext().getAttribute("lastModifies");
//            System.out.println(lastModifies.size());
//            ArrayList<File> changedFolders = GetFilesAndDirectories.checkFolderChanges(lastModifies);
//            GetFilesAndDirectories.updateFolders(changedFolders);
//        }
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {

    }

    public void contextDestroyed(ServletContextEvent sce) {

    }

    public void sessionCreated(HttpSessionEvent se) {

    }

    public void sessionDestroyed(HttpSessionEvent se) {

    }

    public void attributeAdded(HttpSessionBindingEvent sbe) {

    }

    public void attributeRemoved(HttpSessionBindingEvent sbe) {

    }

    public void attributeReplaced(HttpSessionBindingEvent sbe) {

    }
}
