package pozzo.watchdog.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.SeekBar;

/**
 * This is a specialized {@link SeekBar} created to a more interactive way to user select its check
 * 	frequency over our watch dog application.
 *
 * @author Luiz Gustavo Pozzo
 * @since 19/04/15.
 */
public class FrequencySeekBar extends SeekBar {
	private float fontSize;

	public FrequencySeekBar(Context context) {
		super(context);
		init();
	}

	public FrequencySeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public FrequencySeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	/**
	 * Just to make sure all is as we wanted it to be, we call it for every constructor.
	 */
	private void init() {
		setPadding(120, 0, 0, 0);
		setMax(50);
		fontSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				18f, getResources().getDisplayMetrics());
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		//Choose our text color
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextSize(fontSize);

		//Draw a little lower than middle
		float y = getHeight() / 1.5f;
		canvas.drawText(inScaleToString(getProgress()), 0f, y, paint);
	}

	/**
	 * Take a look at {@link #getProgressInSeconds} for specialized progress.
	 *
	 * @return progress 0-100.
	 */
	@Override
	public synchronized int getProgress() {
		return super.getProgress();
	}

	/**
	 * @return selected progress in seconds.
	 */
	public int getProgressInSeconds() {
		return inScale(getProgress());
	}

	/**
	 * @return selected progress in milliseconds.
	 */
	public long getProgressInMilliseconds() {
		return inScale(getProgress()) * 1000l;
	}

	/**
	 * Created to be a scale represented from original 0 - 50 converted to 5 - 43200.
	 * It does not follow a linear scale.
	 *
	 * @param progress selected.
	 * @return Converted for second scale.
	 */
	private int inScale(int progress) {
		if(progress < 13) {
			return progress * 5;
		} else if(progress < 21) {
			return (progress - 11) * 60;
		} else if(progress < 40) {
			return (progress - 19) * 300;
		} else {
			return (progress - 38) * 3600;
		}
	}

	/**
	 * ex: 5s, 60s, 120s, 10m, 100m, 2h, 12h.
	 *
	 * @param progress selected.
	 * @return Represented as a human readable string.
	 */
	private String inScaleToString(int progress) {
		if(progress < 13) {
			return (progress * 5) + "s";
		} else if(progress < 21) {
			return ((progress - 11) * 60) + "s";
		} else if(progress < 40) {
			return ((progress - 19) * 5) + "m";
		} else {
			return (progress - 38) + "h";
		}
	}
}
