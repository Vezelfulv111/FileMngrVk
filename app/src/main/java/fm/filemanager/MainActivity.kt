package fm.filemanager

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if(!checkPermission())
            requestPermission()
        else
            showRootFolder()

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            for (i in permissions.indices) {
                val permission = permissions[i]
                val grantResult = grantResults[i]
                if (permission == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        showRootFolder()//пользователь дал разрешение продолжаем работу
                    }
                    else {
                        //пользователь отклонил разрешение, выводится сообщение, зачем данное разрешение нужно
                        Toast.makeText(this@MainActivity,
                            "Storage permission is requires,please allow from settings", Toast.LENGTH_SHORT ).show()
                    }
                }
            }
        }
    }

    private fun showRootFolder() {
        val path = Environment.getExternalStorageDirectory().path
        val root =  File(path)
        val filesAndFolders  = root.listFiles()
        if (filesAndFolders != null) {
            val listViewFiles : ListView = findViewById(R.id.fileList) // находим список
            val fileAdapter = FileAdapter(this@MainActivity, filesAndFolders)//передаем аргументы в адаптер
            listViewFiles.adapter = fileAdapter
        }
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this@MainActivity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
      ActivityCompat.requestPermissions(this@MainActivity,
          arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),123)
    }
}