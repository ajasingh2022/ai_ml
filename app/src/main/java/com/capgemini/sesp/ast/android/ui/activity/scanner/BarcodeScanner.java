package com.capgemini.sesp.ast.android.ui.activity.scanner;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ApplicationAstSep;
import com.capgemini.sesp.ast.android.ui.activity.meter_change.validate_measurepoint_id.CameraPreview;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;


public class BarcodeScanner extends AppCompatActivity implements OnClickListener {

	
	private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;

    private TextView scanText;
    private Button scanButton, finishButton;

    private ImageScanner scanner;

    private boolean barcodeScanned = false;
    private boolean previewing = true;
    
    private String scanResult;

    static {
        System.loadLibrary("iconv");
    } 

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        // Instance barcode scanner
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        FrameLayout preview = findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

        scanText = findViewById(R.id.scanText);

        scanButton = findViewById(R.id.ScanButton);
        scanButton.setOnClickListener(this);
        
        finishButton = findViewById(R.id.ScanFinish);
        finishButton.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        Log.d("BarcodeScanner", "Activity is being destroyed");
        super.onDestroy();
        Log.d("BarcodeScanner", "Releasing camera");
        releaseCamera();
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
            Camera.Parameters cParameters = c.getParameters();
            PackageManager pm = ApplicationAstSep.context.getPackageManager();

            if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
                Toast.makeText(ApplicationAstSep.context, "Your phone does not have flash light support.", Toast.LENGTH_LONG).show();
//                finish();
            }else {
                cParameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
            }
            c.setParameters(cParameters);
        } catch (Exception e){
            writeLog("BarcodeScanner  :getCameraInstance() ", e);
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
            public void run() {
                if (previewing)
                    mCamera.autoFocus(autoFocusCB);
            }
        };

    PreviewCallback previewCb = new PreviewCallback() {
            public void onPreviewFrame(byte[] data, Camera camera) {
                try{
                Camera.Parameters parameters = camera.getParameters();
                Size size = parameters.getPreviewSize();

                Image barcode = new Image(size.width, size.height, "Y800");
                barcode.setData(data);

                int result = scanner.scanImage(barcode);
                
                if (result != 0) {
                    previewing = false;
                    mCamera.setPreviewCallback(null);
                    mCamera.stopPreview();
                    
                    SymbolSet syms = scanner.getResults();
                    for (Symbol sym : syms) {
                        scanResult =  AndroidUtilsAstSep.removePrefixFromGiaiIfPresent(sym.getData(), true);
                        scanText.setText((getResources().getString(R.string.result)) + ": " + scanResult);
                        barcodeScanned = true;
                    }
                }
                } catch (Exception e) {
                    writeLog("BarcodeScanner  :onPreviewFrame() ", e);
                }
            }
        };

    // Mimic continuous auto-focusing
    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
            public void onAutoFocus(boolean success, Camera camera) {
                autoFocusHandler.postDelayed(doAutoFocus, 1000);
            }
        };

	@Override
	public void onClick(View v) {
        try {
            if (v.getId() == R.id.ScanButton) {
                if (barcodeScanned) {
                    barcodeScanned = false;
                    scanText.setText("Scanning...");
                    mCamera.setPreviewCallback(previewCb);
                    mCamera.startPreview();
                    previewing = true;
                    mCamera.autoFocus(autoFocusCB);
                }
            } else if (v.getId() == R.id.ScanFinish) {
                Intent intent = new Intent();
                intent.putExtra("barcode_result", scanResult);

                if (getParent() == null) {
                    setResult(Activity.RESULT_OK, intent);
                } else {
                    getParent().setResult(Activity.RESULT_OK, intent);
                }
                finish();
            }

        } catch (Exception e) {
            writeLog("BarcodeScanner  :onClick() ", e);
        }
    }
}
