package wdfeer

import arc.util.Time
import mindustry.Vars
import mindustry.gen.BlockUnitc
import mindustry.gen.Groups
import mindustry.gen.Player
import mindustry.world.blocks.defense.turrets.Turret.TurretBuild
import mindustry.world.blocks.production.GenericCrafter
import mindustry.world.blocks.production.GenericCrafter.GenericCrafterBuild
import mindustry.world.blocks.storage.CoreBlock.CoreBuild
import wdfeer.Blessing.*

data class BlessingState(var local: Blessing, val remote: MutableMap<Player, Blessing>)

val BlessingState.blessings: Map<Player, Blessing>
    get() = remote + (Groups.player.first() to local)

fun BlessingState.update() {
    val delta = Time.delta
    for ((player, blessing) in blessings) {
        when (blessing) {
            Reimu -> Groups.build.filter { it.team == player.team() }.filterIsInstance<CoreBuild>()
                .forEach { it.healFract(0.1f * delta) }

            Nitori -> Groups.build.filter { it.team == player.team() }.filterIsInstance<GenericCrafterBuild>()
                .forEach { it.progress += it.getProgressIncrease((it.block as GenericCrafter).craftTime) / 10f }

            Takane -> ((player.unit() as? BlockUnitc)?.tile() as? TurretBuild)?.apply {
                reloadCounter += delta
            }

            Sanae -> player.unit().heal(80f * delta)
            Aya -> player.unit().speedMultiplier = 2f // FIXME

            else -> {}
        }
    }
}