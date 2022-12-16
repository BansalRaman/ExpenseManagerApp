package com.developer.ramanbansal.billsplitter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.developer.ramanbansal.billsplitter.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class HomeActivity<findViewById> extends Activity {
    TextView name,mail;
    Button logout;


    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        View currentPage = null;
//        switch(position){
//            case 0:
//                currentPage = LayoutInflater.from(context).inflate(R.layout.page0, null)
//                break;
//            case 1:
//                currentPage = LayoutInflater.from(context).inflate(R.layout.page1, null)
//                ///////////// This page will be default ////////////////////
//                ((ViewPager)container).setCurrentItem(position);
//                ////////////////////////////////////////////////////////////
//                break;
//            case 2:
//                currentPage = LayoutInflater.from(context).inflate(R.layout.page2, null)
//                break;
//            return currentPage;
//        }

        @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        name= findViewById(R.id.name);
        mail=findViewById(R.id.mail);
        logout=findViewById(R.id.logout);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc= GoogleSignIn.getClient(this,gso);
        GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null){
            String Name= account.getDisplayName();
            String Mail= account.getEmail();
            name.setText(Name);
            mail.setText(Mail);
        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignOut();
            }
        });
        findViewById(R.id.startt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ExpenActivity.class);
                startActivity(intent);
            }
        });

    }
    private void SignOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(getApplicationContext(),main_signActivity.class));
                finish();
            }
        });
    }

}
