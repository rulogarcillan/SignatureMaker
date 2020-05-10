/*
 __ _                   _                                 _
/ _(_) __ _ _ __   __ _| |_ _   _ _ __ ___    /\/\   __ _| | _____ _ __
\ \| |/ _` | '_ \ / _` | __| | | | '__/ _ \  /    \ / _` | |/ / _ \ '__|
_\ \ | (_| | | | | (_| | |_| |_| | | |  __/ / /\/\ \ (_| |   <  __/ |
\__/_|\__, |_| |_|\__,_|\__|\__,_|_|  \___| \/    \/\__,_|_|\_\___|_|
      |___/

Copyright (C) 2018  Raúl Rodríguez Concepción www.wepica.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/
package src.chooser;

import android.os.Environment;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

final class Files {


    static final String root = Environment.getExternalStorageDirectory().getAbsolutePath();

    static void addFolder(String path) {
        File file = new File(path);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
    }

    static String getPathPreviousFolder(String path) {

        File folder = new File(path);
        String previousPath = root;
        if (!path.equals(root)) {
            previousPath = folder.getParentFile().toString();
        }
        return previousPath;
    }

    static ArrayList<String> loadItems(String path) {
        File[] files;
        File folder;
        ArrayList<String> arrayItems = new ArrayList<>();
        arrayItems.add("..");

        folder = new File(path);
        if (folder.exists()) {
            files = folder.listFiles();
            if (files != null) {
                Arrays.sort(files);
                for (File file : files) {
                    if (file.isDirectory()) {
                        arrayItems.add(file.getName());
                    }
                }
            }

        }
        return arrayItems;

    }
}