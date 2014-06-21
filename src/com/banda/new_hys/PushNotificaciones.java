package com.banda.new_hys;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class PushNotificaciones extends Activity {

	private HumildadySoledadBbAdapter dbAdapter;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notificaciones);

		Intent intent = getIntent();

		if (intent.getExtras().getString("com.parse.Data") != null) {

			try {
				JSONObject json = new JSONObject(intent.getExtras().getString(
						"com.parse.Data"));
				String notificationText = json.getString("alert");

				TextView txt = (TextView) findViewById(R.id.NotificationVist);
				txt.setText(notificationText);

				dbAdapter = new HumildadySoledadBbAdapter(this);
				dbAdapter.abrir();

				ContentValues reg = new ContentValues();
				reg.put(HumildadySoledadBbAdapter.C_COLUMNA_ALERTA,
						notificationText);

				dbAdapter.insert(reg);
				dbAdapter.cerrar();

			} catch (JSONException e) {
				Log.e(e.getMessage(), "Error al parsear JSON de parse.com");
			}
		} else {

			HashMap<String, String> meMap = new HashMap<String, String>();

			Bundle bundle = getIntent().getExtras();
			meMap = (HashMap<String, String>) bundle.getSerializable("Mensaje");

			TextView txt = (TextView) findViewById(R.id.NotificationVist);
			txt.setText(meMap.get("Mensaje"));

		}
	}
}
