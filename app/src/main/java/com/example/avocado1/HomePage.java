package com.example.avocado1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.Navigation;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.avocado1.DatabaseAccess;


import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ViewDatabase";
    private Toolbar toolbar;
    private TextView helloUser;
    private FirebaseAuth mAuth;
    private User muser;
    private User user;
    private TextView emailNav;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private List<String> genresList = new ArrayList<>();
    private Button buttons[] = new Button[5];
    TextView textViewDisplayName;
    TextView textViewDisplayGenres;
    TextView textViewDisplayFollowingMovies;
    TextView textViewDisplayFollowingTvshows;
    private TMDBRecyclerViewAdapter mTMDBRecyclerViewAdapter;
    private GetTMDBJsonData getTMDBJsonData;
    // private DatabaseAccess dba= new DatabaseAccess();
    // public String str1=null;
    //  public String str2="";
    private Map<String, Movie> userFollow;


    public static Map<String, Object> userMap;


    private static final String baseURL = "https://api.themoviedb.org/3/discover/movie?api_key=5ba2372e5f26794510a9b0987dddf17b&language=he-IL&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&year=2019";
    //private static final String baseURI ="https://api.themoviedb.org/3";
    //private static final String SearchURI ="https://api.themoviedb.org/3/search/movie?query=man in black&api_key=5ba2372e5f26794510a9b0987dddf17b&language=he-IL";
    //private static final String GenresListURI ="https://api.themoviedb.org/3/genre/movie/list?api_key=5ba2372e5f26794510a9b0987dddf17b&language=he-il";
    //private static final String TopRated_TVShowsURI =" https://api.themoviedb.org/3/tv/top_rated?api_key=5ba2372e5f26794510a9b0987dddf17b&language=he-il&page=1";
    private static final String language = "he-IL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        textViewDisplayName = (TextView) findViewById(R.id.helloUser);
        textViewDisplayGenres = (TextView) findViewById(R.id.genres);
        textViewDisplayFollowingMovies = (TextView) findViewById(R.id.followingMovies);
        textViewDisplayFollowingTvshows = (TextView) findViewById(R.id.followingTvshows);


        toolbar = findViewById(R.id.toolbarId);
        emailNav = findViewById(R.id.emailNavBarId);
        setSupportActionBar(toolbar);

        loadUserInformation();
//        updateGenres(genresList);
        DisplayGenres(buttons);
        // DisplayFollowing();
        // str1= dba.getPreferencesFromDatabase(str2);
        // Toast.makeText(HomePage.this, "preflist from dba"+ str1, Toast.LENGTH_LONG).show();

        setOnUserListener();
        Log.d("user:", "" + this.user);

        //   Toast.makeText(HomePage.this, "following movies from muser"+ userFollow.toString(), Toast.LENGTH_LONG).show();


    }

    @Override
    public void onStart() {
        super.onStart();
        // checkAuth();
//        mAuth.addAuthStateListener(mAuthListener);
    }

    private void loadUserInformation() {

        final FirebaseUser fbUser = mAuth.getCurrentUser();


        if (fbUser.getDisplayName() != null) {
            textViewDisplayName.setText("·¯ÂÎÈÌ ‰·‡ÈÌ,  " + fbUser.getDisplayName() + "!");
        }


    }

//    private void updateGenres(final List<String> genres) {
//
//        myRef = FirebaseDatabase.getInstance().getReference("Users");
//        final String userName = mAuth.getCurrentUser().getDisplayName();
//
//        myRef.child(userName).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                String genres = dataSnapshot.child("preferences").getValue().toString();
//
//                genres = genres.replace("[", "");
//                genres = genres.replace("]", "");
//
//                textViewDisplayGenres.setText(genres);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//
//        });
//
//    }

//    private void displayFollowingMovies () {
//
//        Map<String, HashMap<String,Movie>> movieMap = this.user.getFollowingMovies();
//
//
//        Iterator<Map.Entry<String, HashMap<String,Movie>>> it = movieMap.entrySet().iterator();
//        Movie movie= new Movie();
//
//
//        while (it.hasNext()) {
//
//            Map<String, Movie> map = it.next().getValue();
//            Iterator<Map.Entry<String,Movie>> it1 = map.entrySet().iterator();
//            ArrayList<Movie> movieArrayList = new ArrayList<>();
//            while(it1.hasNext()){
//                Entry<String, Movie> entry = it1.next();
//
//                switch(entry.getKey()) {
//
//                    case ("title"):
//                       movie.setTitle(entry.getValue());
//                        Log.d("Title:","" + it1.next().getKey() + "value:" + movie.getTitle());
//                        break;
//
//                    case ("popularity"):
//                        movie.setPopularity(entry.get);
//
//                        break;
//
//
//                    case ("overview"):
//                        break;
//
//
//                    case ("poster"):
//                        break;
//
//
//                }
//
//                Log.d("movieAdded:","" + it1.next().getKey() + "value:" + it1.next().getValue());
//                Log.d("Title:","" + it1.next().getKey() + "value:" + movie.getTitle());
//            }
//
//
//
//
//            //   textViewDisplayFollowingMovies.setText(this.user.getFollowingMovies().toString());
//        }
//    }

    private void DisplayGenres(final Button buttons[]) {
        myRef = FirebaseDatabase.getInstance().getReference("Users");
        final String userName = mAuth.getCurrentUser().getDisplayName();


        myRef.child(userName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String genresStr = textViewDisplayGenres.getText().toString();

                long numOfGenres = dataSnapshot.child("preferences").getChildrenCount();

                createButtons(genresStr, numOfGenres);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void checkAuth() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(HomePage.this, "Successfully signed in with: " + user.getEmail(), Toast.LENGTH_LONG).show();

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Toast.makeText(HomePage.this, "Successfully signed out " + user.getEmail(), Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private void createButtons(String genreStr, long numOfGenres) {
        Log.d(TAG, "string: " + genreStr);


        List<String> genresList = Arrays.asList(genreStr.split("\\s*,\\s*"));
        Toast.makeText(HomePage.this, "genres list from func: " + genresList, Toast.LENGTH_LONG).show();

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.buttonsLayout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int color = 0x5AB300;

        for (int i = 0; i < numOfGenres; i++) {

            Button genresBtn = new Button(this);
            linearLayout.addView(genresBtn);
            genresBtn.setText(genresList.get(i));
            //genresBtn.setBackgroundColor(Color.RED);

            //genresBtn.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, color));

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);


    }


    private void setOnUserListener() {

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getDisplayName()).child("followingMovies");
//        final String userName = mAuth.getCurrentUser().getDisplayName();

        //Log.d("xxxx"+user.getFollowingMovies());


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
//                List<Object> values = (List<Object>) td.values();

                ArrayList<Movie> arrayList = new ArrayList<>();
                Iterator it = td.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();

                    System.out.println(pair.getKey() + " = " + pair.getValue());
                    DatabaseReference movieRef = myRef.child((String) pair.getKey());

                    movieRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Movie movie = dataSnapshot.getValue(Movie.class);

                            System.out.println(movie.toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }


//                createUser();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void createUser() {
        if (user == null) {
            this.user = new User();

        }
        this.user.setEmail((String) userMap.get("email"));
        if (userMap.get("followingMovies") != null)
            this.user.setFollowingMovies((List<Movie>) userMap.get("followingMovies"));

        if (userMap.get("followingTvShows") != null)
            this.user.setFollowingTVshows((ArrayList<Movie>) userMap.get("followingTvShows"));
        this.user.setId((String) userMap.get("id"));
        this.user.setUsername((String) userMap.get("userName"));
        if (userMap.get("preferences") != null)
            this.user.setPreferences((ArrayList<String>) userMap.get("preferences"));
        this.user.setPassword((String) userMap.get("password"));


        //  displayFollowingMovies();


    }


    @Override
    public void onBackPressed() {
     /*   DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
       */
    }

    public boolean onNavigationItemSelected(MenuItem item) {

        return true;


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_homeId:
                Intent HomeIntent = new Intent(this, HomePage.class);
                startActivity(HomeIntent);
                return true;


            case R.id.action_accountId:
                Intent AccountIntent = new Intent(this, AccountPage.class);
                startActivity(AccountIntent);
                return true;

            case R.id.action_notificationId:
                Toast.makeText(this, "notifications selected", Toast.LENGTH_LONG).show();
                return true;

            case R.id.action_genresId:
                Toast.makeText(this, "genres selected", Toast.LENGTH_LONG).show();
                return true;

            case R.id.action_moviesId:
                Intent MovieIntent = new Intent(this, MovieDetailActivity.class);
                startActivity(MovieIntent);
                return true;

            case R.id.action_tvShowsId:
                Intent TvShowsIntent = new Intent(this, TvShowDetailActivity.class);
                startActivity(TvShowsIntent);
                return true;

            case R.id.action_calendar:
                Intent CalendarIntent = new Intent(this, CalendarActivity.class);
                startActivity(CalendarIntent);
                return true;


            case R.id.action_signOutId:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                            }
                        });
                Toast.makeText(this, "signed out", Toast.LENGTH_LONG).show();
                Intent signOutIntent = new Intent(this, LoginPage.class);
                startActivity(signOutIntent);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }
}