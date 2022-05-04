package com.dngames.dino;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
private String vypisScore;
BitmapFont yourBitmapFontName;
int obstacleSpeed = 300;
int timeToSpeed = 2000;
  

   @Override
   public void create() {
      cactusObstacleImage = new Texture(Gdx.files.internal("cactus.png"));
      birdObstacleImage = new Texture(Gdx.files.internal("birdObstacle.png"));
      dinoImage = new Texture(Gdx.files.internal("dino.png"));

      score = 0;
    vypisScore = "score: 0";
    yourBitmapFontName = new BitmapFont();

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
      bird.height = 64;
    
      birdObstacles.add(bird); 
      lastObstacleSpawnTime = TimeUtils.millis();
   }
   
   private void spawnCactusObstacle() {
      Rectangle cactus = new Rectangle();
       cactus.y = 20;
           cactus.x = 800 - 64;
      cactus.width = 64;
      cactus.height = 64;
      cactusObstacles.add(cactus);
      lastObstacleSpawnTime = TimeUtils.millis();
   }
   
  

   @Override
   public void render() {
      ScreenUtils.clear(0, 0, 0.2f, 1);
      
      camera.update();

      batch.setProjectionMatrix(camera.combined); 

      batch.begin();
      
      yourBitmapFontName.setColor(1.0f, 1.0f, 1.0f, 1.0f);
yourBitmapFontName.draw(batch, vypisScore, 10, 470);

      batch.draw(dinoImage, dino.x, dino.y);
      
      

for(Rectangle cactus: cactusObstacles) {
         batch.draw(cactusObstacleImage, cactus.x, cactus.y);
}
     
      for(Rectangle bird: birdObstacles) {
         
      Sprite birdSize = new Sprite(birdObstacleImage);
      birdSize.setScale(0.8f);
      birdSize.setPosition(bird.x, bird.y);
      birdSize.setSize(bird.width, bird.height);
      birdSize.draw(batch);
      }
      
      batch.end();
     
      if(!highestPointReached){
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
      
      for (Iterator<Rectangle> iter = cactusObstacles.iterator(); iter.hasNext(); ) {
         Rectangle cactus = iter.next();
         cactus.x -= obstacleSpeed * Gdx.graphics.getDeltaTime();
         if(cactus.x < 0 - 80) iter.remove();
         if(cactus.overlaps(dino)) {
            iter.remove();  
         }
      }
   
          for (Iterator<Rectangle> birdIter = birdObstacles.iterator(); birdIter.hasNext(); ) {
         Rectangle bird = birdIter.next();
         bird.x -= obstacleSpeed * Gdx.graphics.getDeltaTime();
         if(bird.x < 0 - 80) birdIter.remove();
         if(bird.overlaps(dino)) {
            birdIter.remove();  
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