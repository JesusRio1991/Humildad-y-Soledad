package com.banda.new_hys;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class HumildadySoledadBd extends SQLiteOpenHelper {

	private static int version = 1;
	private static String name = "HySbd";
	private static CursorFactory factory = null;

	public HumildadySoledadBd(Context context) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Log.i(this.getClass().toString(), "Creando base de datos");

		db.execSQL("CREATE TABLE HYS(" + "_id INTEGER PRIMARY KEY,"
				+ "hip_notificacion VARCHAR(224),"
				+ "hip_fecha TIMESTAMP NOT NULL DEFAULT current_timestamp );");

		// Log.i(this.getClass().toString(), "Tabla HYS creada");

		/*
		 * Insertamos datos iniciales
		 */
		db.execSQL("INSERT INTO HYS (_id,hip_notificacion) VALUES(0,'Bienvenido a la v2.0 de la aplicación oficial de Humildad y Soledad')");
		db.execSQL("INSERT INTO HYS (_id,hip_notificacion) VALUES(1,'Esta es la primera noticia, aparecerá al instalar la aplicación, si la dejas pulsada podrás borrarla o compartirla')");

		// Log.i(this.getClass().toString(), "Datos iniciales HYS insertados");

		// Log.i(this.getClass().toString(), "Base de datos creada");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}