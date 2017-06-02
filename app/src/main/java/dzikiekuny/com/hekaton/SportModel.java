package dzikiekuny.com.hekaton;

/**
 * Created by wodzu on 03.06.17.
 */

public class SportModel {
    int drawableInt;
    String name;

    public SportModel(int drawableInt, String name) {
        this.drawableInt = drawableInt;
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
