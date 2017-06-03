package dzikiekuny.com.hekaton.Models;

/**
 * Created by igor on 03.06.17.
 */

public class SportModel {
    private int drawableInt;
    private String name;

    public SportModel(String name) {
        this.drawableInt = dzikiekuny.com.hekaton.R.id.tick;
        this.name = name;
    }

    public int getDrawableInt() {
        return drawableInt;
    }

    public void setDrawableInt(int drawableInt) {
        this.drawableInt = drawableInt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
