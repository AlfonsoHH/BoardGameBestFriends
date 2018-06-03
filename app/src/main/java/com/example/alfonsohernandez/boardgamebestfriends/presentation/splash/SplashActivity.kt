package com.example.alfonsohernandez.boardgamebestfriends.presentation.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.*
import com.example.alfonsohernandez.boardgamebestfriends.presentation.login.LoginActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.tab.TabActivity
import com.facebook.AccessToken
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import durdinapps.rxfirebase2.RxFirebaseDatabase.setValue
import com.google.firebase.database.DatabaseReference




class SplashActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        val firebaseInstance = FirebaseDatabase.getInstance().getReference("meetings")
//        var key = firebaseInstance.push().key
//        firebaseInstance.child("SP-18").child(key).setValue(Meeting(key,"Partida de Above and Below","Una partida para echar el rato","16:00_12/12/1212","LDpp8M2kLynemJqdz6y","-LDpmqoGScVa4sQXKBkR","url","172818","https://cf.geekdo-images.com/thumb/img/ONhKaMo__wRDkxngqt3ieBUu9pU=/fit-in/200x150/pic2398773.jpg","pArBsgoh6tfCHUbviYE7LRhBliq1",2));
//        var key2 = firebaseInstance.push().key
//        firebaseInstance.child("SP-18").child(key2).setValue(Meeting(key,"Partida de Blood Rage","Otra partida para echar el rato","20:00_23/07/2018","LDpp8M2kLynemJqdz6y","lCI1WiNk00SgMrN1mzbHUnqugtu1","url","170216","https://cf.geekdo-images.com/thumb/img/JPo2K0kSzYwCSqgEFdJbCzXFaTA=/fit-in/200x150/pic2439223.jpg","lCI1WiNk00SgMrN1mzbHUnqugtu1",1));
//
//        val firebaseInstance2 = FirebaseDatabase.getInstance().getReference("meeting-users")
//        firebaseInstance2.child("SP-18").child(key).child("lCI1WiNk00SgMrN1mzbHUnqugtu1").setValue(true)
//        firebaseInstance2.child("SP-18").child(key).child("pArBsgoh6tfCHUbviYE7LRhBliq1").setValue(true)
//        firebaseInstance2.child("SP-18").child(key2).child("lCI1WiNk00SgMrN1mzbHUnqugtu1").setValue(true)
//        firebaseInstance2.child("SP-18").child(key2).child("pArBsgoh6tfCHUbviYE7LRhBliq1").setValue(true)
//
//        val firebaseInstance3 = FirebaseDatabase.getInstance().getReference("user-meetings")
//        firebaseInstance3.child("lCI1WiNk00SgMrN1mzbHUnqugtu1").child(key).setValue(true)
//        firebaseInstance3.child("pArBsgoh6tfCHUbviYE7LRhBliq1").child(key).setValue(true)
//        firebaseInstance3.child("lCI1WiNk00SgMrN1mzbHUnqugtu1").child(key2).setValue(true)
//        firebaseInstance3.child("pArBsgoh6tfCHUbviYE7LRhBliq1").child(key2).setValue(true)

//        val firebaseInstance = FirebaseDatabase.getInstance().getReference("groups")
//        var key = firebaseInstance.push().key
//        firebaseInstance.child(key).setValue(Group(key,"url","Yo y Test","Both Alfonsososososos","kurqxfGLOxO5vWx6ZJtbmwCvF732"));
//
//        val firebaseInstance2 = FirebaseDatabase.getInstance().getReference("group-users")
//        firebaseInstance2.child(key).child("lCI1WiNk00SgMrN1mzbHUnqugtu1").setValue(true)
//        firebaseInstance2.child(key).child("kurqxfGLOxO5vWx6ZJtbmwCvF732").setValue(true)
//
//        val firebaseInstance3 = FirebaseDatabase.getInstance().getReference("user-groups")
//        firebaseInstance3.child("lCI1WiNk00SgMrN1mzbHUnqugtu1").child(key).setValue(true)
//        firebaseInstance3.child("kurqxfGLOxO5vWx6ZJtbmwCvF732").child(key).setValue(true)
//
//        val firebaseInstance4 = FirebaseDatabase.getInstance().getReference("group-games")
//        firebaseInstance4.child(key).child("7865").setValue(true)
//        firebaseInstance4.child(key).child("172818").setValue(true)
//        firebaseInstance4.child(key).child("124742").setValue(true)

//        val firebaseInstance = FirebaseDatabase.getInstance().getReference("places")
//        var key = firebaseInstance.push().key
//        firebaseInstance.child("SP-18").child(key).setValue(Place(key,"url","Dune","Calle Cruz, 2",37.173606,-3.601976,1,3,5,"lCI1WiNk00SgMrN1mzbHUnqugtu1",true,"Mon-Fri_09:00-20:00",null,null));
//
//        var key1 = firebaseInstance.push().key
//        firebaseInstance.child("SP-18").child(key1).setValue(Place(key1,"url","Continental","Calle Seminario, 11",37.174390,-3.605300,2,4,6,"lCI1WiNk00SgMrN1mzbHUnqugtu1",true,"Mon-Fri_09:00-20:00","Sat_09:00-12:00 16:00-22:00",null));
//
//        val firebaseInstance2 = FirebaseDatabase.getInstance().getReference("user-places")
//        firebaseInstance2.child("lCI1WiNk00SgMrN1mzbHUnqugtu1").child("SP-18").child("-LDpmqoGScVa4sQXKBkR").setValue(true)
//        firebaseInstance2.child("lCI1WiNk00SgMrN1mzbHUnqugtu1").child("SP-18").child("-LDpmqoNGrHvjT4pn6P4").setValue(true)
//
//        val firebaseInstance3 = FirebaseDatabase.getInstance().getReference("place-games")
//        firebaseInstance3.child("-LDpmqoGScVa4sQXKBkR").child("170216").setValue(true)
//        firebaseInstance3.child("-LDpmqoGScVa4sQXKBkR").child("7865").setValue(true)

//        var firebaseInstance = FirebaseDatabase.getInstance().getReference("games")
//        firebaseInstance.child("7865").setValue(Game("7865","https://cf.geekdo-images.com/thumb/img/Kk309UtSrQu3flO3Rs_Vxuumvd4=/fit-in/200x150/pic1229634.jpg","10 Days in Africa","",2,4,25,6.56))
//        firebaseInstance.child("172818").setValue(Game("172818","https://cf.geekdo-images.com/thumb/img/ONhKaMo__wRDkxngqt3ieBUu9pU=/fit-in/200x150/pic2398773.jpg","Above and Below","",2,4,90,7.58))
//        firebaseInstance.child("124742").setValue(Game("124742","https://cf.geekdo-images.com/thumb/img/fJp94uxjjBE5bGT0IxWZ5ePpe8A=/fit-in/200x150/pic3738560.jpg","Android: Netrunner","",2,2,45,7.92))
//        firebaseInstance.child("170216").setValue(Game("170216","https://cf.geekdo-images.com/thumb/img/JPo2K0kSzYwCSqgEFdJbCzXFaTA=/fit-in/200x150/pic2439223.jpg","Blood Rage","",2,4,90,8.07))
//
//        var firebaseInstance2 = FirebaseDatabase.getInstance().getReference("user-games")
//        firebaseInstance2.child("lCI1WiNk00SgMrN1mzbHUnqugtu1").child("7865").setValue(true)
//        firebaseInstance2.child("lCI1WiNk00SgMrN1mzbHUnqugtu1").child("172818").setValue(true)
//        firebaseInstance2.child("pArBsgoh6tfCHUbviYE7LRhBliq1").child("172818").setValue(true)
//        firebaseInstance2.child("pArBsgoh6tfCHUbviYE7LRhBliq1").child("124742").setValue(true)

//        var firebaseInstance = FirebaseDatabase.getInstance().getReference("users")
//        val key = firebaseInstance.push().key
//        firebaseInstance.child(key).setValue(User(key,"dummy@dummy.com","Dummy","url","facebook","SP-1"))

//        var firebaseInstance = FirebaseDatabase.getInstance().getReference("regions")
//        firebaseInstance.child("SP-1").setValue(Region("SP-1","Álava","Spain"))
//        firebaseInstance.child("SP-2").setValue(Region("SP-2","Albacete","Spain"))
//        firebaseInstance.child("SP-3").setValue(Region("SP-3","Alicante","Spain"))
//        firebaseInstance.child("SP-4").setValue(Region("SP-4","Almeria","Spain"))
//        firebaseInstance.child("SP-5").setValue(Region("SP-5","Asturias","Spain"))
//        firebaseInstance.child("SP-6").setValue(Region("SP-6","Ávila","Spain"))
//        firebaseInstance.child("SP-7").setValue(Region("SP-7","Badajoz","Spain"))
//        firebaseInstance.child("SP-8").setValue(Region("SP-8","Barcelona","Spain"))
//        firebaseInstance.child("SP-9").setValue(Region("SP-9","Burgos","Spain"))
//        firebaseInstance.child("SP-10").setValue(Region("SP-10","Cáceres","Spain"))
//        firebaseInstance.child("SP-11").setValue(Region("SP-11","Cádiz","Spain"))
//        firebaseInstance.child("SP-12").setValue(Region("SP-12","Cantabria","Spain"))
//        firebaseInstance.child("SP-13").setValue(Region("SP-13","Castellón","Spain"))
//        firebaseInstance.child("SP-14").setValue(Region("SP-14","Ciudad Real","Spain"))
//        firebaseInstance.child("SP-15").setValue(Region("SP-15","Córdoba","Spain"))
//        firebaseInstance.child("SP-16").setValue(Region("SP-16","Cuenca","Spain"))
//        firebaseInstance.child("SP-17").setValue(Region("SP-17","Gerona","Spain"))
//        firebaseInstance.child("SP-18").setValue(Region("SP-18","Granada","Spain"))
//        firebaseInstance.child("SP-19").setValue(Region("SP-19","Guadalajara","Spain"))
//        firebaseInstance.child("SP-20").setValue(Region("SP-20","Guipúzcoa","Spain"))
//        firebaseInstance.child("SP-21").setValue(Region("SP-21","Huelva","Spain"))
//        firebaseInstance.child("SP-22").setValue(Region("SP-22","Huesca","Spain"))
//        firebaseInstance.child("SP-23").setValue(Region("SP-23","Islas Baleares","Spain"))
//        firebaseInstance.child("SP-24").setValue(Region("SP-24","Jaen","Spain"))
//        firebaseInstance.child("SP-25").setValue(Region("SP-25","La Coruña","Spain"))
//        firebaseInstance.child("SP-26").setValue(Region("SP-26","La Rioja","Spain"))
//        firebaseInstance.child("SP-27").setValue(Region("SP-27","Las Palmas","Spain"))
//        firebaseInstance.child("SP-28").setValue(Region("SP-28","León","Spain"))
//        firebaseInstance.child("SP-29").setValue(Region("SP-29","Lérida","Spain"))
//        firebaseInstance.child("SP-30").setValue(Region("SP-30","Lugo","Spain"))
//        firebaseInstance.child("SP-31").setValue(Region("SP-31","Madrid","Spain"))
//        firebaseInstance.child("SP-32").setValue(Region("SP-32","Málaga","Spain"))
//        firebaseInstance.child("SP-33").setValue(Region("SP-33","Murcia","Spain"))
//        firebaseInstance.child("SP-34").setValue(Region("SP-34","Navarra","Spain"))
//        firebaseInstance.child("SP-35").setValue(Region("SP-35","Orense","Spain"))
//        firebaseInstance.child("SP-36").setValue(Region("SP-36","Palencia","Spain"))
//        firebaseInstance.child("SP-37").setValue(Region("SP-37","Pontevedra","Spain"))
//        firebaseInstance.child("SP-38").setValue(Region("SP-38","Salamanca","Spain"))
//        firebaseInstance.child("SP-39").setValue(Region("SP-39","Segovia","Spain"))
//        firebaseInstance.child("SP-40").setValue(Region("SP-40","Sevilla","Spain"))
//        firebaseInstance.child("SP-41").setValue(Region("SP-41","Soria","Spain"))
//        firebaseInstance.child("SP-42").setValue(Region("SP-42","Tarragona","Spain"))
//        firebaseInstance.child("SP-43").setValue(Region("SP-43","Tenerife","Spain"))
//        firebaseInstance.child("SP-44").setValue(Region("SP-44","Teruel","Spain"))
//        firebaseInstance.child("SP-45").setValue(Region("SP-45","Toledo","Spain"))
//        firebaseInstance.child("SP-46").setValue(Region("SP-46","Valencia","Spain"))
//        firebaseInstance.child("SP-47").setValue(Region("SP-47","Valladolid","Spain"))
//        firebaseInstance.child("SP-48").setValue(Region("SP-48","Vizcaya","Spain"))
//        firebaseInstance.child("SP-49").setValue(Region("SP-49","Zamora","Spain"))
//        firebaseInstance.child("SP-50").setValue(Region("SP-50","Zaragoza","Spain"))


        mAuth = FirebaseAuth.getInstance()

        val currentUser = mAuth.getCurrentUser()

        if(currentUser!=null)
            startAPP()
        else
            startLogin()
    }

    fun startAPP(){
        var intent = Intent(this,TabActivity::class.java)
        startActivity(intent)
    }

    fun startLogin(){
        var intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }
}
