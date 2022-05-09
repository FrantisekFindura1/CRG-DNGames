package com.dngames.dino;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Random;

public class DinoGame extends ApplicationAdapter {
   private Texture birdObstacleImage;
   private Texture cactusObstacleImage;
   private Texture dinoImage;
   private SpriteBatch batch;
   private OrthographicCamera camera;
   private Rectangle dino;
   private Array<Rectangle> cactusObstacles;
   private Array<Rectangle> birdObstacles;
   private long lastObstacleSpawnTime;
   float velocity = 0.0f;
   float gravity = 0.6f;
   boolean highestPointReached = false;
   Random rand = new Random();
   private int score;
   private int highScore;
private String vypisScore;
private String deathScreenText;
private String vypisHighScore;
BitmapFont scoreFont;
BitmapFont deathScreenFont;
int obstacleSpeed = 300;
int timeToSpeed = 2000;
boolean isDead = false;
  

   @Override
   public void create() {
      cactusObstacleImage = new Texture(Gdx.files.internal("cactus.png"));
      birdObstacleImage = new Texture(Gdx.files.internal("birdObstacle.png"));
      dinoImage = new Texture(Gdx.files.internal("dino.png"));
      
     
      score = 0;
    vypisScore = "score: 0";
    vypisHighScore = "hi-score: 0";
    deathScreenText = "       GAME OVER \npress spacebar to retry ";
    
    scoreFont = new BitmapFont();
    deathScreenFont = new BitmapFont();

      camera = new OrthographicCamera();
      camera.setToOrtho(false, 800, 480);
      batch = new SpriteBatch();
     
      dino = new Rectangle();
      dino.x = -64;
      dino.y = 20;
      dino.width = 64;
      dino.height = 64;

      cactusObstacles = new Array<Rectangle>();
      birdObstacles = new Array<Rectangle>();
      spawnCactusObstacle();
   }

   private void spawnBirdObstacle() {
      Rectangle bird = new Rectangle();
          bird.y = 220;
          bird.x = 800 - 64;
      bird.width = 64;
      bird.height = 48;
      birdObstacles.add(bird); 
      lastObstacleSpawnTime = TimeUtils.millis();
   }
   
   private void spawnCactusObstacle() {
      Rectangle cactus = new Rectangle();
       cactus.y = 20;
           cactus.x = 800 - 64;
      cactus.width = 48;
      cactus.height = 65;
      cactusObstacles.add(cactus);
      lastObstacleSpawnTime = TimeUtils.millis();
   }
   
   private void resetOnDeath(){
        isDead = false;
        score = 0;
        dino.y=20;
        obstacleSpeed = 300;
        timeToSpeed = 2000;
        
            for (Iterator<Rectangle> birdIter = birdObstacles.iterator(); birdIter.hasNext(); ) {   //vymaze vsetky kaktusy a vtaky ked stlacim medzernik
         Rectangle bird = birdIter.next();
         if(bird.x > 0) birdIter.remove();
             }
                  
             for (Iterator<Rectangle> iter = cactusObstacles.iterator(); iter.hasNext(); ) {
         Rectangle cactus = iter.next();
         if(cactus.x >0) iter.remove();
             }
   }

   @Override
   public void render() {
      ScreenUtils.clear(1, 1, 1, 1);
      Preferences prefs = Gdx.app.getPreferences("My Preferences");
      
      camera.update();

      batch.setProjectionMatrix(camera.combined); 

      batch.begin();
      
      scoreFont.setColor(0f, 0f, 0f, 1.0f);
      deathScreenFont.setColor(0f, 0f, 0f, 1.0f);
      deathScreenFont.getData().setScale(2);
      
scoreFont.draw(batch, vypisScore, 10, 470);
scoreFont.draw(batch, vypisHighScore, 700, 470);

if(isDead){
deathScreenFont.draw(batch, deathScreenText, 250, 250);
}
      Sprite dinoSize = new Sprite(dinoImage);
      dinoSize.setScale(1f);
      dinoSize.setPosition(dino.x, dino.y);
      dinoSize.setSize(dino.width, dino.height);
      dinoSize.draw(batch);
      

for(Rectangle cactus: cactusObstacles) {
         Sprite cactusSize = new Sprite(cactusObstacleImage);
          cactusSize.setScale(1f);
      cactusSize.setPosition(cactus.x, cactus.y);
      cactusSize.setSize(cactus.width, cactus.height);
      cactusSize.draw(batch);
      }

     
      for(Rectangle bird: birdObstacles) {
         
      Sprite birdSize = new Sprite(birdObstacleImage);
      birdSize.setScale(1f);
      birdSize.setPosition(bird.x, bird.y);
      birdSize.setSize(bird.width, bird.height);
      birdSize.draw(batch);
      }
      
      batch.end();
     
      if(!highestPointReached && !isDead){
      if(Gdx.input.isKeyPressed(Keys.UP) && dino.y <= 20){ 
          velocity = -15;
      }
       if(Gdx.input.isKeyPressed(Keys.DOWN) && dino.y >50){ 
          velocity = 15;
      }
      if(dino.y < 20){
          velocity = 0;
          dino.y = 20;
      }
      else{
          velocity = velocity + gravity;
          dino.y = dino.y-velocity;
      }
      if(dino.y > 220){
          highestPointReached = true;
      }
      }
      else if(isDead){
          velocity = 0; 
          dino.y = dino.y-velocity;
      }
      if(highestPointReached){
          if(dino.y < 20){
          velocity = 15;
      }
      else{
          velocity = velocity + gravity;
          dino.y = dino.y-velocity;
      }
          if(dino.y < 20){
              highestPointReached = false;
          }
      }

      if(dino.x < 0) dino.x = 0;
      if(dino.y > 480) dino.y = 480 - 80;
      
      
      if(!isDead){
 if(obstacleSpeed <1200){
      if(TimeUtils.millis() - lastObstacleSpawnTime > timeToSpeed){
          int randomObstacleSpawn = rand.nextInt(2);
          if(randomObstacleSpawn == 0){
          spawnCactusObstacle();
          }
          else if(randomObstacleSpawn == 1){
              spawnBirdObstacle();
          }
          obstacleSpeed+=20;
          timeToSpeed -= 30;
      }
   }
  else{
      if(TimeUtils.millis() - lastObstacleSpawnTime > timeToSpeed){
         int randomObstacleSpawn = rand.nextInt(2);
          if(randomObstacleSpawn == 0){
          spawnCactusObstacle();
          }
          else if(randomObstacleSpawn == 1){
              spawnBirdObstacle();
          }
          
      }
   }
 score++;
  vypisScore = "score: " + score;
  highScore = prefs.getInteger("highscore");
  if(highScore < score){
      highScore = score;
      prefs.putInteger("highscore", highScore);
            prefs.flush();
  }
  vypisHighScore = "hi-score: " + highScore;
      
      
      for (Iterator<Rectangle> iter = cactusObstacles.iterator(); iter.hasNext(); ) {
         Rectangle cactus = iter.next();
         cactus.x -= obstacleSpeed * Gdx.graphics.getDeltaTime();
         if(cactus.x < 0 - 80) iter.remove();
         if(cactus.overlaps(dino)) { 
            isDead = true;
            
         }
      }
   
          for (Iterator<Rectangle> birdIter = birdObstacles.iterator(); birdIter.hasNext(); ) {
         Rectangle bird = birdIter.next();
         bird.x -= obstacleSpeed * Gdx.graphics.getDeltaTime();
         if(bird.x < 0 - 80) birdIter.remove();
         if(bird.overlaps(dino)) {  
            isDead = true;
         }
      }
          
   }
         if(isDead){
             if(Gdx.input.isKeyPressed(Keys.SPACE)){
                resetOnDeath();
             }
        }
         
   }
   
   @Override
   public void dispose() {
      // dispose of all the native resources
      cactusObstacleImage.dispose();
      birdObstacleImage.dispose();
      dinoImage.dispose();
      batch.dispose();
   }
}