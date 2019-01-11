package in.technogenie.hamlet.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.TimerTask;

import in.technogenie.hamlet.MainActivity;
import in.technogenie.hamlet.R;
import in.technogenie.hamlet.adapter.ImageViewAdapter;
import in.technogenie.hamlet.utils.SliderAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private final static String TAG = HomeFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ViewPager viewPager;
    private TabLayout indicator;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String[] web = { "Members", "Gallery", "Messages", "Events","Promotions", "Contact Us" };
    private Integer[] mThumbIds = { R.drawable.ic_person_white, R.drawable.ic_menu_gallery_white,
            R.drawable.ic_message_white, R.drawable.ic_notifications_white, R.drawable.ic_live_tv_white,
            R.drawable.ic_contacts_white};
    GridView gridview;

    private final int[] GalImages = new int[] {
            R.drawable.scene1,
            R.drawable.scene2,
            R.drawable.scene3
    };

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
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
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        // Timer for the HomePage Slider
        //Timer timer = new Timer();
        //timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager=(ViewPager)view.findViewById(R.id.viewPager);
        indicator=(TabLayout)view.findViewById(R.id.indicator);

        viewPager.setAdapter(new SliderAdapter(getContext(), GalImages));
        indicator.setupWithViewPager(viewPager, true);

        gridview = (GridView) view.findViewById(R.id.home_action_view);
        gridview.setAdapter(new ImageViewAdapter(getContext(), web, mThumbIds,  "HomeActivity"));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(getContext(), "Activity :" + web[position], Toast.LENGTH_SHORT).show();

                //private String[] web = { "Members", "Gallery", "Messages", "Events","Promotions", "Contact Us" };
                if ((web[position]).equals("Members")) {
                    replaceFragment(new MemberMasterFragment(), web[position]);

                } else if ((web[position]).equals("Gallery")) {
                    replaceFragment(new GalleryFragment(), web[position]);

                } else if ((web[position]).equals("Messages")) {
                    replaceFragment(new MessagesFragment(), web[position]);

                } else if ((web[position]).equals("Events")) {
                    replaceFragment(new EventsFragment(), web[position]);

                } else if ((web[position]).equals("Promotions")) {
                    replaceFragment(new PromotionsFragment(), web[position]);

                } else if ((web[position]).equals("Contact Us")) {
                    //replaceFragment(new PromotionsFragment(), R.layout.fragment_promotions);
                    //Intent intent = new Intent()

                }
            }
        });

        return view;
    }

    // Replace current Fragment with the destination Fragment.
    public void replaceFragment(Fragment destFragment, String screenName) {

        Log.d(TAG, "Entering replaceFragment..:" + screenName);
        // First get FragmentManager object.
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        ((MainActivity) getActivity()).getSupportActionBar().setTitle(screenName);

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.frame, destFragment);

        // Commit the Fragment replace action.
        fragmentTransaction.commit();

        Log.d(TAG, "Exiting replaceFragment..");
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

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            HomeFragment.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < GalImages.length - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }
}

