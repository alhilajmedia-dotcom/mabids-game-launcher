package io.github.mabid.utils;

public class GameItem {

	private final String NAME;
	private GameProperties.GameConfig config;
	
	public GameItem(GameProperties.GameConfig config) {
		this.config = config;
		NAME = config.name;
	}
	
	public String getName() {
		return NAME;
	}
	
	public void setConfig(GameProperties.GameConfig config) {
		this.config = config;
	}
	
	public GameProperties.GameConfig getConfig() {
		return config;
	}
}
