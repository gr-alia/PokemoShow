package com.alia.pokemoshow;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";

    private RecyclerView mPokemoRecyclerView;
    private List<GalleryItem> mItems = new ArrayList<>();
    private GridLayoutManager mLayoutManager;

    public static Fragment newInstance() {
        return new GalleryFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemsTask().execute();
        Log.i(TAG, "Background thread started");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gallery, container,
                false);
        mPokemoRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_photo_gallery_recycler_view);
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mPokemoRecyclerView.setLayoutManager(mLayoutManager);
        setupAdapter();
        return v;
    }

    private void setupAdapter() {
        if (isAdded()) {
            mPokemoRecyclerView.setAdapter(new PokemoAdapter(mItems));
        }
    }

    private class PokemoAdapter extends RecyclerView.Adapter<PokemoHolder> {
        private List<GalleryItem> mGalleryItems;

        public PokemoAdapter(List<GalleryItem> galleryItems) {
            mGalleryItems = galleryItems;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public PokemoHolder onCreateViewHolder(ViewGroup viewGroup,
                                               int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.gallery_item, viewGroup, false);
            return new PokemoHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(PokemoHolder pokemoHolder, int position) {
            //get element from your mGalleryItems dataset at this position
            GalleryItem galleryItem = mGalleryItems.get(position);
            //replace the contents of the view with that element
            pokemoHolder.bindPokemoItem(galleryItem);

        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }

    private class PokemoHolder extends RecyclerView.ViewHolder {
        private ImageView mItemImageView;
        private TextView mItemTextView;

        public PokemoHolder(View itemView) {
            super(itemView);
            mItemImageView = (ImageView) itemView.findViewById(R.id.fragment_photo_gallery_image_view);
            mItemTextView = (TextView) itemView.findViewById(R.id.fragment_photo_gallery_name_place);

        }

        public void bindPokemoItem(GalleryItem galleryItem) {
            mItemTextView.setText(galleryItem.getName());

            Picasso.with(getActivity())
                    .load(galleryItem.getUrl())
                    .placeholder(R.drawable.some_img)
                    .into(mItemImageView);

        }
    }
    private class FetchItemsTask extends AsyncTask<Void, Void, List<GalleryItem>> {
        @Override
        protected List<GalleryItem> doInBackground(Void... params) {

            return new PokemonFetcher().fetchItems();

        }
        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            mItems = items;
            setupAdapter();
        }

    }
}
