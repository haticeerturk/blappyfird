package com.example.deneme_1;

import java.io.IOException;
import java.util.Random;


import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.BuildableTexture;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.scene.background.SpriteBackground;
import org.anddev.andengine.entity.text.ChangeableText;

import android.graphics.Color;
import android.widget.Toast;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MainActivity extends BaseGameActivity implements SensorEventListener  {

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 800;
	private Camera camera;
	
	private Scene scene;
	private Scene sceneGameOver;
	
	private Texture textureResim1;
	private Texture textureResim2;
	private TextureRegion textRegResim1;
	private TextureRegion textRegResim2;
	private Sprite spriteResim;
	
	private Texture textureBG;
	private TextureRegion textRegBG;
	private Sprite spriteBG;
	
	private Texture textAnime;
	private TiledTextureRegion tiledTexReg;
	private AnimatedSprite animSprite;
	
	private Sprite[] blocksSprite = new Sprite[10];
	
	private Random rand = new Random();
	
	/*Yaz� i�in kullan�lacak nesneler...*/
	private Font font;
	private BuildableTexture bTex;
	private ChangeableText cTexScor;
	
	private Sound flySoundUp;
	private Sound flySoundRight;
	private Sound scorSound;
		
	/*Deneme nesneleri*/
	private Sprite denemeSprite; //Sil!
	private ChangeableText cTexDeneme; //Sil!
	
	
	/*Menu nesneleri | Ba�lang��!*/
	private Scene sceneMenu;
	
	private Texture textureMenuBG;
	private Texture textureGameName;
	private Texture textureMenuStart;
	private Texture textureMenuExit;
	private Texture textureMenuAbout;
	
	private TextureRegion textRegMenuBG;
	private TextureRegion textRegGameName;
	private TextureRegion textRegMenuStart;
	private TextureRegion textRegMenuExit;
	private TextureRegion textRegMenuAbout;
	
	private Sprite spriteMenuBG;
	private Sprite spriteGameName;
	private Sprite spriteMenuStart;
	private Sprite spriteMenuExit;
	private Sprite spriteMenuAbout;
	/*Menu nesneleri | Biti�!*/
	
	private SensorManager sensorManager;
	
	
	@Override
	public Engine onLoadEngine() {
		
		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
	    
		final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
		engineOptions.getTouchOptions().setRunOnUpdateThread(true);
		engineOptions.setNeedsSound(true); //Ses oynatabilmek i�in.
	    Engine engine = new Engine(engineOptions);
		
	    return engine;
	}

	@Override
	public void onLoadResources()  {
		
		textureResim1 = new Texture(256,32,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		textRegResim1 = TextureRegionFactory.createFromAsset(textureResim1, this, "gfx/block.png", 0, 0);
		
		textureResim2 = new Texture(256,32,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		textRegResim2 = TextureRegionFactory.createFromAsset(textureResim2, this, "gfx/blockters.png", 0, 0);
		
		textureBG = new Texture(512,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		textRegBG = TextureRegionFactory.createFromAsset(textureBG, this, "gfx/bg_.png", 0, 0);
		
		textureGameName = new Texture(512,256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		textRegGameName = TextureRegionFactory.createFromAsset(textureGameName, this, "gfx/gamename.png", 0, 0);
		
		textureMenuBG = new Texture(512, 1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		textRegMenuBG = TextureRegionFactory.createFromAsset(textureMenuBG, this, "gfx/bg_.png", 0, 0);
		
		textureMenuStart = new Texture(512, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		textRegMenuStart = TextureRegionFactory.createFromAsset(textureMenuStart, this, "gfx/play.png", 0, 0);
		
		textureMenuExit = new Texture(512, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		textRegMenuExit = TextureRegionFactory.createFromAsset(textureMenuExit, this, "gfx/exit.png", 0, 0);
		
		textureMenuAbout = new Texture(512, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		textRegMenuAbout = TextureRegionFactory.createFromAsset(textureMenuAbout, this, "gfx/about.png", 0, 0);
		
		textAnime = new Texture(128, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		createFlySounds(mEngine);
		createTextObject(mEngine);
		
		//0, 0 parametreli frame'in ba�lama kordinatlar�.
		tiledTexReg = TextureRegionFactory.createTiledFromAsset(textAnime, this, "gfx/tile2.png", 0, 0, 4, 2);
		
		mEngine.getTextureManager().loadTexture(textAnime);
		mEngine.getTextureManager().loadTexture(textureBG);
		mEngine.getTextureManager().loadTexture(textureMenuBG);
		mEngine.getTextureManager().loadTexture(textureGameName);
		mEngine.getTextureManager().loadTexture(textureResim1); 
		mEngine.getTextureManager().loadTexture(textureResim2); 
		mEngine.getTextureManager().loadTexture(textureMenuStart);
		mEngine.getTextureManager().loadTexture(textureMenuExit);
		mEngine.getTextureManager().loadTexture(textureMenuAbout);
	}

	
		
	float distanceBetweenBlocksY = 250;
	float distanceBetweenBlocksX = 100;
	float blockCount = 6;
	float blockHeight = 30;
	float XKOR = 0;
	float YKOR = 0;
	float KORVALUE = 40;
	float gravity = 0.9f;  
	float speedY = 0; 
	float speedX = 5;
	float blockSpeed = 2;
	int spriteSize = 32;
	int scor = 0;
	long animationSpeed = 33;
	
	boolean continueGame = false;
	boolean ySpeedBool = false;
	boolean isTouch = false;
	boolean isTouchCenter = false;
	boolean isTouchLeft = false;
	boolean isTouchRight = false;
	
	@Override
	public Scene onLoadScene() {
		sensorManager = (SensorManager) this.getSystemService(this.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), sensorManager.SENSOR_DELAY_GAME);
 
		//sceneMenu olu�turulur ve menu tamamlan�r.
		this.sceneMenu = new Scene();
		this.scene = new Scene();
		
		createMenu(); //Menu olu�turulur.
        createGameScene(); //Oyun scenei olu�turulur.
        
		mEngine.registerUpdateHandler(new TimerHandler(0.02f, true, new ITimerCallback() {
	        @Override
	        public void onTimePassed(final TimerHandler pTimerHandler) {
	        	if(continueGame)
	        	{
	        		drawBird(pTimerHandler.getTimerSeconds());
	        		updateBlocks(pTimerHandler.getTimerSeconds());
	        		isCollision();
	        	}
	        }
		}));
		
		/*Deneme Alan� Ba�lang�� | Silinecek!     /*
		denemeSprite = new Sprite(0, 100, textRegResim1);
		denemeSprite.setSize(222, 20);
		scene.attachChild(denemeSprite);
		/*Deneme Alan� Biti� | Silinecek!*/
		
        return sceneMenu; //�lk olarak sceneMenu d�nmeli!
	}
	

	
	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
	}
	
	
	
	
	/*Sadece karakteri �izmez, jumping, gravity, velocity burada ayarlan�r!*/
	public void drawBird(float timerSeconds)

	{
		YKOR = animSprite.getY();
		XKOR = animSprite.getX();
	
		if(isTouch) {//Gravity 
			continueGame = true;
			if(ySpeedBool)
			{
			speedY = -10.0f;
			ySpeedBool = false;
			}
			  
			
		if(isTouchRight) {
			XKOR += speedX * timerSeconds * KORVALUE;
    		YKOR += speedY * timerSeconds * KORVALUE;      
    		speedY += gravity * timerSeconds * KORVALUE; 
		}
		else if(isTouchLeft) {
			XKOR -= speedX * timerSeconds * KORVALUE;
        	YKOR += speedY * timerSeconds * KORVALUE;      
        	speedY += gravity * timerSeconds * KORVALUE;  
		}
		else if(isTouchCenter){
			YKOR += speedY * timerSeconds * KORVALUE;      
			speedY += gravity * timerSeconds * KORVALUE;   
		}
		
		//YKOR > CAMERA_HEIGHT - spriteResim1.getWidth()
    	if (YKOR > CAMERA_HEIGHT - spriteSize) {
    		isTouch = false;  	
    		isTouchCenter = false;
    		isTouchLeft = false;
    		isTouchRight = false;
    		animSprite.stopAnimation();
    	}
    	
    	//spriteResim1.setPosition(XKOR, YKOR);	
		animSprite.setPosition(XKOR, YKOR);
		}
		
	}
	
	public void updateBlocks(float timerSeconds)
	{
		float tempY, sizeW; //_ olanlar sa� taraftaki blocklar i�in olut�uruldu.
		for(int i=0; i<blockCount; i++)
		{	
			if(i%2 == 0)
			{
				tempY = blocksSprite[i].getY();
				
				tempY += blockSpeed;
				
				if(blocksSprite[i].getY() > CAMERA_HEIGHT)
				{
					scorInc(); //Skor artt�rma i�lemi yap�l�r.
					tempY = 0;
					
					sizeW = rand.nextInt(350);
					blocksSprite[i].setSize(sizeW, blockHeight);
				}
				blocksSprite[i].setPosition(0, tempY);
			}	
			else if(i % 2 == 1)
			{
				tempY = blocksSprite[i].getY();
				tempY += blockSpeed;
				
				if(blocksSprite[i].getY() > CAMERA_HEIGHT)
				{
					tempY = 0;
					
					sizeW = (int) ((CAMERA_WIDTH - blocksSprite[i - 1].getWidth()) - distanceBetweenBlocksX);
					
					blocksSprite[i].setSize(sizeW, blockHeight);
				}
					
				blocksSprite[i].setPosition(blocksSprite[i - 1].getWidth() + distanceBetweenBlocksX, tempY);
				//Bu form�l ilede olur, sizeW kullanarakta olabilir.
				
			}
		}
	}
	
	public void scorInc()
	{
		if(scorSound != null)
			scorSound.play();
		scor+=100;
		cTexScor.setText(String.valueOf(scor));
	}
	
	public void isCollision()
	{
		float sprX = animSprite.getX();
		float sprY = animSprite.getY();
		float bX,bY;
		
		for(int i=0; i<blockCount; i++)
		{
			bX = blocksSprite[i].getX();
			bY = blocksSprite[i].getY();

			if((sprX >= bX && sprX < bX + blocksSprite[i].getWidth() && sprY >= bY && sprY < bY + blocksSprite[i].getHeight())
			|| (sprX + spriteSize >= bX && sprX + spriteSize < bX + blocksSprite[i].getWidth() && sprY >= bY && sprY < bY + blocksSprite[i].getHeight())
			|| (sprX >= bX && sprX < bX + blocksSprite[i].getWidth() && sprY + spriteSize >= bY && sprY + spriteSize < bY + blocksSprite[i].getHeight())
			|| (sprX + spriteSize >= bX && sprX + spriteSize < bX + blocksSprite[i].getWidth() && sprY + spriteSize >= bY && sprY + spriteSize < bY + blocksSprite[i].getHeight()))
			{
				gameOver();
			}
		}
	}
	
	public void gameOver()
	{
		//Burda oyunSonu adl� fonksiyonu �a��r ve onun i�inde gameOver scenini g�ster.
		mEngine.setScene(sceneMenu);
		animSprite.setPosition(CAMERA_WIDTH/2 - spriteSize/2, CAMERA_HEIGHT - spriteSize);
		
		int sizeW;
		for(int i=0; i<blockCount; i++)
		{
			if(i%2==0)
			{
				//blockX = rand.nextInt(225);
				blocksSprite[i].setPosition(0, i/2 * distanceBetweenBlocksY);
				
				sizeW = rand.nextInt(350);
				blocksSprite[i].setSize(sizeW, blockHeight);
			}
			else if(i%2==1)
			{
				sizeW = (int) ((CAMERA_WIDTH - blocksSprite[i - 1].getWidth()) - distanceBetweenBlocksX);
				 
				blocksSprite[i].setPosition(blocksSprite[i - 1].getWidth() + distanceBetweenBlocksX, (i-1)/2 * distanceBetweenBlocksY);
				//Bu form�l ilede olur, sizeW kullanarakta olabilir.
				
				blocksSprite[i].setSize(sizeW, blockHeight);
			}
		}
		
		continueGame = false;
		scor = 0;
	}
	
	public void createFlySounds(Engine mEngine)
	{
		try
		{
			if(flySoundUp == null)
			{
				flySoundUp = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "sounds/touch1.mp3");
			}
			else
			{
				flySoundUp = null;
				flySoundUp = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "sounds/touch1.mp3");	
			}
			
			if(flySoundRight == null)
			{
				flySoundRight = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "sounds/touch2.mp3");
			}
			else
			{
				flySoundRight = null;
				flySoundRight = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "sounds/touch2.mp3");	
			}
		}	
		catch(IllegalStateException e)
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		try
		{
			if(scorSound == null)
			{
				scorSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "sounds/point.mp3");
			}
			else
			{
				scorSound = null;
				scorSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "sounds/point.mp3");	
			}
		}	
		catch(IllegalStateException e)
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
	}
	
	public void createTextObject(Engine mEngine)
	{
		if(bTex == null)
		{
			bTex = new BuildableTexture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		}
		else
		{
			bTex = null;
			bTex = new BuildableTexture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		}
		mEngine.getTextureManager().loadTexture(bTex);
	
		this.font = FontFactory.createStrokeFromAsset(this.bTex, this, "fonts/arcade.ttf", 75, true, Color.WHITE, 2, Color.rgb(97, 134, 147));
		mEngine.getFontManager().loadFont(font);
	}
	
	
	public void drawBlocks()
	{
		int sizeW;
		for(int i=0; i<blockCount; i++)
		{
			if(i%2==0)
			{
				//blockX = rand.nextInt(225);
				spriteResim = new Sprite(0, i/2 * distanceBetweenBlocksY, textRegResim1);
				
				sizeW = rand.nextInt(350);
				spriteResim.setSize(sizeW, blockHeight);
				blocksSprite[i] = spriteResim;
			}
			else if(i%2==1)
			{
				sizeW = (int) ((CAMERA_WIDTH - blocksSprite[i - 1].getWidth()) - distanceBetweenBlocksX);
				 
				spriteResim = new Sprite(blocksSprite[i - 1].getWidth() + distanceBetweenBlocksX, (i-1)/2 * distanceBetweenBlocksY, textRegResim2);
				//Bu form�l ilede olur, sizeW kullanarakta olabilir.
				
				spriteResim.setSize(sizeW, blockHeight);
				blocksSprite[i] = spriteResim;
			}
		}
	}
	
	public void createGameScene()
	{
		drawBlocks();
		
		animSprite = new AnimatedSprite(CAMERA_WIDTH/2 - spriteSize/2, CAMERA_HEIGHT - spriteSize, tiledTexReg);
        spriteBG = new Sprite(0, 0, textRegBG);
		
        this.scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			@Override
			public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {

				/*Arka arkaya bas�ld���nda y�kseliyormu� �ekli verilmesi i�in...*/
				if(speedY < -9.0f)
					animSprite.stopAnimation();
				else
					animSprite.animate(animationSpeed);
				//cTexDeneme.setText(String.valueOf(speedY));
				
				continueGame = true;
				//Bu ko�ul s�rekli bas�m� engeller. (Bas�l� tutmay�)
				if(pSceneTouchEvent.isActionDown()) {
					
					//if(pSceneTouchEvent.getX() > CAMERA_WIDTH - spriteSize * 2 || sensorX <= -2) //Dokunarak z�plama i�in
					if(sensorX <= -2)
					{
						if(flySoundRight != null)
							flySoundRight.play();
						
						isTouchRight = true;
						isTouchLeft = false;
			        	isTouchCenter = false;
					}
					//else if(pSceneTouchEvent.getX() < spriteSize * 2 || sensorX >= 2)
					else if(sensorX >= 2)
					{
						if(flySoundRight != null)
							flySoundRight.play();
						
						isTouchRight = false;
		        		isTouchCenter = false;
						isTouchLeft = true;
					}
					else //Topun y�r�ngesine dokunuldu ise... 	  
					{
						if(flySoundUp != null)
							flySoundUp.play();
						
						isTouchCenter = true;
						isTouchLeft = false;
		        		isTouchRight = false;
					}
					    animSprite.animate(animationSpeed);
						isTouch = true;
						ySpeedBool = true;
					}
					return false;
			}
		});

        for(int i=0; i<blockCount; i++)
        {
        	scene.registerTouchArea(blocksSprite[i]); //Olmazsa olmaz!
        	scene.attachChild(blocksSprite[i]);
        }
        
        scene.attachChild(animSprite);
        
        //Skor tabelas� eklenir.
        cTexScor = new ChangeableText(20, 20, this.font, "", 3);
        cTexScor.setText("0");
        scene.attachChild(cTexScor);
        
        /*Deneme ama�l� text! !Sil*/
        //cTexDeneme = new ChangeableText(0, 0, this.font, "", 10);
        //cTexDeneme.setText("DenemeLog");
        //scene.attachChild(cTexDeneme);
        
        scene.setBackground(new SpriteBackground(spriteBG));
        //scene.setBackground(new ColorBackground(0, 234, 255));
	}
	
	public void createMenu()
	{
		spriteMenuBG = new Sprite(0, 0, textRegMenuBG);
		
		spriteMenuStart = new Sprite(0, 320, textRegMenuStart)
		{
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp())
					mEngine.setScene(scene);
				return true;
			};
		};
		
		spriteMenuExit = new Sprite(0, 435, textRegMenuExit)
		{
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp())
				{
					finish();
					System.exit(0);
				}
				return true;
			};
		};
		
		spriteGameName = new Sprite(0, 50, textRegGameName);
		spriteMenuAbout = new Sprite(0, 540, textRegMenuAbout); //T�klanma k�sm�n� daha sonra yap!
		
		sceneMenu.setBackground(new SpriteBackground(spriteMenuBG));
		
		sceneMenu.registerTouchArea(spriteMenuStart);
		sceneMenu.registerTouchArea(spriteMenuExit);
		
		sceneMenu.attachChild(spriteGameName);
		sceneMenu.attachChild(spriteMenuStart);
		sceneMenu.attachChild(spriteMenuExit);
		sceneMenu.attachChild(spriteMenuAbout);
	}
	
	int sensorX = 0;
	public void onSensorChanged(SensorEvent event) {
		sensorX = (int)event.values[0];
    }
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //
    }
	
}
