import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.awt.Image;

/**
 * list resources available from the classpath @ *
 */
public class ObtenirRessources{

    public static List<String> getFilenames(Pattern pattern, String directoryName) throws URISyntaxException, UnsupportedEncodingException, IOException {
        List<String> filenames = new ArrayList<>();
        
        URL url = Thread.currentThread().getContextClassLoader().getResource(directoryName);
        if (url != null) {
            if (url.getProtocol().equals("file")) {
                File file = Paths.get(url.toURI()).toFile();
                if (file != null) {
                    File[] files = file.listFiles();
                    if (files != null) {
                        for (File filename : files) {
                            final boolean accept = pattern.matcher(filename.toString()).matches();
                            if(accept){
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
                                filenames.add(resource.toString());
                            }
                        }
                    }
                }
            }
        }
        return filenames;
    }

    public static HashMap<String, Image> getImagesAndFilenames(Pattern pattern, String directoryName) throws URISyntaxException, UnsupportedEncodingException, IOException {
        HashMap<String, Image> images = new HashMap<String, Image>();
        Pattern filenamePattern = Pattern.compile("[ \\w-]+?(?=\\.)");
        Matcher filenameMatcher;

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
                                final Image img = ImageIO.read( new FileInputStream(filename));
                                // On veut récupèrer le nom du fichier
                                // Or on obtient le chemin
                                // ex: G:\Documents\GitHub\Perseverance_Trip\Perseverance_II\target\classes\res\symboles\checkbox.png
                                // Ici on veut obtenir "checkbox"
                                // On utilise donc le patterne Regex suivant: [ \w-]+?(?=\.)
                                // A la fin on tronque le caractère "."
                                
                                filenameMatcher = filenamePattern.matcher(filename.toString());
                                if (filenameMatcher.find())
                                    images.put(filenameMatcher.group(0), img);
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
                                // On veut récupèrer le nom du fichier
                                // Or on obtient le chemin
                                // ex: G:\Documents\GitHub\Perseverance_Trip\Perseverance_II\target\classes\res\symboles\checkbox.png
                                // Ici on veut obtenir "checkbox"
                                // On utilise donc le patterne Regex suivant: [ \w-]+?(?=\.)
                                // A la fin on tronque le caractère "."
                                
                                filenameMatcher = filenamePattern.matcher(resource.toString());
                                if (filenameMatcher.find())
                                    images.put(filenameMatcher.group(0), ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream(name)));
                            }
                        }
                    }
                }
            }
        }
        return images;
    }

    public static HashMap<String, AudioInputStream> getAudioAndFilenames(Pattern pattern, String directoryName) throws URISyntaxException, UnsupportedEncodingException, IOException {
        HashMap<String, AudioInputStream> images = new HashMap<String, AudioInputStream>();
        Pattern filenamePattern = Pattern.compile("[ \\w-]+?(?=\\.)");
        Matcher filenameMatcher;

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
                                try {
                                    AudioInputStream AUDIO = createReusableAudioInputStream(AudioSystem.getAudioInputStream(filename));
                                    filenameMatcher = filenamePattern.matcher(filename.toString());
                                    if (filenameMatcher.find())
                                        images.put(filenameMatcher.group(0), AUDIO);
                                } catch (UnsupportedAudioFileException e) {
                                    e.printStackTrace();
                                } 
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
                                filenameMatcher = filenamePattern.matcher(resource.toString());
                                if (filenameMatcher.find())
                                    images.put(filenameMatcher.group(0), AudioSystem.getAudioInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream(name)));
                            }
                        }
                    }
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                }
            }
        }
        return images;
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
                                images.add(ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream(name)));
                            }
                        }
                    }
                }
            }
        }
        return images;
    }

    // Mettre un fichier audio en cache:
    // Par Mike Clark: https://stackoverflow.com/questions/9999961/song-plays-first-time-but-does-not-play-once-stopped-clip-in-java
    public static AudioInputStream createReusableAudioInputStream(AudioInputStream ais) 
        throws IOException, UnsupportedAudioFileException
    {
        try
        {
            // Pour réutiliser un AudioInputStream en appellant reset(),
            // l'inputStream sous-jacent doit supporter reset().

            // Donc on transforme l'AudioInputStream en ByteArrayInputStream,
            // car ce stream supporte la fonction reset().

            // On créée alors un nouvel AudioInputStream à partir du ByteArrayInputStream.
            // Le nouvel AudioInputStream supporte alors le reset().
            byte[] buffer = new byte[1024 * 32];
            int read = 0;
            ByteArrayOutputStream baos = 
                new ByteArrayOutputStream(buffer.length);
            while ((read = ais.read(buffer, 0, buffer.length)) != -1)
            {
                baos.write(buffer, 0, read);
            }
            AudioInputStream reusableAis = 
                new AudioInputStream(
                        new ByteArrayInputStream(baos.toByteArray()),
                        ais.getFormat(),
                        AudioSystem.NOT_SPECIFIED);
            return reusableAis;
        }
        finally
        {
            if (ais != null)
            {
                ais.close();
            }
        }
    }
}  