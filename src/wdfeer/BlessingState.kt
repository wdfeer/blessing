package wdfeer

import arc.util.Time
import mindustry.Vars
import mindustry.game.EventType
import mindustry.gen.Groups
import mindustry.gen.Player
import mindustry.world.blocks.defense.turrets.Turret.TurretBuild
import mindustry.world.blocks.storage.CoreBlock.CoreBuild
import wdfeer.Blessing.*

data class BlessingState(val blessings: MutableMap<Player, Blessing>, var lastUpdateTime: Long)

fun BlessingState.update() {
    for ((player, blessing) in blessings) {
        val delta = Time.timeSinceNanos(lastUpdateTime) / 1e9f
        when (blessing) {
            Reimu -> Groups.build.filter { it.team == player.team() }.filterIsInstance<CoreBuild>()
                .forEach { it.healFract(0.1f * delta) }

            Nitori -> Groups.build.filter { it.team == Vars.player.team() }
                .forEach { it.efficiency += 0.2f } // FIXME: doesn't work
            Takane -> Groups.build.filter { it.team == Vars.player.team() }.filterIsInstance<TurretBuild>()
                .forEach { it.efficiency += 1f } // FIXME: doesn't work

            Sanae -> player.unit().heal(80f * delta)
            Aya -> player.unit().speedMultiplier = 2f

            else -> {}
        }
        lastUpdateTime = Time.nanos()
    }
}

fun BlessingState.onBlockBuilt(event: EventType.BlockBuildEndEvent) {
    event.tile.build ?: return
    when (blessings[event.unit.player]) {
        Takane -> {
            event.tile.build.health *= 0.5f
            event.tile.build.maxHealth *= 0.5f
        }

        else -> {}
    }
}