package in.technogenie.hamlet.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import in.technogenie.hamlet.R;
import in.technogenie.hamlet.beans.CustomerVO;
import in.technogenie.hamlet.utils.CommunicationsUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MemberDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MemberDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemberDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    CustomerVO customerVO;

    ImageView memberImage;
    TextView memberName;
    TextView jcrtName;
    TextView memberDesignation;
    TextView lomName;
    TextView memberOccupation;
    TextView memberCategory;
    TextView memberProduct;
    TextView website;
    TextView memberContact;
    TextView bloodGroup;
    TextView homeAddress;
    TextView officeAddress;
    TextView memberEmail;
    TextView memberDOB;
    TextView memberAnniversary;
    ImageButton userPhone;
    ImageButton userSMS;

    private final static String TAG = MemberDetailsFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    public MemberDetailsFragment() {
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
    public static MemberDetailsFragment newInstance(String param1, String param2) {
        MemberDetailsFragment fragment = new MemberDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            customerVO = (CustomerVO) getArguments().getSerializable("customer");
            Log.d("MemberDetailsFragment", customerVO.toString());
        }

        // show the UP button
        //((MainActivity) getActivity()).showUpButton();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_member_details, container, false);

        memberImage = (ImageView) view.findViewById(R.id.member_Image);
        memberName = (TextView) view.findViewById(R.id.member_name);
        jcrtName = (TextView) view.findViewById(R.id.jcrt_name);
        memberDesignation = (TextView) view.findViewById(R.id.member_designation);
        lomName = (TextView) view.findViewById(R.id.lomName);
        memberOccupation = (TextView) view.findViewById(R.id.member_occupation);
        memberCategory = (TextView) view.findViewById(R.id.member_category);
        memberProduct = (TextView) view.findViewById(R.id.member_products);
        website = (TextView) view.findViewById(R.id.website);

        memberContact = (TextView) view.findViewById(R.id.member_contact);
        bloodGroup = (TextView) view.findViewById(R.id.blood_group);
        homeAddress = (TextView) view.findViewById(R.id.home_address);
        officeAddress = (TextView) view.findViewById(R.id.office_address);
        memberEmail = (TextView) view.findViewById(R.id.member_email);
        memberDOB = (TextView) view.findViewById(R.id.member_dob);
        memberAnniversary = (TextView) view.findViewById(R.id.member_anniversary);
        userPhone = (ImageButton) view.findViewById(R.id.user_phone);
        userSMS = (ImageButton) view.findViewById(R.id.user_sms);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        memberName.setText(customerVO.getName());
        jcrtName.setText(customerVO.getJcrtName());
        memberDesignation.setText(customerVO.getCurrentRole());
        lomName.setText(customerVO.getLomName());
        memberOccupation.setText(customerVO.getCompanyName());
        memberCategory.setText(customerVO.getCategory());
        memberProduct.setText(customerVO.getProducts());

        if (customerVO.getWebsite() != null) {
            //website.setText("<a href=\"" + customerVO.getWebsite() + "\" >" + customerVO.getWebsite() + "</a>");
            website.setClickable(true);
            website.setMovementMethod(LinkMovementMethod.getInstance());
            String text = "<a href=\"" + customerVO.getWebsite() + "\" >" + customerVO.getWebsite() + "</a>";
            website.setText(Html.fromHtml(text));
        }

        memberContact.setText(customerVO.getMobile());
        bloodGroup.setText(customerVO.getBloodGroup());
        memberEmail.setText(customerVO.getEmailID());
        if (customerVO.getDateOfBirth() != null) {
            memberDOB.setText(dateFormat.format(customerVO.getDateOfBirth()));
        }
        if (customerVO.getAnniversary() != null) {
            memberAnniversary.setText(dateFormat.format(customerVO.getAnniversary()));
        }
        homeAddress.setText(customerVO.getAddress());
        officeAddress.setText(customerVO.getOfficeAddress());

        userPhone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Method to make a phone call to members
                CommunicationsUtils.onCall(getActivity(), customerVO.getMobile());
                /*try {
                    // set the data
                    String uri = "tel:" + customerVO.getMobile();
                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));
                    startActivity(callIntent);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Your call has failed...",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }*/
            }
        });

        userSMS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    // set the data
                    String uri = "sms:" + customerVO.getMobile();
                    Intent callIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(callIntent);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Your call has failed...",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 123:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    CommunicationsUtils.onCall(getActivity(), customerVO.getMobile());
                } else {
                    Snackbar.make(getView(), "Permission Not Granted.", 2000);
                    Log.d("TAG", "Call Permission Not Granted");
                }
                break;

            default:
                break;
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "MenuItemId :" + item.getItemId());

        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

/*    @Override
    public void onBackStackChanged() {
        // enable Up button only  if there are entries on the backstack
        if(getActivity().getSupportFragmentManager().getBackStackEntryCount() < 1) {
            ((MainActivity)getActivity()).hideUpButton();
        }
    }*/

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
}
