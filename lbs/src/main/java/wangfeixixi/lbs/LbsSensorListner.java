package wangfeixixi.lbs;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public abstract class LbsSensorListner implements SensorEventListener {

    private Context mCtx;

    public LbsSensorListner(Context ctx) {
        mCtx = ctx;
    }

    private long lastTime = 0;
    private final int TIME_SENSOR = 100;
    private float mAngle;

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (System.currentTimeMillis() - lastTime < TIME_SENSOR) {
            return;
        }
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ORIENTATION: {
                float x = event.values[0];
                x += SensorRotateUtils.getScreenRotationOnPhone(mCtx);
                x %= 360.0F;
                if (x > 180.0F)
                    x -= 360.0F;
                else if (x < -180.0F)
                    x += 360.0F;

                if (Math.abs(mAngle - x) < 3.0f) {
                    break;
                }
                mAngle = Float.isNaN(x) ? 0 : x;
                onSensorLisner(360 - mAngle);
                lastTime = System.currentTimeMillis();
            }
        }

    }

    public abstract void onSensorLisner(float v);
}
