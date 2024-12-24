package wdfeer

import arc.Core
import arc.Events
import arc.util.Time
import mindustry.Vars
import mindustry.game.EventType
import mindustry.logic.LExecutor.Var
import mindustry.mod.*
import mindustry.ui.dialogs.BaseDialog
import mindustry.world.blocks.defense.turrets.Turret.TurretBuild
import wdfeer.Blessing.*

@Suppress("unused")
class BlessingsMod : Mod() {
    var activeBlessing = None

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
                Reimu -> it.tile.build.maxHealth *= 1.2f
                Nitori -> if (it.tile.build is TurretBuild) it.tile.build.maxHealth *= 1.4f
                Takane -> {
                    if (it.tile.build is TurretBuild) it.tile.build.maxHealth *= 2f
                    else {
                        it.tile.build.maxHealth *= 0.7f
                        it.tile.build.health *= 0.7f
                    }
                }

                else -> {}
            }
        }

        scheduleUpdate()
    }


    private fun update() {
        val delta = Time.delta
        when (activeBlessing) {
            Sanae -> Vars.player.unit().heal(40f * delta)
            Aya -> {
                Vars.player.unit().speedMultiplier = 3f
            }

            else -> {}
        }
    }

    private fun scheduleUpdate() {
        Time.runTask(1f) {
            if (Vars.state.isGame && !Vars.state.isPaused) update()
            scheduleUpdate()
        }
    }
}

enum class Blessing(val description: String) {
    None("Unchanged mindustry experience"),
    Reimu("Built blocks have +20% hp."),
    Nitori("Built turrets have +40% hp."),
    Takane("Built turrets have doubled hp, but all other blocks have -30% hp."),
    Sanae("Controlled unit heals 40 hp / second."),
    Aya("Controlled unit travels thrice as fast.")
}
