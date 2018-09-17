package com.serkanerip.web.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class GetFilesAndDirectories {

    private final static Map<File, ArrayList<File>> filesAndDirectories = new HashMap<>();
    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void getFiles(File file) {
        ArrayList<File> files = new ArrayList<>();
        try{
            for (File f: file.listFiles()) {
                if (f.isDirectory()){
                    Thread t = new Thread(() -> getFiles(f));
                    executorService.execute(t);
                }
                files.add(f);
            }
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        synchronized (filesAndDirectories){
            filesAndDirectories.put(file, files);
        };

    }

    public static ArrayList<File> getFiles(String folderName)
    {
        return filesAndDirectories.get(new File(folderName));
    }

    public static Map<File, ArrayList<File>>getAllFiles(String BASEPATH) throws InterruptedException {
        filesAndDirectories.clear();
        long start = System.currentTimeMillis();
        getFiles(new File(BASEPATH));
        if (!executorService.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
            executorService.shutdownNow();
            if (!executorService.awaitTermination(1000, TimeUnit.MILLISECONDS))
                System.err.println("Pool did not terminate");
        }
        System.out.println("Finished in " + (System.currentTimeMillis() - start)/1000.0 + " seconds");
        return filesAndDirectories;
    }

    public static ArrayList<File> checkFolderChanges(Map<File, String> lastModifies)
    {
        ArrayList<File> changedFolders = new ArrayList<>();
        lastModifies.forEach( (f, s) -> {
            if (!(f.lastModified()+"").equals(s)) {
                changedFolders.add(f);
            }
        });
        return changedFolders;
    }

    public static void updateFolders(ArrayList<File> files)
    {
        if (files.size() > 0)
            files.forEach(f -> {
                filesAndDirectories.get(f).clear();
                for (File sb : f.listFiles()) {
                    filesAndDirectories.get(f).add(sb);
                }
            });
    }

}
