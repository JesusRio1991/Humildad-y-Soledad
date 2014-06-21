package com.banda.new_hys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class Inicio extends Activity {

	private long splashDelay = 1000; // 1 segundo ( 1000 milisegundos )

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inicio);

		// Forzar la salida a Internet a los Android 4.0+

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		// Iniciamos el MainActivity cuando el tiempo haya acabado!

		final TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Intent mainIntent = new Intent().setClass(Inicio.this,
						MainActivity.class);
				startActivity(mainIntent);
				finish();
			}
		};

		// Comprobamos en segundo plano si los archivos de Actuaciones y Semana
		// Santa han cambiado, si es así los descargamos en local

		new Thread(new Runnable() {
			public void run() {

				String urls = "http://humildadysoledad.es/APP/actuaciones.xml";
				String name = "Actuaciones";

				comprobarSiExiste(urls, name);

				urls = "http://humildadysoledad.es/APP/ss.xml";
				name = "SemanaSanta";

				comprobarSiExiste(urls, name);

				urls = "http://humildadysoledad.es/feed/";
				name = "Feed";

				comprobarSiExiste(urls, name);

				// Iniciamos el la cuenta atrás para ir al MainActivity
				
				runOnUiThread(new Runnable() {
					public void run() {
						Timer timer = new Timer();
						timer.schedule(task, splashDelay);
					}
				});
			}
		}).start();

	}

	// Comprueba si el el archivo pasado por parametro ha cambiado en el
	// servidor, si no, lo descarga
	public void comprobarSiExiste(String urls, String name) {

		long longDateFileWeb = LastModified(urls);
		Date dateFileWeb = new Date(longDateFileWeb);

		File fileDateInt = new File(this.getFilesDir(), name + ".txt");

		if (fileDateInt.exists()) {

			String dateFileInt = ultimaVezDate(name);

			if (dateFileWeb.toString().equals(dateFileInt)) {

			} else {

				fileDateInt.delete();
				ultimaVezDateGuardar(name, dateFileWeb.toString());
				DownloadFiles(urls, name);

			}
		} else {

			fileDateInt.delete();
			ultimaVezDateGuardar(name, dateFileWeb.toString());
			DownloadFiles(urls, name);
		}

	}

	// Guarda en un archivo la fecha de la ultima modificacion del archivo que
	// esta en el servidor
	public void ultimaVezDateGuardar(String name, String dateFile) {

		try {
			FileOutputStream fos = openFileOutput(name + ".txt", MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fos);

			osw.write(dateFile);
			osw.flush();
			osw.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	// Consulta la fecha alojada en el archivo interno de la aplicacion de la
	// ultima vez que se modifico el archivo del servidor y devuelve la fecha
	public String ultimaVezDate(String ultimaVez) {

		String s = "";

		try {
			FileInputStream fis = openFileInput(ultimaVez + ".txt");
			InputStreamReader isr = new InputStreamReader(fis);

			char[] inputBuffer = new char[100];

			int charRead;
			while ((charRead = isr.read(inputBuffer)) > 0) {

				String readString = String
						.copyValueOf(inputBuffer, 0, charRead);
				s += readString;

				inputBuffer = new char[100];
			}

			isr.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return s;
	}

	// Consulta la fecha de la ultima vez que el archivo del servidor fue
	// modificado
	public static long LastModified(String url) {
		HttpURLConnection.setFollowRedirects(false);
		HttpURLConnection con = null;

		try {
			con = (HttpURLConnection) new URL(url).openConnection();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long date = con.getLastModified();

		return date;
	}

	// Descarga los archivos del servidor para alojarlos en local
	@SuppressWarnings("static-access")
	public void DownloadFiles(String urls, String name) {

		try {
			URL url = new URL(urls);
			URLConnection conexion = url.openConnection();
			conexion.connect();
			int lenghtOfFile = conexion.getContentLength();

			InputStream is = url.openStream();
			FileOutputStream fos;

			byte data[] = new byte[1024];
			int count = 0;
			long total = 0;
			int progress = 0;

			fos = openFileOutput(name + ".xml", this.MODE_PRIVATE);

			while ((count = is.read(data)) != -1) {
				total += count;
				int progress_temp = (int) total * 100 / lenghtOfFile;
				if (progress_temp % 10 == 0 && progress != progress_temp) {
					progress = progress_temp;
				}
				fos.write(data, 0, count);

			}
			is.close();
			fos.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
