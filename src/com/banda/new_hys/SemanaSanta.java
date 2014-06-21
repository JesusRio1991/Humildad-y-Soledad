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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SemanaSanta extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_semana_santa);

		SimpleAdapter adapter = new SimpleAdapter(this,
				getSemanaSanta(this, 0), R.layout.list_item_ultimassemanasanta,
				new String[] { "Mensaje", "Fecha" },
				new int[] { R.id.txtVista,
						R.id.txtFechaSemanaSanta });

		final ListView listaSemanaSanta = (ListView) findViewById(R.id.lstSemanaSanta);
		listaSemanaSanta.setAdapter(adapter);
		

		listaSemanaSanta.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int posicion, long arg3) {

				@SuppressWarnings("unchecked")
				HashMap<String, String> meMap = (HashMap<String, String>) listaSemanaSanta
						.getItemAtPosition(posicion);

				Intent intent = new Intent(SemanaSanta.this, Vista.class);
				intent.putExtra("Data", meMap);
				startActivity(intent);

			}
		});

	}

	public static List<Map<String, String>> getSemanaSanta(Context cxt,
			int contador) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();

		try {
			FileInputStream fil = cxt.openFileInput("SemanaSanta.xml");

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
			} else if (items.getLength() < contador) {
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

					if (etiqueta.equals("mensaje")) {
						datum.put("Mensaje", "" + obtenerTexto(dato));
					}
					if (etiqueta.equals("fecha")) {
						datum.put("Fecha", "" + obtenerTexto(dato));
					}
					if (etiqueta.equals("ciudad")) {
						datum.put("Ciudad", "" + obtenerTexto(dato));
					}
					if (etiqueta.equals("uniforme")) {
						datum.put("Uniforme", "" + obtenerTexto(dato));
					}
					if (etiqueta.equals("horario")) {
						datum.put("Horario", "" + obtenerTexto(dato));
					}
					if (etiqueta.equals("quedadaR")) {
						datum.put("QuedadaR", "" + obtenerTexto(dato));
					}
					if (etiqueta.equals("quedadaS")) {
						datum.put("QuedadaS", "" + obtenerTexto(dato));
					}
					if (etiqueta.equals("img")) {
						datum.put("Img", "" + obtenerTexto(dato));
					}
				}

				data.add(datum);
			}

		} catch (Exception ex) {
			Log.e("XmlTips", "Error al leer fichero XML.");
		}

		return data;
	}

	private static String obtenerTexto(Node dato) {
		StringBuilder texto = new StringBuilder();
		NodeList fragmentos = dato.getChildNodes();

		for (int k = 0; k < fragmentos.getLength(); k++) {
			texto.append(fragmentos.item(k).getNodeValue());
		}

		return texto.toString();
	}
}
