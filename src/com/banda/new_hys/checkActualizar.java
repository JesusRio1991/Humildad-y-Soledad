package com.banda.new_hys;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
 
import org.json.JSONException;
import org.json.JSONObject;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
 
public class checkActualizar {
 
    /**
     * El enlace al archivo p�blico de informaci�n de la versi�n. Puede ser de
     * Dropbox, un hosting propio o cualquier otro servicio similar.
     */
    private static final String INFO_FILE = "https://dl.dropboxusercontent.com/u/15616448/checkHyS.txt";
 
    /**
     * El c�digo de versi�n establecido en el AndroidManifest.xml de la versi�n
     * instalada de la aplicaci�n. Es el valor num�rico que usa Android para
     * diferenciar las versiones.
     */
    private int currentVersionCode;
    /**
     * El nombre de versi�n establecido en el AndroidManifest.xml de la versi�n
     * instalada. Es la cadena de texto que se usa para identificar al versi�n
     * de cara al usuario.
     */
    private String currentVersionName;
 
    /**
     * El c�digo de versi�n establecido en el AndroidManifest.xml de la �ltima
     * versi�n disponible de la aplicaci�n.
     */
    private int latestVersionCode;
    /**
     * El nombre de versi�n establecido en el AndroidManifest.xml de la �ltima
     * versi�n disponible.
     */
    private String latestVersionName;
 
    /**
     * Enlace de descarga directa de la �ltima versi�n disponible.
     */
    private String downloadURL;
 
    /**
     * M�todo para inicializar el objeto. Se debe llamar antes que a cualquie
     * otro, y en un hilo propio (o un AsyncTask) para no bloquear al interfaz
     * ya que hace uso de Internet.
     *
     * @param context
     *            El contexto de la aplicaci�n, para obtener la informaci�n de
     *            la versi�n actual.
     */
    void getData(Context context) {
        try{
            // Datos locales
            PackageInfo pckginfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            currentVersionCode = pckginfo.versionCode;
            currentVersionName = pckginfo.versionName;
 
            // Datos remotos
            String data = downloadHttp(new URL(INFO_FILE));
            JSONObject json = new JSONObject(data);
            latestVersionCode = json.getInt("versionCode");
            latestVersionName = json.getString("versionName");
            downloadURL = json.getString("downloadURL");
            Log.d("AutoUpdate", "Datos obtenidos con �xito");
        }catch(JSONException e){
            Log.e("AutoUpdate", "Ha habido un error con el JSON", e);
        }catch(NameNotFoundException e){
            Log.e("AutoUpdate", "Ha habido un error con el packete :S", e);
        }catch(IOException e){
            Log.e("AutoUpdate", "Ha habido un error con la descarga", e);
        }
    } // getData
 
    /**
     * M�todo para comparar la versi�n actual con la �ltima .
     *
     * @return true si hay una versi�n m�s nueva disponible que la actual.
     */
    boolean isNewVersionAvailable() {
        return getLatestVersionCode() > getCurrentVersionCode();
    }
 
    /**
     * Devuelve el c�digo de versi�n actual.
     *
     * @return
     */
    int getCurrentVersionCode() {
        return currentVersionCode;
    }
 
    /**
     * Devuelve el nombre de versi�n actual.
     *
     * @return
     */
    String getCurrentVersionName() {
        return currentVersionName;
    }
 
    /**
     * Devuelve el c�digo de la �ltima versi�n disponible.
     *
     * @return
     */
    int getLatestVersionCode() {
        return latestVersionCode;
    }
 
    /**
     * Devuelve el nombre de la �ltima versi�n disponible.
     *
     * @return
     */
    String getLatestVersionName() {
        return latestVersionName;
    }
 
    /**
     * Devuelve el enlace de descarga de la �ltima versi�n disponible
     *
     * @return
     */
    String getDownloadURL() {
        return downloadURL;
    }
 
    /**
     * M�todo auxiliar usado por getData() para leer el archivo de informaci�n.
     * Encargado de conectarse a la red, descargar el archivo y convertirlo a
     * String.
     *
     * @param url
     *            La URL del archivo que se quiere descargar.
     * @return Cadena de texto con el contenido del archivo
     * @throws IOException
     *             Si hay alg�n problema en la conexi�n
     */
    private static String downloadHttp(URL url) throws IOException {
    	HttpURLConnection c = (HttpURLConnection)url.openConnection();
        c.setRequestMethod("GET");
        c.setReadTimeout(15 * 1000);
        c.setUseCaches(false);
        c.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        
        while( (line = reader.readLine()) != null )
            stringBuilder.append(line + "\n");
        
        return stringBuilder.toString();
    } // downloadHttp

} // checkActualizar
