package pl.wojtach.nazakupy

import android.app.Application
import pl.wojtach.nazakupy.room.ShoppingDataBase

class NaZakupyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        ShoppingDataBase.setAppContext(this)
    }
}