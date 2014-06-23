package com.banda.new_hys;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

public class Application extends android.app.Application {

	public Application() {
	}

	@Override
	public void onCreate() {
		super.onCreate();

		// Initialize the Parse SDK.
		Parse.initialize(this, "4HfhwXVQc79EYtQGYOxXPlesobCMLBqhBotkFCv8",
				"Do2MgVEyOnGDk3vmY0X6FPfPU7FsuTVCFgnj8WGC");

		// Specify an Activity to handle all pushes by default.
		PushService.setDefaultPushCallback(this, PushNotificaciones.class);
		
		//Guardar en BackGround
		ParseInstallation.getCurrentInstallation().saveInBackground();
	}
}
