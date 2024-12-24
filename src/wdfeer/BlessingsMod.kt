package wdfeer

import arc.Events
import arc.util.Time
import mindustry.game.EventType
import mindustry.mod.*
import mindustry.ui.dialogs.BaseDialog

@Suppress("unused")
class BlessingsMod : Mod() {
    init {
        Events.on(EventType.ClientLoadEvent::class.java) {
            Time.runTask(10f) {
                BaseDialog("Select Blessing").apply {
                    cont.apply {
                        for (blessing in Blessing.entries) {
                            table {
                                button(blessing.name) {
                                    // TODO
                                    hide()
                                }.pad(20f).size(90f, 40f)
                                add(blessing.description)
                            }.row()
                        }
                    }
                    show()
                }
            }
        }
    }
}

enum class Blessing(val description: String) {
    None("Unchanged mindustry experience"),
    Reimu("Built blocks have +100% max hp"),
}
