
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;

// Credit: T. Thomas, https://stackoverflow.com/questions/11919009/using-javax-sound-sampled-clip-to-play-loop-and-stop-multiple-sounds-in-a-game
public class Son {
    
    private int index = 0;
    private String[] audioNames;
    private AudioInputStream[] audioStreams;
    private boolean concurrentClip = false; // Si "vrai": Crée un nouveau clip à chaque son joué. Si "faux": ré-utilise le même clip.
    private boolean pause = false;
    private int volume;
    private ArrayList<Clip> clipList = new ArrayList<Clip>();
    private ArrayList<FloatControl> gainControlList = new ArrayList<FloatControl>();

    public Son(HashMap<String, AudioInputStream> audioList, int volume, boolean concurrentClip) throws LineUnavailableException {
        // On veut classer les éléments, et les obtenir à partir d'un index
        // On passe donc d'une HashMap à 2 ArrayList triées
        List<String> triClefs = audioList.entrySet()
                        .stream()
                        .sorted((p1,p2) -> p1.getKey().compareTo(p2.getKey()))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());
        audioNames = new String[triClefs.size()];
        audioNames = triClefs.toArray(audioNames);
        audioStreams = new AudioInputStream[triClefs.size()];
        for (int i=0; i < audioNames.length; i++)
            audioStreams[i] = audioList.get(audioNames[i]);
        
        this.concurrentClip = concurrentClip;
        this.volume = volume;
        if (!this.concurrentClip) {
            Clip clip = AudioSystem.getClip();
            clipList.add(clip);
            gainControlList.add( null );
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
        while (!audioNames[index].equals(audioName) && index < audioNames.length) {
            index ++;
        }
        if (index != audioNames.length)
            play(index);
    }
    public void playBackwards() throws IOException, LineUnavailableException {

        index = index-1;
        if (index < 0)
            index = audioStreams.length-1;
        play(index);
    }
    public void play() throws IOException, LineUnavailableException {
        index = (index+1)%audioStreams.length;
        play(index);
    }
    public void play(int i) throws IOException, LineUnavailableException {
        if (i < 0 || i > audioStreams.length)
            i = (index+1)%audioStreams.length;
        index = i;
        
        /*
        Clip clip = AudioSystem.getClip();
        AudioInputStream stream = audioStreams[i];
        stream.reset();
        stop();
        clip.open(audioStreams[i]);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        setGain(gainControl);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
        */
        
        AudioInputStream stream = audioStreams[i];
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
    public void pauseOrResume(){
        if (pause) {
            resume();
            loop();
        } else
            pause();
    }
    public void pause(){
        pause = !pause;
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
        pause = !pause;
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
}