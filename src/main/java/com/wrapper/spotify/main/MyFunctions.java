package com.wrapper.spotify.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.PlaylistTracksRequest;
import com.wrapper.spotify.methods.TopTracksRequest;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.PlaylistTrack;
import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.Track;

public class MyFunctions {
	
	public static final String USER_ID = "113332110";
	public static final String CLIENT_ID = "f61f0c0e79a847bb81b6b0dc0bb1d560";
	public static final String CLIENT_SECRET_ID = "55895fa91cb24d1d9d5d92aaeafe0cc5";
	public static final String PLAYLIST_1 = "2RDB4sWGcFQwXV3xAtZmYW";
	public static final String PLAYLIST_2 = "1wNTPhOFWnLyCeyyTt1sAH";
	public static final String PLAYLIST_3 = "2mVg7Vz8GnRbQ84Oc7mjuv";
	public static final String PLAYLIST_4 = "5cWd4i7jpufWxWVFgOQY12";
	public static final String[] COUNTRIES = { "AD", "AR", "AT", "AU", "BE", "BG", "BO", "BR", "CA", "CH", "CL", "CO", "CR", "CY", "CZ", "DE", "DK", "DO", "EC", "EE", "ES", "FI", "FR", "GB", "GR", "GT", "HK", "HN", "HU", "IE", "IS", "IT", "LI", "LT", "LU", "LV", "MC", "MT", "MX", "MY", "NI", "NL", "NO", "NZ", "PA", "PE", "PH", "PL", "PT", "PY", "RO", "SE", "SG", "SI", "SK", "SV", "TR", "TW", "US", "UY" };

	public static List<PlaylistTrack> getPlaylistTrack (String ownerID, String playlistID, Api api) {
		PlaylistTracksRequest request = api.getPlaylistTracks(ownerID, playlistID).build();
		List<PlaylistTrack> playlistTracks = new ArrayList<PlaylistTrack>();
		
		try {
		   Page<PlaylistTrack> page = request.get();
		   playlistTracks = page.getItems();

		} catch (Exception e) {
		   System.out.println("Something went wrong!" + e.getMessage());
		}
		return playlistTracks;
	}
	
	public static List<Track> getTopTracks (String artistId, String countryCode, Api api) {
		TopTracksRequest request = api.getTopTracksForArtist(artistId, countryCode).build();
		List<Track> topTracks = new ArrayList<Track>();
		
		try {
			topTracks = request.get();
		} catch (Exception e) {
		   System.out.println("Something went wrong!" + e.getMessage());
		}
		return topTracks;
	}
	
	public static Api getApi() {
		/* Client Credentials flow */	
		final Api api = Api.builder()
		  .clientId(CLIENT_ID)
		  .clientSecret(CLIENT_SECRET_ID)
		  .build();
		
		/* Create a request object. */
		ClientCredentialsGrantRequest request = api.clientCredentialsGrant().build();
		
		/* Use the request object to make the request, either asynchronously (getAsync) or synchronously (get) */
		SettableFuture<ClientCredentials> responseFuture = request.getAsync();
		
		/* Add callbacks to handle success and failure */
		Futures.addCallback(responseFuture, new FutureCallback<ClientCredentials>() {
		  
		  public void onSuccess(ClientCredentials clientCredentials) {
		    /* The tokens were retrieved successfully! */
		    System.out.println("Successfully retrieved an access token! " + clientCredentials.getAccessToken());
		    System.out.println("The access token expires in " + clientCredentials.getExpiresIn() + " seconds");
		
		    /* Set access token on the Api object so that it's used going forward */
		    api.setAccessToken(clientCredentials.getAccessToken());
		    
		    /* Please note that this flow does not return a refresh token.
		     * That's only for the Authorization code flow */
		  }
		
		  public void onFailure(Throwable throwable) {
		    /* An error occurred while getting the access token. This is probably caused by the client id or
		     * client secret is invalid. */
			  System.out.println("ERROR 'Client Credentials flow'");
		  }
		});
		return api;
	}
	
	public static List<String> getResultsList() {		
		Api api = getApi();
		
		List<String> playlistIdsList = new ArrayList<String>();
		List<PlaylistTrack> playlistTracksList = new ArrayList<PlaylistTrack>();
		List<Track> topTracksList = new ArrayList<Track>();
		List<String> countryCodeList = new ArrayList<String>();
		SimpleArtist artist;
        List<String> finalList = new ArrayList<String>();
        
		/*
		 * Calendar calendar = Calendar.getInstance();
		 * int year = calendar.get(Calendar.YEAR);
		 * int month = calendar.get(Calendar.MONTH);
		 * int day = calendar.get(Calendar.DAY_OF_MONTH);
		 */
        
		playlistIdsList.add(PLAYLIST_1);
		playlistIdsList.add(PLAYLIST_2);
		playlistIdsList.add(PLAYLIST_3);
		playlistIdsList.add(PLAYLIST_4);
		for (String country : COUNTRIES) { countryCodeList.add(country); }
		
	    for (String playlistsId : playlistIdsList) {
			playlistTracksList = getPlaylistTrack(USER_ID, playlistsId, api);
			for (PlaylistTrack playlistTrack : playlistTracksList) {
				artist = playlistTrack.getTrack().getArtists().get(0);
				for (String countryCode : countryCodeList) {
					topTracksList = getTopTracks(artist.getId(), countryCode, api);
					for (Track track : topTracksList) {
						finalList.add(countryCode + " | " + track.getArtists().get(0).getId() + " | " + track.getArtists().get(0).getName() + " | " + track.getId() + " | " + track.getName() + " | " + track.getPopularity());
					}
				}
			}
		}
		return finalList;
	}
	
}
