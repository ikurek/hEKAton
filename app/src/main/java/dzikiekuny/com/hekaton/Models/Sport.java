package dzikiekuny.com.hekaton.Models;

import android.content.Context;
import android.graphics.drawable.Drawable;

import dzikiekuny.com.hekaton.R;

/**
 * Created by kacperraczy on 03.06.2017.
 */

public enum Sport {
    Football, Basketball, Volleyball, TableTennis, Tennis, Cycling, Running;

    public Drawable getDrawable(Context context) {
        Drawable result = null;
        switch (this) {
            case  Football:
                result = context.getResources().getDrawable(R.drawable.football);
                break;
            case  Basketball:
                result = context.getResources().getDrawable(R.drawable.basketball);
                break;
            case Volleyball:
                result = context.getResources().getDrawable(R.drawable.volleyball);
                break;
            case TableTennis:
                result = context.getResources().getDrawable(R.drawable.tabletennis);
                break;
            case Tennis:
                result = context.getResources().getDrawable(R.drawable.tennis);
                break;
            case Cycling:
                result = context.getResources().getDrawable(R.drawable.cycling);
                break;
            case Running:
                result = context.getResources().getDrawable(R.drawable.running);
                break;
        }
        return result;
    }
}
