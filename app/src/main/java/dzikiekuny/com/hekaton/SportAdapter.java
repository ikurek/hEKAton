package dzikiekuny.com.hekaton;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;



import java.util.List;



/**
 * Created by Bartosz Szlapa on 27/04/2017.
 */

public class SportAdapter extends RecyclerView.Adapter<SportAdapter.MyViewHolder> {

    private final List<SportModel> sportList;
    private final Context mContext;
    private final List<Boolean> takePart;

    public SportAdapter(List<SportModel> userList, List<Boolean> takePart, Context context) {
        this.sportList = userList;
        this.mContext = context;
        this.takePart = takePart;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        SportModel sport = sportList.get(position);
        if(takePart.get(position))
            holder.tick.setVisibility(View.VISIBLE);
        else
            holder.tick.setVisibility(View.INVISIBLE);
        holder.userAvatar.setImageResource(sport.getDrawableInt());
        holder.userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.tick.getVisibility()==View.INVISIBLE)
                    holder.tick.setVisibility(View.VISIBLE);
                else
                    holder.tick.setVisibility(View.INVISIBLE);
                takePart.set(position, !takePart.get(position));
            }
        });



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
