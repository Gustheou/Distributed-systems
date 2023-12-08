package Util;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Musicas {
  Clip clip;
  
  public Musicas(Clip clip){
    this.clip = clip;
  }
  public void audioIniciarAlgoritmo(File musicFile){
    try {
      AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
      clip = AudioSystem.getClip();
      clip.open(audioStream);
      clip.start();
    } catch (Exception e) {
      System.out.println("Excecao na musica: " + e.getMessage());
    }
  }

  public void interromperMusica(){
    clip.stop();
    clip.close();
  }
 
}