package com.tamilgk3000.quizexams;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class AdapterWallpaper extends RecyclerView.Adapter {

    private static final int AD_VIEW_TYPE = 2;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final String[] users = new String[]{"Aadhini", "Aathi", "Amaravati", "Ankavi", "Anpumozhi", "Arulvezhi", "Aadhi",
            "Ravi", "Anbushelvan", "Anpalahan", "Boopalan", "Bharani", "Chentamilan", "Geerthi", "Chelliyan", "Eelavarasi",
            "Elakkieya", "Ellilmozhi", "Ellilazhahi", "Elilarasan", "Gagan", "Gagnesh", "Jai Darsh", "Kaamik", "Kanishk", "Kaushik",
            "Lenisha", "Maarish", "Nagila", "Nancy", "Nanda", "Omesh", "Onish", "Saanjali", "Sadhya", "Sabarish", "Sabita", "Saarik",
            "Saarth", "Saathvi", "Sabarish", "Taalish", "Taamas", "Taanish", "Tamilan", "Tanishk", "Tamil Selvi", "Ujwani", "Urvisha", "Vaahila"};
    private final List data;
    private ArrayList<String> bg_array;
    private Context context;
    private RecyclerViewClickListener recyclerViewClickListener;
    private ArrayList<Integer> bg_array1;
    private LoadRating loadRating;
    private Methods methods;


    public AdapterWallpaper(Context context, List<Object> arrayList, RecyclerViewClickListener recyclerViewClickListener) {
        this.data = arrayList;
        this.context = context;
        this.recyclerViewClickListener = recyclerViewClickListener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case AD_VIEW_TYPE:

            case VIEW_PROG:
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wall_layout_progressbar,
                        parent, false);
                return new ProgressViewHolder(v);
            default:
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.wall_layout_image_wall,
                        parent, false);
                final MyViewHolder myViewHolder = new MyViewHolder(itemView);
                return myViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case AD_VIEW_TYPE:

                break;

            default:
                if (holder instanceof MyViewHolder) {

//                    ((MyViewHolder) holder).textView_cat.setText(((ItemWallpaper) data.get(position)).getCName());
                    bg_array = new ArrayList<String>();
                    bg_array1 = new ArrayList<Integer>();

                    for (int i = 0; i < data.size(); i++) {
                        for (int j = 0; j < users.length; j++) {
                            bg_array.add(users[j]);
                        }
                    }
//                    bg_array.subList(data.size(), bg_array.size()).clear();
//                    bg_array1.subList(data.size(), bg_array1.size()).clear();

//                    int random = new Random().nextInt(bg_array.size());
                  /*  ((MyViewHolder) holder).circleImageView.setImageResource(bg_array1.get(random));
                    ((MyViewHolder) holder).Username.setText(bg_array.get(random));
*/

                    /*if (((ItemWallpaper) data.get(position)).getTotalViews() != null) {
                        ((MyViewHolder) holder).view_count.setText("" + ((ItemWallpaper) data.get(position)).getTotalViews() + " Views");
                    }*/

//                    if (((ItemWallpaper) data.get(position)).getTags() != null) {
//                        ((MyViewHolder) holder).iv_hash_tags.setVisibility(View.GONE);
//                        ((MyViewHolder) holder).iv_hash_tags.setText("" + ((ItemWallpaper) data.get(position)).getTags());
//                    } else {
//                        ((MyViewHolder) holder).iv_hash_tags.setVisibility(View.GONE);
//                    }

                 /*   if (((ItemWallpaper) data.get(position)).getTotalRate() != null) {
                        if (Integer.parseInt(((ItemWallpaper) data.get(position)).getTotalRate()) > 1) {
                            ((MyViewHolder) holder).rating_details.setText("" +
                                    Integer.parseInt(((ItemWallpaper) data.get(position)).getTotalRate()));
                        } else if (Integer.parseInt(((ItemWallpaper) data.get(position)).getTotalRate()) == 1) {
                            ((MyViewHolder) holder).rating_details.setText("" +
                                    Integer.parseInt(((ItemWallpaper) data.get(position)).getTotalRate()));
                        } else if (Integer.parseInt(((ItemWallpaper) data.get(position)).getTotalRate()) == 0) {
                            ((MyViewHolder) holder).rating_details.setText("0");
                        }
                    }*/


                    Picasso.get()
                            .load(((ItemWallpaper) data.get(position)).getImage())
                            .placeholder(R.mipmap.ic_launcher)
                            .into(((MyViewHolder) holder).imageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });


                  /*  ((MyViewHolder) holder).optionMenu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            BottomSheetMenuDialog dialog = new BottomSheetBuilder(context, R.style.AppTheme_BottomSheetDialog)
                                    .setMode(BottomSheetBuilder.MODE_LIST)
                                    .setMenu(R.menu.menu_bottom_simple_sheet)
                                    .setItemClickListener(new BottomSheetItemClickListener() {
                                        @Override
                                        public void onBottomSheetItemClick(MenuItem item) {
                                            switch (item.getItemId()) {
                                                case R.id.preview:
                                                    recyclerViewClickListener.onClick(holder.getAdapterPosition());
                                                    break;
                                                case R.id.download:
                                                    new ShareImages(context, "save").execute(((ItemWallpaper) data.get(holder.getAdapterPosition())).getImage());
                                                    Toast.makeText(context, "Image Download Successfully", Toast.LENGTH_SHORT).show();
                                                    break;
                                                case R.id.twitter_share:
                                                    new ShareImages(context, "twitter").execute(((ItemWallpaper) data.get(holder.getAdapterPosition())).getImage());
                                                    break;
                                                case R.id.instagram_share:
                                                    new ShareImages(context, "instagram").execute(((ItemWallpaper) data.get(holder.getAdapterPosition())).getImage());
                                                    break;
                                                case R.id.fb_share:
                                                    new ShareImages(context, "facebook").execute(((ItemWallpaper) data.get(holder.getAdapterPosition())).getImage());
                                                    break;
                                                case R.id.whatsapp_share:
                                                    new ShareImages(context, "whatsapp").execute(((ItemWallpaper) data.get(holder.getAdapterPosition())).getImage());
                                                    break;
                                                case R.id.share:
                                                    new ShareImages(context, "share").execute(((ItemWallpaper) data.get(holder.getAdapterPosition())).getImage());
                                                    break;
                                            }
                                        }
                                    })
                                    .createDialog();

                            dialog.show();
                        }
                    });*/
                    ((MyViewHolder) holder).share_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new ShareImages(context, "share").execute(((ItemWallpaper) data.get(holder.getAdapterPosition())).getImage());
                        }
                    });
                    ((MyViewHolder) holder).download_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new ShareImages(context, "save").execute(((ItemWallpaper) data.get(holder.getAdapterPosition())).getImage());
                            Toast.makeText(context, "Image Download Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });

                    ((MyViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                          recyclerViewClickListener.onClick(holder.getAdapterPosition());
                        }
                    });
                    ((MyViewHolder) holder).Whatsapps.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new ShareImages(context, "whatsapp").execute(((ItemWallpaper) data.get(holder.getAdapterPosition())).getImage());
                        }
                    });
                } else {
                    if (getItemCount() == 1) {
                        ProgressViewHolder.progressBar.setVisibility(View.GONE);
                    }
                }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void hideHeader() {
        ProgressViewHolder.progressBar.setVisibility(View.GONE);
    }

    public boolean isHeader(int position) {
        return (position >= 0 && position == data.size());
    }

    @Override
    public int getItemViewType(int position) {
        Object recyclerViewItem = data.get(position);
        if (recyclerViewItem instanceof ItemWallpaper) {
            return isHeader(position) ? VIEW_PROG : VIEW_ITEM;
        }
        return AD_VIEW_TYPE;
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private static ProgressBar progressBar;

        private ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
        }
    }

   /* private void populateNativeAdView(UnifiedNativeAd nativeAd,
                                      UnifiedNativeAdView adView) {
        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd);
    }
*/
    private class MyViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView circleImageView;
        TextView Username;
        ImageView optionMenu;
        ImageView share_layout, download_layout, Whatsapps, facebook, instagram;
        private RelativeLayout rootlayout;
        private ImageView imageView;
        //        private ImageView imageView_fav;
        private TextView textView_cat, rating_details, iv_hash_tags, view_count;
        private View vieww;

        private MyViewHolder(View view) {
            super(view);
//            circleImageView = view.findViewById(R.id.iv_cat_image);
            rootlayout = view.findViewById(R.id.rootlayout);

            imageView = view.findViewById(R.id.iv_wall);
//            Username = view.findViewById(R.id.iv_user_name);
//            optionMenu = view.findViewById(R.id.option_menu);
            rating_details = view.findViewById(R.id.rating_details);
//            iv_hash_tags = view.findViewById(R.id.iv_hash_tags);
            view_count = view.findViewById(R.id.tv_wall_details_views);
            share_layout = view.findViewById(R.id.share);
            download_layout = view.findViewById(R.id.download);
            Whatsapps = view.findViewById(R.id.whatsapp);

  facebook = view.findViewById(R.id.facebook);
//            instagram = view.findViewById(R.id.instagram);


//            imageView_fav = view.findViewById(R.id.iv_wall_fav);
//            likeButton = view.findViewById(R.id.button_wall_fav);
//            textView_cat = view.findViewById(R.id.tv_wall_cat);
            vieww = view.findViewById(R.id.view_wall);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class MyTask extends AsyncTask<String, Void, String> {

        String downloads = "", type = "";

        MyTask(String type) {
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = Methods.getJSONString(params[0]);
            try {
                JSONObject jOb = new JSONObject(json);
                JSONArray jsonArray = jOb.getJSONArray(Constant.TAG_ROOT);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject objJson = jsonArray.getJSONObject(i);
                    downloads = objJson.getString(Constant.TAG_WALL_DOWNLOADS);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return params[1];
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            int p = Integer.parseInt(result);
//            ((ItemWallpaper) data.get(p)).setTotalDownloads(downloads);
         /*   dbHelper.updateView(((ItemWallpaper) data.get(p)).getId(),
                    ((ItemWallpaper) data.get(p)).getTotalViews(),
                    ((ItemWallpaper) data.get(p)).getTotalDownloads());*/
        }
    }
}
