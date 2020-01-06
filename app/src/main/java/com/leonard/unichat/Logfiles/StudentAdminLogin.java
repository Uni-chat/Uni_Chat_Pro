package com.leonard.unichat.Logfiles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leonard.unichat.Messages.MainActivity;
import com.leonard.unichat.R;
import com.leonard.unichat.Utils;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
//{@link StudentAdminLogin.OnFragmentInteractionListener} interface
 // to handle interaction events.
 // Use the {@link StudentAdminLogin#newInstance} factory method to
 //create an instance of this fragment.//
public class StudentAdminLogin extends Fragment {


    private View view;
    private AutoCompleteTextView edtStdAdminLoginEmail, edtStdAdminLoginPass;
    private String txtPassColor;
    private TextView forgtPass;
    private Button signInValue;
    private String usType = "Admin";
    private String currentUserUID, userType;
    private CheckBox rememberChechBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPreferenceEditor;
    private boolean saveLoginState;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference checkUserRef;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private static FragmentManager fragmentManager;


  /*  // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
*/
    public StudentAdminLogin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
     //@param param1 Parameter 1.
     ////@param param2 Parameter 2.
      //@return A new instance of fragment StudentAdminLogin.

    /*// TODO: Rename and change types and number of parameters
    public static StudentAdminLogin newInstance(String param1, String param2) {
        StudentAdminLogin fragment = new StudentAdminLogin();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_student_admin_login, container, false);
        initViews();
        return view;
    }

    /*// TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

   /* public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/

    private void initViews () {

        edtStdAdminLoginEmail = (AutoCompleteTextView) view.findViewById(R.id.edtStdAdminLoginEmail);
        edtStdAdminLoginPass = (AutoCompleteTextView) view.findViewById(R.id.edtStdAdminLoginPass);
        forgtPass = (TextView) view.findViewById(R.id.forgtPass);
        signInValue = (Button) view.findViewById(R.id.signInValue);

        // CheckBox implementation
        rememberChechBox = (CheckBox) view.findViewById(R.id.rememberChechBox);
        loginPreferences = getActivity().getSharedPreferences("loginPrefsData", Context.MODE_PRIVATE);
        loginPreferenceEditor = loginPreferences.edit();

        // CheckBox Preferences
        saveLoginState = loginPreferences.getBoolean("saveLoginState", true);
        if (saveLoginState == true) {
            edtStdAdminLoginEmail.setText(loginPreferences.getString("Username",null ));
            edtStdAdminLoginPass.setText(loginPreferences.getString("Password", null));
        }


      fragmentManager = getActivity().getSupportFragmentManager();

        firebaseAuth = firebaseAuth.getInstance();
        fragmentManager = getActivity().getSupportFragmentManager();

      txtPassColor = "<font color = white > Don't you remember your </font> <font color = yellow > password ? </font>";
      forgtPass.setText(Html.fromHtml(txtPassColor));

        signInValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adminLoginToApp();
            }
        });

        forgtPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentManager.beginTransaction().replace(R.id.frameContainer,
                        new ForgetPassword(), Utils.ForgetPassword).commit();
            }
        });
    }

    private void adminLoginToApp () {

        String adminUserLoginEmail = edtStdAdminLoginEmail.getText().toString().trim();
        String adminUserLoginPassword = edtStdAdminLoginPass.getText().toString().trim();

        if (TextUtils.isEmpty(adminUserLoginEmail)) {

            Toast.makeText( getActivity(), " Enter Email Address !",Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(adminUserLoginPassword)) {
            Toast.makeText(getActivity(), " Enter Your Password !", Toast.LENGTH_SHORT).show();
            return;
        }

        // Remember Checked
        if (rememberChechBox.isChecked()){
            loginPreferenceEditor.putBoolean("saveLoginState", true);
            loginPreferenceEditor.putString("Username", adminUserLoginEmail);
            loginPreferenceEditor.putString("Password", adminUserLoginPassword);
            loginPreferenceEditor.commit();
        }

        firebaseAuth.signInWithEmailAndPassword(adminUserLoginEmail, adminUserLoginPassword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {

                        firebaseDatabase = FirebaseDatabase.getInstance();
                        checkUserRef = firebaseDatabase.getReference("Users/Admin");
                        currentUserUID = firebaseAuth.getCurrentUser().getUid();

                        checkUserRef.child(currentUserUID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    userType = dataSnapshot.child("USER_TYPE").getValue().toString();

                                    if (userType.equals(usType)) {

                                        if (task.isSuccessful()) {

                                            try {

                                                Intent intent = new Intent(getContext(), MainActivity.class);
                                                intent.putExtra("US_REF", usType);
                                                startActivity(intent);

                                                Toast.makeText(getActivity(), "Log in Success.", Toast.LENGTH_SHORT).show();
                                                getActivity().finish();


                                            } catch (Exception e) {

                                                e.printStackTrace();
                                            }
                                        } else {
                                            Toast.makeText(getActivity(), "Log in Failed.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {

                                        FirebaseAuth.getInstance().signOut();
                                        Toast.makeText(getActivity(), "Your are not Registered as Admin", Toast.LENGTH_SHORT).show();

                                    }
                                } else {

                                    FirebaseAuth.getInstance().signOut();
                                    Toast.makeText(getActivity(), "Your are not Registered as Admin", Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });
    }
}
