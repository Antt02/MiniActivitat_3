package udl.eps.intentsimplicits

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.SearchManager
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import udl.eps.intentsimplicits.databinding.ActivityMainBinding

class MainActivity : ComponentActivity(), View.OnClickListener {

    private lateinit var readContactsReqPermLaunc: ActivityResultLauncher<String>
    private lateinit var callsReqPermLaunc: ActivityResultLauncher<String>
    private lateinit var accessGalleryPermLaunc: ActivityResultLauncher<String>
    private lateinit var image:ImageView
    private lateinit var contact:TextView

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val btn1 = binding.button1
        val btn2 = binding.button2
        val btn3 = binding.button3
        val btn4 = binding.button4
        val btn5 = binding.button5
        val btn6 = binding.button6
        val btn7 = binding.buttonMARCAR
        val btn8 = binding.buttonSMS
        val btn9 = binding.buttonEMAIL
        val btn10 = binding.buttonGALERIA
        image = binding.imatge
        contact = binding.contacte
        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
        btn3.setOnClickListener(this)
        btn4.setOnClickListener(this)
        btn5.setOnClickListener(this)
        btn6.setOnClickListener(this)
        btn7.setOnClickListener(this)
        btn8.setOnClickListener(this)
        btn9.setOnClickListener(this)
        btn10.setOnClickListener(this)

        readContactsReqPermLaunc = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
                accessContactsAction()
            } else {
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
                showSnackbar(
                    R.string.permission_denied_explanation,
                    R.string.settings
                ) { // Build intent that displays the App settings screen.
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts(
                        "package",
                        //BuildConfig.APPLICATION_ID , null
                        packageName, null
                    )  // Amb la darrera API level deprecated. Ara és packageName
                    intent.data = uri
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        }

        callsReqPermLaunc = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your app.
                callPhoneAction()
            } else {
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied. At the
                // same time, respect the user's decision.
                showSnackbar(
                    R.string.permission_denied_explanation,
                    R.string.settings
                ) { // Build intent that displays the App settings screen.
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts(
                        "package",
                        BuildConfig.APPLICATION_ID , null
                    )  // Amb la darrera API level deprecated. Ara és packageName
                    intent.data = uri
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        }

        accessGalleryPermLaunc = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your app.
                accessGalleryAction()
            } else {
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied. At the
                // same time, respect the user's decision.
                showSnackbar(
                    R.string.permission_denied_explanation,
                    R.string.settings
                ) { // Build intent that displays the App settings screen.
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts(
                        "package",
                        BuildConfig.APPLICATION_ID , null
                    )  // Amb la darrera API level deprecated. Ara és packageName
                    intent.data = uri
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        }
        //if (!ckeckPermissions()) requestPermissions()
    }

    @SuppressLint("NonConstantResourceId")
    override fun onClick(v: View) {
        val lat = getString(R.string.lat)
        val lon = getString(R.string.lon)
        val url = getString(R.string.url)
        val address = getString(R.string.direccion)
        val textToSearch = getString(R.string.textoABuscar)
        when (v.id) {
            R.id.button1 -> locateByCoordinates(lat, lon)
            R.id.button2 -> locateByAddress(address)
            R.id.button3 -> accessWeb(url)
            R.id.button4 -> seachGoogle(textToSearch)
            R.id.button5 -> callPhoneIfPermissions()
            R.id.button6 -> accessContactsIfPermissions()
            R.id.buttonMARCAR -> dial()
            R.id.buttonSMS -> sendSMS()
            R.id.buttonEMAIL -> sendEMAIL()
            R.id.buttonGALERIA -> accessGalleryIfPermissions()
        }
    }

    private fun sendEMAIL() {
        Toast.makeText(this, getString(R.string.opcion9), Toast.LENGTH_LONG).show()
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        val adresses = arrayOf("alg20@alumnes.udl.cat")
        intent.putExtra(Intent.EXTRA_EMAIL, adresses)
        intent.putExtra(Intent.EXTRA_SUBJECT, "Correu de prova")
        intent.putExtra(Intent.EXTRA_TEXT, "Aquest es un correu de prova. No respondre.")
        startActivity(intent)
    }

    private fun sendSMS() {
        Toast.makeText(this, getString(R.string.opcion8), Toast.LENGTH_LONG).show()
        val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("sms", "666666666", null))
        intent.putExtra("sms_body", "Text per defecte a enviar")
        startActivity(intent)
    }

    private fun dial() {
        Toast.makeText(this, getString(R.string.opcion7), Toast.LENGTH_LONG).show()
        val intent = Intent(Intent.ACTION_DIAL)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
     }

    private fun locateByCoordinates(lat: String, lon: String) {
        Toast.makeText(this, getString(R.string.opcion1), Toast.LENGTH_LONG).show()
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:$lat,$lon?q=$lat,$lon"))
        startActivity(intent)
    }

    private fun locateByAddress(address: String) {
        Toast.makeText(this, getString(R.string.opcion2), Toast.LENGTH_LONG).show()
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$address"))
        startActivity(intent)
    }

    private fun accessWeb(url: String) {
        val intent: Intent
        Toast.makeText(this, getString(R.string.opcion3), Toast.LENGTH_LONG).show()
        intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun seachGoogle(textToSearch: String) {
        Toast.makeText(this, getString(R.string.opcion4), Toast.LENGTH_LONG).show()
        val intent = Intent(Intent.ACTION_WEB_SEARCH)
        intent.putExtra(SearchManager.QUERY, textToSearch)
        startActivity(intent)
        //Alternative method
        //val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com/search?q=$textToSearch"))
    }

    private fun ckeckPermissions(): Boolean {
        return (ckeckPermissionsCallPhone() && ckeckPermissionsReadContacts())
    }

    private fun ckeckPermissionsCallPhone(): Boolean {
        return ActivityCompat.checkSelfPermission(applicationContext,
            Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun ckeckPermissionsReadContacts(): Boolean {
        return ActivityCompat.checkSelfPermission(applicationContext,
            Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun ckeckPermissionsAccessGalery(): Boolean {
        return ActivityCompat.checkSelfPermission(applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE),
            0
        )
    }

    private fun requestPermissionsCallPhone() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.CALL_PHONE
        )
        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            showSnackbar(
                R.string.callphone_permission_rationale,
                android.R.string.ok
            ) { // Request permission
                callsReqPermLaunc.launch(Manifest.permission.CALL_PHONE)
            }
        } else {
            Log.i(TAG, "Requesting permission")
            // Request permission.
            callsReqPermLaunc.launch(Manifest.permission.CALL_PHONE)
        }
    }

    private fun requestPermissionsReadContacts() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.READ_CONTACTS
        )
        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            showSnackbar(
                R.string.readcontacts_permission_rationale,
                android.R.string.ok
            ) { // Request permission
                readContactsReqPermLaunc.launch(Manifest.permission.CALL_PHONE)
            }
        } else {
            Log.i(TAG, "Requesting permission")
            // Request permission.
            readContactsReqPermLaunc.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    private fun requestPermissionsAccessGalery(){
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            showSnackbar(
                R.string.accessgallery_permission_rationale,
                android.R.string.ok
            ) { // Request permission
                accessGalleryPermLaunc.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            Log.i(TAG, "Requesting permission")
            // Request permission.
            accessGalleryPermLaunc.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun accessContactsIfPermissions() {
        if (!ckeckPermissionsReadContacts()) requestPermissionsReadContacts() else accessContactsAction()
    }

    private fun accessContactsAction() {
        Toast.makeText(this, getString(R.string.opcion6), Toast.LENGTH_LONG).show()
        val intent = Intent(Intent.ACTION_PICK)
        intent.data = ContactsContract.Contacts.CONTENT_URI
        startActivityForResult(intent, 2)
    }

    private fun callPhoneIfPermissions() {
        if (!ckeckPermissionsCallPhone()) requestPermissionsCallPhone() else callPhoneAction()
    }

    private fun callPhoneAction() {
        Toast.makeText(this, getString(R.string.opcion5), Toast.LENGTH_LONG).show()
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getText(R.string.telef)))
        startActivity(intent)
    }

    private fun accessGalleryIfPermissions() {
        if (ckeckPermissionsAccessGalery()) requestPermissionsAccessGalery() else accessGalleryAction()
    }

    private fun accessGalleryAction() {
        Toast.makeText(this, getString(R.string.opcion10), Toast.LENGTH_LONG).show()
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }

    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==1 && resultCode==RESULT_OK){
            val uri: Uri? = data?.data
            image.setImageURI(uri)
        }else {
            val uriContact: Uri? = data?.data
            val cursor: Cursor? = uriContact?.let { contentResolver.query(it, null, null, null, null) }
            if (cursor != null) {
                if(cursor.moveToFirst()){
                    contact.text=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                }
            }
        }
    }

    private fun showSnackbar(
        mainTextStringId: Int, actionStringId: Int,
        listener: View.OnClickListener
    ) {
        Snackbar.make(
            findViewById(android.R.id.content),
            getString(mainTextStringId),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(getString(actionStringId), listener).show()
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}