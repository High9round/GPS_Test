package com.hi9h_9r0und.gps_test

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if(grantLocationPermission())
        {

            var lm:LocationManager= getSystemService(Context.LOCATION_SERVICE) as LocationManager
            textView_Result.text="위치정보 수신중..."

            try
            {
                // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기~!!!
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                        100, // 통지사이의 최소 시간간격 (miliSecond)
                        1.0f, // 통지사이의 최소 변경거리 (m)
                        mLocationListener)
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                        100, // 통지사이의 최소 시간간격 (miliSecond)
                        1.0f, // 통지사이의 최소 변경거리 (m)
                        mLocationListener)
            }
            catch (ex:SecurityException)
            {
                textView_Result.text="위치정보 미수신"
                lm.removeUpdates(mLocationListener)
            }
        }
        else
        {
            textView_Result.text="위치권환 획득필요"
        }

    }

    private val mLocationListener = object:LocationListener {
        override fun onLocationChanged(location:Location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

            Log.d("test", "onLocationChanged, location:" + location);
            var longitude = location.longitude; //경도
            var latitude = location.latitude;   //위도
            var altitude = location.altitude;   //고도
            var accuracy = location.accuracy;    //정확도
            var provider = location.provider;   //위치제공자
            //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
            //Network 위치제공자에 의한 위치변화
            //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.
            textView_Result.text="위치정보 : " + provider.toString() + "\n위도 : " + longitude.toString() + "\n경도 : " + latitude.toString()  + "\n고도 : " + altitude.toString() + "\n정확도 : "  + accuracy.toString()
            textViewMgrs.text=getMGRS(longitude,latitude)
        }

        override fun onProviderDisabled(provider: String?) {
            // Disbled시
            Log.d("test", "onProviderDisabled, provider:" + provider)
        }



        override fun onProviderEnabled(provider:String) {
            // Enabled시
            Log.d("test", "onProviderEnabled, provider:" + provider)
        }

        override fun onStatusChanged(provider:String, status:Int, extras:Bundle) {
            // 변경시
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras)
        }
    }

    private fun getMGRS(lon:Double, lat:Double):String
    {
        var result:String=""

        return result
    }

    private fun grantLocationPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                //Log.v(FragmentActivity.TAG, "Permission is granted")
                Toast.makeText(this, "ACCESS_FINE_LOCATION is Granted", Toast.LENGTH_SHORT).show()
                return true
            }
            else
            {
                //Log.v(FragmentActivity.TAG, "Permission is revoked")
                Toast.makeText(this, "ACCESS_FINE_LOCATION is Revoked", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)

                return false
            }
        }
        else
        {
            Toast.makeText(this, "ACCESS_FINE_LOCATION is Grant", Toast.LENGTH_SHORT).show()
            //Log.d(FragmentActivity.TAG, "External Storage Permission is Grant ")
            return true
        }
    }
}
