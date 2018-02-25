package appdev.obenita.fallingrandomobjects;

/**
 * Created by Obenita on 25/2/2018.
 */

public class Objects {
    int maxWidth;
    int maxHeight;
    int width;
    int height;
    //float rate;

    public Objects(int maxHeight, int maxWidth, int height, int width)
    {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.height = height;
        this.width = width;
        //rate = 5;
    }

    //public void tick(){posY += rate;}


    public int getMaxWidth()
    {
        return maxWidth;
    }

    public int getMaxHeight()
    {
        return maxHeight;
    }

    public int getWidth() {return width;}

    public int getHeight() {return height;}

    public void setHeight(int height){this.height = height;}
}
