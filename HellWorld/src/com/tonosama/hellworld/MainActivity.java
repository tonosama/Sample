package com.tonosama.hellworld;

import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private Twitter twitter = null;
	private RequestToken requestToken = null;
	
	// �Ƃ������[���C�g��
	private String CONSUMER_KEY ="5kiw0nYlJzJK2DGYsNhvQ";
	private String CONSUMER_SECRET = "0czhp9oF4wBwo3yQYEKsFbymCOSXaTT1GB5W2ss";
	
	private String nechatterStatus;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // �f�[�^�ۑ�
        SharedPreferences pref = getSharedPreferences("Twitter_setting", MODE_PRIVATE);
        // 
        nechatterStatus = pref.getString("status","");
        
		Button loginbutton = (Button) findViewById(R.id.tweetLogin);
        loginbutton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) { 
        		//�����ݒ肳��Ă��邩�H
        		if(isConnected(nechatterStatus)){
        			
        			//disconnectTwitter();
					
					Intent intent2=new Intent();
					intent2.setClassName("com.tonosama.hellworld","com.tonosama.hellworld.Tweet");
					intent2.setAction(Intent.ACTION_VIEW);
					startActivityForResult(intent2,0);
					
        		}else{
        			
        			try {
						connectTwitter();
					} catch (TwitterException e) {
						//showToast(R.string.nechatter_connect_error);
					}
        		}
        		
        		
        	}
        });
        
        Button logoutbutton = (Button) findViewById(R.id.tweetLogout);
        logoutbutton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) { 
        		
        		disconnectTwitter();
				try {
					connectTwitter();
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        	
        });

        
        final String[] data = {"���͂悤","����ɂ���","���₷�݂Ȃ���"};
        ListView lv =(ListView)findViewById(R.id.listView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.rowdata,data);
        lv.setAdapter(arrayAdapter);
        
        // setOnItemClickListerner�ŃN���b�N���̃C�x���g�N���X�Ăяo��
        lv.setOnItemClickListener(new ClickEvent());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    // ListView�̃C�x���g�N���X
    class ClickEvent implements OnItemClickListener{
        // onItemClick���\�b�h�ɂ́AAdapterView(adapter)�A�I����������View(TextView)�A�I�����ꂽ�ʒu��int�l�AID������long�l���n�����
    	public void onItemClick(AdapterView<?> adapter,View view,int position,long id)
    	{
    		AlertDialog.Builder diag = new AlertDialog.Builder(MainActivity.this);
    		// �_�C�A���O�̓��e�\��
    		diag.setTitle("�Ԃ₫�܂����H�H");
    		TextView textView = (TextView)view;
    		diag.setMessage(textView.getText());
    		
    		// OK�{�^���̐ݒ�
    		diag.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which) {
    				// TODO �����������ꂽ���\�b�h�E�X�^�u
    				Log.d("AlertDialog", "Positive which :" + which);
    				}
    		});
    		
    		// Cancel�{�^���̐ݒ�
    		diag.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO �����������ꂽ���\�b�h�E�X�^�u
    				Log.d("AlertDialog", "Negative which :" + which);
				}
			});
    		
            diag.create();
            diag.show();            
			
    	}
    }
    
    
    //---
    
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

		if(resultCode == RESULT_OK){
			super.onActivityResult(requestCode, resultCode, intent);

			AccessToken accessToken = null;

			try {
				accessToken = twitter.getOAuthAccessToken(
						requestToken,
						intent.getExtras().getString("oauth_verifier"));

				
		        SharedPreferences pref=getSharedPreferences("Twitter_seting",MODE_PRIVATE);

		        SharedPreferences.Editor editor=pref.edit();
		        editor.putString("oauth_token",accessToken.getToken());
		        editor.putString("oauth_token_secret",accessToken.getTokenSecret());
		        editor.putString("status","available");

		        editor.commit();
		        
		       
		        //�Ԃ₭�y�[�W��GO
		        Intent intent2 = new Intent(this, Tweet.class);
				this.startActivityForResult(intent2, 0);
		        
		        //finish();
			} catch (TwitterException e) {
				//showToast(R.string.nechatter_connect_error);
			}
		}
	}
    
    
    
    
    private void connectTwitter() throws TwitterException{
		

		//�Q�l:http://groups.google.com/group/twitter4j/browse_thread/thread/d18c179ba0d85351
		//�p�ꂾ���Ǔǂ�łˁI
		ConfigurationBuilder confbuilder  = new ConfigurationBuilder(); 

		confbuilder.setOAuthConsumerKey(CONSUMER_KEY).setOAuthConsumerSecret(CONSUMER_SECRET); 

		twitter = new TwitterFactory(confbuilder.build()).getInstance();
		
		
		String CALLBACK_URL = "myapp://oauth";
		// requestToken���N���X�ϐ��B
		try {
			requestToken = twitter.getOAuthRequestToken(CALLBACK_URL);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		  // �F�ؗpURL���C���e���g�ɃZ�b�g�B
		  // TwitterLogin��Activity�̃N���X���B
		  Intent intent = new Intent(this, TwitterLogin.class);
		  intent.putExtra("auth_url", requestToken.getAuthorizationURL());

		  // �A�N�e�B�r�e�B���N��
		  this.startActivityForResult(intent, 0);
		
	}
    
    
    
    final private boolean isConnected(String nechatterStatus){
		if(nechatterStatus != null && nechatterStatus.equals("available")){
			return true;
		}else{
			return false;
		}
	}
    
    
    public void disconnectTwitter(){
		
        SharedPreferences pref=getSharedPreferences("Twitter_seting",MODE_PRIVATE);

        SharedPreferences.Editor editor=pref.edit();
        editor.remove("oauth_token");
        editor.remove("oauth_token_secret");
        editor.remove("status");

        editor.commit();
        
        
        
        //finish();
    }
}
