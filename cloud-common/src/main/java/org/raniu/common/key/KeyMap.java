package org.raniu.common.key;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.common.key
 * @className: KeyMap
 * @author: Raniu
 * @description: TODO
 * @date: 2024/5/17 17:04
 * @version: 1.0
 */
public class KeyMap {
    public static final String PUBLIC_KEY;
    public static final String PRIVATE_KEY;
    static {
        try {
            PUBLIC_KEY = Files.readString(Paths.get("/www/wwwroot/cloud/public_key.txt"));
            PRIVATE_KEY = Files.readString(Paths.get("/www/wwwroot/cloud/private_key.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
