package in.technogenie.hamlet.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import java.util.ArrayList;

import in.technogenie.hamlet.R;
import in.technogenie.hamlet.beans.GalleryItemVO;
import in.technogenie.hamlet.gallery.GalleryAdapter;
import in.technogenie.hamlet.gallery.SlideShowFragment;

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
    public ArrayList<GalleryItemVO> galleryItems;
    //Read storage permission request code
    private static final int RC_READ_STORAGE = 5;
    GalleryAdapter mGalleryAdapter;
    RecyclerView recyclerView;

    private static final String ARG_PARAM2 = "param2";

    private final String image_titles[] = {
            "Image 1",
            "Image 2",
            "Image 3",
            "Image 4",
    };

    private final Integer image_ids[] = {
            R.drawable.scene1,
            R.drawable.scene2,
            R.drawable.scene3,
            R.drawable.technogenie,
    };

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.imagegallery);
        //recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        //ArrayList<GalleryItemVO> galleryItemVOS = prepareData();
        mGalleryAdapter = new GalleryAdapter(this);
        recyclerView.setAdapter(mGalleryAdapter);

        //check for read storage permission
        if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //Get images
            //galleryItems = GalleryUtils.getImages(this.getActivity());
            galleryItems = prepareData();

            Log.d("GalleryFragment", galleryItems.toString());
            // add images to gallery recyclerview using adapter
            mGalleryAdapter.addGalleryItems(galleryItems);
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

    private ArrayList<GalleryItemVO> prepareData(){
        Log.d("GalleryFragment", "Inside prepareData..");
        ArrayList<GalleryItemVO> theimage = new ArrayList<>();
        for(int i = 0; i< image_titles.length; i++){
            GalleryItemVO galleryItemVO = new GalleryItemVO();
            galleryItemVO.setImageName(image_titles[i]);
            galleryItemVO.setImageUri(image_ids[i]);
            theimage.add(galleryItemVO);
        }
        Log.d("GalleryFragment", "Exiting prepareData :" + theimage.size());
        return theimage;
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
                galleryItems = prepareData();

                // add images to gallery recyclerview using adapter
                mGalleryAdapter.addGalleryItems(galleryItems);

            } else {
                Toast.makeText(getContext(), "Storage Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

