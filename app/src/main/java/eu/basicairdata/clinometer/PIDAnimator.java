/*
 * PIDAnimator - Java Class for Android
 */

package eu.basicairdata.clinometer;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class PIDAnimator extends TimerTask {

    private final Handler handler = new Handler();
    private final Timer mTimer = new Timer();

    private float r = 0;            // Set-point
    private float y = 0;
    private float y_old = 0;
    private float u = 0;            // Output Value
    private float v = 0;

    private float t;
    private long t_ms;

    private float kp;
    private float ki;
    private float kd;
    private float kt = 0.3f;        // De-saturation gain

    private float P = 0;            // Proportional Action
    private float I = 0;            // Integral Action
    private float D = 0;            // Derivative Action


    public PIDAnimator(float initialValue, float Kp, float Ki, float Kd, long t_millis) {
        u = initialValue;
        y = initialValue;
        r = initialValue;
        t_ms = t_millis;
        t = t_ms / 1000.0f;

        kp = Kp;
        kd = Kd;
        ki = Ki;

        mTimer.scheduleAtFixedRate(this, 0, t_millis);
    }


    public void run() {
        handler.post(new Runnable() {
             @Override
             public void run() {
                 //Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

                 calculate();

                  // DEBUG LOG
//                long now = System.currentTimeMillis();
//                if (now - prevtime > 100) {
//                    Log.i("PIDAnimator", "UPDATE: u = " + u + " y = " + y + " r = " + r + " P = " + P + " I = " + I + " D = " + D);
//                    prevtime = now;
//                }
             }
        });
    }


    /**
     * Performs a step of the discrete time PID.
     */
    private void calculate() {
        P = kp * (r - y);                               // Proportional Action
        D = kd * (y - y_old) / t;                       // Derivative Action

        v = P + I + D;
        u = Math.min(3600.0f, Math.max(v, -3600.0f));   // Simulate the saturation of the sensor
        y_old = y;
        y = y + 0.3f *u;

        I = I + ki * (r - y) * t + kt * (u - v) * t;    // Integral Action
    }


    /**
     * Changes the final value of the Animation to a new value.
     * @param setPoint The new set Point
     */
    public void setTargetValue(float setPoint) {
        float deltar = r - setPoint;
        //Log.i("PIDAnimator", "UPDATE: r = " + r + "    setPoint = " + setPoint);
        if (Math.abs(deltar) > 180.0f) {
            if (deltar < 0.0f) {
                y += 360.0f;
                y_old += 360.0f;
            } else {
                y -= 360.0f;
                y_old -= 360.0f;
            }
        }
        r = setPoint;
    }

    /**
     * Returns the current value of the Animation.
     * @return The current value
     */
    public float getValue() {
        return y % 360.0f;
    }

    public void setValue(float setPoint) {
        P = 0;
        I = 0;
        D = 0;
        u = setPoint;
        y = setPoint;
        y_old = setPoint;
        r = setPoint;
    }
}
