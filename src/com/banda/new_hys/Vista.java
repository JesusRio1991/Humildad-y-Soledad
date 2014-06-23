package com.banda.new_hys;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Vista extends Activity {

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vista);

		HashMap<String, String> meMap = new HashMap<String, String>();

		Bundle bundle = getIntent().getExtras();
		meMap = (HashMap<String, String>) bundle.getSerializable("Data");

		String[] value0 = new String[] { meMap.get("Fecha") };
		String[] value1 = new String[] { meMap.get("Ciudad") };
		String[] value2 = new String[] { meMap.get("Uniforme") };
		String[] value3 = new String[] { meMap.get("Horario") };
		String[] value4 = new String[] { meMap.get("QuedadaR") };
		String[] value5 = new String[] { meMap.get("QuedadaS") };

		ArrayAdapter<String> adapter0 = new ArrayAdapter<String>(this,
				R.layout.list_item_vista, R.id.txtVista, value0);

		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				R.layout.list_item_vista, R.id.txtVista, value1);

		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				R.layout.list_item_vista, R.id.txtVista, value2);

		ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
				R.layout.list_item_vista, R.id.txtVista, value3);

		ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this,
				R.layout.list_item_vista, R.id.txtVista, value4);

		ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(this,
				R.layout.list_item_vista, R.id.txtVista, value5);

		TextView txt = (TextView) findViewById(R.id.txtHermandad);
		txt.setText(meMap.get("Mensaje"));

		ListView lst0 = (ListView) findViewById(R.id.lstFecha);
		lst0.setAdapter(adapter0);

		ListView lst1 = (ListView) findViewById(R.id.lstCiudad);
		lst1.setAdapter(adapter1);

		ListView lst2 = (ListView) findViewById(R.id.lstUniforme);
		lst2.setAdapter(adapter2);

		ListView lst3 = (ListView) findViewById(R.id.lstHorario);
		lst3.setAdapter(adapter3);

		ListView lst4 = (ListView) findViewById(R.id.lstQuedadaR);
		lst4.setAdapter(adapter4);

		ListView lst5 = (ListView) findViewById(R.id.lstQuedadaS);
		lst5.setAdapter(adapter5);

		final String Img = meMap.get("Img");

		if ( Img.equals(" ") )
			Toast.makeText(Vista.this, "�No hay imagen!", Toast.LENGTH_LONG).show();
		else {

			new Thread(new Runnable() {
				public void run() {
					
					final ImageView imageView = (ImageView) findViewById(R.id.imageVista);
					final ProgressBar pgb = (ProgressBar) findViewById(R.id.progressBar1);
					pgb.setVisibility(View.VISIBLE);
					URL req = null;
					
					try {
						req = new URL(Img);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}

					try {
						final Bitmap mIcon_val = BitmapFactory.decodeStream(req
								.openConnection().getInputStream());
						imageView.post((new Runnable() {
							public void run() {

								Display display = getWindowManager()
										.getDefaultDisplay();
								int width = ((display.getWidth() * 80) / 100);
								int height = ((display.getHeight() * 30) / 100);
								LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(
										width, height);
								imageView.setLayoutParams(parms);

								imageView.setImageBitmap(mIcon_val);
								pgb.setVisibility(View.INVISIBLE);
							} // run
						})); // post

					} catch (IOException e) {
						Toast.makeText(Vista.this,
								"¡No se ha podido cargar la imagen!",
								Toast.LENGTH_LONG).show();
					} // catch
				} // run
			}).start(); // Thread

		} // else

	} // onCreate

} // Vista
