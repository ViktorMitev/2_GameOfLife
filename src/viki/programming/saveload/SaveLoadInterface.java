package viki.programming.saveload;

import viki.programming.gameoflife.GameSimulator;

public interface SaveLoadInterface {
	public void save(GameSimulator model);
	public GameSimulator load(String name);
}