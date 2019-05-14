package wat.projectsi.client.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import wat.projectsi.R;

public class GalleryAdapter extends BaseAdapter {

    private Context ctx;
    private ArrayList<Uri> mArrayUri;
    public GalleryAdapter(Context ctx, ArrayList<Uri> mArrayUri) {

        this.ctx = ctx;
        this.mArrayUri = mArrayUri;
    }

    @Override
    public int getCount() {
        return mArrayUri.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayUri.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.single_image, parent, false);

        ImageView ivGallery = (ImageView) itemView.findViewById(R.id.single_photo);

        ivGallery.setImageURI(mArrayUri.get(position));

        return itemView;
    }
}
