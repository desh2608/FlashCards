package com.example.flashcards;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class FlashGame extends SurfaceView implements SurfaceHolder.Callback{

	public FlashGame(Context context) {
		super(context);
		getHolder().addCallback(this);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		setWillNotDraw(false); //Allows us to use invalidate() to call onDraw()


	     Thread _thread = new PanelThread(getHolder(), this); //Start the thread that
	        ((PanelThread) _thread).setRunning(true);                     //will make calls to 
	        _thread.start();                              //onDraw()
	}
	

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("null")
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		 try {
	           Thread _thread = null; 
	           ((PanelThread) _thread).setRunning(false);                //Tells thread to stop
	     _thread.join();                           //Removes thread from mem.
	 } catch (InterruptedException e) {}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		
	}
	
	class PanelThread extends Thread{
		private SurfaceHolder _surfaceHolder;
		private FlashGame _panel;
        private boolean _run = false;
        
        public PanelThread(SurfaceHolder surfaceHolder, FlashGame panel) {
            _surfaceHolder = surfaceHolder;
            _panel = panel;
        }


        public void setRunning(boolean run) { //Allow us to stop the thread
            _run = run;
        }


        @Override
        public void run() {
            Canvas c;
            while (_run) {     //When setRunning(false) occurs, _run is 
                c = null;      //set to false and loop ends, stopping thread


                try {


                    c = _surfaceHolder.lockCanvas(null);
                    synchronized (_surfaceHolder) {


                     //Insert methods to modify positions of items in onDraw()
                     postInvalidate();


                    }
  } finally {
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }
    }
}
	

