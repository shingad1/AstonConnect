package com.devin.astonconnect.Post;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.devin.astonconnect.Loading.LoadingDialog;
import com.devin.astonconnect.MainActivity;
import com.devin.astonconnect.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class ReviewImagePostActivity extends AppCompatActivity {

    private Uri imageUri;       //uri of the image
    private String myUrl = ""; //url of the stored image
    private StorageTask uploadTask ;
    private StorageReference storageReference;

    private ImageView close, image_taken;
    private Button postBtn, cancelBtn;
    private EditText description, title;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_image_post);

        //Loading Dialog, passed in the activity and the loading resource file we want to show
        loadingDialog = new LoadingDialog(ReviewImagePostActivity.this, R.layout.custom_loading_dialog);


        close       = findViewById(R.id.close);
        image_taken = findViewById(R.id.image_taken);
        postBtn     = findViewById(R.id.postButton);
        cancelBtn   = findViewById(R.id.cancel_button);
        title       = findViewById(R.id.title);
        description = findViewById(R.id.description);

        storageReference = FirebaseStorage.getInstance().getReference("posts");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReviewImagePostActivity.this, MainActivity.class));
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReviewImagePostActivity.this, MainActivity.class));
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        //Setting up the crop image activity
        CropImage.activity()
                .setAspectRatio(1, 1)
                .start(ReviewImagePostActivity.this);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        //Progressbar stuff goes here
        loadingDialog.startLoadingAnimation();


        if(imageUri != null){
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + "jpg");

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        myUrl = downloadUri.toString();

                        //TODO: Potentially change this to be nested within a user parent element in the database. USER ID > POST ID > post details.
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
                        String postid = reference.push().getKey();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("postid", postid);
                        hashMap.put("postImage", myUrl);
                        hashMap.put("title", title.getText().toString());
                        hashMap.put("description", description.getText().toString());
                        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("isImagePost", false);

                        reference.child(postid).setValue(hashMap);

                        //Dismiss progress bar here

                        startActivity(new Intent(ReviewImagePostActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(ReviewImagePostActivity.this, "There was an issue communicating with the database. "+ "\n" +
                                "Please ensure you have a reliable internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ReviewImagePostActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No Image selected. Please ensure an image is selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            image_taken.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Failed to take the image. Please try again later", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ReviewImagePostActivity.this, MainActivity.class));
            finish();
        }
    }
}