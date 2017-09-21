package de.dk.util.game;

import java.util.Objects;

import de.dk.util.timing.Pulse;
import de.dk.util.timing.PulseController;

public class Animation<I> {
   private int index;
   private final I[] images;
   private PulseController pulse;

   public Animation(I[] images, float fps) {
      this.images = Objects.requireNonNull(images);
      this.pulse = new PulseController(this::nextImage, fps);
   }

   public I getImage() {
      pulse.update();
      return images[index];
   }

   protected void nextImage(Pulse pulse) {
      if (++index >= images.length)
         index = 0;
   }

   public void reset() {
      index = 0;
      pulse.reset();
   }

   public void setFps(float fps) {
      pulse.setCps(fps);
   }

   public float getFps() {
      return pulse.getCps();
   }

}