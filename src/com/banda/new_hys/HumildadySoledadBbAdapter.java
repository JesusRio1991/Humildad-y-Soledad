package com.banda.new_hys;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class HumildadySoledadBbAdapter {

	/**
	 * Definimos constante con el nombre de la tabla
	 */
	public static final String C_TABLA = "HYS";

	/**
	 * Definimos constantes con el nombre de las columnas de la tabla
	 */
	public static final String C_COLUMNA_ID = "_id";
	public static final String C_COLUMNA_ALERTA = "hip_notificacion";
	public static final String C_COLUMNA_FECHA = "hip_fecha";

	private static Context contexto;
	private static HumildadySoledadBd dbHelper;
	private static SQLiteDatabase db;

	/**
	 * Definimos lista de columnas de la tabla para utilizarla en las consultas
	 * a la base de datos
	 */
	private static String[] columnas = new String[] { C_COLUMNA_ID,
			C_COLUMNA_ALERTA, C_COLUMNA_FECHA };

	public HumildadySoledadBbAdapter(Context context) {
		HumildadySoledadBbAdapter.contexto = context;
	}

	public HumildadySoledadBbAdapter abrir() throws SQLException {
		dbHelper = new HumildadySoledadBd(contexto);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void cerrar() {
		dbHelper.close();
		db.close();
	}

	/**
	 * Devuelve cursor con todos las columnas de la tabla
	 */
	@SuppressLint("SimpleDateFormat")
	public ArrayList<Map<String, String>> getCursor(String howMuch)
			throws SQLException {

		if (db == null)
			abrir();

		ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();

		if (howMuch.equals("Todos")) {
			howMuch = null;
		}

		Cursor c = db.query(true, C_TABLA, columnas, null, null, null, null,
				C_COLUMNA_ID + " DESC", howMuch);

		int id = c.getColumnIndex("hip_notificacion");
		int fecha = c.getColumnIndex("hip_fecha");

		SimpleDateFormat _inputFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat _outputFormat = new SimpleDateFormat("dd/MM/yyyy");

		_inputFormat.setTimeZone(TimeZone.getTimeZone("GTM"));
		Date parsed = null;

		Calendar cal = new GregorianCalendar();
		Date date = cal.getTime();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String formatteDate = df.format(date);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			HashMap<String, String> datum = new HashMap<String, String>();
			datum.put("Mensaje", "" + c.getString(id));

			try {
				parsed = _inputFormat.parse(c.getString(fecha));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (_outputFormat.format(parsed).equals(formatteDate)) {
				datum.put("Hora", "" + "Hoy");
			} else {
				datum.put("Hora", "" + _outputFormat.format(parsed));
			}

			data.add(datum);
		}

		c.close();

		if (db != null)
			cerrar();

		return data;
	}

	public long insert(ContentValues reg) {
		if (db == null)
			abrir();

		return db.insert(C_TABLA, null, reg);
	}

	public boolean delete(String name) {
		if (db == null)
			abrir();

		return db.delete(C_TABLA, C_COLUMNA_ALERTA + "='" + name + "'", null) > 0;
	}
}