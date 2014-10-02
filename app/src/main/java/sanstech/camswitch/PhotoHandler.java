package sanstech.camswitch;

import android.content.Context;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoHandler implements Camera.PictureCallback {

    private final Context context;
    private Camera camera = null;

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
            Camera.Parameters params = mCamera.getParameters();
            params.setRotation(getCameraOrientation());
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            List<Camera.Size> sizes = params.getSupportedPictureSizes();
            setPictureSize(int width, int height);
            mCamera.setParameters(params);
            camera.takePicture(shutterCallback, rawCallback,jpegCallback);
        }catch (Exception ex){
            Toast.makeText(context, "Error taking snap!!!",
                    Toast.LENGTH_LONG).show();
        }
    }

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            Log.d("bt", "onShutter'd");
        }
    };

    private boolean cameraExists(){
        return (camera.getNumberOfCameras() > 0);
    }

    private Camera getCamera() {
        int cameraId = -1;
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
            Log.d("bt", "onPictureTaken - raw");
        }
    };

    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFileDir = getDir();

            if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

                Log.d("MakePhotoActivity.DEBUG_TAG", "Can't create directory to save image.");
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
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                Toast.makeText(context, "New Image saved:" + photoFile,
                        Toast.LENGTH_LONG).show();
                Log.d("dt", "onPictureTaken - wrote bytes: " + data.length);
            } catch (Exception error) {
                Log.d("MakePhotoActivity.DEBUG_TAG", "File" + filename + "not saved: "
                        + error.getMessage());
                Toast.makeText(context, "Image could not be saved.",
                        Toast.LENGTH_LONG).show();
            }

//                outStream = new FileOutputStream(String.format(
//                        "/storage/sdcard0/collect/%d", photoFile));
//                outStream.write(data);
//                outStream.close();

//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//            }
            Log.d("bt", "onPictureTaken - jpeg");
        }
    };


    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

        File pictureFileDir = getDir();

        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

            Log.d("MakePhotoActivity.DEBUG_TAG", "Can't create directory to save image.");
            Toast.makeText(context, "Can't create directory to save image.",
                    Toast.LENGTH_LONG).show();
            return;

        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        String photoFile = "Picture_" + date + ".jpg";

        String filename = pictureFileDir.getPath() + File.separator + photoFile;

        File pictureFile = new File(filename);

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            Toast.makeText(context, "New Image saved:" + photoFile,
                    Toast.LENGTH_LONG).show();
        } catch (Exception error) {
            Log.d("MakePhotoActivity.DEBUG_TAG", "File" + filename + "not saved: "
                    + error.getMessage());
            Toast.makeText(context, "Image could not be saved.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private File getDir() {
        return new File("/storage/sdcard0/", "BT-Area");
    }
}
