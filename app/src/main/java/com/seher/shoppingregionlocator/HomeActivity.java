package com.seher.shoppingregionlocator;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.seher.shoppingregionlocator.Map.view.MainActivity;
import com.seher.shoppingregionlocator.helperClasses.User;
import com.seher.shoppingregionlocator.navigation.HomePageFragment;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {

    private HomePageFragment homePageFragment;
    private Profile prof;

//    private rooms room;
//    private devices device;
   // private AppBarConfiguration mAppBarConfiguration;
    private Bundle bundle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    private String username;
    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;
    private ImageView navImage;
    private TextView navUsername;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_homepage);

        toolbar= findViewById(R.id.toolbar);
        drawerLayout=findViewById(R.id.home_drawer_layout);
        navigationView=findViewById(R.id.home_drawer_navigation);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        homePageFragment =new HomePageFragment();
//        room=new rooms();
          prof=new Profile();
//        device=new devices();

//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);


        //on opening drawer the fragment should be
        if(savedInstanceState==null) {   //this if will check on rotation as on rotaion item destroy and recreate but due to this save instance selected item remain same
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homePageFragment).addToBackStack("home").commit();
            navigationView.setCheckedItem(R.id.home);
        }


        username=getIntent().getStringExtra("username");
        type=getIntent().getStringExtra("type");

        bundle=new Bundle();
        bundle.putString("username","abc");
        bundle.putString("type",type);




        View headerView = navigationView.getHeaderView(0);
        navUsername= (TextView) headerView.findViewById(R.id.navigation_header_username);

        navImage=headerView.findViewById(R.id.header_image);

//        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(type);
//        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    User u= postSnapshot.getValue(User.class);
//                    navUsername.setText(u.getFullname());
//
//                    if(username.equals(u.getFullname())) {
//                        if(u.getProfileImage()!=null)
//                        {
//                           // Toast.makeText(getApplicationContext(),"Image found",Toast.LENGTH_LONG).show();
////                            Glide.with(getApplicationContext())
////                                    .load(u.getProfileImage())
////                                    .centerCrop()
////                                    .placeholder(R.drawable.profile)
////                                    .into(navImage);
//                        }
//                        break;
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getApplicationContext(),databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.home: {
                   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homePageFragment).addToBackStack("home").commit();
                break;
            }
            case R.id.offline: {
                Intent i = new Intent(this, MainActivity.class);
                ArrayList<String> selectedStrings=new ArrayList<>();
                i.putStringArrayListExtra("SELECTED_PLACES", selectedStrings);
                i.putExtra("IS_OFFLINE", true);
                startActivity(i);

                //  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homePageFragment).addToBackStack("home").commit();
                break;
            }
            case R.id.favouites: {
                Intent i = new Intent(this, MainActivity.class);
                ArrayList<String> selectedStrings=new ArrayList<>();
                i.putExtra("fav", true);
                startActivity(i);

                //  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homePageFragment).addToBackStack("home").commit();
                break;
            }

//            case R.id.nav_surveillance: {
//
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, Surveillance).addToBackStack("Surveillance").commit();
//                Surveillance.setArguments(bundle);
//                break;
//            }
//            case R.id.nav_emergency_contacts: {
//
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, emergency_contact).addToBackStack("nav_emergency_contacts").commit();
//                emergency_contact.setArguments(bundle);
//                break;
//            }
//            case R.id.nav_floors: {
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, floor).addToBackStack("floor").commit();
//                floor.setArguments(bundle);
//                break;
//            }
//            case R.id.nav_rooms: {
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, room).addToBackStack("room").commit();
//                room.setArguments(bundle);
//                break;
//            }
            case R.id.profile: {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, prof).addToBackStack("prof").commit();
                prof.setArguments(bundle);
                break;
            }
//
            case R.id.logout: {
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent=new Intent(getApplicationContext(), Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                break;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

//    @Override
//    public void passDara(String data) {
//        device.updateData( data);
//    }


//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
//

    @Override
    public void onDestroy() {
        super.onDestroy();
//        databaseReference.removeEventListener(eventListener);
    }


}
