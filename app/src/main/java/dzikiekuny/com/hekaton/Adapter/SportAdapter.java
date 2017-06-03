package dzikiekuny.com.hekaton.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import dzikiekuny.com.hekaton.Models.SportModel;
import dzikiekuny.com.hekaton.R;

/**
 * Created by igor on 03.06.17.
 */

public class SportAdapter extends RecyclerView.Adapter<SportAdapter.MyViewHolder> {

    private final List<SportModel> sportList;
    private final Context mContext;

<<<<<<<Updated upstream
    public SportAdapter(List<SportModel> userList, Context context) {
=======
        private int pressed = -1;

    public SportAdapter(List < Sport > userList, Context context, AddNewActivity activity) {
>>>>>>>Stashed changes
        this.sportList = userList;
        this.mContext = context;
    }

    public int currentSelected() {
        return pressed;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
<<<<<<<Updated upstream
        SportModel sport = sportList.get(position);
        Log.e("TAK", String.valueOf(position));
        holder.userAvatar.setImageDrawable(mContext.getResources().getDrawable(R.drawable.tick));
=======
        Sport sport = sportList.get(position);

        holder.userAvatar.setImageDrawable((Sport.values()[position].getDrawable(this.mContext)));
        if (position != pressed)
            holder.tick.setVisibility(View.INVISIBLE);
        else {
            holder.tick.setVisibility(View.VISIBLE);

        }


        holder.userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Pozycja", Integer.toString(position));
                if (pressed == position)
                    pressed = -1;
                else
                    pressed = position;

                mActivity.refresh();

                Log.i("Pressed", Integer.toString(currentSelected()));
            }
        });
>>>>>>>Stashed changes
    }

    @Override
    public int getItemCount() {
        return sportList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final ImageView userAvatar;
        public final ImageView tick;

        public MyViewHolder(View view) {
            super(view);
            userAvatar = (ImageView) view.findViewById(R.id.user_avatar);
            tick = (ImageView) view.findViewById(R.id.tick);
        }
    }
}