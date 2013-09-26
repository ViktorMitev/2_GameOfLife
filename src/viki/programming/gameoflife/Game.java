package viki.programming.gameoflife;

import viki.programming.saveload.*;


public class Game {
	
	public static void main(String[] args) {
		
			GameSimulator model = new GameSimulator(15, 15);			
			GameInterface view = new GameInterface(model);
			SaveLoadFromProperty saveLoad = new SaveLoadFromProperty(model, view);
			@SuppressWarnings("unused")
			GameController controller = new GameController(model, view, saveLoad);
		
			view.display();

	}

	
}
