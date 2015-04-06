package src.chooser;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public final class Files {


    public static final String root = Environment.getExternalStorageDirectory().getAbsolutePath();


    public static ArrayList<String> loadItems(String path) {

        File[] files;
        File folder;


        ArrayList<String> arrayItems = new ArrayList<>();
        arrayItems.add("..");

        folder = new File(path);
        if (folder.exists()) {
            files = folder.listFiles();
            Arrays.sort(files);

            for (File file : files) {
                if (file.isDirectory()) {
                    arrayItems.add(file.getName());

                }
            }
        }
        return arrayItems;

    }

    public static String getPathPreviousFolder(String path) {

        File folder = new File(path);
        String previousPath = root;

        if (!path.equals(root))
            previousPath = folder.getParentFile().toString();


        return previousPath;

    }


    public static void addFolder(String path) {

        File file = new File(path);

        if (!file.isDirectory()) {
            file.mkdirs();
        }
    }


}