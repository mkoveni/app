package apps.scg.craton.scgsurveyapp.services;

import android.media.MediaPlayer;
import android.media.MediaRecorder;

import java.io.IOException;

import apps.scg.craton.scgsurveyapp.exceptions.DataException;

/**
 * Created by CodeThunder on 2017/03/14.
 */

public class AudioRecorderService {

    private MediaRecorder mediaRecorder;
    private String fileName;

    public AudioRecorderService(String fileName)
    {

        setFileName(fileName);
        initializeRecorder();

    }

    public void initializeRecorder()
    {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(getFileName());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(fileName);
    }

    public void startRecoding() throws DataException
    {
        try
        {
            mediaRecorder.prepare();
            mediaRecorder.start();
        }
        catch (IOException e)
        {
            throw new DataException("Something went wrong");
        }
    }

    public void stopRecording()
    {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileName()
    {
        return fileName;
    }

    public MediaRecorder getMediaRecorder()
    {
        return mediaRecorder;
    }



}
