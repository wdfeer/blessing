package wdfeer

import arc.Events
import mindustry.Vars
import mindustry.game.EventType
import mindustry.ui.dialogs.BaseDialog

private lateinit var blessingSelectionDialog: BaseDialog

fun BlessingsMod.initUI() {
    Events.on(EventType.ClientLoadEvent::class.java) {
        blessingSelectionDialog = createBlessingSelectionUI()
        Vars.ui.menufrag.addButton("Blessings", ::showBlessingSelectionUI)
    }
}

private fun showBlessingSelectionUI() {
    blessingSelectionDialog.show()
}

private fun BlessingsMod.createBlessingSelectionUI(): BaseDialog {
    return BaseDialog("Select Blessing").apply {
        cont.apply {
            for (blessing in Blessing.entries) {
                table {
                    button(blessing.name) {
                        activeBlessing = blessing
                        hide()
                    }.pad(20f).size(130f, 60f)
                    add(blessing.description)
                }.row()
            }
        }
    }
}