package in.technogenie.hamlet.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import in.technogenie.hamlet.R;
import in.technogenie.hamlet.beans.Upload;
import in.technogenie.hamlet.gallery.GalleryAdapter;
import in.technogenie.hamlet.gallery.SlideShowFragment;
import in.technogenie.hamlet.utils.Constants;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GalleryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryFragment extends Fragment implements GalleryAdapter.GalleryAdapterCallBacks {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    //Deceleration of list of  GalleryItems
    public ArrayList<Upload> galleryItems;
    //Read storage permission request code
    private static final int RC_READ_STORAGE = 5;
    GalleryAdapter mGalleryAdapter;
    RecyclerView recyclerView;
    private FloatingActionButton uploadImages;
    private final static String TAG = GalleryFragment.class.getSimpleName();
    //progress dialog
    private ProgressDialog progressDialog;

    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    List<String> imagesEncodedList;

    //Firebase

    StorageReference storageReference;
    private DatabaseReference mDatabase;

    private static final String ARG_PARAM2 = "param2";
/*
    private final String image_titles[] = {
            "Image 1",
            "Image 2",
            "Image 3",
            "Image 4",
    };*/

/*    private final Integer image_ids[] = {
            R.drawable.scene1,
            R.drawable.scene2,
            R.drawable.scene3,
            R.drawable.technogenie,
    };*/

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public GalleryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GalleryFragment newInstance(String param1, String param2) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //Instantiate Firebase Storage & Database
        storageReference = FirebaseStorage.getInstance().getReference(Constants.STORAGE_GALLERY_PATH);
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DB_GALLERY_PATH);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.imagegallery);
        //recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        //ArrayList<GalleryItemVO> galleryItemVOS = prepareData();

        recyclerView.setAdapter(mGalleryAdapter);

        uploadImages = (FloatingActionButton) view.findViewById(R.id.upload_images);

        //Listner for New Members Floating Action Button
        uploadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });

        //check for read storage permission
        if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //Get images
            //galleryItems = GalleryUtils.getImages(this.getActivity());
            prepareData();

            Log.d("GalleryFragment", galleryItems.toString());
            // add images to gallery recyclerview using adapter
            //mGalleryAdapter.addGalleryItems(galleryItems);
        } else {
            //request permission
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RC_READ_STORAGE);
        }


        return view;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void prepareData() {
        Log.d("GalleryFragment", "Inside prepareData..");
        mGalleryAdapter = new GalleryAdapter(this);
        galleryItems = new ArrayList<>();

        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DB_GALLERY_PATH);


        //adding an event listener to fetch values
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                //iterating through all the values in database
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    galleryItems.add(upload);
                }
                //creating adapter
                mGalleryAdapter.addGalleryItems(galleryItems);

                //adding adapter to recyclerview
                recyclerView.setAdapter(mGalleryAdapter);

                //dismissing the progress dialog
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });



/*        for (int i = 0; i < image_titles.length; i++) {

            Upload galleryItemVO = new Upload();
            galleryItemVO.setName(image_titles[i]);
            galleryItemVO.setUrl(image_ids[i]);
            theimage.add(galleryItemVO);
        }*/
        Log.d("GalleryFragment", "Exiting prepareData :");
        //return theimage;
    }


    @Override
    public void onItemSelected(int position) {
        //create fullscreen SlideShowFragment dialog
        SlideShowFragment slideShowFragment = SlideShowFragment.newInstance(position, galleryItems);
        //setUp style for slide show fragment
        slideShowFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        //finally show dialogue
        slideShowFragment.show(getActivity().getSupportFragmentManager(), null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_READ_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Get images
                //galleryItems = GalleryUtils.getImages(getContext());
                prepareData();

                // add images to gallery recyclerview using adapter
                //mGalleryAdapter.addGalleryItems(galleryItems);

            } else {
                Toast.makeText(getContext(), "Storage Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(TAG, "Entering onActivityResult: " + resultCode);
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {

                if (data.getData() != null) {
                    Uri mImageUri = data.getData();

                    try {
                        uploadImage(getContext(), mImageUri.getLastPathSegment(), mImageUri);
                    } catch (Exception e) {
                        Log.e(TAG, "Error uploading image :" + e);
                    }
                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            uploadImage(getContext(), uri.getLastPathSegment(), uri);
                        }
                        Log.v(TAG, "Selected Images" + mArrayUri.size());
                    }
                }
            } else {
                Toast.makeText(getContext(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
            Log.e(TAG, "Exception in GalleryFragment:" + e);
        }

        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "Exiting onActivityResult: ");
    }

    /**
     * Upload Selected Image in Firebase Storage
     *
     * @param context
     * @param mImageUri
     */
    private void uploadImage(final Context context, final String name, final Uri mImageUri) {

        Log.d(TAG, "Entering uploadImage :" + name + " :: " + mImageUri);
        if (mImageUri != null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            //getting the storage reference
            final String fileName = Constants.STORAGE_GALLERY_PATH + System.currentTimeMillis() + "." + getFileExtension(mImageUri);
            final StorageReference ref = storageReference.child(fileName);


            ref.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String downloadUrl = uri.toString();
                            Log.d(TAG, "onSuccess Download URL :" + downloadUrl);

                            //creating the upload object to store uploaded image details
                            Upload upload = new Upload(fileName.trim(), downloadUrl);

                            //adding an upload to firebase database
                            String uploadId = mDatabase.push().getKey();
                            mDatabase.child(uploadId).setValue(upload);
                            progressDialog.dismiss();
                        }
                    });

                    Toast.makeText(context, "File Uploaded", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}

