package com.wrapper.spotify.main;

import java.util.List;

public class RandomTrackProvider implements TrackProvider {

	public void start() {		
	}

	public void stop() {		
	}

	public List<String> getResult() {
		return MyFunctions.getResultsList();
	}

}
