package app;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

// Credit: T. Thomas, https://stackoverflow.com/questions/11919009/using-javax-sound-sampled-clip-to-play-loop-and-stop-multiple-sounds-in-a-game
public class Sound {
    
    private int index = 0;
    private ArrayList<AudioInputStream> cachedSounds = new ArrayList<AudioInputStream>();
    private boolean concurrentClip = false; // Si "vrai": Crée un nouveau clip à chaque son joué. Si "faux": ré-utilise le même clip.
    private int volume;
    private ArrayList<Clip> clipList = new ArrayList<Clip>();
    private ArrayList<FloatControl> gainControlList = new ArrayList<FloatControl>();
    private String[] filesName;

    public Sound(String[] filePaths, int volume, boolean concurrentClip) throws LineUnavailableException {
        this(filePaths, volume, concurrentClip, filePaths);
    }
    public Sound(String[] filePaths, int volume, boolean concurrentClip, String[] filesName) throws LineUnavailableException {
        this.filesName = filesName;
        this.concurrentClip = concurrentClip;
        this.volume = volume;
        if (!this.concurrentClip) {
            Clip clip = AudioSystem.getClip();
            clipList.add(clip);
            gainControlList.add( null );
        }
        this.load(filePaths);
    }
    public Sound(String filePath, int volume, boolean concurrentClip) throws LineUnavailableException {
        this(filePath, volume, concurrentClip, filePath);
    }
    public Sound(String filePath, int volume, boolean concurrentClip, String fileName) throws LineUnavailableException {
        this.filesName = new String[] {fileName};
        this.concurrentClip = concurrentClip;
        this.volume = volume;
        if (!this.concurrentClip) {
            Clip clip = AudioSystem.getClip();
            clipList.add(clip);
            gainControlList.add( null );
        }
        this.load(new String[] {filePath});
    }

    public void load(String[] fileName) {
        // On charge les fichier audio .wav
        for (String singleFileName: fileName) {
            try {
                File file = new File(singleFileName);
                if (file.exists()) {
                    AudioInputStream sound = createReusableAudioInputStream(file);
                    cachedSounds.add(sound);
                }
                else {
                    throw new RuntimeException("Sound: file not found: " + fileName);
                }
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
                throw new RuntimeException("Sound: Malformed URL: " + e);
            }
            catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
                throw new RuntimeException("Sound: Unsupported Audio File: " + e);
            }
            catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Sound: Input/Output Error: " + e);
            }
        }
    }
    public void setVolume(int volume) {
        this.volume = volume;
        for (FloatControl f: gainControlList) {
            setGain(f);
        }
    }
    private void setGain(FloatControl f) {
        float range = f.getMaximum() - f.getMinimum();
        f.setValue( (range * ((float)this.volume/100f)) + f.getMinimum() );
    }

    public void play(String audioName) throws IOException, LineUnavailableException {
        int index = 0;
        while (!filesName[index].contains(audioName) && index < filesName.length) {
            index ++;
        }
        if (index != filesName.length)
            play(index);
    }
    public void play() throws IOException, LineUnavailableException {
        index = (index+1)%cachedSounds.size();
        play(index);
    }
    public void play(int i) throws IOException, LineUnavailableException {
        if (i < 0 || i > cachedSounds.size())
            i = (index+1)%cachedSounds.size();
        index = i;
        AudioInputStream stream = cachedSounds.get(i);
        // Comme tous les InputStreams, AudioInputStream ne peut être lu qu'une fois
        // Cependant, on peut le réinitialiser avec reset()
        stream.reset();
        Clip clip;
        if (concurrentClip) {
            clip = AudioSystem.getClip();
            clip.open(stream);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            this.setGain(gainControl);
            clipList.add(clip);
            gainControlList.add(gainControl);
        } else {
            this.stop(); // On réinitialise le "clip"
            clip = clipList.get(clipList.size()-1);
            clip.open(stream);
            gainControlList.set( gainControlList.size()-1, (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN));
            this.setGain(gainControlList.get(gainControlList.size()-1));
        }
        clip.start();
    }
    // On loop le dernier clip créée X fois
    public void loop(int loopNumber){
        clipList.get(clipList.size()-1).loop(loopNumber);
    }
    // On loop le dernier clip créée indéfiniment
    public void loop(){
        clipList.get(clipList.size()-1).loop(Clip.LOOP_CONTINUOUSLY);
    }
    // On stoppe tous les clips créées et on les enlève de la liste
    public void stop(){
        if (concurrentClip) {
            Iterator i = clipList.iterator();
            Clip tmpClip;
            while (i.hasNext()) {
                tmpClip = (Clip) i.next();
                // On met la son joué sur pause  
                tmpClip.stop();

                // On permet à un nouveau son d'être joué
                tmpClip.close();
                i.remove();
            }
        } else {
            // On met le son joué sur pause  
            clipList.get(clipList.size()-1).stop();

            // On permet à un nouveau son d'être joué
            clipList.get(clipList.size()-1).close();
        }
    }
    public void pause(){
        if (concurrentClip) {
            for (Clip c: clipList) {
                // On met la son joué sur pause  
                c.stop();
            }
        } else {
            // On met le son joué sur pause  
            clipList.get(clipList.size()-1).stop();
        }
    }
    public void resume(){
        if (concurrentClip) {
            int i = 0;
            for (Clip c: clipList) {
                // On lit le fichier audio
                c.start();
                i++;
            }
        } else {
            // On lit le fichier audio
            clipList.get(clipList.size()-1).start();
        }
    }
    // Mettre un fichier audio en cache:
    // Par Mike Clark: https://stackoverflow.com/questions/9999961/song-plays-first-time-but-does-not-play-once-stopped-clip-in-java
    private AudioInputStream createReusableAudioInputStream(File file) 
        throws IOException, UnsupportedAudioFileException
    {
        AudioInputStream ais = null;
        try
        {
            ais = AudioSystem.getAudioInputStream(file);
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