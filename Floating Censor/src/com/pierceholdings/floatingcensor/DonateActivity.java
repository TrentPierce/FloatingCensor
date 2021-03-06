package com.pierceholdings.floatingcensor;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.pierceholdings.floatingcensor.utils.IabHelper;
import com.pierceholdings.floatingcensor.utils.IabResult;
import com.pierceholdings.floatingcensor.utils.Purchase;
/**
 * Implements Google Play in-app billing v3 for the donate feature. Plenty of interesting stuff here.
 * Scroll down for details
 */
/**
 * Implements Google Play in-app billing v3 for the donate feature. Plenty of interesting stuff here.
 * Scroll down for details
 */


public class DonateActivity extends Activity {

	
	
	static final String SKU_SMALL = "gum";
	String TAG = "Floating Censor";
	
	boolean unlocked = false;

	private Toast toast = null;

	// (arbitrary) request code for the purchase flow
	static final int RC_REQUEST = 10001;

	// the helper object
	IabHelper mHelper;

	// Button setups
	Button button_small;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.donate);
	
		
		// In-app purchase stuff
		//Remember to copy your application's specific license key from google play here
		//for security purposes, save it to an xml if it needs to be on github
		String base64EncodedPublicKey = getString(R.string.app_license);
		Log.d(TAG, "Creating IAB helper.");
		mHelper = new IabHelper(this, base64EncodedPublicKey);

		// enable debug logging (for a production application, you should set
		// this to false).
		mHelper.enableDebugLogging(false);

		// Start setup. This is asynchronous and the specified listener
		// will be called once setup completes.
		Log.d(TAG, "Starting setup.");
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			@Override
			public void onIabSetupFinished(IabResult result) {
				Log.d(TAG, "Setup finished.");

				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					toast(getString(R.string.in_app_bill_error) + result);
					return;
				}
				makeDonation(1);
				// Have we been disposed of in the meantime? If so, quit.
				if (mHelper == null)
					return;

				// IAB is fully set up. Now, let's get an inventory of stuff we own.
				//   --commented out here as we didn't need it for donation purposes.
				// Log.d(TAG, "Setup successful. Querying inventory.");
				// mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// very important:
		Log.d(TAG, "Destroying helper.");
		if (mHelper != null) {
			mHelper.dispose();
			mHelper = null;
		}
	}

	//DO NOT SKIP THIS METHOD
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + ","
				+ data);
		if (mHelper == null)
			return;

		// Pass on the activity result to the helper for handling
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			// not handled, so handle it ourselves (here's where you'd
			// perform any handling of activity results not related to in-app
			// billing...
			super.onActivityResult(requestCode, resultCode, data);
		} else {
			Log.d(TAG, "onActivityResult handled by IABUtil.");
		}
	}
	 private void savePreferences(String key, boolean value) {
		 
		         SharedPreferences sharedPreferences = PreferenceManager
		                 .getDefaultSharedPreferences(this);
		         Editor editor = sharedPreferences.edit();
		         editor.putBoolean("unlocked", true);
		         editor.commit();
		         Intent i = getBaseContext().getPackageManager()
		                 .getLaunchIntentForPackage( getBaseContext().getPackageName() );
		    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    startActivity(i);
		         
		     }
	 

	/** Verifies the developer payload of a purchase. */
	boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();

		/**Follow google guidelines to create your own payload string here, in case it is needed.
		*Remember it is recommended to store the keys on your own server for added protection
		USE as necessary*/

		return true;
	}

	// Callback for when a purchase is finished
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		@Override
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			Log.d(TAG, "Purchase finished: " + result + ", purchase: "
					+ purchase);

			// if we were disposed of in the meantime, quit.
			if (mHelper == null)
				return;

			if (result.isFailure()) {
				toast(getString(R.string.purchase_error) + result);
				// setWaitScreen(false);
				finish();
				return;
			}
			if (!verifyDeveloperPayload(purchase)) {
				toast(getString(R.string.error_verification));
				// setWaitScreen(false);
				finish();
				return;
			}

			Log.d(TAG, "Purchase successful.");

			if (purchase.getSku().equals(SKU_SMALL))
					 {

				 Log.d(TAG, "Unlock IAP");
				 toast(getString(R.string.thank_you));
				 savePreferences(TAG, unlocked);
			//	mHelper.consumeAsync(purchase, mConsumeFinishedListener);
			}

		}
	};

	// Called when consumption is complete
	IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
		@Override
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			Log.d(TAG, "Consumption finished. Purchase: " + purchase
					+ ", result: " + result);

			// if we were disposed of in the meantime, quit.
			if (mHelper == null)
				return;

			//check which SKU is consumed here and then proceed.

			if (result.isSuccess()) {

				Log.d(TAG, "Consumption successful. Provisioning.");

				toast(getString(R.string.thank_you));
				savePreferences(TAG, unlocked);
			} else {
				toast(getString(R.string.error_consume) + result);
			}


			Log.d(TAG, "End consumption flow.");
		}
	};

	//the button clicks send an int value which would then call the specific SKU, depending on the 
	//application
	public void makeDonation(int value) {
		//check your own payload string.
		String payload = "";

		switch (value) {
		case (1):
			mHelper.launchPurchaseFlow(this, SKU_SMALL, RC_REQUEST,
					mPurchaseFinishedListener, payload);
			System.out.println("small purchase");
			break;
		default:
			break;
		}

	}

	private void toast(final String msg) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (toast == null) {
					toast = Toast.makeText(getApplicationContext(), "",
							Toast.LENGTH_SHORT);
				}
				toast.setText(msg);
				toast.show();
			}
		});
	}

}
