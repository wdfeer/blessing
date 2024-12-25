package wdfeer

import arc.Core
import arc.Events
import arc.util.Time
import mindustry.Vars
import mindustry.game.EventType
import mindustry.gen.Groups
import mindustry.mod.*
import mindustry.world.blocks.defense.turrets.Turret.TurretBuild
import mindustry.world.blocks.storage.CoreBlock.CoreBuild
import wdfeer.Blessing.*

@Suppress("unused")
class BlessingsMod : Mod() {
    var activeBlessing = None

    init {
        initUI()

        Events.on(EventType.BlockBuildEndEvent::class.java) {
            it.tile.build ?: return@on
            when (activeBlessing) {
                Takane -> {
                    it.tile.build.health *= 0.5f
                    it.tile.build.maxHealth *= 0.5f
                }

                else -> {}
            }
        }

        scheduleUpdate()
    }

    private var lastUpdateTime: Long = Time.nanos()
    private fun update() {
        val delta = Time.timeSinceNanos(lastUpdateTime) / 1e9f
        when (activeBlessing) {
            Reimu -> Groups.build.filter { it.team == Vars.player.team() }.filterIsInstance<CoreBuild>()
                .forEach { it.healFract(0.1f * delta) }

            Nitori -> Groups.build.filter { it.team == Vars.player.team() }.forEach { it.efficiency += 0.2f }
            Takane -> Groups.build.filter { it.team == Vars.player.team() }.filterIsInstance<TurretBuild>()
                .forEach { it.efficiency += 1f }

            Sanae -> Vars.player.unit().heal(80f * delta)
            Aya -> Vars.player.unit().speedMultiplier = 2f

            else -> {}
        }
        lastUpdateTime = Time.nanos()
    }

    private fun scheduleUpdate() {
        Core.app.post {
            if (Vars.state.isGame && !Vars.state.isPaused) update()
            scheduleUpdate()
        }
    }
}

enum class Blessing(val description: String) {
    None("Classic mindustry experience"),
    Reimu("Core 10% hp/s mending"),
    Nitori("Blocks +20% speed"),
    Takane("Turrets +100% speed, All built blocks -50% hp"),
    Sanae("Controlled unit 80 hp/s healing"),
    Aya("Controlled unit +100% speed")
}
