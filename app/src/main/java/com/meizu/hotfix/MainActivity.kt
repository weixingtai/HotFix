package com.meizu.hotfix

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.meizu.hotfix.databinding.ActivityMainBinding
import com.meizu.hotfix.util.FileUtil
import com.meizu.hotfix.util.HotFixUtil
import com.meizu.hotfix.util.StrUtil
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tvMain.text = StrUtil.getDisplayText()

        binding.btnOrigin.setOnClickListener {
//            Log.i("wxt","filesDirL: ${assets.open("patch1.dex")}")
            val file = File(filesDir, "patch/patch1.dex")
            if (file.exists()) {
                Toast.makeText(this, "已完成热更新！", Toast.LENGTH_SHORT).show()
            } else {
                FileUtil.copyAssetFileToPrivateDir(this,"patch1.dex")
                HotFixUtil.inject(classLoader, file)
                Toast.makeText(this, "热更新成功, 请重启应用！", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnAndfix.setOnClickListener {
            val file = File(filesDir, "patch/patch2.dex")
            if (file.exists()) {
                Toast.makeText(this, "已完成热更新！", Toast.LENGTH_SHORT).show()
            } else {
//                FileUtil.copyAssetFileToPrivateDir(this,"patch1.dex")
//                HotFixUtil.inject(classLoader, file)
                Toast.makeText(this, "热更新成功, 请重启应用！", Toast.LENGTH_SHORT).show()
            }
        }

    }
}