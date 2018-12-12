package in.technogenie.hamlet.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import in.technogenie.hamlet.R;
import in.technogenie.hamlet.beans.GalleryItemVO;
import in.technogenie.hamlet.utils.ScreenUtils;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private ArrayList<GalleryItemVO> galleryList;
    private Context context;
    //Declare GalleryAdapterCallBacks
    private GalleryAdapterCallBacks mAdapterCallBacks;

    public GalleryAdapter(Context context, ArrayList<GalleryItemVO> galleryList) {
        this.galleryList = galleryList;
        this.context = context;
    }

    public GalleryAdapter(Context context) {
        this.context = context;
        //get GalleryAdapterCallBacks from contex
        this.mAdapterCallBacks = (GalleryAdapterCallBacks) context;
        //Initialize GalleryItem List
        this.galleryList = new ArrayList<>();
    }

    //This method will take care of adding new Gallery items to RecyclerView
    public void addGalleryItems(List<GalleryItemVO> galleryList) {
        int previousSize = this.galleryList.size();
        this.galleryList.addAll(galleryList);
        notifyItemRangeInserted(previousSize, galleryList.size());
    }



    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gallery_cell, viewGroup, false);
        return new ViewHolder(view);

    }

    public void onBindViewHolder(GalleryAdapter.ViewHolder viewHolder, final int i) {
       /* viewHolder.title.setText(galleryList.get(i).getImageName());
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        viewHolder.img.setImageResource(new Integer((galleryList.get(i).getImageUri())));*/

        //get current Gallery Item
        GalleryItemVO currentItem = galleryList.get(i);
        //Create file to load with Picasso lib
        File imageViewThoumb = new File(currentItem.getImageUri());

        //Load with Picasso
        Picasso.get()
                .load(imageViewThoumb)
                .centerCrop()
                .resize(ScreenUtils.getScreenWidth(context) / 2, ScreenUtils.getScreenHeight(context) / 3)//Resize image to width half of screen and height 1/3 of screen height
                .into(viewHolder.img);
        //set name of Image
        viewHolder.title.setText(currentItem.getImageName());
        //set on click listener on imageViewThumbnail
        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call onItemSelected method and pass the position and let activity decide what to do when item selected
                mAdapterCallBacks.onItemSelected(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView img;
        public ViewHolder(View view) {
            super(view);

            title = (TextView)view.findViewById(R.id.title);
            img = (ImageView) view.findViewById(R.id.img);

            //img.setOnClickListener();
        }
    }


    //Interface for communication of Adapter and MainActivity
    public interface GalleryAdapterCallBacks {
        void onItemSelected(int position);
    }
}