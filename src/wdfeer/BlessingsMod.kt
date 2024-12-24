package wdfeer

import arc.Events
import arc.util.Time
import mindustry.game.EventType
import mindustry.mod.*
import mindustry.ui.dialogs.BaseDialog
import mindustry.world.blocks.defense.turrets.ItemTurret.ItemTurretBuild
import mindustry.world.blocks.defense.turrets.Turret.TurretBuild

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
                                }.pad(20f).size(130f, 60f)
                                add(blessing.description)
                            }.row()
                        }
                    }
                    show()
                }
            }
        }

        Events.on(EventType.BlockBuildEndEvent::class.java) {
            it.tile.build ?: return@on
            when (activeBlessing) {
                Blessing.None -> {}
                Blessing.Reimu -> it.tile.build.maxHealth *= 1.2f
                Blessing.Nitori -> {
                    if (it.tile.build is TurretBuild)
                        it.tile.build.maxHealth *= 1.4f
                }
                Blessing.Takane -> {
                    if (it.tile.build is TurretBuild)
                        it.tile.build.maxHealth *= 2f
                    else {
                        it.tile.build.maxHealth *= 0.7f
                        it.tile.build.health *= 0.7f
                    }
                }
            }
        }
    }
}

enum class Blessing(val description: String) {
    None("Unchanged mindustry experience"),
    Reimu("Built blocks have +20% hp"),
    Nitori("Built turrets have +40% hp"),
    Takane("Built turrets have doubled hp, but all other blocks have -30% hp")
}
