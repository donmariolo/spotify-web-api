package com.wrapper.spotify.main;

import java.util.List;

public interface TrackProvider {
	void start();
	void stop();
	List<String> getResult();
}
