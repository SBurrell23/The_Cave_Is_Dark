import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.Icon;

import org.lwjgl.*;
import org.lwjgl.openal.AL;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.Color;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;


//Steven Burrell
//Started 11/8/2012
//First real platformer attempt in over a year.

public class Main {

	//You
	Rectangle h = new Rectangle(100,300,25,50);
	//Ground Blocks
	ArrayList<Rectangle> ground = new ArrayList<Rectangle>();
	ArrayList<Rectangle> enemy = new ArrayList<Rectangle>();
	ArrayList<Rectangle> finish = new ArrayList<Rectangle>();
	//Other variables
	boolean jumping = false,falling = false;
	int startingJumpY,peakJumpY;
	int level = 0;
	int jumpHeight = 140;
	boolean graphics = true;
	boolean grid = false;
	int direction = 1;
	int heroAni = 0;
	Audio introMusic,caveMusic;
	boolean caveMusicOnce = true;
	boolean endMusic = true;
	boolean enterKeyPressed = false;
	int level9clear = 0;
	
	public Main() throws LWJGLException{
		

		initGL();//also loads assets
		//caveMusic.playAsMusic(1, 1, true);
		//introMusic.playAsMusic(1, 1, true);
		
		resetLevel();
		while(!Display.isCloseRequested()){
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			gameLoop();	
			Display.update();
			Display.sync(60);
		}
		AL.destroy();
		Display.destroy();
	}

	Texture heroTexture,levelOneTexture, heroright,heroright2,heroleft,heroleft2,herojumpleft,herojumpright;
	Texture lvl1,lvl2,lvl3,lvl4,lvl5,lvl6,lvl7,lvl8,lvl9,lvl10,lvl11;
	Texture level5and6pole,level7fireball,level8firebar,level9block,level10wall;
	Texture lvl0back, lvl0front,cavebackground,loreslide,endcredits, door,doorlight;
	public void loadAssets(){
			 
				try {
					//sound
					introMusic = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("assets/music/intromusic.wav"));
					caveMusic = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("assets/music/cavemusic.wav"));
					//
					heroright = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/hero/heroright.png"));
					heroright2 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/hero/heroright2.png"));
					heroleft = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/hero/heroleft.png"));
					heroleft2 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/hero/heroleft2.png"));
					herojumpleft = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/hero/herojumpleft.png"));
					herojumpright = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/hero/herojumpright.png"));
					//levels
					//levelOneTexture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/levels/levelonedemo.png"));
					cavebackground = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/levels/cavebackground.png"));
					loreslide = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/levels/loreslide.png"));
					endcredits = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/levels/endcredits.png"));
					lvl1 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/levels/level1.png"));
					lvl2 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/levels/level2.png"));
					lvl3 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/levels/level3.png"));
					lvl4 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/levels/level4.png"));
					lvl5 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/levels/level5.png"));
					lvl6 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/levels/level6.png"));
					lvl7 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/levels/level7.png"));
					lvl8 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/levels/level8.png"));
					lvl9 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/levels/level9.png"));
					lvl10 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/levels/level10.png"));
					lvl11 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/levels/lvl11.png"));
					//
					lvl0back = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/levels/level0back.png"));
					lvl0front = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/levels/level0front.png"));
					//
					level5and6pole = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/levelassets/level5and6pole.png"));
					level7fireball = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/levelassets/level7fireball.png"));
					level8firebar = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/levelassets/level8firebar.png"));
					level9block = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/levelassets/level9block.png"));
					level10wall = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/levelassets/level10wall.png"));
					door = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/levelassets/door.png"));
					doorlight = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("assets/levelassets/doorlight.png"));
					//placeholder = TextureLoader.getTexture("PNG", this.getClass().getClassLoader().getResource("assets/placeholder.png").openStream());
				}catch (IOException e){e.printStackTrace();}
		
	}
	
	public void initGL() throws LWJGLException{
		Display.setDisplayMode(new DisplayMode(1200,600));
		Display.setTitle("The Cave Is Dark");
		Display.create();
		Display.setVSyncEnabled(true);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		// enable alpha blending (toggles simple and loaded graphics)
		
		//initialize openGl
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0,1200,600,0,1,-1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		loadAssets();
	}
	


	public void gameLoop()
	{

		handleWhenToPlayWhatMusic();
		
		if(grid)
			drawGrid();
		
		if(level > 0 && level < 11)
			drawTexture(cavebackground,0,0);
		
		if(level == 11)
			drawTexture(lvl11,0,0);
		
		createLevel(level);
		
		if(level != 12)
			drawHero(direction);
		if(level == 0){
			drawTexture(lvl0front,0,0);
			drawTexture(loreslide,0,0);
		}
		if(level == 11)
			drawTexture(lvl0front,0,0);
		
		
		simulateGravity();
		handleMovement();
		handleJumping();
		handleEnemyCollision();
		checkforFinish();

		printMouseClick();
	}
	
	public void handleWhenToPlayWhatMusic(){
		
		if(level == 0 && caveMusicOnce){
			SoundStore.get().poll(0);
			caveMusic.playAsMusic(1, 1, true);
			caveMusicOnce = false;
			}
		if(level == 11 && endMusic){
			caveMusic.stop();
			SoundStore.get().poll(0);
			introMusic.playAsMusic(1, 1, true);
			caveMusicOnce = true;
			endMusic = false;
		}
	}
	
	public void drawGrid(){
		GL11.glColor3d(2, 2, 2);
		for(int i = 0;i <(600);i+=10){
			GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex2i(0,i);//Upper-Left
			GL11.glVertex2i(1200,i);//Upper-Left
			GL11.glEnd();
		}
		for(int i = 0;i <(1200);i+=10){
			GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex2i(i,0);//Upper-Left
			GL11.glVertex2i(i,600);//Upper-Left
			GL11.glEnd();
		}
		
	}
	
	public void drawHero(int n){
		
		if(graphics){
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
		else if (!graphics ){
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glRecti(h.getX(),h.getY(), h.getX()+25, h.getY()+50);
		}
	
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); 
		Color.white.bind();
		
		//HANDLES HERO ANIMATION N = 1 = FACING RIGHT, N = 0 = FACING LEFT
		if(!jumping && !isNextDownGround()){
			if(n == 1){
				herojumpright.bind();drawTexture(herojumpright,h.getX(),h.getY());
			}
			else{
				herojumpleft.bind();drawTexture(herojumpleft,h.getX(),h.getY());
			}
				
		}
		else{
		if(n == 1){
			if(heroAni <= 6){
			heroright.bind();drawTexture(heroright,h.getX(),h.getY());
			}
			else{
				heroright2.bind();drawTexture(heroright2,h.getX(),h.getY());
						}
			if(heroAni >= 12)
				heroAni = 0;
		}
		if(n == 0){
			if(heroAni <= 6){
			heroleft.bind();drawTexture(heroleft,h.getX(),h.getY());
					}
			else{
				heroleft2.bind();drawTexture(heroleft2,h.getX(),h.getY());
				}
			
			if(heroAni >= 12)
				heroAni = 0;
		}
		}
		//END HERO ANIMATION HANDLE
		//DRAW CHOSEN HERO POSE
	
	}
	
	public void handleMovement(){
		if ((Keyboard.isKeyDown(Keyboard.KEY_D)|| Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) && h.getX() < 1180  && !isNextSidewaysMoveAWallRight()){
			h.setX(h.getX()+5);
			direction = 1;
			heroAni++;
		}
		if ((Keyboard.isKeyDown(Keyboard.KEY_A)|| Keyboard.isKeyDown(Keyboard.KEY_LEFT))  && h.getX() > 0 && !isNextSidewaysMoveAWallLeft()){
			h.setX(h.getX()-5);
			direction = 0;
			heroAni++;
			}
		if ((Keyboard.isKeyDown(Keyboard.KEY_SPACE) || Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP)) && !jumping && !falling)
			initJump();
		/* Cannot override gravity (future ground stomp?)
		if (Keyboard.isKeyDown(Keyboard.KEY_S)  && h.getY() < 550 && !isNextDownGround())
			h.setY(h.getY()+5);
		*/
		if((Keyboard.isKeyDown(Keyboard.KEY_RETURN)) && level == 12){
			enterKeyPressed = true;
			
		}
	}

	public void simulateGravity()
	{
		if(h.getY() < 550)
		{
			if(!isNextDownGround())
				h.setY(h.getY()+5);
		}
	}
	

	public void initJump()
	{
	    startingJumpY = h.getY();
		peakJumpY = h.getY()-jumpHeight;
		//
		jumping = true;
	}
	
	public void handleJumping(){
		//if you have not peaked and you are still jumping move up
		if(h.getY() != peakJumpY && jumping == true)
			h.setY(h.getY()-15);
		//if you peaked or hit something then you are no longer jumping
		if(h.getY() == peakJumpY || isNextUpGround())
			jumping = false;
		//if you are about to hit the ground stop falling
		if(isNextDownGround())
			falling = false;
		//if you are not about to hit the ground then you are still falling
		if(!isNextDownGround())
			falling = true;
	
	}
	
	//Checks if your next movement downward will touch some kind of ground
	public boolean isNextDownGround(){
		for(int i = 0;i < ground.size();i++){
			Rectangle temp = new Rectangle(h.getX(),h.getY()+1,h.getWidth(),h.getHeight());
			if(temp.intersects(ground.get(i))){
				return true;
			}
		}
		
		return false;
	}
	//checks if your next movement upward will touch some kind of ground
	public boolean isNextUpGround(){
		for(int i = 0;i < ground.size();i++){
			Rectangle temp = new Rectangle(h.getX(),h.getY()-16,h.getWidth(),h.getHeight());
			if(temp.intersects(ground.get(i)))
				return true;
		}
		return false;
	}
	public boolean isNextSidewaysMoveAWallRight(){
		for(int i = 0;i < ground.size();i++){
			Rectangle temp = new Rectangle(h.getX()+1,h.getY(),h.getWidth(),h.getHeight());
			if(temp.intersects(ground.get(i)))
				return true;
		}
		return false;
	}
	public boolean isNextSidewaysMoveAWallLeft(){
		for(int i = 0;i < ground.size();i++){
			Rectangle temp = new Rectangle(h.getX()-1,h.getY(),h.getWidth(),h.getHeight());
			if(temp.intersects(ground.get(i)))
				return true;
		}
		return false;
	}
	
	public void createLevelZero(){
		drawTexture(lvl0back,0,0);
		//ground
		GL11.glColor3d(0, 0, 2);
		ground.add(new Rectangle(0,460,1300,700));
		//GL11.glRecti(0,460,1300,1160);
		
		//finish
		GL11.glColor3d(0, 2, 0);
		finish.add(new Rectangle(1119,378,50,50));
		//GL11.glRecti(1119,378,1169,428);
	}
	
	public void createLevelOne(){
		drawBackground(lvl1,2050,1022);
		drawTexture(door,1116,140);
		ground.clear();
		enemy.clear();
		//ground
		GL11.glColor3d(0, 0, 2);
		ground.add(new Rectangle(0,500,1200,50));
		GL11.glRecti(0,500,1200,550);
		ground.add(new Rectangle(300,250,50,50));
		GL11.glRecti(300,250,350,300);
		ground.add(new Rectangle(800,350,100,50));
		GL11.glRecti(800,350,900,400);
		ground.add(new Rectangle(500,300,75,50));
		GL11.glRecti(500,300,575,350);	
		ground.add(new Rectangle(1000,450,75,50));
		GL11.glRecti(1000, 450, 1075, 500);
		ground.add(new Rectangle(690,290,25,25));
		GL11.glRecti(690,290,715,315);
		ground.add(new Rectangle(150,105,25,25));
		GL11.glRecti(150,105,175,130);
		ground.add(new Rectangle(210,190,25,25));
		GL11.glRecti(210,190,235,215);
		ground.add(new Rectangle(865,50,200,100));
		GL11.glRecti(865,50,1065,150);
		ground.add(new Rectangle(250,35,200,10));
		GL11.glRecti(250,35,450,45);
		ground.add(new Rectangle(640,85,300,10));
		GL11.glRecti(640,85,940,95);
		ground.add(new Rectangle(1115,190,40,10));
		GL11.glRecti(1115,190,1155,200);
		//finish
		GL11.glColor3d(0, 2, 0);
		finish.add(new Rectangle(1125,160,25,25));
		GL11.glRecti(1125,160,1150,185);
		//
		
	}
	
	public void createLevelTwo(){
			drawBackground(lvl2,2050,1022);
			ground.clear();
			enemy.clear();
		//ground
		GL11.glColor3d(0, 0, 2);
		ground.add(new Rectangle(1075,520,200,100));
		GL11.glRecti(1075,520,1275,620);
		ground.add(new Rectangle(990,420,50,50));
		GL11.glRecti(990,420,1040,470);
		ground.add(new Rectangle(870,310,50,50));
		GL11.glRecti(870,310,920,360);
		ground.add(new Rectangle(660,300,50,50));
		GL11.glRecti(660,300,710,350);
		ground.add(new Rectangle(400,300,50,50));
		GL11.glRecti(400,300,450,350);
		ground.add(new Rectangle(200,230,50,50));
		GL11.glRecti(200,230,250,280);
		ground.add(new Rectangle(80,160,50,50));
		GL11.glRecti(80,160,130,210);
		//enemies
		GL11.glColor3d(2, 0, 0);
		enemy.add(new Rectangle(0,540,1075,100));
		GL11.glRecti(0,540,1075,640);
		//finish
		GL11.glColor3d(0, 2, 0);
		finish.add(new Rectangle(14,53,25,25));
		GL11.glRecti(14,53,39,78);
		drawTexture(door,9,28);
	
	}
	public void createLevelThree(){
		drawBackground(lvl3,2050,1022);
		ground.clear();
		enemy.clear();
		//ground
		GL11.glColor3d(0, 0, 2);
		ground.add(new Rectangle(0,540,100,100));
		GL11.glRecti(0,540,100,640);

		ground.add(new Rectangle(300,540,10,50));
		GL11.glRecti(300,540,310,590);
		ground.add(new Rectangle(510,540,10,50));
		GL11.glRecti(510,540,520,590);
		ground.add(new Rectangle(730,540,10,50));
		GL11.glRecti(730,540,740,590);
		ground.add(new Rectangle(950,540,10,50));
		GL11.glRecti(950,540,960,590);
		ground.add(new Rectangle(1060,460,30,30));
		GL11.glRecti(1060,460,1090,490);
		ground.add(new Rectangle(1140,360,30,30));
		GL11.glRecti(1140,360,1170,390);
		ground.add(new Rectangle(100,290,900,25));
		GL11.glRecti(100,290,1000,315);
		ground.add(new Rectangle(0,400,100,25));
		GL11.glRecti(0,400,100,425);
		//enemy
		GL11.glColor3d(2, 0, 0);
		enemy.add(new Rectangle(100,550,1200,100));
		GL11.glRecti(100,550,1300,650);
		//finish
		GL11.glColor3d(0, 2, 0);
		finish.add(new Rectangle(560,260,25,25));
		GL11.glRecti(560,260,585,285);
		drawTexture(door,550,240);
	}
	public void createLevelFour(){
		drawBackground(lvl4,2050,1022);
		ground.clear();
		enemy.clear();
		//ground
		GL11.glColor3d(0, 0, 2);
		ground.add(new Rectangle(1140,80,100,10));
		GL11.glRecti(1140,80,1200,90);
		ground.add(new Rectangle(1140,-10,1200,20));
		GL11.glRecti(1140,-10,2340,10);
		ground.add(new Rectangle(0,580,1200,100));
		GL11.glRecti(0,580,1200,680);
		ground.add(new Rectangle(660,360,25,25));
		GL11.glRecti(660,360,685,385);
		ground.add(new Rectangle(780,460,25,25));
		GL11.glRecti(780,460,805,485);
		ground.add(new Rectangle(780,250,25,25));
		GL11.glRecti(780,250,805,275);
		ground.add(new Rectangle(660,160,25,25));
		GL11.glRecti(660,160,685,185);
		//
		ground.add(new Rectangle(10,360,25,25));
		GL11.glRecti(10,360,35,385);
		ground.add(new Rectangle(130,460,25,25));
		GL11.glRecti(130,460,155,485);
		ground.add(new Rectangle(130,250,25,25));
		GL11.glRecti(130,250,155,275);
		ground.add(new Rectangle(10,160,25,25));
		GL11.glRecti(10,160,35,185);
		//enemy
		GL11.glColor3d(2, 0, 0);
		enemy.add(new Rectangle(900,0,25,500));
		GL11.glRecti(900,0,925,500);
		enemy.add(new Rectangle(200,0,25,500));
		GL11.glRecti(200,0,225,500);
		enemy.add(new Rectangle(550,110,25,500));
		GL11.glRecti(550,110,575,610);
		enemy.add(new Rectangle(1100,130,50,30));
		GL11.glRecti(1100,130,1150,160);
		enemy.add(new Rectangle(1047,178,50,30));
		GL11.glRecti(1047,178,1097,208);
		enemy.add(new Rectangle(992,225,50,30));
		GL11.glRecti(992,225,1042,255);
		enemy.add(new Rectangle(941,439,50,30));
		GL11.glRecti(941,439,991,469);
		enemy.add(new Rectangle(290,460,25,25));
		GL11.glRecti(290,460,315,485);
		enemy.add(new Rectangle(330,434,25,25));
		GL11.glRecti(330,434,355,459);
		enemy.add(new Rectangle(357,399,25,25));
		GL11.glRecti(357,399,382,424);
		enemy.add(new Rectangle(396,347,25,25));
		GL11.glRecti(396,347,421,372);
		enemy.add(new Rectangle(432,285,25,25));
		GL11.glRecti(432,285,457,310);
		enemy.add(new Rectangle(463,241,25,25));
		GL11.glRecti(463,241,488,266);
		enemy.add(new Rectangle(495,183,25,25));
		GL11.glRecti(495,183,520,208);
		enemy.add(new Rectangle(526,138,25,25));
		GL11.glRecti(526,138,551,163);
	
		//finish
		GL11.glColor3d(0, 2, 0);
		finish.add(new Rectangle(23,22,25,25));
		GL11.glRecti(23,22,48,47);
		drawTexture(door,12,4);
	}

	public void createLevelFive(){
		ground.clear();
		enemy.clear();
		//ground
		GL11.glColor3d(0, 0, 2);
		ground.add(new Rectangle(0,580,1200,100));
		GL11.glRecti(0,580,1200,680);
		ground.add(new Rectangle(90,0,25,360));
		GL11.glRecti(90,0,115,360);
		ground.add(new Rectangle(0,335,100,25));
		GL11.glRecti(0,335,100,360);
		//enemy
		bouncingEnemyLine();
		ground.add(new Rectangle(116,0,1200,360));
		GL11.glRecti(116,0,1316,360);
		//finish
		GL11.glColor3d(0, 2, 0);
		finish.add(new Rectangle(1148,548,25,25));
		GL11.glRecti(1148,548,1173,573);
		drawBackground(lvl5,2050,1022);
		drawTexture(door,1142,530);
	}
	public void createLevelSix(){
		drawBackground(lvl6,2050,1022);
		ground.clear();
		enemy.clear();
		//ground
		GL11.glColor3d(0, 0, 2);
		ground.add(new Rectangle(0,580,1200,100));
		GL11.glRecti(0,580,1200,680);
		ground.add(new Rectangle(90,0,25,360));
		GL11.glRecti(90,0,115,360);
		ground.add(new Rectangle(0,335,100,25));
		GL11.glRecti(0,335,100,360);
		//enemy
		bouncingEnemyLine();
		ground.add(new Rectangle(116,0,1200,360));
		GL11.glRecti(116,0,1316,360);
		//finish
		GL11.glColor3d(0, 2, 0);
		finish.add(new Rectangle(21,528,25,25));
		//GL11.glRecti(21,528,46,553);
		drawBackground(lvl6,2050,1022);
		drawTexture(door,19,530);
	}
	
	public void createLevelSeven(){
		drawBackground(lvl7,2050,1022);
		ground.clear();
		enemy.clear();
		//ground
		GL11.glColor3d(0, 0, 2);
		ground.add(new Rectangle(0,0,100,10));
		GL11.glRecti(0,0,100,10);
		ground.add(new Rectangle(0,100,50,10));
		GL11.glRecti(0,100,65,110);
		ground.add(new Rectangle(180,390,50,10));
		GL11.glRecti(180,390,230,400);
		ground.add(new Rectangle(300,300,50,10));
		GL11.glRecti(300,300,350,310);
		ground.add(new Rectangle(400,190,50,10));
		GL11.glRecti(400,190,450,200);
		ground.add(new Rectangle(520,90,50,10));
		GL11.glRecti(520,90,570,100);
		ground.add(new Rectangle(440,490,50,10));
		GL11.glRecti(440,490,490,500);
		ground.add(new Rectangle(340,580,200,10));
		GL11.glRecti(340,580,540,590);
		ground.add(new Rectangle(540,580,200,10));
		GL11.glRecti(540,580,740,590);
		ground.add(new Rectangle(790,480,50,10));
		GL11.glRecti(790,480,840,490);
		ground.add(new Rectangle(920,390,50,10));
		GL11.glRecti(920,390,970,400);
		ground.add(new Rectangle(1010,290,50,10));
		GL11.glRecti(1010,290,1060,300);
		//enemy
		GL11.glColor3d(2, 0, 0);
		flyingBlobs();
		enemy.add(new Rectangle(133,420,200,10));
		GL11.glRecti(133,420,333,430);
		enemy.add(new Rectangle(282,332,200,10));
		GL11.glRecti(282,332,482,342);
		enemy.add(new Rectangle(392,221,200,10));
		GL11.glRecti(392,221,592,231);
		enemy.add(new Rectangle(485,515,200,10));
		GL11.glRecti(485,515,685,525);
		enemy.add(new Rectangle(687,62,10,450));
		GL11.glRecti(687,62,697,512);
		enemy.add(new Rectangle(0,590,1300,10));
		GL11.glRecti(0,590,1300,600);
		//finish
		GL11.glColor3d(0, 2, 0);
		finish.add(new Rectangle(1140,317,25,25));
		GL11.glRecti(1140,317,1165,342);
		drawBackground(lvl7,2050,1022);
		drawTexture(door,1135,296);
	}
	public void createLevelEight(){
		//drawBackground(lvl8,2050,1022);
		ground.clear();
		enemy.clear();
		//ground
		GL11.glColor3d(0, 0, 2);
		ground.add(new Rectangle(1070,490,500,300));
		//GL11.glRecti(1070,490,1570,790);
		ground.add(new Rectangle(50,60,25,700));
		//GL11.glRecti(50,60,75,760);
		ground.add(new Rectangle(75,595,1200,10));
		//GL11.glRecti(75,595,1275,605);
		ground.add(new Rectangle(140,500,25,25));
		//GL11.glRecti(140,500,165,525);
		ground.add(new Rectangle(280,440,25,25));
		//GL11.glRecti(280,440,305,465);
		ground.add(new Rectangle(350,390,25,25));
		//GL11.glRecti(350,390,375,415);
		ground.add(new Rectangle(420,350,25,25));
		//GL11.glRecti(420,350,445,375);
		ground.add(new Rectangle(490,320,25,25));
		//GL11.glRecti(490,320,515,345);
		ground.add(new Rectangle(560,320,25,25));
		//GL11.glRecti(560,320,585,345);
		ground.add(new Rectangle(700,320,25,25));
		//GL11.glRecti(700,320,725,345);
		ground.add(new Rectangle(770,250,25,25));
		//GL11.glRecti(770,250,795,275);
		ground.add(new Rectangle(910,240,25,25));
		//GL11.glRecti(910,240,935,265);
		ground.add(new Rectangle(1020,200,25,25));
		//GL11.glRecti(1020,200,1045,225);
		ground.add(new Rectangle(1070,80,25,25));
		//GL11.glRecti(1070,80,1095,105);
		ground.add(new Rectangle(1070,230,25,25));
		//GL11.glRecti(1070,230,1095,255);
		ground.add(new Rectangle(1150,150,25,25));
		//GL11.glRecti(1150,150,1175,175);
		//
		ground.add(new Rectangle(0,0,900,10));
		GL11.glRecti(0,0,900,10);
		ground.add(new Rectangle(60,60,975,10));
		GL11.glRecti(60,60,975+60,70);
		//enemy
		movingBar();
		//finish
		GL11.glColor3d(0, 2, 0);
		finish.add(new Rectangle(12,559,25,25));
		GL11.glRecti(12,559,37,584);
		drawBackground(lvl8,2050,1022);
		drawTexture(door,4,550);
	}
	
	public void createLevelNine(){
		//drawBackground(lvl9,2050,1022);
		enemy.clear();
		//ground
		GL11.glColor3d(0, 0, 2);
		ground.add(new Rectangle(0,585,1250,20));
		GL11.glRecti(0,585,1250,605);
		blinkingPlatforms();
		//enemy
		GL11.glColor3d(2, 0, 0);
		enemy.add(new Rectangle(115,580,1200,50));
		GL11.glRecti(115,580,1304,635);
		//finish
		GL11.glColor3d(0, 2, 0);
		finish.add(new Rectangle(21,23,25,25));
		GL11.glRecti(21,23,46,48);
		drawBackground(lvl9,2050,1022);
		drawTexture(door,13,8);
	}
	public void createLevelTen(){
		//drawBackground(lvl10,2050,1022);
		//ground
		ground.clear();
		enemy.clear();
		GL11.glColor3d(0, 0, 2);
		ground.add(new Rectangle(0,575,1300,25));
		//GL11.glRecti(0,575,1300,615);
		ground.add(new Rectangle(0,0,50,10));
		//GL11.glRecti(0,0,50,10);
		ground.add(new Rectangle(0,80,1100,10));
		//GL11.glRecti(0,80,1100,90);
		ground.add(new Rectangle(0,75+280,1100,10));
		//GL11.glRecti(0,75+280,1100,85+280);
		ground.add(new Rectangle(100,210,1200,10));
		//GL11.glRecti(100,210,1200,220);
		ground.add(new Rectangle(100,490,1200,10));
		//GL11.glRecti(100,490,1300,500);
		//enemy
		drawBackground(lvl10,2050,1022);
		collapsingWall();
		//first row
		enemy.add(new Rectangle(158,45,10,10));
		//GL11.glRecti(158,45,168,55);
		enemy.add(new Rectangle(407,42,10,10));
		//GL11.glRecti(407,42,417,52);
		enemy.add(new Rectangle(657,39,10,10));
		//GL11.glRecti(657,39,667,49);
		enemy.add(new Rectangle(898,35,10,10));
		//GL11.glRecti(898,35,908,45);
		//second row
		for(int i = 0;i < 9;i++){
		int dist = 80;
		enemy.add(new Rectangle(155+(i*dist),190,10,10));
		//GL11.glRecti(155+(i*dist),190,165+(i*dist),200);
		}
		enemy.add(new Rectangle(952,189,10,10));
		//GL11.glRecti(952,189,962,199);
		//third row
		enemy.add(new Rectangle(156,245,10,10));
		//GL11.glRecti(156,245,166,255);
		enemy.add(new Rectangle(297,333,10,10));
		//GL11.glRecti(297,333,307,343);
		enemy.add(new Rectangle(469,236,10,10));
		//GL11.glRecti(469,236,479,246);
		enemy.add(new Rectangle(637,336,10,10));
		//GL11.glRecti(637,336,647,346);
		enemy.add(new Rectangle(816,234,10,10));
		//GL11.glRecti(816,234,826,244);
		enemy.add(new Rectangle(966,342,10,10));
		//GL11.glRecti(966,342,976,352);
		//final jumping row
		for(int i = 0;i < 9;i++){
		int dist = 95;
		enemy.add(new Rectangle(155+(i*dist),470,10,10));
		//GL11.glRecti(155+(i*dist),470,165+(i*dist),480);
		}
		//finish
		GL11.glColor3d(0, 2, 0);
		finish.add(new Rectangle(1153,544,25,25));
		//GL11.glRecti(1153,544,1178,569);
		drawTexture(doorlight,1150,525);
	}
	
	public void createLevelEleven(){
		//ground
		GL11.glColor3d(0, 0, 2);
		ground.add(new Rectangle(0,460,1300,700));
		GL11.glRecti(0,460,1300,1160);
		
		//finish
		GL11.glColor3d(0, 2, 0);
		finish.add(new Rectangle(969,286,150,150));
		//GL11.glRecti(969,286,1119,436);
	}
	
	public void createLevelTwelve(){
		
		drawTexture(endcredits,0,0);
		//finish
		GL11.glColor3d(0, 2, 0);
		finish.add(new Rectangle(13000,9000,50,50));
		GL11.glRecti(13000,90000,130000000,9000000);
	}
	
	int cWY = -1100;
	int cWLoc = 0;
	public void collapsingWall(){
		double speed = 1;
		//enemy
		GL11.glColor3d(2, 0, 0);
		enemy.add(new Rectangle(0,cWY,1200, 700));
		GL11.glRecti(0,cWY,1200,cWY+700);
		drawTexture(level10wall,0,cWY);
		
		if(cWLoc > 180)
			cWY+=speed;
			
		cWLoc++;

		
	}
	
	int blinker = 0;
	public void blinkingPlatforms(){
		
		if(blinker <  100){
			ground.add(new Rectangle(105,515,50,50));
			//GL11.glRecti(105,515,155,565);
			ground.add(new Rectangle(335,365,50,50));
			//GL11.glRecti(335,365,385,415);
			ground.add(new Rectangle(775,495,50,50));
			//GL11.glRecti(775,495,825,545);
			ground.add(new Rectangle(1070,330,50,50));
			//GL11.glRecti(1070,330,1120,380);
			ground.add(new Rectangle(720,200,50,50));
			//GL11.glRecti(720,200,770,250);
			ground.add(new Rectangle(340,130,50,50));
			//GL11.glRecti(340,130,390,180);
			ground.add(new Rectangle(10,70,50,50));
			//GL11.glRecti(10,70,60,120);
			drawTexture(level9block,105,515);
			drawTexture(level9block,335,365);
			drawTexture(level9block,775,495);
			drawTexture(level9block,1070,330);
			drawTexture(level9block,720,200);
			drawTexture(level9block,340,130);
			drawTexture(level9block,10,70);


		}
		if(blinker >  100){
			ground.add(new Rectangle(220,440,50,50));
			//GL11.glRecti(220,440,270,490);
			ground.add(new Rectangle(530,360,50,50));
			//GL11.glRecti(530,360,580,410);
			ground.add(new Rectangle(940,425,50,50));
			//GL11.glRecti(940,425,990,475);
			ground.add(new Rectangle(910,220,50,50));
			//GL11.glRecti(910,220,960,270);
			ground.add(new Rectangle(520,135,50,50));
			//GL11.glRecti(520,135,570,185);
			ground.add(new Rectangle(160,105,50,50));
			//GL11.glRecti(160,105,210,155);
			drawTexture(level9block,220,440);
			drawTexture(level9block,530,360);
			drawTexture(level9block,940,425);
			drawTexture(level9block,910,220);
			drawTexture(level9block,520,135);
			drawTexture(level9block,160,105);
		}
		
		blinker++;
		
		if(blinker == 200){
			blinker = 0;
		}
		level9clear++;
		System.out.println(level9clear);
		if(level9clear >= 5000)
			ground.clear();
	}
	
	
	int mby = 5;
	boolean barUp = false;
	public void movingBar(){
		
		int speed = 5;
		
		enemy.clear();
		GL11.glColor3d(2, 0, 0);
		for(int i = 0;i < 7;i++){
			
		enemy.add(new Rectangle(103+(i*140),mby,100,25));
		//GL11.glRecti(103+(i*140),mby,203+(i*140),mby+25);
		drawTexture(level8firebar,103+(i*140),mby);
		}
		
		if(mby < 570 && !barUp)
			mby+=speed;
		if(mby >= 570)
			barUp = true;
		if(mby >= 5 && barUp)
			mby-=speed;
		if(mby <= 5)
			barUp = false;
			
		
		
	}
	
	
	
	int e7 = 300;
	boolean movingleft = true;
	public void flyingBlobs(){
	
		int speed = 4;
		//enemy
		GL11.glColor3d(2, 0, 0);
		enemy.clear();
		for(int k = 0;k < 8;k ++){
		for(int i = 0;i < 7;i++){
			if(i == 6){
				enemy.add(new Rectangle(e7+(k*300),i*100-10,10,10));
				GL11.glRecti(e7+(k*300),i*100-10,e7+(k*300)+10,(i*100)-10+10);
				drawTexture(level7fireball,e7+(k*300),i*100-10);
			}
			else{
			enemy.add(new Rectangle(e7+(k*300),i*100,10,10));
			GL11.glRecti(e7+(k*300),i*100,e7+(k*300)+10,(i*100)+10);
				drawTexture(level7fireball,e7+(k*300),i*100);
			}
		}
		}
		if(e7 != -1000)
			e7 = e7-speed;
		if(e7 == -1000)
			e7 = -100;
		
		
		
		
		
	}
	
	int e1 = 480;
	boolean movingdown = true;
	boolean movingup = false;
	public void bouncingEnemyLine(){
	
		int speed = 5;
		//enemy
		GL11.glColor3d(2, 0, 0);
		enemy.clear();
		for(int i = 1;i < 11;i++){
		if(i%2 == 0){
			enemy.add(new Rectangle((i*100)+55,e1,25,170));
			//GL11.glRecti(((i*100)+55),e1,((i*100)+55)+25,e1+170);
			drawTexture(level5and6pole,((i*100)+55),e1);
		}
		else{
			enemy.add(new Rectangle((i*100)+55,e1-600,25,625));
			//GL11.glRecti(((i*100)+55),e1-600,((i*100)+55)+25,e1+25);
			drawTexture(level5and6pole,((i*100)+55),e1-(480-180));
		}
		}
		if(level == 5 || level == 6){
	
		if(e1 != 550 && movingdown){
			e1 = e1+speed;
		
		}
		if(e1 == 550){
			movingdown = false;
			movingup = true;
			
		}
		if(movingup){
			e1 = e1-speed;
		}
		if(e1 == 450){
			movingup = false;
			movingdown = true;
			}
		}
	}

	public void createLevel(int n)
	{
		switch(n){
		case 0:
			createLevelZero();
			break;
		case 1:
			createLevelOne();
			break;
		case 2:
			createLevelTwo();
			break;
		case 3:
			createLevelThree();
			break;
		case 4:
			createLevelFour();
			break;
		case 5:
			createLevelFive();
			break;
		case 6:
			createLevelSix();
			break;
		case 7:
			createLevelSeven();
			break;
		case 8:
			createLevelEight();
			break;
		case 9:
			createLevelNine();
			break;
		case 10:
			createLevelTen();
			break;
		case 11:
			createLevelEleven();
			break;
		case 12:
			createLevelTwelve();
			break;

		}

	}
	
	public void drawBackground(Texture t, int x, int y)
	{
		if(graphics){
		Color.white.bind();
		t.bind();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0,0);
		GL11.glVertex2d( 0, 0); 
		GL11.glTexCoord2f( 1,0);
		GL11. glVertex2d( x,0); 
		GL11.glTexCoord2f(1,1);
		GL11. glVertex2d( x,y);
		GL11.glTexCoord2f(0,1);
		GL11. glVertex2d( 0,y);
		GL11.glEnd();
		}
	}
	public void drawTexture(Texture t, int x, int y)
	{

		if(graphics){
		      float width = t.getImageWidth();
		      float height = t.getImageHeight();
		      //the physical width of the texture which will be used in glTexCoord (generally a float between 0 and 1)
		      float textureWidth = t.getWidth();
		      float textureHeight = t.getHeight();
		      //texture offsets, for texture atlas purposes. leave at 0 for full image
		      float textureOffsetX = 0;
		      float textureOffsetY = 0;
		      //where on screen to draw the image
		      Color.white.bind();
		      t.bind();
		      GL11.glBegin(GL11.GL_QUADS);
		         GL11.glTexCoord2f(textureOffsetX, textureOffsetY);
		         GL11.glVertex2f(x, y);
		         GL11.glTexCoord2f(textureOffsetX, textureOffsetY + textureHeight);
		         GL11.glVertex2f(x, y + height);
		         GL11.glTexCoord2f(textureOffsetX + textureWidth, textureOffsetY + textureHeight);
		         GL11.glVertex2f(x + width, y + height);
		         GL11.glTexCoord2f(textureOffsetX + textureWidth, textureOffsetY);
		         GL11.glVertex2f(x + width, y);
		      GL11.glEnd();
		
		}
	}
	
	public void checkforFinish(){
		if(h.intersects(finish.get(0))){
			level++;
			ground.clear();
			finish.clear();
			enemy.clear();
			resetLevel();
		}
		if(level == 12 && enterKeyPressed){
			level = 0;
			ground.clear();
			finish.clear();
			enemy.clear();
			resetLevel();
			enterKeyPressed = false;

		}
			
	}
	
	public void handleEnemyCollision(){
		for(int i = 0;i < enemy.size();i++){
			if(h.intersects(enemy.get(i))){
				if(level == 10){
					cWY = -1100;
					cWLoc = 0;
					enemy.clear();
				}
				
				resetLevel();
			}
				
		}
	}
	
	public void resetLevel(){
		switch(level){
		case 0:
			h.setX(20);
			h.setY(400);
			break;
		case 1:
			h.setX(45);
			h.setY(435);
			break;
		case 2:
			h.setX(1120);
			h.setY(450);
			break;
		case 3:
			h.setX(20);
			h.setY(450);
			break;
		case 4:
			h.setX(1170);
			h.setY(20);
			break;
		case 5:
			h.setX(30);
			h.setY(445);
			break;

		case 6:
			h.setX(1140);
			h.setY(520);
			break;
		case 7:
			h.setX(20);
			h.setY(20);
			break;
		case 8:
			h.setX(1160);
			h.setY(400);
			break;
		case 9:
			h.setX(10);
			h.setY(510);
			break;
		case 10:
			h.setX(10);
			h.setY(20);
			break;
		case 11:
			h.setX(20);
			h.setY(400);
			break;
		}
	}

	public void printMouseClick()
	{
		int width = 150;
		int height = 150;
		//Blinker

		if(Mouse.isButtonDown(0)){
			
			System.out.println( "ground.add(new Rectangle(" +  Mouse.getX() +","+(600-Mouse.getY())+"," + width + "," + height + "));");
			System.out.println( "GL11.glRecti(" + Mouse.getX() +","+(600-Mouse.getY())+","+ ( Mouse.getX()+width) + "," + ((600-Mouse.getY())+height) + ");");
			
	
		}
		if(Mouse.isButtonDown(1)){
			//GL11.glColor3d(0, 2, 0);
			//GL11.glRecti(h.getX(),h.getY(), h.getX()+25, h.getY()+50);
//			if(graphics)
//				graphics = false;
//			else if(!graphics)
//				graphics = true;
				
		}
	}
	
	public static void main(String[] args) throws LWJGLException{
		new Main();
	}

}
