package com.example.contactapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
public class FaceIdOpenDoor extends AppCompatActivity {

    protected Interpreter tflite;
    private int imageSizeX;
    private int imageSizeY;

    private static final float IMAGE_MEAN = 0.0f;
    private static final float IMAGE_STD = 1.0f;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    public Bitmap oribitmap, cropped;
    Uri imageuri;
    private ImageView addImageView;
    BottomNavigationView bottonNavMenu;
    private ImageView removeImageView;
    private Button openButton;
    private SeekBar timeSeekBar;
    private TextView timeTextView;
    private int selectedTime = 0;

    private Button closeButton;
    private DatabaseReference mDatabase;
    private CountDownTimer countDownTimer;
    ImageView oriImage;
    Button buverify;
    TextView result_text;
    private Switch switchDevice;
    float[][] ori_embedding = new float[1][128];
    float[][] test_embedding = new float[1][128];
    List<float[][]> embeddingsList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_id_open_door);

        initComponents();
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("Maisons").child("maison1").child("services").child("porte").child("enabled");
//        switchDevice = findViewById(R.id.deviceSwitch);
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String enabledValue = dataSnapshot.getValue(String.class);
//                if (enabledValue != null) {
//                    boolean isEnabled = enabledValue.equals("1");
//                    switchDevice.setChecked(isEnabled);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Gérer l'erreur ici, si nécessaire
//            }
//        });
//
//        switchDevice.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            String value = isChecked ? "1" : "0";
//            databaseReference.setValue(value);
//        });
        // Get a reference to the Firebase Storage
        storageReference = FirebaseStorage.getInstance().getReference().child("users");
        addImageView = findViewById(R.id.addImageView);
        removeImageView = findViewById(R.id.removeImageView);
//        timeSeekBar = findViewById(R.id.timeSeekBar);
//        timeTextView = findViewById(R.id.timeTextView);
        bottonNavMenu = findViewById(R.id.bottomNavMenu);


//        // Set up the seekbar listener
//        timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                // Update the selectedTime variable based on the seekbar progress
//                switch (progress) {
//                    case 0:
//                        selectedTime = 0; // Never
//                        timeTextView.setText("Select time to close the door: Never");
//                        break;
//                    case 1:
//                        selectedTime = 5; // 5 minutes
//                        timeTextView.setText("Select time to close the door: 5 min");
//                        break;
//                    case 2:
//                        selectedTime = 10; // 10 minutes
//                        timeTextView.setText("Select time to close the door: 10 min");
//                        break;
//                    case 3:
//                        selectedTime = 30; // 30 minutes
//                        timeTextView.setText("Select time to close the door: 30 min");
//                        break;
//                }
//            }
//
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                // Not used
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                // Not used
//            }
//        });
        // Download the images from Firebase Storage
        downloadImages();
        closeButton = findViewById(R.id.closeButton);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("command");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Récupérer la valeur de "porte" dans la base de données
                String porteValue = dataSnapshot.getValue(String.class);
                if (porteValue.equals("rotate_90")) {
                    // Afficher l'image pour "porte fermée"
                    addImageView.setVisibility(View.VISIBLE);
                    removeImageView.setVisibility(View.GONE);
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }
                    Toast.makeText(FaceIdOpenDoor.this, "Door is closed", Toast.LENGTH_SHORT).show();
                } else if (porteValue.equals("rotate_180")) {
                    // Afficher l'image pour "porte ouverte"
                    addImageView.setVisibility(View.GONE);
                    removeImageView.setVisibility(View.VISIBLE);
                    if (selectedTime > 0) {
                        startCountDownTimer();
                    }
                    Toast.makeText(FaceIdOpenDoor.this, "Door is opened", Toast.LENGTH_SHORT).show();

                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageView.setVisibility(View.VISIBLE);
                removeImageView.setVisibility(View.GONE);
                mDatabase.setValue("rotate_90");
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }

            }
        });

        bottonNavMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Home:
                        Intent myintent = new Intent(FaceIdOpenDoor.this, Liste_contacts.class);
                        startActivity(myintent);
                        break;
                    case R.id.Settings:
                        Intent settingsIntent = new Intent(FaceIdOpenDoor.this, settings.class);
                        startActivity(settingsIntent);
                        break;
                    case R.id.Profile:
                        Intent Intent2 = new Intent(FaceIdOpenDoor.this, Profile.class);
                        startActivity(Intent2);
                        break;

                }
                return true;
            }
        });
    }
    private void showImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image Source");
        builder.setItems(new CharSequence[]{"Camera", "Gallery"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            // Capture image from camera
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, REQUEST_CAMERA);
                        } else {
                            // Choose image from gallery
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, REQUEST_GALLERY);
                        }
                    }
                });
        builder.show();
    }
    private void initComponents() {
        buverify = findViewById(R.id.openButton);
        try {
            tflite = new Interpreter(loadmodelfile(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        buverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog();
            }
        });
    }

    private void downloadImages() {
        if (uploadTask != null && uploadTask.isInProgress()) {
            Toast.makeText(FaceIdOpenDoor.this, "Image download is in progress", Toast.LENGTH_SHORT).show();
        } else {
            // Download the images from Firebase Storage
            storageReference.listAll()
                    .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                        @Override
                        public void onSuccess(ListResult listResult) {
                            for (StorageReference item : listResult.getItems()) {
                                downloadImage(item);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle any errors
                            Toast.makeText(FaceIdOpenDoor.this, "Failed to download images", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void downloadImage(StorageReference imageRef) {
        try {
            final File localFile = File.createTempFile("image", "jpg");
            imageRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Image download successfully, load it into the embeddings list
                            Bitmap downloadedBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            float[][] embedding = get_embedding(downloadedBitmap);
                            embeddingsList.add(embedding);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle any errors
                            Toast.makeText(FaceIdOpenDoor.this, "Failed to download image", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double calculate_distance(float[][] ori_embedding, float[][] test_embedding) {
        double sum = 0.0;
        for (int i = 0; i < 128; i++) {
            sum += Math.pow((ori_embedding[0][i] - test_embedding[0][i]), 2.0);
        }
        return Math.sqrt(sum);
    }

    private float[][] get_embedding(Bitmap bitmap) {
        TensorImage inputImageBuffer;
        float[][] embedding = new float[1][128];

        int imageTensorIndex = 0;
        int[] imageShape = tflite.getInputTensor(imageTensorIndex).shape(); // {1, height, width, 3}
        imageSizeY = imageShape[1];
        imageSizeX = imageShape[2];
        DataType imageDataType = tflite.getInputTensor(imageTensorIndex).dataType();

        inputImageBuffer = new TensorImage(imageDataType);

        inputImageBuffer = loadImage(bitmap, inputImageBuffer);

        tflite.run(inputImageBuffer.getBuffer(), embedding);

        return embedding;
    }

    private TensorImage loadImage(final Bitmap bitmap, TensorImage inputImageBuffer) {
        // Loads bitmap into a TensorImage.
        inputImageBuffer.load(bitmap);

        // Creates processor for the TensorImage.
        int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        // TODO(b/143564309): Fuse ops inside ImageProcessor.
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
                        .add(new ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                        .add(getPreprocessNormalizeOp())
                        .build();
        return imageProcessor.process(inputImageBuffer);
    }

    private MappedByteBuffer loadmodelfile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd("Qfacenet.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startoffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startoffset, declaredLength);
    }

    private TensorOperator getPreprocessNormalizeOp() {
        return new NormalizeOp(IMAGE_MEAN, IMAGE_STD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageuri = data.getData();
            try {
                oribitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);

                ori_embedding = get_embedding(oribitmap);

                boolean foundSameFace = false;
                for (float[][] embedding : embeddingsList) {
                    double distance = calculate_distance(ori_embedding, embedding);
                    if (distance < 6.0) {
                        foundSameFace = true;
                        break;
                    }
                }

                if (foundSameFace){
                    //  result_text.setText("Result: Same Faces");
                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("command");
                    databaseRef.setValue("rotate_180");}
                else{
                    // result_text.setText("Result: Different Faces");
                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("command");
                    databaseRef.setValue("rotate_90");}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Bitmap capturedImage = (Bitmap) data.getExtras().get("data");

            if (capturedImage != null) {
                ori_embedding = get_embedding(capturedImage);

                boolean foundSameFace = false;
                for (float[][] embedding : embeddingsList) {
                    double distance = calculate_distance(ori_embedding, embedding);
                    if (distance < 6.0) {
                        foundSameFace = true;
                        break;
                    }
                }

                if (foundSameFace) {
                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("command");
                    databaseRef.setValue("rotate_180");
                } else {
                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("command");
                    databaseRef.setValue("rotate_90");
                }
            }
        }
    }

    public void face_detector(final Bitmap bitmap, final String imagetype) {
        final InputImage image = InputImage.fromBitmap(bitmap, 0);
        FaceDetector detector = FaceDetection.getClient();
        detector.process(image)
                .addOnSuccessListener(
                        new OnSuccessListener<List<Face>>() {
                            @Override
                            public void onSuccess(List<Face> faces) {
                                // Task completed successfully
                                for (Face face : faces) {
                                    Rect bounds = face.getBoundingBox();
                                    int left = Math.max(bounds.left, 0);
                                    int top = Math.max(bounds.top, 0);
                                    int right = Math.min(bounds.right, bitmap.getWidth());
                                    int bottom = Math.min(bounds.bottom, bitmap.getHeight());

                                    // Ajuster les coordonnées de recadrage si elles sont invalides
                                    if (left >= right || top >= bottom) {
                                        left = 0;
                                        top = 0;
                                        right = bitmap.getWidth();
                                        bottom = bitmap.getHeight();
                                    }

                                    cropped = Bitmap.createBitmap(bitmap, left, top, right - left, bottom - top);
                                    get_embaddings(cropped, imagetype);
                                }
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
    }

    public void get_embaddings(Bitmap bitmap, String imagetype) {
        float[][] embedding = get_embedding(bitmap);

        if (imagetype.equals("original"))
            ori_embedding = embedding;
        else if (imagetype.equals("test")) {
            test_embedding = embedding;
            face_detector(bitmap, imagetype);

        }
    }
    private void startCountDownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(selectedTime * 60 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                // Update the database with the "1" value to close the door
                mDatabase.setValue("rotate_90");
            }
        }.start();
    }
}