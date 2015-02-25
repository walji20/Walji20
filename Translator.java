package com.example.translator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import android.support.v7.app.ActionBarActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	EditText txtTranslate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onTranslateClick(View view) {
		EditText txttranslate = (EditText) findViewById(R.id.txttranslate);
		if (!isEmty(txttranslate)) {
			Toast.makeText(this, "Getting Traslations", Toast.LENGTH_LONG)
					.show();
			new SaveTheFeed().execute();

		} else {

			Toast.makeText(this, "Enter WOrd To Translate", Toast.LENGTH_SHORT)
					.show();
		}
	}

	protected boolean isEmty(EditText editText) {
		return editText.getText().toString().trim().length() == 0;
	}

	class SaveTheFeed extends AsyncTask<Void, Void, Void> {
		String jsonString = "";
		String result = "";
		
		@Override
		protected Void doInBackground(Void... voids) {
			EditText txttranslate = (EditText) findViewById(R.id.txttranslate);
			String WordtoTranslate = txttranslate.getText().toString();
			WordtoTranslate = WordtoTranslate.replace(" ", "+");
			String key = "trnsl.1.1.20150225T005818Z.7e2585c7f427baa8.05540d0f8ec96fd001b1e35f0bf16550521663cc";

			DefaultHttpClient httClient = new DefaultHttpClient(
					new BasicHttpParams());
			HttpPost httpPost = new HttpPost(
					"https://translate.yandex.net/api/v1.5/tr.json/translate?key="+ key +"&lang=en-id&text="+ WordtoTranslate);
			httpPost.setHeader("Content-type", "apliaction/json");
			InputStream inputStream = null;

			try {
				HttpResponse response = httClient.execute(httpPost);
				// Holds the message sent by the response
				HttpEntity entity = response.getEntity();

				// Get the content sent
				inputStream = entity.getContent();

				// A BufferedReader is used because it is efficient
				// The InputStreamReader converts the bytes into characters
				// My JSON data is UTF-8 so I read that encoding
				// 8 defines the input buffer size
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream, "UTF-8"), 8);

				// Storing each line of data in a StringBuilder
				StringBuilder sb = new StringBuilder();

				String line = null;

				// readLine reads all characters up to a \n and then stores them
				while ((line = reader.readLine()) != null) {

					sb.append(line + "\n");

				}

				// Save the results in a String
				jsonString = sb.toString();

				result = jsonString;

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {

			// Put the translations in the TextView
			TextView translationTextView = (TextView) findViewById(R.id.txthasil);

			translationTextView.setText(result);
		}
	}
}
