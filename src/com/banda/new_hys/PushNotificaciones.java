package com.banda.new_hys;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class PushNotificaciones extends Activity {

	private HumildadySoledadBbAdapter dbAdapter;
	HashMap<String, String> meMap = new HashMap<String, String>();

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notificaciones);

		TextView textView2 = (TextView) findViewById(R.id.textView2);
		SpannableStringBuilder ssb = new SpannableStringBuilder(
				"      Compartir");
		Bitmap smiley = BitmapFactory.decodeResource(getResources(),
				R.drawable.share);
		ssb.setSpan(new ImageSpan(smiley), 0, 4, 0);
		textView2.setText(ssb, BufferType.SPANNABLE);

		TextView textView1 = (TextView) findViewById(R.id.textView1);
		SpannableStringBuilder ssb1 = new SpannableStringBuilder("      Borrar");
		Bitmap smiley1 = BitmapFactory.decodeResource(getResources(),
				R.drawable.delete);
		ssb1.setSpan(new ImageSpan(smiley1), 0, 4,
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		textView1.setText(ssb1, BufferType.SPANNABLE);

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

			Bundle bundle = getIntent().getExtras();
			meMap = (HashMap<String, String>) bundle.getSerializable("Mensaje");

			TextView txt = (TextView) findViewById(R.id.NotificationVist);
			txt.setText(meMap.get("Mensaje"));

		}
	}

	@SuppressWarnings("unchecked")
	public void borrarNoticia(View view) {

		Intent intent = getIntent();
		dbAdapter = new HumildadySoledadBbAdapter(this);
		dbAdapter.abrir();

		if (intent.getExtras().getString("com.parse.Data") != null) {

			try {
				JSONObject json = new JSONObject(intent.getExtras().getString(
						"com.parse.Data"));
				final String notificationText = json.getString("alert");

				AlertDialog.Builder adb = new AlertDialog.Builder(this);
				adb.setTitle("Borrar elemento");
				adb.setMessage("¿Desea eliminar este elemento?");
				adb.setIcon(android.R.drawable.ic_dialog_alert);

				adb.setPositiveButton("SI",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								dbAdapter.delete(notificationText);
								dbAdapter.cerrar();
								finish();
							}
						});

				adb.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});

				adb.show();

			} catch (JSONException e) {
				Log.e(e.getMessage(), "Error al parsear JSON de parse.com");
			}

		} else {

			Bundle bundle = getIntent().getExtras();
			meMap = (HashMap<String, String>) bundle.getSerializable("Mensaje");

			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setTitle("Borrar elemento");
			adb.setMessage("¿Desea eliminar este elemento?");
			adb.setIcon(android.R.drawable.ic_dialog_alert);

			adb.setPositiveButton("SI", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dbAdapter.delete(meMap.get("Mensaje"));
					dbAdapter.cerrar();
					finish();

				}
			});

			adb.setNegativeButton("NO", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

				}
			});

			adb.show();

		}

	}

	@SuppressWarnings("unchecked")
	public void compartirNoticia(View view) {

		Intent intent = getIntent();
		String notificationText = null;

		if (intent.getExtras().getString("com.parse.Data") != null) {

			try {
				JSONObject json = new JSONObject(intent.getExtras().getString(
						"com.parse.Data"));
				notificationText = json.getString("alert");

			} catch (JSONException e) {
				Log.e(e.getMessage(), "Error al parsear JSON de parse.com");
			}

		} else {

			Bundle bundle = getIntent().getExtras();
			meMap = (HashMap<String, String>) bundle.getSerializable("Mensaje");
			notificationText = meMap.get("Mensaje");

		}

		String message = notificationText;
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("text/plain");
		share.putExtra(Intent.EXTRA_TEXT, message);

		startActivity(Intent.createChooser(share, "Compartir con"));

	}

}
