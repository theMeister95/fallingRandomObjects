package appdev.obenita.fallingrandomobjects;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends Activity implements GestureDetector.OnGestureListener{

    Chronometer myChronometer;
    RelativeLayout objectMargin;

    Integer [] image = {R.drawable.book, R.drawable.eraser, R.drawable.stress};

    int chronometerChecker = 0, checkerUser = 0;

    Animation fall, sideways;

    private ArrayList<Objects> object;
    int maxHeight= -1, maxWidth = -1;

    private GestureDetector gestureScanner;

    View.OnTouchListener gestureListener;
    MotionEvent initialME, finalME;
    VelocityTracker mVelocityTracker = null;

    ImageView userLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        objectMargin = (RelativeLayout) findViewById(R.id.rl);
        myChronometer = (Chronometer)findViewById(R.id.chronometer);

        myChronometer.setBase(SystemClock.elapsedRealtime());
        myChronometer.start();

        object = new ArrayList<Objects>();

        Vibrator  v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //v.vibrate(2500);

        myChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

            @Override

            public void onChronometerTick(Chronometer chronometer) {
                chronometerChecker++;
                Random rand = new Random();
                int rndImage, rndPosition, rndNumber, rndChecker = 1;

                rndImage = rand.nextInt(image.length);
                rndPosition = rand.nextInt(2);
                rndNumber = rand.nextInt(100);

                if(chronometerChecker > 3) {
                    if(chronometerChecker % 2==0) {
                        if (rndChecker == 1 && rndNumber < 50) {
                            doDraw(image[rndImage], rndPosition);
                            rndChecker = 1;
                        }
                    }
                    rndChecker = 0;
                }
                if(object.size() > 0) {
                    for (int i = 0; i < object.size(); i++) {
                        /*if (object.get(i).getMaxHeight() > objectMargin.getHeight()) {
                            Toast.makeText(MainActivity.this, "collision", Toast.LENGTH_SHORT).show();
                            object.remove(i);
                        }

                        if (object.get(i).getMaxHeight() < (userLogo.getHeight() / 2)) {
                            Toast.makeText(MainActivity.this, "collision", Toast.LENGTH_SHORT).show();
                            object.remove(i);
                        }*/
                        int obj = object.get(i).getHeight();

                        if(obj < objectMargin.getHeight()){
                            object.get(i).setHeight(obj -= 1);
                        }else{
                            object.remove(i);
                        }

                    }
                }
            }

        });

        userLogo = (ImageView) findViewById(R.id.imgLogo);

        gestureScanner = new GestureDetector(getBaseContext(), this);

        gestureListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        initialME= event;

                        if(mVelocityTracker == null) {
                            // Retrieve a new VelocityTracker object to watch the velocity of a motion.
                            mVelocityTracker = VelocityTracker.obtain();
                        }
                        else {
                            // Reset the velocity tracker back to its initial state.
                            mVelocityTracker.clear();
                        }
                        // Add a user's movement to the tracker.
                        mVelocityTracker.addMovement(event);

                        break;
                    case MotionEvent.ACTION_MOVE:
                        mVelocityTracker.addMovement(event);
                        // When you want to determine the velocity, call
                        // computeCurrentVelocity(). Then call getXVelocity()
                        // and getYVelocity() to retrieve the velocity for each pointer ID.
                        mVelocityTracker.computeCurrentVelocity(1000);
                        break;
                    case MotionEvent.ACTION_UP:
                        finalME=event;
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        // Return a VelocityTracker object back to be re-used by others.
                        mVelocityTracker.recycle();
                        break;
                }
                return onFling(initialME, finalME, mVelocityTracker.getXVelocity(), mVelocityTracker.getYVelocity());
                //return false;
            }
        };
        userLogo.setOnTouchListener(gestureListener);

    }

    public void doDraw(int image, int position){
        final ImageView imageView = new ImageView(getApplicationContext());

        imageView.setImageDrawable(getDrawable(image));

        Drawable drawable = imageView.getDrawable();
        try {
            Field maxWidthField = ImageView.class.getDeclaredField("mMaxWidth");
            Field maxHeightField = ImageView.class.getDeclaredField("mMaxHeight");
            maxWidthField.setAccessible(true);
            maxHeightField.setAccessible(true);

            maxWidth = (Integer) maxWidthField.get(imageView);
            maxHeight = (Integer) maxHeightField.get(imageView);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        LayoutParams layoutParams = new LayoutParams(150, 150);

        if(position == 1){
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }else{
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        }

        layoutParams.rightMargin = 160;
        layoutParams.leftMargin = 160;

        imageView.setAdjustViewBounds(true);
        imageView.setLayoutParams(layoutParams);

        Objects obj = new Objects(maxHeight, maxWidth, imageView.getHeight(), imageView.getWidth());
        object.add(obj);

        objectMargin.addView(imageView);

        //fall = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fall);
        //imageView.startAnimation(fall);
    }

    /*public void earthquake(){
        Vibrator  v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};

        if (v.hasVibrator()) {
            Log.v("Can Vibrate", "YES");
            // The '-1' here means to vibrate once, as '-1' is out of bounds in the pattern array
            v.vibrate(pattern, -1);
        } else {
            Log.v("Can Vibrate", "NO");
        }
    }*/

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        /*System.out.println("Inside onFling() of GenesMotionDetector.java");
        System.out.println("e1= "+ e1);
        System.out.println("e2= "+ e2);
        System.out.println("velocityX= "+ velocityX);
        System.out.println("velocityY= "+ velocityY);*/

        if((int)velocityX > 0 && checkerUser == 0){
            sideways = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right);
            userLogo.startAnimation(sideways);
            checkerUser++;
        }else if((int)velocityX < 0 && checkerUser == 1){
            sideways = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left);
            userLogo.startAnimation(sideways);
            checkerUser--;
        }
        return true;
    }
    @Override
    public boolean onTouchEvent(MotionEvent me) {return gestureScanner.onTouchEvent(me);}
    @Override
    public boolean onDown(MotionEvent e) {return true;}
    @Override
    public void onLongPress(MotionEvent e) {}
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {return true;}
    @Override
    public void onShowPress(MotionEvent e) {}
    @Override
    public boolean onSingleTapUp(MotionEvent e) {return true;}

}
