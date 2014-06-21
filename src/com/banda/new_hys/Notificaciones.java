package com.banda.new_hys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Notificaciones extends Activity {

	private HumildadySoledadBbAdapter dbAdapter;
	private List<Map<String, String>> data;
	private SimpleAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notificaciones_todos);

		dbAdapter = new HumildadySoledadBbAdapter(this);
		dbAdapter.abrir();

		adapter = getAdapter();

		final ListView listaUltimasNoticias = (ListView) findViewById(R.id.lstNoticias);
		listaUltimasNoticias.setAdapter(adapter);

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
								Notificaciones.this);

						adb.setTitle("¿Desea eliminar este elemento?");
						adb.setIcon(android.R.drawable.ic_dialog_alert);

						adb.setPositiveButton("SI",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
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

				Intent intent = new Intent(Notificaciones.this,
						PushNotificaciones.class);
				intent.putExtra("Mensaje", meMap);
				startActivity(intent);

			}
		});
		
		dbAdapter.cerrar();
	}

	public List<Map<String, String>> getData() {
		
		dbAdapter.abrir();
		data = dbAdapter.getCursor("Todos");

		return data;
	}
	
	public SimpleAdapter getAdapter(){
		
		
		final SimpleAdapter adapter = new SimpleAdapter(this, getData(),
				R.layout.list_item_ultimasnoticias, new String[] { "Mensaje",
						"Hora" }, new int[] { R.id.txtVista,
						R.id.txtFechaSemanaSanta });
		
		return adapter;
		
	}
}
