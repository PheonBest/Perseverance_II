import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import java.awt.Image;

/**
 * list resources available from the classpath @ *
 */
public class ObtenirRessources{

    public static List<String> getFilenamesForDirnameFromCP(Pattern pattern, String directoryName) throws URISyntaxException, UnsupportedEncodingException, IOException {
        List<String> filenames = new ArrayList<>();
        
        URL url = Thread.currentThread().getContextClassLoader().getResource(directoryName);
        if (url != null) {
            if (url.getProtocol().equals("file")) {
                File file = Paths.get(url.toURI()).toFile();
                if (file != null) {
                    File[] files = file.listFiles();
                    System.out.println(files.length);
                    if (files != null) {
                        for (File filename : files) {
                            final boolean accept = pattern.matcher(filename.toString()).matches();
                            if(accept){
                                System.out.println(filename.toString());
                                filenames.add(filename.toString());
                            }
                        }
                    }
                }
            } else if (url.getProtocol().equals("jar")) {
                String dirname = directoryName + "/";
                String path = url.getPath();
                String jarPath = path.substring(5, path.indexOf("!"));
                try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8.name()))) {
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if (!dirname.equals(name)) {
                        //if (name.startsWith(dirname) && !dirname.equals(name)) {
                            URL resource = Thread.currentThread().getContextClassLoader().getResource(name);
                            final boolean accept = pattern.matcher(resource.toString()).matches();
                            if(accept){
                                System.out.println(resource.toString());
                                filenames.add(resource.toString());
                            }
                        }
                    }
                }
            }
        }
        return filenames;
    }

    public static List<Image> getImages(Pattern pattern, String directoryName) throws URISyntaxException, UnsupportedEncodingException, IOException {
        List<Image> images = new ArrayList<>();
        
        URL url = Thread.currentThread().getContextClassLoader().getResource(directoryName);
        if (url != null) {
            if (url.getProtocol().equals("file")) {
                final File file = Paths.get(url.toURI()).toFile();
                if (file != null) {
                    File[] files = file.listFiles();
                    if (files != null) {
                        for (File filename : files) {
                            final boolean accept = pattern.matcher(filename.toString()).matches();
                            if (accept) {
                                //System.out.println(filename.toString());
                                final Image img = ImageIO.read( new FileInputStream(filename));
                                images.add(img);
                            }
                        }
                    }
                }
            } else if (url.getProtocol().equals("jar")) {
                String dirname = directoryName + "/";
                String path = url.getPath();
                String jarPath = path.substring(5, path.indexOf("!"));
                try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8.name()))) {
                    Enumeration<JarEntry> entries = jar.entries();
                    List<JarEntry> sortedEntries = Collections.list(entries);
                    sortedEntries.sort(new Comparator<JarEntry>(){
                        @Override
                        public int compare(JarEntry o1, JarEntry o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                    for(JarEntry entry: sortedEntries) {
                        String name = entry.getName();
                        if (!dirname.equals(name)) {
                        //if (name.startsWith(dirname) && !dirname.equals(name)) {
                            URL resource = Thread.currentThread().getContextClassLoader().getResource(name);
                            final boolean accept = pattern.matcher(resource.toString()).matches();
                            if (accept) {
                                System.out.println(resource.toString());
                                images.add(ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream(name)));
                            }
                        }
                    }
                }
            }
        }
        return images;
    }
}  