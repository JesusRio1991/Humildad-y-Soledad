package com.banda.new_hys;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class MainActivity extends Activity {

	private HumildadySoledadBbAdapter dbAdapter;
	private checkActualizar mVC = new checkActualizar();
	ProgressDialog progressBar;
	private Handler handler = new Handler();
	private List<Map<String, String>> data;
	private SimpleAdapter adapter;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// -------------------------------------------------- //
		// Forzar la salida a Internet en Android 4.0 +
		// -------------------------------------------------- //

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		// -------------------------------------------------- //
		// Adaptador y ListView para el apartado de NOTICIAS
		// -------------------------------------------------- //

		adapter = getAdapter();

		final ListView listaUltimasNoticias = (ListView) findViewById(R.id.lstActuaciones);
		listaUltimasNoticias.setAdapter(adapter);
		updateListViewHeight(listaUltimasNoticias);

		listaUltimasNoticias.setLongClickable(true);

		listaUltimasNoticias
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@SuppressWarnings("unchecked")
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, final int pos, long id) {
						// TODO Auto-generated method stub
						final HashMap<String, String> meMap = (HashMap<String, String>) listaUltimasNoticias
								.getItemAtPosition(pos);

						AlertDialog.Builder adb = new AlertDialog.Builder(
								MainActivity.this);

						adb.setTitle("¿Desea eliminar este elemento?");
						adb.setIcon(android.R.drawable.ic_dialog_alert);

						adb.setPositiveButton("SI",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dbAdapter = new HumildadySoledadBbAdapter(
												MainActivity.this);
										dbAdapter.abrir();
										dbAdapter.delete(meMap.get("Mensaje"));
										dbAdapter.cerrar();
										listaUltimasNoticias.invalidate();
										listaUltimasNoticias
												.setAdapter(getAdapter());
										listaUltimasNoticias.invalidate();
										adapter.notifyDataSetChanged();
									}
								});

						adb.setNegativeButton("NO",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

									}
								});

						adb.show();

						return true;

					}
				});

		listaUltimasNoticias.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int posicion, long arg3) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> meMap = (HashMap<String, String>) listaUltimasNoticias
						.getItemAtPosition(posicion);

				Intent intent = new Intent(MainActivity.this,
						PushNotificaciones.class);
				intent.putExtra("Mensaje", meMap);
				startActivity(intent);

			}
		});

		// -------------------------------------------------- //
		// Adaptador y ListView para el apartado de ACTUACIONES
		// -------------------------------------------------- //

		SimpleAdapter adapter1 = new SimpleAdapter(this,
				Actuaciones.getActuaciones(this, 2),
				R.layout.list_item_ultimasactuaciones, new String[] {
						"Mensaje", "Fecha" }, new int[] { R.id.txtVista,
						R.id.txtFechaSemanaSanta });

		final ListView listaUltimasActuaciones = (ListView) findViewById(R.id.lstUltimasActuaciones);
		listaUltimasActuaciones.setAdapter(adapter1);
		updateListViewHeight(listaUltimasActuaciones);

		listaUltimasActuaciones
				.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int posicion, long arg3) {
						@SuppressWarnings("unchecked")
						HashMap<String, String> meMap = (HashMap<String, String>) listaUltimasActuaciones
								.getItemAtPosition(posicion);

						Intent intent = new Intent(MainActivity.this,
								Vista.class);
						intent.putExtra("Data", meMap);
						startActivity(intent);

					}
				});

		// -------------------------------------------------- //
		// Adaptador y ListView para el apartado de SEMANA SANTA
		// -------------------------------------------------- //

		SimpleAdapter adapter2 = new SimpleAdapter(this,
				SemanaSanta.getSemanaSanta(this, 2),
				R.layout.list_item_ultimassemanasanta, new String[] {
						"Mensaje", "Fecha" }, new int[] { R.id.txtVista,
						R.id.txtFechaSemanaSanta });

		final ListView listaUltimasSemanaSanta = (ListView) findViewById(R.id.lstUltimasSemanaSanta);
		listaUltimasSemanaSanta.setAdapter(adapter2);
		updateListViewHeight(listaUltimasSemanaSanta);

		listaUltimasSemanaSanta
				.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int posicion, long arg3) {

						@SuppressWarnings("unchecked")
						HashMap<String, String> meMap = (HashMap<String, String>) listaUltimasSemanaSanta
								.getItemAtPosition(posicion);

						Intent intent = new Intent(MainActivity.this,
								Vista.class);
						intent.putExtra("Data", meMap);
						startActivity(intent);

					}
				});

		// -------------------------------------------------- //
		// Adaptador y ListView para el apartado de LA WEB
		// -------------------------------------------------- //

		SimpleAdapter adapter3 = new SimpleAdapter(this, getWeb(this, 2),
				R.layout.list_item_ultimasweb, new String[] { "Mensaje" },
				new int[] { R.id.txtVista, });

		final ListView listaTwitter = (ListView) findViewById(R.id.lstTwitter);
		listaTwitter.setAdapter(adapter3);
		updateListViewHeight(listaTwitter);

		listaTwitter.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int posicion, long arg3) {

				@SuppressWarnings("unchecked")
				HashMap<String, String> meMap = (HashMap<String, String>) listaTwitter
						.getItemAtPosition(posicion);

				String Link = meMap.get("Link");

				startActivity(new Intent("android.intent.action.VIEW", Uri
						.parse(Link)));

			}
		});

		dbAdapter.cerrar();
	}

	// Se Obtiene los datos XML y se llama a ObtenerTexto para obtener el texto
	public static List<Map<String, String>> getWeb(Context cxt, int contador) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();

		try {

			FileInputStream fil = cxt.openFileInput("Feed.xml");

			// Creamos un nuevo parser DOM
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Realizamos lalectura completa del XML
			Document dom = builder.parse(fil);

			// Nos posicionamos en el nodo principal del árbol (<rss>)
			Element root = dom.getDocumentElement();

			// Localizamos todos los elementos <item>
			NodeList items = root.getElementsByTagName("item");

			if (contador == 0) {
				contador = items.getLength();
			}

			for (int i = 0; i < contador; i++) {

				HashMap<String, String> datum = new HashMap<String, String>();

				// Obtenemos la noticia actual
				Node item = items.item(i);

				// Obtenemos la lista de datos de la noticia actual
				NodeList datosNoticia = item.getChildNodes();

				// Procesamos cada dato de la noticia
				for (int j = 0; j < datosNoticia.getLength(); j++) {
					Node dato = datosNoticia.item(j);
					String etiqueta = dato.getNodeName();

					if (etiqueta.equals("title")) {
						datum.put("Mensaje", "" + obtenerTexto(dato));
					}
					if (etiqueta.equals("link")) {
						datum.put("Link", "" + obtenerTexto(dato));
					}
				}

				data.add(datum);

			}

		} catch (Exception ex) {
			Log.e("XmlTips", "Error al leer fichero XML.");

		}

		return data;
	}

	// Se Obtiene el texto del XML
	private static String obtenerTexto(Node dato) {
		StringBuilder texto = new StringBuilder();
		NodeList fragmentos = dato.getChildNodes();

		for (int k = 0; k < fragmentos.getLength(); k++) {
			texto.append(fragmentos.item(k).getNodeValue());
		}

		return texto.toString();
	}

	// Actualiza la vista del ListView para poder ponerla dentro del ScrollView
	public static void updateListViewHeight(ListView myListView) {

		ListAdapter myListAdapter = myListView.getAdapter();
		int totalHeight = 0;

		if (myListAdapter == null) {
			return;
		}

		for (int size = 0; size < myListAdapter.getCount(); size++) {
			View listItem = myListAdapter.getView(size, null, myListView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = myListView.getLayoutParams();
		params.height = totalHeight
				+ (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));

		myListView.setLayoutParams(params);
	}

	// Crea el menú de la aplicación
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	// Obtiene que item del menú ha sido seleccionado
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.actualizar:

			// create and display a new ProgressBarDialog
			progressBar = new ProgressDialog(this);
			progressBar.setCancelable(true);
			progressBar.setMessage("Comprobando actualizaciones ...");
			progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressBar.setProgress(0);
			progressBar.setMax(100);
			progressBar.show();

			Thread downloadThread = new Thread(backgroundDownload,
					"checkActualizar");
			downloadThread.start();

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// Obtiene los datos de la aplicacion para actualizar
	private Runnable backgroundDownload = new Runnable() {
		@Override
		public void run() {
			mVC.getData(MainActivity.this);
			handler.post(finishBackgroundDownload);
		}
	};

	// Descarga la version actualizada o nos dice que ya está actualizada
	private Runnable finishBackgroundDownload = new Runnable() {
		@SuppressLint("ShowToast")
		@Override
		public void run() {
			progressBar.dismiss();
			if (mVC.isNewVersionAvailable()) {
				// Hay una nueva versión disponible
				startActivity(new Intent("android.intent.action.VIEW",
						Uri.parse(mVC.getDownloadURL())));

			} else {
				Toast.makeText(MainActivity.this,
						"¡Tu aplicación está actualizada!", Toast.LENGTH_LONG)
						.show();

			}
		}
	};

	// Intent a Actuaciones
	public void goActuaciones(View view) {
		Intent intent = new Intent(MainActivity.this, Actuaciones.class);
		startActivity(intent);
	}

	// Intent a Noticias
	public void goNoticias(View view) {
		Intent intent = new Intent(MainActivity.this, Notificaciones.class);
		startActivity(intent);
	}

	// Intente a Semana Santa
	public void goSemanaSanta(View view) {
		Intent intent = new Intent(MainActivity.this, SemanaSanta.class);
		startActivity(intent);
	}

	public List<Map<String, String>> getData() {

		dbAdapter = new HumildadySoledadBbAdapter(this);
		dbAdapter.abrir();
		data = dbAdapter.getCursor("2");
		dbAdapter.cerrar();

		return data;
	}

	public SimpleAdapter getAdapter() {

		dbAdapter = new HumildadySoledadBbAdapter(this);
		dbAdapter.abrir();
		data = getData();
		dbAdapter.cerrar();

		SimpleAdapter adapter = new SimpleAdapter(this, data,
				R.layout.list_item_ultimasnoticias, new String[] { "Mensaje",
						"Hora" }, new int[] { R.id.txtVista,
						R.id.txtFechaSemanaSanta });

		return adapter;

	}
}
