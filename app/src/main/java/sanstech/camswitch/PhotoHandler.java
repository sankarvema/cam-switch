package sanstech.camswitch;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PhotoHandler{

    public static int Counter=0;
    private final Context context;
    private Camera camera = null;
    private int cameraId = -1;

    public PhotoHandler(Context context) {
        this.context = context;
    }

    public void takePhoto(){
        if(!cameraExists()) {
            Toast.makeText(context, "No cameras found",
                    Toast.LENGTH_LONG).show();
            return;
        }

        camera = getCamera();

        try{
            camera.startPreview();
            Camera.Parameters params = camera.getParameters();
            //params.setRotation(params.getCameraOrientation());
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            List<Camera.Size> sizes = params.getSupportedPictureSizes();
            params.setPictureSize(2160,4800);
            //params.set("orientation", "landscape");
            params.setRotation(90);

//            camera.setParameters(params);
//            int rotation = MainActivity.Instance.getWindowManager().getDefaultDisplay()
//                    .getRotation();
//
//            int degrees = 0;
//            switch (rotation) {
//                case Surface.ROTATION_0: degrees = 270; break;
//                case Surface.ROTATION_90: degrees = 270; break;
//                case Surface.ROTATION_180: degrees = 270; break;
//                case Surface.ROTATION_270: degrees = 270; break;
//            }
//
//            int result;
//            Camera.CameraInfo info = new Camera.CameraInfo();
//            Camera.getCameraInfo(cameraId,info);
//            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//                result = (info.orientation + degrees) % 360;
//                result = (360 - result) % 360;  // compensate the mirror
//            } else {  // back-facing
//                result = (info.orientation - degrees + 360) % 360;
//            }


            //camera.setDisplayOrientation(90);
            camera.takePicture(shutterCallback, rawCallback,jpegCallback);
            Thread.sleep(700);
        }catch (Exception ex){
            Toast.makeText(context, "Error taking snap!!!",
                    Toast.LENGTH_LONG).show();
        }
    }

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            Log.d("CamSwitch", "onShutter'd");
            Toast.makeText(context, "Shutter callback",
                    Toast.LENGTH_LONG).show();
        }
    };

    private boolean cameraExists(){
        return (camera.getNumberOfCameras() > 0);
    }

    private Camera getCamera() {
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                break;
            }
        }
        Camera aCam = Camera.open(cameraId);
        if(aCam == null)
            Toast.makeText(context, "Error opening camera!!!",
                    Toast.LENGTH_LONG).show();
        return aCam;
    }
    /** Handles data for raw picture */
    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d("CamSwitch", "onPictureTaken - raw");
            Toast.makeText(context, "Raw call back",
                    Toast.LENGTH_LONG).show();
        }
    };

    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            Toast.makeText(context, "Jpeg callback", Toast.LENGTH_LONG).show();
            PhotoHandler.Counter ++;
            File pictureFileDir = getDir();

            if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

                Log.d("CamSwitch", "Can't create directory to save image.");
                Toast.makeText(context, "Can't create directory to save image.",
                        Toast.LENGTH_LONG).show();
                return;

            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
            String date = dateFormat.format(new Date());
            String photoFile = "bt_" + date + ".jpg";

            String filename = pictureFileDir.getPath() + File.separator + photoFile;

            File pictureFile = new File(filename);

            try {
                Toast.makeText(context, "Save file to " + photoFile, Toast.LENGTH_LONG).show();
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                Toast.makeText(context, "File saved to: " + photoFile,
                        Toast.LENGTH_LONG).show();
                Log.d("CamSwitch", "onPictureTaken - wrote bytes: " + data.length);

                //View rootView = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
//                View rootView = View.inflate(context, R.layout.activity_main, null);
//
//                TextView counter = (TextView) rootView.findViewById(R.id.counter_display);
//
//                counter.setText(Integer.toString(PhotoHandler.Counter));

            } catch (Exception error) {
                Log.d("CamSwitch", "File" + filename + "not saved: "
                        + error.getMessage());
                Toast.makeText(context, "Error saving file",
                        Toast.LENGTH_LONG).show();
            }
            finally
            {
                // release camera after completing the act
                if (camera != null) {
                    camera.release();
                    camera = null;
                }
            }

            Log.d("CamSwitch", "onPictureTaken - jpeg");
        }
    };


//    @Override
//    public void onPictureTaken(byte[] data, Camera camera) {
//
//        File pictureFileDir = getDir();
//
//        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
//
//            Log.d("CamSwitch", "Can't create directory to save image.");
//            Toast.makeText(context, "Can't create directory to save image.",
//                    Toast.LENGTH_LONG).show();
//            return;
//
//        }
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
//        String date = dateFormat.format(new Date());
//        String photoFile = "Picture_" + date + ".jpg";
//
//        String filename = pictureFileDir.getPath() + File.separator + photoFile;
//
//        File pictureFile = new File(filename);
//
//        try {
//            FileOutputStream fos = new FileOutputStream(pictureFile);
//            fos.write(data);
//            fos.close();
//            Toast.makeText(context, "New Image saved:" + photoFile,
//                    Toast.LENGTH_LONG).show();
//        } catch (Exception error) {
//            Log.d("CamSwitch", "File" + filename + "not saved: "
//                    + error.getMessage());
//            Toast.makeText(context, "Image could not be saved.",
//                    Toast.LENGTH_LONG).show();
//        }
//        finally
//        {
//            // release camera after completing the act
//            if (camera != null) {
//                camera.release();
//                camera = null;
//            }
//        }
//    }

    private File getDir() {
        return new File("/storage/sdcard0/", "BT-Area");
    }
}
