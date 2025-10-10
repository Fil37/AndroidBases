package UPsay.decouverteAndroid;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class BaseAndroidGraphics extends View {
    float xText,yText;
    float x1, x2, y1, y2;
    public BaseAndroidGraphics(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setXYText(600,600);
        timerHandler.postDelayed(updateTimerThread, 10);
        setOnTouchListener(onTouchListener);
        Sensor accelerometre;
        SensorManager m	=	(SensorManager)	context.getSystemService	(Context.SENSOR_SERVICE);
        accelerometre	=	m.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        m.registerListener(mSensorEventListener,accelerometre,	SensorManager.SENSOR_DELAY_UI);
        invalidate();
    }
    private int pencheH;
    private int penche;
    private int pencheV;
    @Override public void onDraw (Canvas canvas)
    {

        Paint p = new Paint();
        /*définir la couleur de l’objet de dessin */
        p.setColor(Color.BLACK);
        /*définir son style en remplissage*/
        p.setStyle(android.graphics.Paint.Style.FILL);
        /*dessiner un rectangle qui occupe la totalité du View*/
        canvas.drawRect(0,0,getWidth(),getHeight(), p);
        /*définir une autre couleur pour dessiner un texte*/
        p.setColor(Color.GREEN);
        /*définir la taille du texte*/
        p.setTextSize(100);
        /*définir le centre du texte comme étant son origine*/
        p.setTextAlign(android.graphics.Paint.Align.CENTER);
        /*dessiner le texte en positionnant son origine au centre du View */
        String texte = "Bonjour MONDE";
        canvas.drawText(texte, xText , yText, p);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        int height = (int)((float)canvas.getWidth()/b.getWidth()*b.getHeight());
        b = Bitmap.createScaledBitmap(b, canvas.getWidth(), height, false);
        canvas.drawBitmap(b, 0, 0, p);
        Toast.makeText(getContext(),	"Il faut\tsaisir\tune\theure",Toast.LENGTH_SHORT).show();
    }
    private void setXYText(float x, float y) {
        xText = x;
        yText = y;
    }
//    @Override public boolean onTouchEvent(MotionEvent event) {
//        xText = event.getX();
//        yText = event.getY();
//        invalidate();
//        return false;
//    }
    Handler timerHandler = new Handler();
    Runnable updateTimerThread = new Runnable()
    {
        public void run() {
            timerHandler.postDelayed(this,100);
            if(xText <= getWidth() && yText<=getHeight()){
                xText = xText + 1;
                yText = yText + 1;
            }
            else{
                yText = yText -1;
            }
            invalidate();     // appel de onDraw pour redessiner
            }
    };
    OnTouchListener onTouchListener = new OnTouchListener() {
        @Override public boolean onTouch(View v, MotionEvent event) {
            float dx, dy;
            String direction;
            switch(event.getAction())
            {
                case(MotionEvent.ACTION_DOWN):
                    x1 = event.getX();
                    y1 = event.getY();
                    Log.i("pacman", "appuyé");
                    break;
                case(MotionEvent.ACTION_UP): {
                    x2 = event.getX();
                    y2 = event.getY();
                    dx = x2-x1;
                    dy = y2-y1;             // Use dx and dy to determine the direction of the move
                    if(Math.abs(dx) > Math.abs(dy)) {
                        if(dx>0)
                            direction = "right";
                        else
                            direction = "left";
                    } else {
                        if(dy>0)
                            direction = "down";
                        else
                            direction = "up";
                    }
                    Log.i("pacman", "laché " + direction);
                    Log.i("pacman", "dx = " + dx +"; dy = " + dy);
                    break;
                }
            }
            invalidate();
            return true;
        }
    };
    final SensorEventListener mSensorEventListener	=	new	SensorEventListener()
    {
        @Override
        	public	void	onAccuracyChanged(Sensor sensor,	int	accuracy)
        {

        }
        @Override
        public	void	onSensorChanged(SensorEvent sensorEvent)
        {	//	Que	faire	en	cas	d'évènements	sur	le	capteur	?
            pencheH	=	-(int)(sensorEvent.values[0]);
            pencheV = (int) (sensorEvent.values[1]);
            penche	=	pencheH*pencheH+pencheV* pencheV;
        }
    };


}
