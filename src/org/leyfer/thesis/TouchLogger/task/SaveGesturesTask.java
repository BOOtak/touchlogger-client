package org.leyfer.thesis.TouchLogger.task;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import javax.crypto.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.json.JSONObject;
import org.json.JSONException;
import org.leyfer.thesis.TouchLogger.helper.Encryptor;
import org.leyfer.thesis.TouchLogger.helper.GestureBuffer;
import org.leyfer.thesis.TouchLogger.helper.SymmetricEncryptionResult;

public class SaveGesturesTask extends AsyncTask<GestureBuffer, Void, Void> {

    private final String TAG = "TouchLogger/saveGesture";
    private final Context mContext;

    public SaveGesturesTask(Context context) {
        mContext = context;
    }

    @Override
    public Void doInBackground(GestureBuffer... buffers) {
        for (GestureBuffer buffer : buffers) {
            long millis = System.currentTimeMillis();
            String filename = String.format("data_%d.log", millis);

            File logDataDirectory = mContext.getFilesDir();

            if (!logDataDirectory.exists()) {
                if (!logDataDirectory.mkdirs()) {
                    Log.e(TAG, "Unable to create folder");
                    return null;
                }
            }

            byte[] touchData = buffer.toString().getBytes(Charset.defaultCharset());
            String IV;
            String encryptedData;
            String encryptedKey;
            try {
                SecretKey sessionKey = Encryptor.generateSessionKey();
                SymmetricEncryptionResult result = Encryptor.encryptWithSessionKey(sessionKey, touchData);
                IV = Base64.encodeToString(result.getIV(), Base64.NO_WRAP);
                encryptedData = Base64.encodeToString(result.getEncryptedData(), Base64.NO_WRAP);
                encryptedKey = Base64.encodeToString(Encryptor.encryptData(Encryptor.getPublicKey(),
                        sessionKey.getEncoded()), Base64.NO_WRAP);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return null;
            }

            JSONObject data = new JSONObject();
            try {
                data.put("device_id", Settings.Secure.getString(mContext.getContentResolver(),
                        Settings.Secure.ANDROID_ID))
                        .put("device_model", String.format("%s,%s,%s,%s",
                                Build.MANUFACTURER, Build.BRAND, Build.BOARD, Build.MODEL))
                        .put("session_key", encryptedKey)
                        .put("iv", IV)
                        .put("data", encryptedData);
            } catch (JSONException e) {
                Log.e(TAG, "Unable to form data, exiting.");
                return null;
            }

            File outputFile = new File(logDataDirectory, filename);
            FileOutputStream outputStream;
            try {
                outputStream = new FileOutputStream(outputFile);
                outputStream.write(data.toString().getBytes(Charset.defaultCharset()));
                outputStream.close();
            } catch (FileNotFoundException e) {
                Log.e(TAG, String.format("Unable to find file: %s", e.getMessage()));
            } catch (IOException e) {
                Log.e(TAG, String.format("Unable to write data to file: %s", e.getMessage()));
            }

            if (isCancelled()) {
                break;
            }
        }
        return null;
    }
}
