package com.example.avocado1;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.avocado1.DatabaseAccess;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;


class TMDBRecyclerViewAdapter extends RecyclerView.Adapter<TMDBRecyclerViewAdapter.TMDBMovieHolder>{

    private static final String TAG = "TMDBRecyclerViewAdapter";
    private List<Movie> mMoviesList;
    private Context mContext;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private List <String> followingMovies;
    private DatabaseAccess dba= new DatabaseAccess();


    public TMDBRecyclerViewAdapter(Context context, List<Movie> moviesList) {
        mContext = context;
        this.mMoviesList = moviesList;
    }

    @NonNull
    @Override
    public TMDBMovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        //Called by the layout manager when it needs new view
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_movie_detail, parent, false);
        return new TMDBMovieHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TMDBMovieHolder holder, final int position) {
        // called by the layout manager when it wants new data in an existing row
        final Movie movieItem = mMoviesList.get(position);
        Log.d(TAG, "onBindViewHolder: " + movieItem.getTitle() + "==>" + position);
        Picasso.get().load(movieItem.getPoster_path())
                     .error(R.drawable.baseline_broken_image_black_48dp)
                     .placeholder(R.drawable.baseline_broken_image_black_48dp)
                     .placeholder(R.drawable.baseline_broken_image_black_48dp)
                     .into(holder.moviePoster);

        holder.title.setText(movieItem.getTitle());
        holder.overview.setText(movieItem.getOverview());
/*
        holder.moviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "You Clicked the poster", Toast.LENGTH_LONG).show();
            }
        });


        holder.newMoviesLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(mContext, "You Clicked the layout ", Toast.LENGTH_LONG).show();
            }
        });


*/

        holder.followBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String movieTitle= null;
                String movieOverview= null;
                movieTitle= movieItem.getTitle();
                movieOverview= movieItem.getOverview();





//                followingMovies.add(movieTitle);

                updateMoviesToDataBase(followingMovies,movieItem);
                followingMovies= updateFollowing(followingMovies, movieItem);






            }
        });



    }



    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: called");
        return ((mMoviesList != null) && (mMoviesList.size() != 0) ? mMoviesList.size() : 0);
    }

    void loadNewData(List<Movie> newMovies){
        mMoviesList = newMovies;
        notifyDataSetChanged();
    }

//    public Movie getMovieIntoPosition{
//        return ((mMoviesList != null) && (mMoviesList.size() != 0) ? mMoviesList.get(position) : null);
//    }

    static class TMDBMovieHolder extends RecyclerView.ViewHolder{

        private static final String TAG = "TMDBMovieHolder";
        public LinearLayout newMoviesLinearLayout;


        ImageView moviePoster = null;
        TextView title = null;
        TextView overview= null;
        Button followBtn;


        //TextView overview = null;

        public TMDBMovieHolder(@NonNull View itemView) {
            super(itemView);

            newMoviesLinearLayout= (LinearLayout) itemView.findViewById(R.id.contentmoviedetail);
            Log.d(TAG, "TMDBMovieHolder: starts");
            this.moviePoster = (ImageView)itemView.findViewById(R.id.moviePoster);
            this.title = (TextView)itemView.findViewById(R.id.movieTitle);
            this.overview = (TextView)itemView.findViewById(R.id.movieOverview);
            this.followBtn= (Button) itemView.findViewById(R.id.followBtn);


        }


    }

    private void updateMoviesToDataBase(final List <String> followingMovies, final Movie movieItem) {


        myRef = FirebaseDatabase.getInstance().getReference("Users");

        final String userName = mAuth.getCurrentUser().getDisplayName();

                String movieItemStr = String.valueOf(movieItem.getId());
                ArrayList<Movie>  movieArrayList;

//                myRef.child(userName).child("followingMovies").setValue(followingMovies);
//                myRef.child(userName).child("followingMovies").setValue(movieItem.getId());
//                myRef.child(userName).child("followingMovies").child(movieItemStr);
                myRef.child(userName).child("followingMovies").child(movieItemStr).child("title").setValue(movieItem.getTitle());
                myRef.child(userName).child("followingMovies").child(movieItemStr).child("poster").setValue(movieItem.getPoster_path());
                myRef.child(userName).child("followingMovies").child(movieItemStr).child("overview").setValue(movieItem.getOverview());
                myRef.child(userName).child("followingMovies").child(movieItemStr).child("popularity").setValue(movieItem.getPopularity());

    }

    private List<String> updateFollowing ( final List<String> followingMovies, final Movie movieItem){

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("Users");
        final String userName = mAuth.getCurrentUser().getDisplayName();
        final String movieItemStr = String.valueOf(movieItem.getId());


        myRef.child(userName).child("followingMovies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              //  Map<String, Movie> map = (Map<String, Movie>) dataSnapshot.getValue();
             //   Log.d("mapmap:", map.toString());
              //  followingMovies.add(movieItemStr);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

      //  Toast.makeText(mContext, "following movies is now:"+followingMovies, Toast.LENGTH_LONG).show();

        return followingMovies;
    }
}
