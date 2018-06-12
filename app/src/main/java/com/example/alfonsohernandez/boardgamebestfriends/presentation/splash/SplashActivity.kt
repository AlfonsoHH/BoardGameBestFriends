package com.example.alfonsohernandez.boardgamebestfriends.presentation.splash

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.alfonsohernandez.boardgamebestfriends.R
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Place
import com.example.alfonsohernandez.boardgamebestfriends.domain.models.Region
import com.example.alfonsohernandez.boardgamebestfriends.presentation.login.LoginActivity
import com.example.alfonsohernandez.boardgamebestfriends.presentation.tab.TabActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


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
//        firebaseInstance3.child("kurqxfGLOxO5vWx6ZJtbmwCvF732").child("SP-18").child("-LDuTvXNGJdqc70z5Mnq").setValue(true)
//        firebaseInstance3.child("lCI1WiNk00SgMrN1mzbHUnqugtu1").child("SP-18").child("-LDuTvXNGJdqc70z5Mnq").setValue(false)
//        firebaseInstance3.child("lCI1WiNk00SgMrN1mzbHUnqugtu1").child("SP-18").child("-LDuTvXeIUCH8jjLn6eE").setValue(true)
//        firebaseInstance3.child("lCI1WiNk00SgMrN1mzbHUnqugtu1").child("SP-18").child("-LDvyYTp1_N1TSMd0viF").setValue(true)
//        firebaseInstance3.child("lCI1WiNk00SgMrN1mzbHUnqugtu1").child("SP-18").child("-LDvzbnIeeIll8J0lzl3").setValue(true)
//        firebaseInstance3.child("pArBsgoh6tfCHUbviYE7LRhBliq1").child("SP-18").child("-LDuTvXNGJdqc70z5Mnq").setValue(true)
//        firebaseInstance3.child("pArBsgoh6tfCHUbviYE7LRhBliq1").child("SP-18").child("-LDuTvXeIUCH8jjLn6eE").setValue(true)

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
//        firebaseInstance.child("SP-1").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-2").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-3").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-4").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-5").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-6").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-7").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-8").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-9").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-10").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-11").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-12").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-13").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-14").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-15").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-16").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-17").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-19").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-20").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-21").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-22").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-23").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-24").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-25").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-26").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-27").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-28").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-29").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-30").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-31").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-32").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-33").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-34").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-35").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-36").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-37").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-38").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-39").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-40").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-41").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-42").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-43").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-44").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-45").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-46").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-47").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-48").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-49").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));
//        key = firebaseInstance.push().key
//        firebaseInstance.child("SP-50").child(key).setValue(Place(key,"url","Test","Test",0.0,0.0,1,1,1,"test",false,null,null,null));

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
//        firebaseInstance.child("SP-1").setValue(Region("SP-1","Álava","Spain",42.846718,-2.671635))
//        firebaseInstance.child("SP-2").setValue(Region("SP-2","Albacete","Spain",38.9738639004492,-1.8530662592))
//        firebaseInstance.child("SP-3").setValue(Region("SP-3","Alicante","Spain",38.4091508997595,-0.5127874934))
//        firebaseInstance.child("SP-4").setValue(Region("SP-4","Almeria","Spain",36.837130,-2.463179))
//        firebaseInstance.child("SP-5").setValue(Region("SP-5","Asturias","Spain",43.3579649511212,-5.8733862770))
//        firebaseInstance.child("SP-6").setValue(Region("SP-6","Ávila","Spain",40.6686370870955,-4.6606393671))
//        firebaseInstance.child("SP-7").setValue(Region("SP-7","Badajoz","Spain",38.8685452964506,-6.8170906175))
//        firebaseInstance.child("SP-8").setValue(Region("SP-8","Barcelona","Spain",41.3818,2.1685))
//        firebaseInstance.child("SP-9").setValue(Region("SP-9","Burgos","Spain",42.3499677292503,-3.6822051133))
//        firebaseInstance.child("SP-10").setValue(Region("SP-10","Cáceres","Spain",39.4282320285373,-6.4378180840))
//        firebaseInstance.child("SP-11").setValue(Region("SP-11","Cádiz","Spain",36.5008762379097,-6.2684345022))
//        firebaseInstance.child("SP-12").setValue(Region("SP-12","Cantabria","Spain",43.4722475797229,-3.8199358808))
//        firebaseInstance.child("SP-13").setValue(Region("SP-13","Castellón","Spain",39.986068,-0.036024))
//        firebaseInstance.child("SP-14").setValue(Region("SP-14","Ciudad Real","Spain",38.9554156999021,-3.9809874650))
//        firebaseInstance.child("SP-15").setValue(Region("SP-15","Córdoba","Spain",37.8550964348286,-4.7086738547))
//        firebaseInstance.child("SP-16").setValue(Region("SP-16","Cuenca","Spain",40.2179122243893,-1.9919492596))
//        firebaseInstance.child("SP-17").setValue(Region("SP-17","Gerona","Spain",41.9842,2.8239))
//        firebaseInstance.child("SP-18").setValue(Region("SP-18","Granada","Spain",37.1886273389356,-3.5907775816))
//        firebaseInstance.child("SP-19").setValue(Region("SP-19","Guadalajara","Spain",40.643623699929,-3.17187803208))
//        firebaseInstance.child("SP-20").setValue(Region("SP-20","Guipúzcoa","Spain",43.2918111063554,-1.9885133578))
//        firebaseInstance.child("SP-21").setValue(Region("SP-21","Huelva","Spain",37.2582017932977,-6.9293230281))
//        firebaseInstance.child("SP-22").setValue(Region("SP-22","Huesca","Spain",42.1483775677877,-0.4125338057))
//        firebaseInstance.child("SP-23").setValue(Region("SP-23","Islas Baleares","Spain",39.710358,2.995148))
//        firebaseInstance.child("SP-24").setValue(Region("SP-24","Jaen","Spain",37.8258451226024,-3.7331593542))
//        firebaseInstance.child("SP-25").setValue(Region("SP-25","La Coruña","Spain",43.3712591584685,-8.4188010207))
//        firebaseInstance.child("SP-26").setValue(Region("SP-26","La Rioja","Spain",42.4671213247137,-2.4454133612))
//        firebaseInstance.child("SP-27").setValue(Region("SP-27","Las Palmas","Spain",28.123618,-15.432873))
//        firebaseInstance.child("SP-28").setValue(Region("SP-28","León","Spain",42.5735672646409,-5.5671588646))
//        firebaseInstance.child("SP-29").setValue(Region("SP-29","Lérida","Spain",41.6109,0.6419))
//        firebaseInstance.child("SP-30").setValue(Region("SP-30","Lugo","Spain",43.0026319248085,-7.4994393284))
//        firebaseInstance.child("SP-31").setValue(Region("SP-31","Madrid","Spain",40.4893538421231,-3.6827461557))
//        firebaseInstance.child("SP-32").setValue(Region("SP-32","Málaga","Spain",36.7585406465564,-4.3971722687))
//        firebaseInstance.child("SP-33").setValue(Region("SP-33","Murcia","Spain",37.9439891615427,-1.1636353660))
//        firebaseInstance.child("SP-34").setValue(Region("SP-34","Navarra","Spain",42.817991,-1.644215))
//        firebaseInstance.child("SP-35").setValue(Region("SP-35","Orense","Spain",42.350882201474,-7.90210545041))
//        firebaseInstance.child("SP-36").setValue(Region("SP-36","Palencia","Spain",41.9860072206666,-4.5649220055))
//        firebaseInstance.child("SP-37").setValue(Region("SP-37","Pontevedra","Spain",42.4224148354063,-8.6192535874))
//        firebaseInstance.child("SP-38").setValue(Region("SP-38","Salamanca","Spain",40.9559681717042,-5.6802244925))
//        firebaseInstance.child("SP-39").setValue(Region("SP-39","Segovia","Spain",40.9019416673774,-4.1829892208))
//        firebaseInstance.child("SP-40").setValue(Region("SP-40","Sevilla","Spain",37.3914105564361,-5.9591776906))
//        firebaseInstance.child("SP-41").setValue(Region("SP-41","Soria","Spain",41.8151715668429,-2.6511915798))
//        firebaseInstance.child("SP-42").setValue(Region("SP-42","Tarragona","Spain",41.115,1.2499))
//        firebaseInstance.child("SP-43").setValue(Region("SP-43","Tenerife","Spain",28.463938,-16.262598))
//        firebaseInstance.child("SP-44").setValue(Region("SP-44","Teruel","Spain",40.3633442558984,-1.0893794330))
//        firebaseInstance.child("SP-45").setValue(Region("SP-45","Toledo","Spain",39.8676536684123,-4.0098788243))
//        firebaseInstance.child("SP-46").setValue(Region("SP-46","Valencia","Spain",39.4561165311493,-0.3545661635))
//        firebaseInstance.child("SP-47").setValue(Region("SP-47","Valladolid","Spain",41.6522966863625,-4.7285413742))
//        firebaseInstance.child("SP-48").setValue(Region("SP-48","Vizcaya","Spain",43.2603479167077,-2.9334110846))
//        firebaseInstance.child("SP-49").setValue(Region("SP-49","Zamora","Spain",41.5225857844805,-5.8005370411))
//        firebaseInstance.child("SP-50").setValue(Region("SP-50","Zaragoza","Spain",41.6563497,-0.8765662))

        mAuth = FirebaseAuth.getInstance()

        val currentUser = mAuth.getCurrentUser()

        if(currentUser!=null)
            startAPP()
        else
            startLogin()
    }

    fun startAPP(){
        val intent = Intent(this,TabActivity::class.java)
        startActivity(intent)
    }

    fun startLogin(){
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }
}
