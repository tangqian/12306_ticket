/*
 * 文 件 名:  ResManager.java
 * 创 建 人:  tangqian
 * 创建时间:  2014-11-19
 */
package com.free.app.ticket.util;

import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

/**
 * 资源管理器
 */
public class ResManager {
    
    private static final String IMAGES_PATH = "/images/";
    
    private static final ResourceBundle TEXT_BUNDLE = ResourceBundle.getBundle("text");
    
    public static String getText(String key) {
        try {
            return TEXT_BUNDLE.getString(key);
        }
        catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
    
    public static ImageIcon createImageIcon(String filename) {
        return new ImageIcon(getFileURL(filename));
    }
    
    private static URL getFileURL(String filename) {
        String path = IMAGES_PATH + filename;
        return ResManager.class.getResource(path);
    }
}
