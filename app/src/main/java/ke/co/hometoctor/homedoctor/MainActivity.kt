package ke.co.hometoctor.homedoctor




import android.content.ContentValues.TAG
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ke.co.hometoctor.homedoctor.utility.*

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //FirebaseApp.initializeApp(this)
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.getCurrentUser()
        updateUI(currentUser)
    }
    fun updateUI( currentUser: FirebaseUser?){
        if(currentUser==null){
            val transaction=supportFragmentManager.beginTransaction()

            val st=login()
            st.mAuth=mAuth
            transaction.replace(R.id.main_layout,st,"sign_up")
            transaction.setCustomAnimations(R.anim.translateend1,R.anim.translateend1);
            //transaction.addToBackStack(null)
            transaction.commit()
        }
        else{
            val transaction=supportFragmentManager.beginTransaction()
            val st=menu_home()
            st.mAuth=mAuth
            transaction.replace(R.id.main_layout, st,"menu_home")
            transaction.setCustomAnimations(R.anim.translateend1,R.anim.translateend1);
            //transaction.addToBackStack(null)
            transaction.commit()
        }
    }




}
class message: FirebaseInstanceIdService(){
    override fun onTokenRefresh() {
        // Get updated InstanceID token.
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Refreshed token: " + refreshedToken!!)

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken)
    }
}
