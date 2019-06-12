package wat.projectsi.client;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import wat.projectsi.R;

public class UniversalImageLoader extends PagerAdapter {

    Activity activity;
    ImageLoader imageLoader;
    LayoutInflater inflater;
    String[] images;


    public UniversalImageLoader(Activity activity,ImageLoader imageLoader, String[] images) {
        this.imageLoader = imageLoader;
        this.activity = activity;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_item,  container, false);

        ImageView imageView = itemView.findViewById(R.id.imageViewForPage);
        imageLoader.displayImage(images[position], imageView);

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull View container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((View)object);
    }

}
