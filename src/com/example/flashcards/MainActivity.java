package com.example.flashcards;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Handler;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.flashcards.drawing.GameBoard;


public class MainActivity extends ActionBarActivity {
	
	private int time_delay;
	private int choice;
	TextView[] box = new TextView[15];
	Button bReset;
	TextView score;
	
	
	int total = 0;
	int high_score=0;
	
	final Context context = this;
	final Timer    timer = new Timer();
	final Handler handler = new Handler();
	
	
	private Handler frame = new Handler();
    //Divide the frame by 1000 to calculate how many times per second the screen will update.
    private static final int FRAME_RATE = 20; //50 frames per second
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initvars();
        time_delay = getIntent().getExtras().getInt("time");
        choice = getIntent().getExtras().getInt("choice");
        Handler h = new Handler();
        //((Button)findViewById(R.id.the_button)).setOnClickListener((OnClickListener) this);
        //We can't initialize the graphics immediately because the layout manager
        //needs to run first, thus we call back in a sec.
        h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                                initGfx();
                        }
         }, 1000);
        
        
        
            TimerTask doAsynchronousTask = new TimerTask() {       
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @SuppressWarnings("unchecked")
                        public void run() { 
                           try {
                                boolean notFull;
                                if(choice==1)
                                	notFull = generateEquationEquals();
                                else
                                	notFull = generateEquationFactor();
                                if(!notFull){
                                	AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);

                                	dlgAlert.setMessage("GAME OVER. Final score = "+score.getText()+". Click RESET to play again.");
                                	dlgAlert.setTitle("GAME OVER");
                                	dlgAlert.setPositiveButton("OK", null);
                                	dlgAlert.setCancelable(false);
                                	timer.cancel();
                                	dlgAlert.create().show();
                                	updateHS();
                                	
                                	
                                }
                               }
                         catch (Exception e) {
                                // TODO Auto-generated catch block
                            }
                        }
                    });
                }
            };
            timer.schedule(doAsynchronousTask, 0, time_delay);
            
         
    }
    
    private void initvars() {
		// TODO Auto-generated method stub
		box[0] = (TextView)findViewById(R.id.box11);
		box[1] = (TextView)findViewById(R.id.box12);
		box[2] = (TextView)findViewById(R.id.box13);
		box[3] = (TextView)findViewById(R.id.box21);
		box[4] = (TextView)findViewById(R.id.box22);
		box[5] = (TextView)findViewById(R.id.box23);
		box[6] = (TextView)findViewById(R.id.box31);
		box[7] = (TextView)findViewById(R.id.box32);
		box[8] = (TextView)findViewById(R.id.box33);
		box[9] = (TextView)findViewById(R.id.box41);
		box[10] = (TextView)findViewById(R.id.box42);
		box[11] = (TextView)findViewById(R.id.box43);
		box[12] = (TextView)findViewById(R.id.box51);
		box[13] = (TextView)findViewById(R.id.box52);
		box[14] = (TextView)findViewById(R.id.box53);
		bReset = (Button)findViewById(R.id.reset);
		score = (TextView)findViewById(R.id.score);
		for(int i=0;i<15;i++){
   	    	box[i].setOnClickListener(tvClickListener);
	    }
		bReset.setOnClickListener(resetListener);
    }
    
    private OnClickListener resetListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			score.setText("0");
			for(int i=0;i<15;i++){
				box[i].setText("");
			}
			startActivity(getIntent());
		}
    	
    };
    
    private OnClickListener tvClickListener = new OnClickListener(){
    	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String id = getResources().getResourceEntryName(v.getId());
			String substr = id.length() > 2 ? id.substring(id.length() - 2) : id;
    		int i = Integer.parseInt(substr);
    		int x=0;
    		x = (i%10)-1;
    		i /= 10;
    		x += 3*(i-1);
    		int currSelection = x;
    		int prevSelection = searchSelected();
    		if(prevSelection==-1){
    			box[currSelection].setTextColor(Color.parseColor("#FFFFFF"));
    			box[currSelection].setBackgroundColor(Color.parseColor("#FF0000"));
    		}
    		else if(prevSelection==currSelection){
    			box[currSelection].setBackgroundColor(Color.parseColor("#00000000"));
    			box[currSelection].setTextColor(Color.parseColor("#F5E9D2"));
    		}
    		else{
    			boolean res;
    			if(choice==1){
    				if(isSimilarEquals(prevSelection,currSelection))
    					res = true;
    				else
    					res = false;
    			}
    			else{
    				if(isSimilarFactor(prevSelection,currSelection))
    					res = true;
    				else
    					res = false;
    			}
    			if(res){
    				box[currSelection].setBackgroundColor(Color.parseColor("#00000000"));
        			box[currSelection].setTextColor(Color.parseColor("#F5E9D2"));
        			box[prevSelection].setBackgroundColor(Color.parseColor("#00000000"));
        			box[prevSelection].setTextColor(Color.parseColor("#F5E9D2"));
        			box[currSelection].setText("");
        			box[prevSelection].setText("");
        			increaseScore();
        			
    			}
    			else{
    				box[currSelection].setBackgroundColor(Color.parseColor("#00000000"));
        			box[currSelection].setTextColor(Color.parseColor("#F5E9D2"));
        			box[prevSelection].setBackgroundColor(Color.parseColor("#00000000"));
        			box[prevSelection].setTextColor(Color.parseColor("#F5E9D2"));
        			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);

                	dlgAlert.setMessage("GAME OVER. Final score = "+score.getText()+". Click RESET to play again.");
                	dlgAlert.setTitle("GAME OVER");
                	dlgAlert.setPositiveButton("Yes",null);
                	dlgAlert.setCancelable(false);
                	timer.cancel();
                	dlgAlert.create().show();
                	updateHS();
    			}
    		}
		}
		
		
		private boolean isSimilarFactor(int i, int j) {
			// TODO Auto-generated method stub
			int res1,res2;
			int x1 = Integer.parseInt(box[i].getText().toString().substring(0, 1));
			int x2 = Integer.parseInt(box[j].getText().toString().substring(0, 1));
			int l1 = box[i].getText().toString().length();
			int l2 = box[j].getText().toString().length();
			int y1 = Integer.parseInt(box[i].getText().toString().charAt(l1-1)+"");
			int y2 = Integer.parseInt(box[j].getText().toString().charAt(l2-1)+"");
			char op1 = box[i].getText().toString().charAt(2);
			char op2 = box[j].getText().toString().charAt(2);
			if(op1=='x'){
				res1 = x1 * y1;
			}
			else if(op1=='+'){
				res1 = x1 + y1;
			}
			else{
				res1 = (x1>y1)?(x1-y1):(y1-x1);
			}
			if(op2=='x'){
				res2 = x2*y2;
			}
			else if(op1=='+'){
				res2 = x2+y2;
			}
			else{
				res2 = (x2>y2)?(x2-y2):(y2-x2);
			}
			int sm = (res1<res2)?res1:res2;
			int k=(sm==1)?1:2;
			for(;k<=sm;k++){
				if(res1%k==0&&res2%k==0){
					break;
				}
			}
			if(k==sm+1){
				return false;
			}
			else{
				return true;
			}
		}
		
		private boolean isSimilarEquals(int i, int j) {
			// TODO Auto-generated method stub
			int res1,res2;
			int x1 = Integer.parseInt(box[i].getText().toString().substring(0, 1));
			int x2 = Integer.parseInt(box[j].getText().toString().substring(0, 1));
			int l1 = box[i].getText().toString().length();
			int l2 = box[j].getText().toString().length();
			int y1 = Integer.parseInt(box[i].getText().toString().charAt(l1-1)+"");
			int y2 = Integer.parseInt(box[j].getText().toString().charAt(l2-1)+"");
			res1 = x1*y1;
			res2 = x2*y2;
			if(res1==res2){
				return true;
			}
			else{
				return false;
			}
		}
    };
    
    private void updateHS(){
    	int sc = Integer.parseInt(score.getText().toString());
    	if(sc>high_score){
    		high_score = sc;
    	}
    	//setting preferences
    	SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
    	Editor editor = prefs.edit();
    	editor.putInt("hs", high_score);
    	editor.commit();
    }
    
    private void increaseScore(){
    	score.setText(""+(Integer.parseInt(score.getText().toString())+1));
    }
    
    private int searchSelected(){
		int j;
    	for(j=0;j<15;j++){
			if(!(box[j].getCurrentTextColor()==Color.parseColor("#F5E9D2")))
				break;
		}
		if(j==15)
			return -1;
		else
			return j;
    }
    
    private boolean generateEquationFactor(){
    	int i,j=0;
    	int emptyBox[] = new int[15];
    	for(i=0;i<15;i++){
    		if(box[i].getText().equals("")){
    			emptyBox[j] = i;
    			j++;
    		}
    	}
    	if(j==0)
    		return false;
    	else{
    		Random r = new Random();
        	int emptyBoxIndex, emptyBoxNum;
        	emptyBoxIndex = r.nextInt(j);
            emptyBoxNum = emptyBox[emptyBoxIndex];
            int x = r.nextInt(9) + 1;
        	int y = r.nextInt(9) + 1;
        	int op = r.nextInt(3);
        	if(op==0){
        		box[emptyBoxNum].setText(""+x+" x "+y);
        	}
        	else if(op==1){
        		box[emptyBoxNum].setText(""+x+" + "+y);
        	}
        	else{
        		box[emptyBoxNum].setText(""+x+" ~ "+y);
        	}
        	return true;
    	}
    }
    
    private boolean generateEquationEquals(){
    	int i,j=0;
    	int emptyBox[] = new int[15];
    	for(i=0;i<15;i++){
    		if(box[i].getText().equals("")){
    			emptyBox[j] = i;
    			j++;
    		}
    	}
    	if(j==0)
    		return false;
    	else{
    		Random r = new Random();
        	int emptyBoxIndex, emptyBoxNum;
        	emptyBoxIndex = r.nextInt(j);
            emptyBoxNum = emptyBox[emptyBoxIndex];
            int x = r.nextInt(9) + 1;
        	int y = r.nextInt(9) + 1;
        	box[emptyBoxNum].setText(""+x+" x "+y);
        	return true;
    	}
    }

	synchronized public void initGfx() {
        ((GameBoard)findViewById(R.id.the_canvas)).resetStarField();
        ((Button)findViewById(R.id.reset)).setEnabled(true);
        //It's a good idea to remove any existing callbacks to keep
        //them from inadvertently stacking up.
        frame.removeCallbacks(frameUpdate);
        frame.postDelayed(frameUpdate, FRAME_RATE);
    }
    
    synchronized public void onClick(View v) {
             initGfx();
    }
    private Runnable frameUpdate = new Runnable() {
          @Override
          synchronized public void run() {
                  frame.removeCallbacks(frameUpdate);
                  //make any updates to on screen objects here
                  //then invoke the on draw by invalidating the canvas
                  ((GameBoard)findViewById(R.id.the_canvas)).invalidate();
                  frame.postDelayed(frameUpdate, FRAME_RATE);
          }
     };
     
     @Override
     public void onPause() {
         super.onPause();
         /*synchronized(timer){
             try{
                 timer.wait();
             }catch(InterruptedException e){
                 e.printStackTrace();
             }
         }*/
     }
     
     @Override
     public void onResume() {
         super.onResume();
         /*synchronized(timer){
             timer.notify();
         }*/
     }
     
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id){
        /*case R.id.about:
        	try {
				timer.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	Class menupage = null;
        	try {
				menupage = Class.forName("com.example.flashcards.About");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            Intent i = new Intent(MainActivity.this,menupage);
            startActivity(i);
        	break;
        case R.id.hs:
        	//getting preferences 
        	AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        	dlgAlert.setMessage("High Score = "+high_score);
        	dlgAlert.setTitle("High Score");
        	dlgAlert.setPositiveButton("Yes",null);
        	dlgAlert.setCancelable(false);
        	dlgAlert.create().show();
        	break;
        case R.id.diff:
        	
        	break;*/
        case R.id.exit:
        	finish();
        	break;
        }
        return false;
    }
}
