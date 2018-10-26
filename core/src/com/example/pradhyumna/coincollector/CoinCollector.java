package com.example.pradhyumna.coincollector;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class CoinCollector extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man ;
	int stateOfMan=0;
	int pause=0;
	float velocity = 0;
	float gravity = 0.4f;
	int many = 0;
	Rectangle manRectangle;

	int gameState = 0;

	Random random;

	ArrayList<Integer> coinX = new ArrayList<Integer>();
	ArrayList<Integer> coinY = new ArrayList<Integer>();
	ArrayList<Rectangle> coinRectangle = new ArrayList<Rectangle>();
	Texture coin;
	int noOfCoin;

	ArrayList<Integer> bombX = new ArrayList<Integer>();
	ArrayList<Integer> bombY = new ArrayList<Integer>();
	ArrayList<Rectangle> bombRectangle = new ArrayList<Rectangle>();
	Texture bomb;
	int noOfBomb;

	int score = 0;
	BitmapFont font;
    BitmapFont fontGame;
	Texture dizzyGameOver;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man = new Texture[4];
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");

		many = Gdx.graphics.getHeight()/2;

		coin = new Texture("coin.png");
		bomb = new Texture("bomb.png");

		random = new Random();

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
        fontGame = new BitmapFont();
        fontGame.setColor(Color.WHITE);
        fontGame.getData().setScale(10);

		dizzyGameOver = new Texture("dizzy-1.png");
	}

	public void coining(){
		float heightCoin = random.nextFloat() * (Gdx.graphics.getHeight()-coin.getHeight());
		coinY.add((int) heightCoin);
		//float widthCoin = random.nextFloat() * Gdx.graphics.getWidth();
		coinX.add(Gdx.graphics.getWidth());
	}

	public void bombing(){
		float heightBomb = random.nextFloat() * (Gdx.graphics.getHeight()-bomb.getHeight());
		bombY.add((int) heightBomb);
		//float widthCoin = random.nextFloat() * Gdx.graphics.getWidth();
		bombX.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {
		batch.begin();

		batch.draw(background , 0 , 0 , Gdx.graphics.getWidth() , Gdx.graphics.getHeight());

		if(gameState == 1){
			//LIVE
			//COINS
			if(noOfCoin < 100){
				noOfCoin++;
			}
			else {
				noOfCoin = 0;
				coining();
			}
			coinRectangle.clear();
			for(int i = 0 ; i<coinX.size() ; i++){
				batch.draw(coin , coinX.get(i) , coinY.get(i),coin.getWidth(),coin.getHeight());
				coinX.set(i , coinX.get(i)-4);
				coinRectangle.add(new Rectangle(coinX.get(i) , coinY.get(i) , coin.getWidth() , coin.getHeight()));
			}

			//BOMBS
			if(noOfBomb < 250){
				noOfBomb++;
			}
			else {
				noOfBomb = 0;
				bombing();
			}
			bombRectangle.clear();
			for(int i = 0 ; i<bombX.size() ; i++){
				batch.draw(bomb , bombX.get(i) , bombY.get(i),bomb.getWidth(),bomb.getHeight());
				bombX.set(i , bombX.get(i)-8);
				bombRectangle.add(new Rectangle(bombX.get(i) , bombY.get(i) , bomb.getWidth() , bomb.getHeight()));
			}


			if(Gdx.input.justTouched()){
				velocity = -15;
			}

			if(pause < 6){
				pause++;
			}
			else{
				pause = 0;
				if(stateOfMan<3){
					stateOfMan++;
				}
				else{
					stateOfMan=0;
				}
			}
			velocity+=gravity;
			many -=velocity;
			if(many <= 0){
				many = 0;
			}
			else if(many>=Gdx.graphics.getHeight()-man[0].getHeight())
			{
				many=Gdx.graphics.getHeight()-man[0].getHeight();
				velocity=0;
			}
		}
		else if(gameState == 0){
			//WAITING
			if(Gdx.input.justTouched()) {
				gameState = 1;
			}
		}
		else if(gameState == 2){
			//OVER
			if(Gdx.input.justTouched()) {
				gameState = 1;
				score = 0;
				many = Gdx.graphics.getHeight()/2;
				velocity = 0;
				coinX.clear();
				coinY.clear();
				coinRectangle.clear();
				bombX.clear();
				bombY.clear();
				bombRectangle.clear();
				noOfCoin = 0;
				noOfBomb = 0;
			}
		}

		if(gameState == 2){
			batch.draw(dizzyGameOver, Gdx.graphics.getWidth() / 2 - man[stateOfMan].getWidth() / 2, many);
		}

		else {
			batch.draw(man[stateOfMan], Gdx.graphics.getWidth() / 2 - man[stateOfMan].getWidth() / 2, many);
		}
		manRectangle = new Rectangle(Gdx.graphics.getWidth()/2 - man[stateOfMan].getWidth()/2 , many , man[stateOfMan].getWidth() , man[stateOfMan].getHeight());
		for(int i=0 ; i < coinRectangle.size() ; i++){
			if(Intersector.overlaps(manRectangle , coinRectangle.get(i))){
				score++;
				coinRectangle.remove(i);
				coinX.remove(i);
				coinY.remove(i);
				break;
			}
		}
		for(int i=0 ; i < bombRectangle.size() ; i++){
			if(Intersector.overlaps(manRectangle , bombRectangle.get(i))){
				fontGame.draw(batch ,  "\n GAME OVER", 100 , 800);
				gameState = 2;
			}
		}

        font.draw(batch , String.valueOf(score) , 100 , 200);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
