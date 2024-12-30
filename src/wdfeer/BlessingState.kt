package wdfeer

import mindustry.Vars
import mindustry.game.EventType
import mindustry.gen.Groups
import mindustry.gen.Player
import mindustry.world.blocks.defense.turrets.Turret.TurretBuild
import mindustry.world.blocks.storage.CoreBlock.CoreBuild
import wdfeer.Blessing.*

data class BlessingState(var local: Blessing, val remote: MutableMap<Player, Blessing>)
val BlessingState.blessings: Map<Player, Blessing>
    get() = remote + (Groups.player.first() to local)

fun BlessingState.update(delta: Float) {
    for ((player, blessing) in blessings) {
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