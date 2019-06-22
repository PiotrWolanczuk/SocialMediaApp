package wat.projectsi.client.adapter;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

import wat.projectsi.R;
import wat.projectsi.client.Picture;
import wat.projectsi.client.model.Image;

public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.ImageViewHolder> {
    private List<Image> mImages;
    private Context mContext;

    public ImageRecyclerAdapter(Context context, List<Image> images) {
        mImages = images;
        mContext = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ImageViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_list_element, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int i) {

        new Picture(holder.mImageView).execute(mImages.get(i).getUrl());
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Implement
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImages!=null?  mImages.size() :0;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;

        public ImageViewHolder(View view) {
            super(view);
            mImageView = view.findViewById(R.id.postImages);
        }
    }
}
