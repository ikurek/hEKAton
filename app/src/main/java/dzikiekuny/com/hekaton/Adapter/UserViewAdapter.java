package dzikiekuny.com.hekaton.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;


import java.util.ArrayList;

import dzikiekuny.com.hekaton.Models.UserModel;
import dzikiekuny.com.hekaton.R;
import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * Created by Kevin on 25.04.2017.
 */

public class UserViewAdapter extends ArrayAdapter {
    private final Context context;
    private ArrayList<UserModel> listUsers = new ArrayList<>();
    private Button buttonFb;
    private Integer layout;

    public UserViewAdapter(Context context, Integer layout, ArrayList<UserModel> listUsers) {
        //super(context, R.layout.user_row, listUsersFbId);
        super(context, layout, listUsers);
        this.context = context;
        this.listUsers = listUsers;
        this.layout = layout;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(layout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.user_row_text);
        final String fbId = listUsers.get(position).getFbid();
        final String name = listUsers.get(position).getName();
        //fb redirect
        buttonFb = (Button) rowView.findViewById(R.id.user_row_fb_button);
            buttonFb.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    context.startActivity(getFacebookIntent(fbId));
                }
            });

        textView.setText(name);

            final ImageView imageView = (ImageView) rowView.findViewById(R.id.user_row_photo);
            Glide.with(context).load("https://graph.facebook.com/" + fbId + "/picture?type=normal")
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(imageView);

        return rowView;
    }

    private Intent getFacebookIntent(String fbId){
        Uri uri;
        try{
            int versionCode = context.getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                uri = Uri.parse("fb://facewebmodal/f?href=" + "https://www.facebook.com/" + fbId);
            } else { //older versions of fb app
                uri = Uri.parse("fb://profile/" + fbId);
            }
        }catch (PackageManager.NameNotFoundException e) {
            uri = Uri.parse("https://www.facebook.com/" + fbId);
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }
}
