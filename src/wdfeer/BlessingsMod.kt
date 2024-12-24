package wdfeer

import arc.Events
import arc.util.Time
import mindustry.game.EventType
import mindustry.mod.*
import mindustry.ui.dialogs.BaseDialog

@Suppress("unused")
class BlessingsMod : Mod() {
    var activeBlessing = Blessing.None

    init {
        Events.on(EventType.ClientLoadEvent::class.java) {
            Time.runTask(10f) {
                BaseDialog("Select Blessing").apply {
                    cont.apply {
                        for (blessing in Blessing.entries) {
                            table {
                                button(blessing.name) {
                                    activeBlessing = blessing
                                    hide()
                                }.pad(20f).size(100f, 50f)
                                add(blessing.description)
                            }.row()
                        }
                    }
                    show()
                }
            }
        }

        Events.on(EventType.BlockBuildEndEvent::class.java) {
            when (activeBlessing) {
                Blessing.None -> {}
                Blessing.Reimu -> it.tile.build.maxHealth *= 2
            }
        }
    }
}

enum class Blessing(val description: String) {
    None("Unchanged mindustry experience"),
    Reimu("Built blocks have +100% max hp"),
}
