package apps.scg.craton.scgsurveyapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import apps.scg.craton.scgsurveyapp.exceptions.DataException;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/09/22
 */

public class AudioService extends Service {

    private AudioRecorderService recorderService;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String filename = intent.getStringExtra("file");
        recorderService = new AudioRecorderService(filename);
        try {
            recorderService.startRecoding();
        } catch (DataException e) {
            e.printStackTrace();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        recorderService.stopRecording();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
