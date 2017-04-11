package pe.herny.movil.preguntas.apppreguntas.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by root on 10/04/17.
 */

public class bdhelper extends SQLiteOpenHelper{

    private static final String DB_PATH = "/data/data/pe.herny.movil.preguntas.apppreguntas/databases/";
    private static final String DB_NAME = "preguntas.db";

    private final Context context;

    private  SQLiteDatabase database;
    public bdhelper(Context context) {
        super(context, DB_NAME, null,1);
        this.context = context;
    }

    public void openDataBase(){
        database = SQLiteDatabase.openDatabase(DB_PATH+DB_NAME,null,SQLiteDatabase.OPEN_READONLY);
    }

    public synchronized void close(){
        if (database!=null){
            database.close();
        }
    }

    public void createDataBase() throws IOException{
        boolean dbExist = checkDataBase();
        SQLiteDatabase DB_READ=null;
        if(dbExist){

        }else {
            DB_READ = this.getReadableDatabase();
            DB_READ.close();
            try {
                copyDataBase();
            }catch (Exception e){
                throw  new Error("ERROR COPIANDO BASE DE DATOS");
            }

        }
    }

    private void copyDataBase() throws IOException{
        InputStream inputStream = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH+DB_NAME;
        OutputStream outputStream = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length= inputStream.read(buffer))!=-1){
            if(length>0){
                outputStream.write(buffer,0,length);
            }
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;

        try {

            checkDB = SQLiteDatabase.openDatabase(DB_PATH+DB_NAME,null,SQLiteDatabase.OPEN_READONLY);

        }catch (Exception e){
            File dbFile =  new File(DB_PATH+DB_NAME);
            return  dbFile.exists();
        }

        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
