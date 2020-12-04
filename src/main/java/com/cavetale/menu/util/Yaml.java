package com.cavetale.menu.util;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public final class Yaml {
    private Yaml() { }

    public static <E> E load(File file, Class<E> clazz) {
        Gson gson = new Gson();
        org.yaml.snakeyaml.Yaml yaml = new org.yaml.snakeyaml.Yaml();
        Object o;
        try (FileReader in = new FileReader(file)) {
            o = yaml.load(in);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
        String tmp = gson.toJson(o);
        return gson.fromJson(tmp, clazz);
    }
}
