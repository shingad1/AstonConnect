package com.devin.astonconnect.Register;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devin.astonconnect.Loading.LoadingDialog;
import com.devin.astonconnect.ProfileEditActivity;
import com.devin.astonconnect.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


public class RegisterFragment2 extends Fragment {

    private RelativeLayout nextButtonLayout, enterImageLayout;
    private EditText password_text, confirm_password_text;
    private CheckBox checkBoxLater;
    private Boolean isImageEntered = false;

    //Uploading image
    private Uri imageUri;
    private StorageTask storageTask;
    private LoadingDialog loadingDialog;
    private ImageView userImage;

    //Firebase
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private FirebaseAuth fAuth;
    private String imageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register2, container, false);

        password_text = view.findViewById(R.id.password_text);
        confirm_password_text = view.findViewById(R.id.confirm_password_text);
        checkBoxLater = view.findViewById(R.id.checkBoxLater);

        //Image enter
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        if(storageReference == null){
            Toast.makeText(getActivity(), "The storage reference is null", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "The storage reference is not null", Toast.LENGTH_SHORT).show();
        }
        loadingDialog = new LoadingDialog(getActivity(), R.layout.custom_loading_dialog);

        userImage = view.findViewById(R.id.userImage);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CropImage.activity()
                        .setAspectRatio(1,1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .getIntent(getContext());

                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        enterImageLayout = view.findViewById(R.id.enterImageLayout);
        enterImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CropImage.activity()
                        .setAspectRatio(1,1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .getIntent(getContext());

                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });


        //Gets the bundle from the previous fragment
        Bundle bundle = getArguments();

        nextButtonLayout = view.findViewById(R.id.nextButtonLayout);
        nextButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_password = password_text.getText().toString();
                String str_confirm_password = confirm_password_text.getText().toString();
                
                if(TextUtils.isEmpty(str_confirm_password) || TextUtils.isEmpty(str_password)
                        || (isImageEntered == false && !checkBoxLater.isChecked())){
                    Toast.makeText(getActivity(), "All fields are required ", Toast.LENGTH_SHORT).show();
                } else if (str_password.length() < 6){
                    Toast.makeText(getActivity(), "Password should have atleast 6 characters", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.equals(str_confirm_password, str_password)){
                    Toast.makeText(getActivity(),"Please make sure passwords match", Toast.LENGTH_SHORT).show();
                } else if (isImageEntered == true){
                    bundle.putString("password", str_password);
                    bundle.putString("imageUrl", imageUrl);
                    Navigation.findNavController(view).navigate(R.id.action_registerFragment2_to_registerFragment3, bundle);
                } else {
                    bundle.putString("password", str_password);
                    bundle.putString("imageUrl", "https://firebasestorage.googleapis.com/v0/b/astonconnect-8c8f6.appspot.com/o/placeholder.png?alt=media&token=4354b93d-b968-4eff-8dee-0b28be3e505b");
                    Navigation.findNavController(view).navigate(R.id.action_registerFragment2_to_registerFragment3, bundle);
                }
            }
        });
        return view;
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        if(imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()+
                    "." + getFileExtension(imageUri));

            storageTask = fileReference.putFile(imageUri);
            storageTask.continueWithTask(new Continuation() {
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
                        isImageEntered = true;
                        Uri downloadUri = task.getResult();
                        String myUrl = downloadUri.toString();
                        imageUrl = myUrl;
                        Glide.with(getActivity()).load(imageUrl).into(userImage);
                        Toast.makeText(getActivity(), imageUrl, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Failed image upload", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                Toast.makeText(getActivity(), "Recieved image Correctly", Toast.LENGTH_SHORT).show();
                uploadImage();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Exception error = result.getError();
                Toast.makeText(getActivity(), "Something has gone wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }
}