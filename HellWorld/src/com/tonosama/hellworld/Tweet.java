package com.tonosama.hellworld;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.internal.http.HttpClientImpl;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Tweet extends Activity {
	private String CONSUMER_KEY = "5kiw0nYlJzJK2DGYsNhvQ";
	private String CONSUMER_SECRET = "0czhp9oF4wBwo3yQYEKsFbymCOSXaTT1GB5W2ss";
	private String defoTEXT = null;
	private String mainTEXT = null;
	
	
	protected void onCreate(Bundle bundle) {

		super.onCreate(bundle);
        setContentView(R.layout.tweet);
        
	    
        
        //�n�b�V���^�O�ǉ��I
        defoTEXT = " #testTwitter ";
        
        final EditText edit = (EditText) findViewById(R.id.editText1);
        edit.setText(defoTEXT);
        edit.setMaxLines(3); 
        edit.setSelection(0);
        
        Button tweetbutton = (Button) findViewById(R.id.tweet);
        tweetbutton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // �N���b�N���̏���
            	SpannableStringBuilder sb = (SpannableStringBuilder)edit.getText();
                mainTEXT = sb.toString();
                
                
                try {
					tweet();
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				finish();
				
				
            }
        });
        
	}
	
	
	public void tweet() throws TwitterException {
		// �L�^���Ă����ݒ�t�@�C����ǂݍ��ށB
		SharedPreferences pref = getSharedPreferences("Twitter_seting",MODE_PRIVATE);

		// �ݒ�t�@�C������oauth_token��oauth_token_secret���擾�B
		String oauthToken  = pref.getString("oauth_token", "");
		String oauthTokenSecret = pref.getString("oauth_token_secret", "");

		ConfigurationBuilder confbuilder  = new ConfigurationBuilder(); 

		confbuilder.setOAuthAccessToken(oauthToken) 
		.setOAuthAccessTokenSecret(oauthTokenSecret) 
		.setOAuthConsumerKey(CONSUMER_KEY) 
		.setOAuthConsumerSecret(CONSUMER_SECRET); 

		Twitter twitter = new TwitterFactory(confbuilder.build()).getInstance(); 

		// �C�ӂ̕�����������ɂ��āA�c�C�[�g�B
		twitter.updateStatus(mainTEXT);
		
		Toast.makeText(this, "�Ԃ₫�܂���", Toast.LENGTH_SHORT).show();
	}

}
